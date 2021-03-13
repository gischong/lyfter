package lifter.app;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class LowerBody extends AppCompatActivity {

    ListView lowerListView;
    String[] lowerexercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lower_body);

        Resources res = getResources();
        lowerListView = (ListView) findViewById(R.id.lowerlistview);
        lowerexercises = res.getStringArray(R.array.lowerexercises);

        LowerAdapter adapter = new LowerAdapter(this, lowerexercises);
        lowerListView.setAdapter(adapter);

        ImageButton infobtn = (ImageButton) findViewById(R.id.lowerinfobtn);

        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LowerBody.this, LowerPop.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // the menu being referenced here is the menu.xml from res/menu/menu.xml

        inflater.inflate(R.menu.menu, menu);

        MenuItem back = menu.findItem(R.id.action_back);
        MenuItem clear = menu.findItem(R.id.action_clear);

        back.setVisible(true);
        clear.setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }


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

            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }
}