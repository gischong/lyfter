package lifter.app;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class BackCorePop extends AppCompatActivity {
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
        descript.setText("Your core is made up of two major muscle groups, the abdomen and obliques. Training these muscles can improve balance and stability along with making everyday activities easier to do. Working out your back will help when you decide to train other parts of your body.");
    }
}
