package lifter.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LowerAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ListView lowerListView;
    String[] lowerexercies;

    public LowerAdapter(Context c, String[] i){
        lowerexercies = i;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return lowerexercies.length;
    }

    @Override
    public Object getItem(int i) {
        return lowerexercies[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.bulletedlist_layout, null);
        TextView exercisesTV = (TextView) v.findViewById(R.id.exercise);

        String lowerbodyexerciese = lowerexercies[i];

        exercisesTV.setText(lowerbodyexerciese);

        return v;
    }
}

