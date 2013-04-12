package il.ac.huji.todolist;

import java.util.List;

import org.json.JSONObject;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import android.content.Context;
import android.database.Cursor;

public class TodoDAL {
	private DBHandler dbh;
    private static final String COL_ID = "_id";
    private static final String COL_TITLE = "title";
    private static final String COL_DUE = "due";
	public TodoDAL(Context context) 
	{ 
		dbh = new DBHandler(context);
		Parse.initialize(context, context.getResources().getString(R.string.parseApplication), context.getResources().getString(R.string.clientKey));
		PushService.subscribe(context, "", TodoListManagerActivity.class);
		PushService.setDefaultPushCallback(context, TodoListManagerActivity.class);
		ParseUser.enableAutomaticUser();
	}
	
	Cursor getCursor()
	{
		return dbh.getReadableDatabase().query("todo", new String[] { COL_ID, COL_TITLE, COL_DUE }, null, null, null, null, null);
	}
	
	public boolean update(ITodoItem todoItem){
		if(todoItem == null || todoItem.getTitle() == null)
			return false;

		try{
			if (!dbh.updateItem(todoItem))
				return false;
			
			final ITodoItem wantedItem = todoItem;
			
			ParseQuery query = new ParseQuery("todo");
			query.whereEqualTo(COL_TITLE, todoItem.getTitle());
			
			List<ParseObject> test = query.find();
		    for(int i = 0; i < test.size(); ++i){
		    	ParseObject pars = test.get(i);
		    	if (wantedItem.getDueDate() != null)
		    		pars.put(COL_DUE, wantedItem.getDueDate().getTime());
		    	else
		    		pars.put(COL_DUE, JSONObject.NULL);
				pars.saveInBackground();
		    }
		}
		catch (Exception e) 
		{
			return false;
		}
		
		return true;
	}
	
	public boolean delete(ITodoItem todoItem){ 
		if(todoItem == null || todoItem.getTitle() == null){
			return false;
		}
		try{
			if (!dbh.deleteItem(todoItem))
				return false;
			
			ParseQuery query = new ParseQuery("todo");
			query.whereEqualTo(COL_TITLE, todoItem.getTitle());
			if (todoItem.getDueDate() == null){
	    		query.whereEqualTo(COL_DUE, JSONObject.NULL);
			}
	    	else{
				query.whereEqualTo(COL_DUE, todoItem.getDueDate().getTime());
	    	}
			List<ParseObject> test = query.find();
		    for(int x = 0; x < test.size(); x++){
		        ParseObject currentObject = test.get(x);
		        currentObject.deleteInBackground();
		    }
		}
		catch (Exception e){
			return false;
		}
		
		return true;
	}
	
	public List<ITodoItem> all(){ return dbh.getAllItems();	}
	
	
	public boolean insert(ITodoItem todoItem){
		if(todoItem == null || todoItem.getTitle() == null){
			return false;
		}
		
		try{
			if (!dbh.addItem(todoItem)){
				return false;
			}
			
			ParseObject parser = new ParseObject("todo");
			parser.put(COL_TITLE, todoItem.getTitle());
			
			if (todoItem.getDueDate() == null){
				parser.put(COL_DUE, JSONObject.NULL);
			}
			else{
				parser.put(COL_DUE, todoItem.getDueDate().getTime());
			}
			
			parser.saveInBackground();
		}
		catch (Exception e){
			return false;
		}
		return true;
	}
	
	
}
