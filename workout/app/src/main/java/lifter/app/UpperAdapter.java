package lifter.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UpperAdapter extends BaseAdapter {

    LayoutInflater inflater;
    ListView upperListView;
    String[] upperexercies;

    public UpperAdapter(Context c,String[] i){
        upperexercies = i;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return upperexercies.length;
    }

    @Override
    public Object getItem(int i) {
        return upperexercies[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.bulletedlist_layout, null);
        TextView exercisesTV = (TextView) v.findViewById(R.id.exercise);

        String upperbodyexerciese = upperexercies[i];

        exercisesTV.setText(upperbodyexerciese);

        return v;
    }
}
