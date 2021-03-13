package lifter.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MyScheduleList extends ArrayAdapter<Schedule> {

    private Activity context;
    private List<Schedule> myScheduleList;
    private Schedule schedule;
    private View myListViewItem;
    private DatabaseReference ref;
    Dialog myDialog;

    public MyScheduleList(Activity context, List<Schedule> myScheduleList){
        super(context, R.layout.activity_schedule_list, myScheduleList);
        final String [] days =  {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        HashMap filler = new HashMap(7);
        final HashMap days_hash = fillHash(filler, days);
        Collections.sort(myScheduleList, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule schedule, Schedule t1) {
                String hashed_x = days_hash.get(schedule.getDay()).toString();
                String hashed_y = days_hash.get(t1.getDay()).toString();
                return hashed_x.compareTo(hashed_y);
            }
        });
        this.context = context;
        this.myScheduleList = myScheduleList;
        myDialog = new Dialog(context);
    }


    public HashMap fillHash(HashMap days, String[] d){
        int ascii_a = 66;
        for(int i=0; i<7 ; i++){
            days.put(d[i], (char)(ascii_a+i));
        }
        return days;
    }


    public void showPopup(View v, Schedule s){
        final Schedule schedule = s;
        Button yes_btn;
        Button no_btn;
        myDialog.setContentView(R.layout.delete_popup);
        yes_btn =  myDialog.findViewById(R.id.yes_btn);
        no_btn = myDialog.findViewById(R.id.no_btn);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSchedule(schedule);
                myDialog.dismiss();

            }
        });

        no_btn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        }));
        myDialog.show();
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        myListViewItem = inflater.inflate(R.layout.activity_schedule_list, null, true);

        ref = FirebaseDatabase.getInstance().getReference("schedule");

        final TextView day = myListViewItem.findViewById(R.id.day);
        final TextView from = myListViewItem.findViewById(R.id.from);
        final TextView to = myListViewItem.findViewById(R.id.to);
        final TextView muscle = myListViewItem.findViewById(R.id.muscle);

        final Button delete_btn = myListViewItem.findViewById(R.id.delete_btn);
        final Button edit_btn = myListViewItem.findViewById(R.id.edit_btn);

        final Schedule mySchedule = myScheduleList.get(position);

        day.setText(mySchedule.getDay());
        from.setText(mySchedule.getFrom());
        to.setText(mySchedule.getTo());
        muscle.setText(mySchedule.getMuscle());


        edit_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(context, AddTimeActivity.class);

                Bundle editted = new Bundle();
                String from_specific = mySchedule.getFromSpecific().toString();



                editted.putString("day", mySchedule.getDay());
                editted.putString("fromTime", mySchedule.getFrom());
                editted.putString("toTime", mySchedule.getTo());
                editted.putString("etMuscle", mySchedule.getMuscle());
                editted.putString("fromSpecific", from_specific);
                editted.putBoolean("edit", true);
                editted.putString("id", mySchedule.getId());
                editted.putString("old_time", mySchedule.getFrom());


                j.putExtras(editted);

                j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(j);
            }
        });


        delete_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v,mySchedule);
            }
        });
        return myListViewItem;

    }


    private void deleteSchedule(Schedule schedule) {
        ref.child(schedule.getId()).removeValue();
        myScheduleList.remove(schedule);
        notifyDataSetChanged();
    }
}