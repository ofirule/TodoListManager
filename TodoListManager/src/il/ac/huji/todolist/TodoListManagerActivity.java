package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import il.ac.huji.todolist.AddNewTodoItemActivity;

public class TodoListManagerActivity extends Activity 
{
	ListView list;
	ArrayList<TodoItem> itemsEntries;
	private ArrayAdapter<TodoItem> newAdapter;
	final static private String callStr = "Call ", telStr = "tel:";
	final static private int callClickPos = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);

		itemsEntries = new ArrayList<TodoItem>();
		newAdapter = new MyNewAdapter(this, itemsEntries);

		list = (ListView)findViewById(R.id.lstTodoItems);
		list.setAdapter(newAdapter);
        registerForContextMenu(list);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo adaptInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		TodoItem selectedItem = newAdapter.getItem(adaptInfo.position);
		switch (item.getItemId())
		{
			case R.id.menuItemCall:
				Intent intentToCall = new Intent(Intent.ACTION_DIAL);
				intentToCall.setData(Uri.parse(selectedItem.title.replace(callStr, telStr)));
			    startActivity(intentToCall);
				break;
			case R.id.menuItemDelete:
				newAdapter.remove(selectedItem);
				break;
		}
		return true;
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.menu_context, menu);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
		TodoItem item = (newAdapter.getItem(info.position));
		menu.setHeaderTitle(item.title);
		if (item.title.contains(callStr))
			menu.getItem(callClickPos).setTitle(item.title);
		else
			menu.removeItem(R.id.menuItemCall);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.todo_list_manager, menu);
		return true;
	}
	
	
	/**
     * Event Handling for Individual menu item selected
     * Identify single menu item by it's id
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.menuItemAdd:
        	Intent addIntent = new Intent(this, AddNewTodoItemActivity.class);
    		startActivityForResult(addIntent, 1337);
    		return true;
    		
        default:
            return super.onOptionsItemSelected(item);
        }
    }   
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 1337 && resultCode == RESULT_OK) {
    		String itemStr = data.getStringExtra("title");
    		Date wantedDate = (Date)data.getSerializableExtra("dueDate");
    		newAdapter.add(new TodoItem(itemStr, wantedDate));
    	}
    }

}