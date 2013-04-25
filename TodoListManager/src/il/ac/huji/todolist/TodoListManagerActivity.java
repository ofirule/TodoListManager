package il.ac.huji.todolist;


import java.util.Date;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import il.ac.huji.todolist.AddNewTodoItemActivity;

public class TodoListManagerActivity extends Activity 
{
	ListView listView;
	Cursor myCursor;
    private MyNewAdapter myAdapter;
    private TodoDAL myDal;
	final static private String callStr = "Call ", telStr = "tel:";
	final static private int callClickPos = 1;
	public final static String SETTINGS_HASHTAG_KEY = "hashtag_key";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_manager);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		myDal = new TodoDAL(this);
        String[] from = { "title", "due" };
        int[] to = { R.id.txtTodoTitle, R.id.txtTodoDueDate };
        myAdapter = new MyNewAdapter(this, R.layout.row, myDal.getCursor(), from, to);
        
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String stringPrefs = sharedPrefs.getString(SETTINGS_HASHTAG_KEY, "todoapp");
        
        HandleTwitterTask task = new HandleTwitterTask(TodoListManagerActivity.this, stringPrefs, myDal, myAdapter);
        task.execute();
        
        listView = (ListView)findViewById(R.id.lstTodoItems);
        listView.setAdapter(myAdapter);
        registerForContextMenu(listView);
        listView = (ListView)findViewById(R.id.lstTodoItems);
        registerForContextMenu(listView);
                       
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo ACMInfo = (AdapterContextMenuInfo)item.getMenuInfo();
        int selectedItemIndex = ACMInfo.position;
        myCursor.moveToPosition(selectedItemIndex);
        boolean isDateNull = (myCursor.getString(2) == null);
        TodoItem selectedItem;
        if (isDateNull){
        	selectedItem = new TodoItem(myCursor.getString(1), null);
        }
        else{
        	selectedItem = new TodoItem(myCursor.getString(1), new Date(myCursor.getLong(2)));
        }
		switch (item.getItemId())
		{
			case R.id.menuItemCall:
				Intent intentToCall = new Intent(Intent.ACTION_DIAL);
				intentToCall.setData(Uri.parse(selectedItem.getTitle().replace(callStr, telStr)));
			    startActivity(intentToCall);
				break;
			case R.id.menuItemDelete:
				myDal.delete(selectedItem);
                refresh();
                break;
		}
		return true;
	}
	
	public void refresh(){
		myCursor = myDal.getCursor();
		myAdapter.changeCursor(myCursor);
		myAdapter.notifyDataSetChanged();
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.menu_context, menu);
        AdapterContextMenuInfo ACMInfo = (AdapterContextMenuInfo)menuInfo;
        
        myCursor = myAdapter.getCursor();
        myCursor.moveToPosition(ACMInfo.position);
        
        String title = myCursor.getString(1);
		menu.setHeaderTitle(title);
		if (title.contains(callStr))
			menu.getItem(callClickPos).setTitle(title);
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
    	case R.id.menuItemSettings:
    		Intent settingsIntent = new Intent(this, SettingsActivity.class);
    		startActivityForResult(settingsIntent, 1338);
    		
    		return true;
    		
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
    		myDal.insert(new TodoItem(data.getStringExtra("title"), (Date)data.getSerializableExtra("dueDate")));
            refresh();    	
            }
    }

}