package lifter.app;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class LowerPop extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popup_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        TextView descripTitle = (TextView) findViewById(R.id.destitle);
        descripTitle.setPaintFlags(descripTitle.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        TextView descript = (TextView) findViewById(R.id.mgdescript);
        descript.setText("Your quadriceps, hamstrings, and calves are major components of your lower body. Some benefits of working out your lower body includes stronger legs for running, preventing lower back pain, and improving balance.");

    }
}
