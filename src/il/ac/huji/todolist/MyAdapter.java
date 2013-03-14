package il.ac.huji.todolist;

import java.util.List; 
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<String> {
    public MyAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = (TextView)super.getView(position, convertView, parent);
		if (position % 2 == 1)
			view.setTextColor(Color.BLUE);
		else
			view.setTextColor(Color.RED);
		return view;
    }
}