package lifter.app;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class CardioPop extends AppCompatActivity {
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
        descript.setText("Your heart is the most important muscle in your body. Like any other muscle, if it isn't used then it will begin to weaken. Aerobic exercises can help improve your immune system, regulate your weight, and blood sugar levels. ");
    }
}
