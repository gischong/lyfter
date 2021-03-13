package lifter.app;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import lifter.app.MainActivity;
import lifter.app.R;

public class Sidebar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth auth;
    FirebaseUser u;
    DatabaseReference databaseSchedule;
    ListView listViewSchedule;
    ListView myListViewSchedule;
    List<Schedule> scheduleList;
    List<Schedule> myScheduleList;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);

        startService(getCurrentFocus());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseSchedule = FirebaseDatabase.getInstance().getReference("schedule");
        auth = FirebaseAuth.getInstance();
        u = auth.getCurrentUser();

        Query query = FirebaseDatabase.getInstance().getReference("schedule")
                .orderByChild("email")
                .equalTo(u.getEmail());

        query.addListenerForSingleValueEvent(listener);

        listViewSchedule = (ListView) findViewById(R.id.listViewSchedule);
        myListViewSchedule = (ListView) findViewById(R.id.ListViewSchedule);

        scheduleList = new ArrayList<>();
        myScheduleList = new ArrayList<>();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser u = auth.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddTimeActivity.class);
                startActivity(i);
            }
        });

        if (u == null) {
            System.out.println("NO LOGIN");
            return;
        }
        TextView email = header.findViewById(R.id.side_user_email);
        email.setText(u.getEmail());
    }


    public void startService(View view){
        Intent i = new Intent(this, BackgroundService.class);
        startService(i);
    }


    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            scheduleList.clear();
            for(DataSnapshot eventSnapshot: dataSnapshot.getChildren()) {
                Schedule schedule = eventSnapshot.getValue(Schedule.class);
                scheduleList.add(schedule);
            }

            ScheduleList adapter = new ScheduleList(Sidebar.this, scheduleList);
            listViewSchedule.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_schedule) {
            Intent i = new Intent(this, MySchedule.class);
            startActivity(i);
        }

        else if (id == R.id.Upperbody){
            Intent i = new Intent(this, UpperBody.class);
            startActivity(i);
        }

        else if (id == R.id.Lowerbody){
            Intent i = new Intent(this, LowerBody.class);
            startActivity(i);
        }

        else if (id == R.id.backcore){
            Intent i = new Intent(this, BackCore.class);
            startActivity(i);
        }

        else if (id == R.id.cardio){
            Intent i = new Intent(this, Cardio.class);
            startActivity(i);
        }

        else if (id == R.id.logout) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            stopService(getCurrentFocus());
            auth.signOut();

            sharedpreferences=getApplicationContext().getSharedPreferences("Preferences", 0);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove("LOGIN");
            editor.apply();

            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        return true;
    }


    public void stopService(View view){
        Intent i = new Intent(this, BackgroundService.class);
        stopService(i);
    }
}
