package lifter.app;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
public class BackgroundService extends Service{
    FirebaseAuth auth;
    FirebaseUser u;
    DatabaseReference ref;
    List<String> dayArray = new ArrayList<String>();
    List<String> fromArray = new ArrayList<String>();


    @Override
    public void onCreate(){
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        ref = FirebaseDatabase.getInstance().getReference("schedule");
        auth = FirebaseAuth.getInstance();
        u = auth.getCurrentUser();


        Handler mHandler = new android.os.Handler();
        ping(mHandler);
        return START_STICKY;
    }


    @Override
    public void onDestroy(){
    }


    private void ping(Handler mHandler) {
        try {
            Query my_query = FirebaseDatabase.getInstance().getReference("schedule")
                    .orderByChild("email")
                    .equalTo(u.getEmail());

            my_query.addListenerForSingleValueEvent(my_listener);

        } catch (Exception e) {
            Log.e("Error", "In onStartCommand");
            e.printStackTrace();
        }
        scheduleNext(mHandler);
    }

    private void scheduleNext(final Handler mHandler) {
        mHandler.postDelayed(new Runnable() {
            public void run() { ping(mHandler); }
        }, 60000);
    }


    @Override
    public IBinder onBind(Intent intent){ return null; }
    ValueEventListener my_listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            dayArray.clear();
            fromArray.clear();

            for (DataSnapshot scheduleSnapshot : dataSnapshot.getChildren()) {
                Schedule schedule = scheduleSnapshot.getValue(Schedule.class);

                String day = schedule.getDay();
                String from = schedule.getFromSpecific();
                String fromStr = schedule.getFrom();

                dayArray.add(day);
                fromArray.add(from);
            }
            workoutTimeCheck(dayArray, fromArray);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };


    //----------------------------------------
    public void workoutTimeCheck(List<String> dayArray, List<String> fromArray){

        String [] workoutDates = new String[dayArray.size()];

        for(int i = 0; i < dayArray.size(); i++){
            workoutDates[i] = dayArray.get(i) + "-" + fromArray.get(i);
        }

        SimpleDateFormat sdf =  new SimpleDateFormat("EEEE-HH:mm");
        String currentDate = sdf.format(new Date());

        //create workoutDates
        //String[] workoutDates = {"Monday-13:00", "Wednesday-14:30", "Friday-13:45"};
        //Convert workoutDates into alarmDates

        HashMap<String, String> alarmDatesDict = new HashMap<String, String>();

        timeConv(workoutDates, alarmDatesDict);


        // Fix here
        if(currentDate.equals(alarmDatesDict.get(currentDate))){
            sendNotification();
        }
    }


    public static void timeConv(String[] workoutDates, HashMap<String, String> alarmDatesDict){

        for(int i = 0; i < workoutDates.length; i++){
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf =  new SimpleDateFormat("EEEE-HH:mm");

            try{
                Date date = sdf.parse(workoutDates[i]);
                cal.setTime(date);
            }
            catch(Exception e){
                System.out.println("Workout date not accepted.");
                System.exit(0);
            }

            cal.add(Calendar.MINUTE, -30);
            Date halfHourBack = cal.getTime();

            String alarmDate = sdf.format(halfHourBack);
            alarmDatesDict.put(alarmDate, alarmDate);
        }
    }
    //----------------------------------------
    private void sendNotification() {
        Intent notifDest = new Intent(this, MySchedule.class);
        PendingIntent pendingNotifDest = PendingIntent.getActivity
                (this, 1, notifDest, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notif = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.flex_logo)
                .setContentTitle("Time for your workout!")
                .setContentText("Workout today in 30 minutes.")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingNotifDest)
                .setAutoCancel(true);
        NotificationManager notifMan = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "YOUR_CHANNEL_ID";
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel Swole",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notifMan.createNotificationChannel(channel);
            notif.setChannelId(channelId);
        }
        notifMan.notify(0, notif.build());
    }
}