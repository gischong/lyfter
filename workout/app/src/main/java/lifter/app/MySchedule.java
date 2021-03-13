package lifter.app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import 	android.view.LayoutInflater;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class MySchedule extends AppCompatActivity{

    private Schedule schedule;

    Dialog myDialog;

    FirebaseAuth auth;
    FirebaseUser u;
    DatabaseReference ref;

    ListView ListViewSchedule;
    List<Schedule> myScheduleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        myDialog = new Dialog(this);

        ref = FirebaseDatabase.getInstance().getReference("schedule");

        auth = FirebaseAuth.getInstance();
        u = auth.getCurrentUser();

        Query my_query = FirebaseDatabase.getInstance().getReference("schedule")
                .orderByChild("email")
                .equalTo(u.getEmail());

        my_query.addListenerForSingleValueEvent(my_listener);
        ListViewSchedule = (ListView) findViewById(R.id.ListViewSchedule);
        myScheduleList = new ArrayList<>();
    }


    protected void onResume() {
        super.onResume();

    }


    // This method will just show the menu item (which is our button "Back")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // the menu being referenced here is the menu.xml from res/menu/menu.xml
        inflater.inflate(R.menu.menu, menu);

        MenuItem item = menu.getItem(0);
        SpannableString s = new SpannableString("Clear");
        s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
        item.setTitle(s);

        return super.onCreateOptionsMenu(menu);
    }


    ValueEventListener my_listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            myScheduleList.clear();
            for (DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
                Schedule schedule = scheduleSnapshot.getValue(Schedule.class);
                myScheduleList.add(schedule);
            }
             Collections.sort(myScheduleList, new Comparator<Schedule>() {
                 @Override
                 public int compare(Schedule schedule, Schedule t1) {
                     return schedule.getDay().compareTo(t1.getDay());
                 }
             });
            MyScheduleList myAdapter = new MyScheduleList(MySchedule.this, myScheduleList);
            ListViewSchedule.setAdapter(myAdapter);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                /*the R.id.action_favorite is the ID of our button (defined in strings.xml).
                Change Activity here (if that's what you're intending to do, which is probably is).
                 */
                Intent i = new Intent(this, Sidebar.class);
                startActivity(i);
                break;

            case R.id.action_clear:
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.clear_popup, null);
                showPopup(v);

            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }


    public void showPopup(View v){
        Button yes_btn, no_btn;
        myDialog.setContentView(R.layout.clear_popup);
        yes_btn =  myDialog.findViewById(R.id.yes_btn);
        no_btn = myDialog.findViewById(R.id.no_btn);

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSchedule();
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


    void clearSchedule(){
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("schedule");
//        ref.child(u.getEmail()).removeValue();

        Query my_query = FirebaseDatabase.getInstance().getReference("schedule")
                .orderByChild("email")
                .equalTo(u.getEmail());

        my_query.addListenerForSingleValueEvent(my_clear_listener);
        myScheduleList = new ArrayList<>();
    }


    ValueEventListener my_clear_listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            myScheduleList.clear();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("schedule");

            for (DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
                Schedule schedule = scheduleSnapshot.getValue(Schedule.class);
                ref.child(schedule.getId()).removeValue();
                myScheduleList.remove(schedule);
            }
            Collections.sort(myScheduleList, new Comparator<Schedule>() {
                @Override
                public int compare(Schedule schedule, Schedule t1) {
                    return schedule.getDay().compareTo(t1.getDay());
                }
            });
            MyScheduleList myAdapter = new MyScheduleList(MySchedule.this, myScheduleList);
            ListViewSchedule.setAdapter(myAdapter);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
