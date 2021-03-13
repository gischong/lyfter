package lifter.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CardioAdapter extends BaseAdapter {
    LayoutInflater inflater;
    ListView upperListView;
    String[] cardio;

    public CardioAdapter(Context c, String[] i){
        cardio = i;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cardio.length;
    }

    @Override
    public Object getItem(int i) {
        return cardio[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.bulletedlist_layout, null);
        TextView exercisesTV = (TextView) v.findViewById(R.id.exercise);

        String cardioexercises = cardio[i];

        exercisesTV.setText(cardioexercises);

        return v;
    }
}

