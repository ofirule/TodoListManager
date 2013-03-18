package il.ac.huji.todolist;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import il.ac.huji.todolist.MyAdapter;
import il.ac.huji.todolist.R;

public class TodoListManagerActivity extends Activity 
{
	ListView list;
	MyAdapter arrAdapt;
	ArrayList<String> items;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
		
		items = new ArrayList<String>();
		
		arrAdapt = new MyAdapter(this, android.R.layout.simple_list_item_1, items);
		arrAdapt.setNotifyOnChange(true);
		
		list = (ListView)findViewById(R.id.lstTodoItems);  
		list.setAdapter(arrAdapt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.menuItemAdd:
            EditText str = (EditText) findViewById(R.id.edtNewItem);
            arrAdapt.add((str.getText()).toString());
    		str.setText("");
            return true;
 
        case R.id.menuItemDelete:
        	int selectedRow = list.getSelectedItemPosition();
        	if (selectedRow < 0)
        		return true;
        	
            items.remove(selectedRow);
		    TodoListManagerActivity.this.runOnUiThread(new Runnable()
		    {
		        public void run() 
		        {
		            arrAdapt.notifyDataSetChanged();
		        }
		    });
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }   
}
