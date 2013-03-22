package il.ac.huji.todolist;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddNewTodoItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Add New Item");
		setContentView(R.layout.add_new_todo_item_activity);

		
		findViewById(R.id.btnCancel).setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		findViewById(R.id.btnOK).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText itemNameActivity = (EditText)findViewById(R.id.edtNewItem);
				String itemStr = itemNameActivity.getText().toString();
				if (itemStr == null || "".equals(itemStr)) 
				{
					setResult(RESULT_CANCELED);
					finish();
				}
				else 
				{
					DatePicker dPicker = (DatePicker)findViewById(R.id.datePicker);
					Calendar cal = Calendar.getInstance();
					cal.set(dPicker.getYear(), dPicker.getMonth(), dPicker.getDayOfMonth());
					Intent userIntent = new Intent();
					userIntent.putExtra("title", itemStr);
					userIntent.putExtra("dueDate", cal.getTime());
					setResult(RESULT_OK, userIntent);
					finish();
				}
			}
		});
	}
	
}
