package lifter.app;

import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UpperPop extends AppCompatActivity {

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
        descript.setText("The upper body region contains multiple muscles. Some of these muscles include the chest, shoulders, biceps, triceps, and forearms. Working out your upper body region can help reduce the risk of injury and protect your bones.");

    }
}


