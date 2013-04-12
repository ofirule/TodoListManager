package il.ac.huji.todolist;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MyNewAdapter extends SimpleCursorAdapter{

	Context _context;

	@SuppressWarnings("deprecation")
	public MyNewAdapter(Context context, int layout, Cursor c, String[] from, int[] to) 
	{
		super(context, layout, c, from, to);
		 _context = context;
	}

	@SuppressLint("SimpleDateFormat")
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.row, null);

		Cursor cursor = (Cursor)getItem(position);
		cursor.moveToPosition(position);
		boolean isDateNull = (cursor.getString(2) == null);//TODO
		TodoItem item;
		if (isDateNull){
			item  = new TodoItem(cursor.getString(1), null);
		}
		else{
			item = new TodoItem(cursor.getString(1), new Date(cursor.getLong(2)));
		}
		TextView textViewItem = (TextView)view.findViewById(R.id.txtTodoTitle);
		textViewItem.setText(item.getTitle());
		TextView textViewDate = (TextView)view.findViewById(R.id.txtTodoDueDate);
		if (item.getDueDate() != null)
		{
			SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
			textViewDate.setText(date.format(item.getDueDate()));
			Date curDate = new Date(System.currentTimeMillis());
			if (curDate.after(item.getDueDate()))
			{
				textViewDate.setTextColor(Color.RED);
				textViewItem.setTextColor(Color.RED);
			}
		}
		else
		{
			textViewDate.setText("No due date");
		}

		return view;
	}
}