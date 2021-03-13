package lifter.app;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


public class AddTimeActivity extends AppCompatActivity {
    Button fromBtn, toBtn, add,  exit;
    Spinner day;
    ProgressBar progress;

    String editted_id;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser u = auth.getCurrentUser();

    int fromHours, fromMinute, toHour, toMinute;
    String user_email = u.getEmail();
    String etMuscle = "";
    String from_specific;
    String old_time;
    boolean edit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout_time);

        fromBtn = (Button) findViewById(R.id.from);
        toBtn = (Button) findViewById(R.id.to);
        day = (Spinner) findViewById(R.id.day);
        add = (Button) findViewById(R.id.add);
        exit = (Button) findViewById(R.id.exit);
        progress = (ProgressBar) findViewById(R.id.progress);

        FirebaseDatabase databaseSchedule = FirebaseDatabase.getInstance();
        final DatabaseReference ref = databaseSchedule.getReference("schedule");


        if(getIntent().getExtras() != null) {
            Intent j = getIntent();
            final Bundle backed = j.getExtras();

            String etDay = backed.getString("day");
            String fromTime = backed.getString("fromTime");
            String toTime = backed.getString("toTime");
            etMuscle = backed.getString("etMuscle");
            edit = backed.getBoolean("edit");
            editted_id = backed.getString("id");
            from_specific = backed.getString("fromSpecific");
            old_time = backed.getString("old_time");
            int from = backed.getInt("fromHours");
            int from_min = backed.getInt("fromMinute");
            int to = backed.getInt("toHour");
            int to_min = backed.getInt("toMinute");

            fromHours = from;
            fromMinute = from_min;

            toHour = to;
            toMinute = to_min;

            setSpinText(day, etDay);
            fromBtn.setText(fromTime);
            toBtn.setText(toTime);

            Log.d("TAG", etMuscle);
        }

        if(!edit) {
            exit.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(AddTimeActivity.this, Sidebar.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        }


        else if(edit){
            exit.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(AddTimeActivity.this, MySchedule.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        }

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addSchedule(ref);
            }
        });
    }



    public void setSpinText(Spinner spin, String text) {
        for(int i= 0; i < spin.getAdapter().getCount(); i++) {
            if(spin.getAdapter().getItem(i).toString().contains(text)) {
                spin.setSelection(i);
            }
        }
    }


    private void addSchedule(DatabaseReference ref){

        final Bundle extras = new Bundle();

        final String fromTime = fromBtn.getText().toString();
        final String toTime = toBtn.getText().toString();
        final String etDay = day.getSelectedItem().toString();
        final String email_content = u.getEmail();

        FirebaseDatabase databaseSchedule = FirebaseDatabase.getInstance();
        final DatabaseReference reference = databaseSchedule.getReference("schedule");

        if(!TextUtils.isEmpty(fromTime)
                && !TextUtils.isEmpty(toTime)
                && !TextUtils.isEmpty(etDay)
                && !etDay.equals("")){

            // toHour must be greater than fromHour
            // reserved range of time must be at least an hour
            if(fromHours < toHour && (toHour - fromHours >= 1) && !edit ){

                ref.orderByChild("email").equalTo(user_email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String workout_day;
                        String new_workout_start = fromTime.substring(fromTime.length() - 2);  //if the workout starts in the AM's
                        String new_workout_finish = toTime.substring(toTime.length() - 2);
                        String from = "";
                        String to = "";
                        int new_ftime = Integer.valueOf(fromTime.replaceAll("[^\\d.]", ""));
                        int new_ttime = Integer.valueOf(toTime.replaceAll("[^\\d.]", ""));

                        boolean before_ok = false;
                        boolean after_ok = false;
                        boolean conflict = false;

                        //checks to see if the times for the start and finish times are AM or PM
                        // if it is PM add 12 to the hours to make it military time
                        if (fromTime.substring(fromTime.length() - 2).equals("PM")) {
                            new_ftime = add_twelve(new_ftime);
                        }
                        if (toTime.substring(toTime.length() - 2).equals("PM")) {
                            new_ttime = add_twelve(new_ttime);
                        }

                        for (DataSnapshot datas : dataSnapshot.getChildren()) {
                            workout_day = datas.child("day").getValue().toString();
                            if (workout_day.equals(etDay)) { //workout times from the database
                                String fr_time = datas.child("from").getValue().toString();
                                String to_time = datas.child("to").getValue().toString();
                                from = datas.child("from").getValue().toString();
                                to = datas.child("to").getValue().toString();

                                String old_workout_start = from.substring(from.length() - 2);
                                String old_workout_finish = to.substring(to.length() - 2);


                                fr_time = fr_time.replaceAll("[^\\d.]", "");
                                to_time = to_time.replaceAll("[^\\d.]", "");
                                int old_ftime = Integer.valueOf(fr_time);
                                int old_ttime = Integer.valueOf(to_time);

                                //checks if the iterated workout is AM or PM
                                // if PM add 12 to it
                                if (from.substring(from.length() - 2).equals("PM")) {
                                    old_ftime = add_twelve(old_ftime);
                                }
                                if (to.substring(to.length() - 2).equals("PM")) {
                                    old_ttime = add_twelve(old_ttime);
                                }
                                //checks for the case if the workout times is also AM or PM and same the start and finish time
                                if (old_workout_start.equals(new_workout_start) && old_workout_finish.equals(new_workout_finish)
                                        && (new_ftime == old_ftime) && (new_ttime == old_ttime)) {
                                    conflict = true;
                                    break;
                                } else {
                                    //check to see if the new workout can be before or after the iterated workout
                                    if ((new_ftime <= old_ftime && new_ttime <= old_ftime)) {
                                        before_ok = true;
                                    } else {
                                        before_ok = false;
                                    }
                                    //if the new workout's start time is before the old workouts end time
                                    if (new_ftime >= old_ttime) { //checks to see if can start a workout after another workout
                                        after_ok = true;
                                    } else {
                                        after_ok = false;
                                    }
                                    //if you cannot put in before or after
                                    if (!(before_ok || after_ok)) {
                                        conflict = true;
                                        break;
                                    }
                                }
                            }
                        }


                        if (conflict) {
                            message("Your new workout overlaps with one of the current workouts " + from + " to " + to);
                        }

                        else{   //if the workout time is not overlapping any previous ones then add it the new workout to firebase
                            String id = reference.push().getKey();

                            Intent i = new Intent(AddTimeActivity.this, AddWorkoutActivity.class);

                            extras.putString("id", id);
                            extras.putString("email_content", email_content);
                            extras.putString("day", etDay);
                            extras.putString("fromTime", fromTime);
                            extras.putString("toTime", toTime);
                            extras.putString("etMuscle", etMuscle);
                            extras.putInt("fromHours", fromHours);
                            extras.putInt("fromMinute", fromMinute);
                            extras.putInt("toHour", toHour);
                            extras.putInt("toMinute", toMinute);
                            extras.putBoolean("edit", edit);
                            extras.putString("old_time", old_time );

                            i.putExtras(extras);
                            startActivity(i);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
            }
            else if(edit){  //if the current workout we are looking at right now is being edited

                Intent i = new Intent(AddTimeActivity.this, AddWorkoutActivity.class);

                extras.putString("id", editted_id);
                extras.putString("email_content", email_content);
                extras.putString("day", etDay);
                extras.putString("fromTime", fromTime);
                extras.putString("toTime", toTime);
                extras.putString("etMuscle", etMuscle);
                extras.putInt("fromHours", fromHours);
                extras.putInt("fromMinute", fromMinute);
                extras.putInt("toHour", toHour);
                extras.putInt("toMinute", toMinute);
                extras.putBoolean("edit", true);
                extras.putString("fromSpecific",from_specific );
                extras.putString("old_time", old_time );


                //check if its a valid time
                int edit_from_hour = Integer.parseInt(fromTime.replaceAll("[^\\d.]", ""));
                int edit_to_hour = Integer.parseInt(toTime.replaceAll("[^\\d.]", ""));
                if(fromTime.substring(fromTime.length()-2).equals("PM")){
                    edit_from_hour = add_twelve(edit_from_hour);
                }
                if(toTime.substring(toTime.length()-2).equals("PM")){
                    edit_to_hour = add_twelve(edit_to_hour);
                }

                if(edit_from_hour <= edit_to_hour && (edit_to_hour - edit_from_hour >= 100)){

                    ref.orderByChild("email").equalTo(user_email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String workout_day;
                            String new_workout_start = fromTime.substring(fromTime.length() - 2);  //if the workout starts in the AM's
                            String new_workout_finish = toTime.substring(toTime.length() - 2);
                            String from;
                            String to;
                            int new_ftime = Integer.valueOf(fromTime.replaceAll("[^\\d.]", ""));
                            int new_ttime = Integer.valueOf(toTime.replaceAll("[^\\d.]", ""));

                            boolean before_ok = false;
                            boolean after_ok = false;
                            boolean conflict = false;

                            //checks to see if the times for the start and finish times are AM or PM
                            // if it is PM add 12 to the hours to make it military time
                            if (fromTime.substring(fromTime.length() - 2).equals("PM")) {
                                new_ftime = add_twelve(new_ftime);
                            }
                            if (toTime.substring(toTime.length() - 2).equals("PM")) {
                                new_ttime = add_twelve(new_ttime);
                            }

                            for (DataSnapshot datas : dataSnapshot.getChildren()) {
                                workout_day = datas.child("day").getValue().toString();
                                if (workout_day.equals(etDay)) { //workout times from the database
                                    String fr_time = datas.child("from").getValue().toString();
                                    String to_time = datas.child("to").getValue().toString();
                                    from = datas.child("from").getValue().toString();
                                    to = datas.child("to").getValue().toString();

                                    String old_workout_start = from.substring(from.length() - 2);
                                    String old_workout_finish = to.substring(to.length() - 2);


                                    fr_time = fr_time.replaceAll("[^\\d.]", "");
                                    to_time = to_time.replaceAll("[^\\d.]", "");
                                    int old_ftime = Integer.valueOf(fr_time);
                                    int old_ttime = Integer.valueOf(to_time);

                                    //checks if the iterated workout is AM or PM
                                    // if PM add 12 to it
                                    if (from.substring(from.length() - 2).equals("PM")) {
                                        old_ftime = add_twelve(old_ftime);
                                    }
                                    if (to.substring(to.length() - 2).equals("PM")) {
                                        old_ttime = add_twelve(old_ttime);
                                    }
                                    //checks for the case if the workout times is also AM or PM and same the start and finish time
                                    if (old_workout_start.equals(new_workout_start) && old_workout_finish.equals(new_workout_finish)
                                            && (new_ftime == old_ftime) && (new_ttime == old_ttime)) {
                                        conflict = true;
                                        break;
                                    } else {
                                        //check to see if the new workout can be before or after the iterated workout
                                        if ((new_ftime <= old_ftime && new_ttime <= old_ftime)) {
                                            before_ok = true;
                                        } else {
                                            before_ok = false;
                                        }
                                        //if the new workout's start time is before the old workouts end time
                                        if (new_ftime >= old_ttime) { //checks to see if can start a workout after another workout
                                            after_ok = true;
                                        } else {
                                            after_ok = false;
                                        }
                                        //if you cannot put in before or after
                                        if (!(before_ok || after_ok)) {
                                            conflict = true;
                                            break;
                                        }
                                    }
                                }
                            }


                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    i.putExtras(extras);
                    startActivity(i);
                }
                else {
                    message("That is not a possible schedule");
                }
            }
            else
                Toast.makeText(this, "You have not filled a correct time slot", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "You have not filled out a required field", Toast.LENGTH_LONG).show();
    }


    public void message(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    public int add_twelve(int time){
        String result;
        int hour = Integer.parseInt(Integer.toString(time).substring(0,1));
        String minute_string = Integer.toString(time);
        String minutes = "" + minute_string.charAt(minute_string.length()-2) + minute_string.charAt(minute_string.length()-1);
        hour += 12;
        result = Integer.toString(hour) + minutes;
        time = Integer.parseInt(result);
        return time;
    }


    //clock for "From" option
    public void setFromBtn(View v) {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog from;
        from = new TimePickerDialog(AddTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm = "";
                Calendar datetime = Calendar.getInstance();
                fromHours = hourOfDay;
                fromMinute = minute;

                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minute);

                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "AM";
                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "PM";

                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR) + "";
                String strMinToShow = String.valueOf(datetime.get(Calendar.MINUTE));

                if (strMinToShow.length() == 1)
                    strMinToShow = "0" + strMinToShow;

                fromBtn.setText(strHrsToShow + ":" + strMinToShow + " " + am_pm);
            }
        },hour, minute,false);
        from.show();
    }


    //clock for "To" option
    public void setToBtn(View v) {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog to;
        to = new TimePickerDialog(AddTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String am_pm = "";
                Calendar datetime = Calendar.getInstance();
                toHour = hourOfDay;
                toMinute = minute;

                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minute);

                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "AM";
                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "PM";

                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR) + "";
                String strMinToShow = String.valueOf(datetime.get(Calendar.MINUTE));

                if (strMinToShow.length() == 1)
                    strMinToShow = "0" + strMinToShow;

                toBtn.setText(strHrsToShow + ":" + strMinToShow + " " + am_pm);
            }
        },hour, minute,false);
        to.show();
    }
}