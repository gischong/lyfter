package lifter.app;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class BackCore extends AppCompatActivity {

    ListView backcoreListView;
    String[] backcore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_core);

        Resources res = getResources();
        backcoreListView = (ListView) findViewById(R.id.backcorelistview);
        backcore = res.getStringArray(R.array.backcore);

        BackCoreAdapter adapter = new BackCoreAdapter(this, backcore);
        backcoreListView.setAdapter(adapter);

        ImageButton infobtn = (ImageButton) findViewById(R.id.backcoreinfobtn);

        infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BackCore.this, BackCorePop.class));
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
