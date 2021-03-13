package lifter.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BackCoreAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ListView backcoreListView;
    String[] backcore;

    public BackCoreAdapter(Context c, String[] i){
        backcore = i;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return backcore.length;
    }

    @Override
    public Object getItem(int i) {
        return backcore[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.bulletedlist_layout, null);
        TextView exercisesTV = (TextView) v.findViewById(R.id.exercise);

        String upperbodyexerciese = backcore[i];

        exercisesTV.setText(upperbodyexerciese);

        return v;
    }
}
