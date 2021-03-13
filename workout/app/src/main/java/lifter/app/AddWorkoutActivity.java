package lifter.app;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class AddWorkoutActivity extends AppCompatActivity {

    Button add,  exit, back;
    Spinner muscle;
    ProgressBar progress;

    FirebaseAuth auth;
    FirebaseUser u;

    String etMuscle = "";
    Boolean edit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);

        muscle = (Spinner) findViewById(R.id.muscle);
        add = (Button) findViewById(R.id.add);
        exit = (Button) findViewById(R.id.exit);
        progress = (ProgressBar) findViewById(R.id.progress);
        back = findViewById(R.id.back);

        FirebaseDatabase databaseSchedule = FirebaseDatabase.getInstance();
        final DatabaseReference ref = databaseSchedule.getReference("schedule");

        auth = FirebaseAuth.getInstance();
        u = auth.getCurrentUser();


        if(getIntent().getExtras() != null) {
            Intent j = getIntent();
            Bundle backed = j.getExtras();

            etMuscle = backed.getString("etMuscle");
            edit = backed.getBoolean("edit");
            if (!etMuscle.equals("")) {
                setSpinText(muscle, etMuscle);
            }
        }


        if(!edit) {
            exit.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(AddWorkoutActivity.this, Sidebar.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        }

        else if(edit){
            exit.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(AddWorkoutActivity.this, MySchedule.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
        }

        back.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent i = getIntent();
                String etMuscle = muscle.getSelectedItem().toString();

                Bundle extras = i.getExtras();
                String day = extras.getString("day");
                String fromTime = extras.getString("fromTime");
                String toTime = extras.getString("toTime");

                int fromHours = extras.getInt("fromHours");
                int fromMinute = extras.getInt("fromMinute");
                int toHour = extras.getInt("toHour");
                int toMinute = extras.getInt("toMinute");

                Bundle backed = new Bundle();
                backed.putString("day", day);
                backed.putString("fromTime", fromTime);
                backed.putString("toTime", toTime);
                backed.putInt("fromHours", fromHours);
                backed.putInt("fromMinute", fromMinute);
                backed.putInt("toHour", toHour);
                backed.putInt("toMinute", toMinute);
                backed.putString("etMuscle", etMuscle);
                backed.putBoolean("edit", edit);
                Intent j = new Intent(AddWorkoutActivity.this, AddTimeActivity.class);
                j.putExtras(backed);
                startActivity(j);
            }
        });


        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if(edit){
                    editWorkout(ref);
                }
                else{
                    addWorkout(ref);
                }

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

    private void editWorkout(DatabaseReference ref){
        if(!TextUtils.isEmpty(etMuscle)
                && !etMuscle.equals("")){
            String fromHourString;
            String fromMinuteString;
            String fromSpecific;
            Intent i  = getIntent();
            Bundle extras = i.getExtras();
            String id = extras.getString("id");
            String email_content = extras.getString("email_content");
            String day = extras.getString("day");
            String fromTime = extras.getString("fromTime");
            String toTime = extras.getString("toTime");
            String etMuscle = muscle.getSelectedItem().toString();
            String old_time = extras.getString("old_time");
            if (old_time.equals(fromTime)) {
                fromSpecific = extras.getString("fromSpecific");
            }
            else {
                int fromHours = extras.getInt("fromHours");
                int fromMinute = extras.getInt("fromMinute");
                fromHourString = Integer.toString(fromHours);
                fromMinuteString = Integer.toString(fromMinute);
                if (fromMinute < 10) {
                    fromMinuteString = "0" + Integer.toString(fromMinute);
                }
                if (fromHours < 10) {
                    fromHourString = "0" + Integer.toString(fromHours);
                }
                fromSpecific = fromHourString + ":" + fromMinuteString;
            }


            ref.child(id).child("from").setValue(fromTime);
            ref.child(id).child("to").setValue(toTime);
            ref.child(id).child("muscle").setValue(etMuscle);
            ref.child(id).child("day").setValue(day);
            ref.child(id).child("fromSpecific").setValue(fromSpecific); //change format to 24 hr time

            Intent j = new Intent(AddWorkoutActivity.this, Sidebar.class);
            startActivity(j);
        }
        else{
            Toast.makeText(this, "You have not selected a muscle", Toast.LENGTH_LONG).show();
        }
    }

    private void addWorkout(DatabaseReference ref) {

        String etMuscle = muscle.getSelectedItem().toString();

        if (!TextUtils.isEmpty(etMuscle)
                && !etMuscle.equals("")){

            Intent i = getIntent();
            Bundle extras = i.getExtras();

            String id = extras.getString("id");
            String email_content = extras.getString("email_content");
            String day = extras.getString("day");
            String fromTime = extras.getString("fromTime");
            String toTime = extras.getString("toTime");
            int fromHours = extras.getInt("fromHours");
            int fromMinute = extras.getInt("fromMinute");

            String fromHourString = Integer.toString(fromHours);
            String fromMinuteString = Integer.toString(fromMinute);
            if(fromMinute<10){
                fromMinuteString = "0" + Integer.toString(fromMinute);
            }
            if(fromHours < 10){
                fromHourString = "0" + Integer.toString(fromHours);
            }

            String fromSpecific = fromHourString + ":" + fromMinuteString;

            // Reflects in Schedule java file (the scheduling object)
            Schedule schedule = new Schedule(id, email_content, day, fromTime, toTime, etMuscle, fromSpecific);
            ref.child(id).setValue(schedule);

            Intent j = new Intent(AddWorkoutActivity.this, Sidebar.class);
            startActivity(j);

        } else
            Toast.makeText(this, "You have not selected a muscle", Toast.LENGTH_LONG).show();
    }
}