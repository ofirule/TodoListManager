package il.ac.huji.todolist;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import org.json.JSONException;
import android.content.SharedPreferences;
import android.widget.SimpleCursorAdapter;

import android.database.Cursor;
import android.os.AsyncTask;
import android.content.DialogInterface;

import android.preference.PreferenceManager;

public class HandleTwitterTask extends AsyncTask<String, Void, ArrayList<TwitObj>> {
	
	private Context _taskContext;
	private String _taskHashtag;
	private TodoDAL _myDAL;
	private ProgressDialog _progressDialog;
	private SimpleCursorAdapter _taskAdapter;
	private long _latestTweetId;
	private DialogInterface.OnClickListener _doNothingListener, _addItemsListener;

	public HandleTwitterTask(Context taskContext, String taskHashtag, TodoDAL myDAL, SimpleCursorAdapter taskAdapter) {
		_myDAL = myDAL;	_taskAdapter = taskAdapter; _taskContext = taskContext;	_taskHashtag = taskHashtag;	
		
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(_taskContext);
		_latestTweetId = prefs.getLong(_taskHashtag + "SinceId", 0);
		setDoNothingListener();
		
	}
	
	
	/////////////////////////////////////PROTECTED_METHODS//////////////////////////////////////////////////////////////

	@Override
	protected ArrayList<TwitObj> doInBackground(String... params) {
		try {
			return TwitObj.getTwitsArray(_taskHashtag,_latestTweetId);
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<TwitObj>();
	}
	
	@Override
	protected void onPreExecute() {
		_progressDialog = new ProgressDialog(_taskContext);
		_progressDialog.setCancelable(false);
		_progressDialog.setTitle("Twitter");
		_progressDialog.setMessage("Searching for twits with the Hashtag: #" + _taskHashtag);
		_progressDialog.show();
		super.onPreExecute();
	}

	@Override 
	protected void onPostExecute(final ArrayList<TwitObj> result) {
		_progressDialog.dismiss();
		setAddItemsListener(result);
		if (result.size() > 0) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(_taskContext);	
			alertDialog.setMessage("TodoListManager found: " + result.size() + " tweets with the given Hashtag. \n Press Yes to add them to list.")
			.setPositiveButton("Yes", _addItemsListener)
			.setNegativeButton("No", _doNothingListener);
			alertDialog.create().show();
		}
		setNewLatestTwitId(result);
		super.onPostExecute(result);
	}

	private void setDoNothingListener(){
		_doNothingListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        };
	}
	
	
	
	
	/////////////////////////////////////PRIVATE_METHODS//////////////////////////////////////////////////////////////

	private void setAddItemsListener(final ArrayList<TwitObj> result){
		_addItemsListener =  new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	for (int j=0; j<result.size(); j++) {
            		if (result.get(j).getId() > _latestTweetId) {
                		_myDAL.insert(new TodoItem(result.get(j).getBody(), null));                		}
            	}
            	
            	Cursor cursor = _myDAL.getCursor();
            	_taskAdapter.changeCursor(cursor);
            	_taskAdapter.notifyDataSetChanged();
            }
        };
	}
	private void setNewLatestTwitId(final ArrayList<TwitObj> result){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(_taskContext);
		SharedPreferences.Editor prefsEditor = sharedPrefs.edit();
		
				
		if (TwitObj.getLatestTweetIdFromArray(result) > sharedPrefs.getLong(_taskHashtag + "SinceId", 0)) {
			prefsEditor.putLong(_taskHashtag + "SinceId", TwitObj.getLatestTweetIdFromArray(result));
			prefsEditor.commit();
		}
	}

}