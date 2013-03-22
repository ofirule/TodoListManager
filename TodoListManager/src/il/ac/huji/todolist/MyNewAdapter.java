
package il.ac.huji.todolist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyNewAdapter extends ArrayAdapter<TodoItem> {
	public MyNewAdapter(
			TodoListManagerActivity todoListManagerActivity,
			ArrayList<TodoItem> entries) {
		super(todoListManagerActivity, android.R.layout.simple_list_item_1, entries);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TodoItem item = getItem(position);
		View itemView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
		TextView textViewItem = (TextView)itemView.findViewById(R.id.txtTodoTitle);
		textViewItem.setText(item.title);
		TextView textViewDate = (TextView)itemView.findViewById(R.id.txtTodoDueDate);
		if (item.dueDate != null)
		{
			SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
			textViewDate.setText(date.format(item.dueDate));
			Date curDate = new Date(System.currentTimeMillis());
			if (curDate.after(item.dueDate))
			{
				textViewDate.setTextColor(Color.RED);
				textViewItem.setTextColor(Color.RED);
			}
		}
		else
		{
			textViewDate.setText("No due date");
		}
		return itemView;
	}
}
