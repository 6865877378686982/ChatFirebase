package com.example.alarmmanager.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.alarmmanager.R;
import com.example.alarmmanager.model.EventModelDB;
import com.example.alarmmanager.model.ListingsModel;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.Viewholder> {


    private ArrayList<ListingsModel> data;
    private ListingsModel model;
    private Context context;
    private Activity activity;

    public ListingsAdapter(ArrayList<ListingsModel> data, Context context, Activity activity) {
        this.data = data;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listings_row, parent, false);

        return new Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {

        model = data.get(position);

        holder.eventText.setText(model.getEvent());
        holder.timeAndDateText.setText("At  " + model.getTime() + "  On  " + model.getDate());

        // long click an item to delete it from database
        holder.toplayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setMessage("Are you Sure you want to delete");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // deleting the selected row from Realm database
                                Realm realm = Realm.getDefaultInstance();
                                RealmResults<EventModelDB> result = realm.where(EventModelDB.class)
                                        .equalTo("timestamp", data.get(position).getTimestamp())
                                        .findAll();

                                if (result != null) {
                                    if (result.size() > 0) {
                                        realm.beginTransaction();
                                        result.deleteFromRealm(0);
                                        realm.commitTransaction();

                                    }
                                }
                                Log.i("clickedposn", "" + position);

                                // Delete the row data from the ArrayList passed to this Adapter
                                data.remove(position);

                                // Refresh the RecyclerView after row deletion
                                notifyDataSetChanged();
                                dialog.cancel();

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


                // context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));


                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        private TextView eventText, timeAndDateText;
        private LinearLayout toplayout;

        public Viewholder(View itemView) {
            super(itemView);

            eventText = (TextView) itemView.findViewById(R.id.event);
            timeAndDateText = (TextView) itemView.findViewById(R.id.time_and_date);
            toplayout = (LinearLayout) itemView.findViewById(R.id.toplayout);

        }
    }


}
