package lifter.app;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public class ScheduleList extends ArrayAdapter<Schedule>{
    private Activity context;
    private List<Schedule> scheduleList;

    public ScheduleList(Activity context, List<Schedule> scheduleList){
        super(context, R.layout.list_layout, scheduleList);
        final String [] days =  {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        HashMap filler = new HashMap(7);
        final HashMap days_hash = fillHash(filler, days);
        Collections.sort(scheduleList, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule schedule, Schedule t1) {
                String hashed_x = days_hash.get(schedule.getDay()).toString();
                String hashed_y = days_hash.get(t1.getDay()).toString();
                return hashed_x.compareTo(hashed_y);
            }
        });

        this.context = context;
        this.scheduleList = scheduleList;
    }

    public HashMap fillHash(HashMap days, String[] d){
        int ascii_a = 66;
        for(int i=0; i<7 ; i++){
            days.put(d[i], (char)(ascii_a+i));
        }
        return days;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        //View listViewItem = inflater.inflate(R.layout.activity_schedule_list, null, true);
        final TextView day = listViewItem.findViewById(R.id.day);
        final TextView from = listViewItem.findViewById(R.id.from);
        final TextView to = listViewItem.findViewById(R.id.to);
        final TextView muscle = listViewItem.findViewById(R.id.muscle);

        final Schedule schedule = scheduleList.get(position);
        day.setText(schedule.getDay());
        from.setText(schedule.getFrom());
        to.setText(schedule.getTo());
        muscle.setText(schedule.getMuscle());


        return listViewItem;
    }
}