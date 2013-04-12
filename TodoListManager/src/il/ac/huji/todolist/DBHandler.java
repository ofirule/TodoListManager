package il.ac.huji.todolist;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressLint("SimpleDateFormat")
public class DBHandler extends SQLiteOpenHelper 
{
	//CONSTANTS
    private static final int DB_VER = 1;
    private static final String DATABASE_NAME = "todo_db";
    private static final String TABLE_NAME = "todo";
    private static final String COL_ID = "_id";
    private static final String COL_TITLE = "title";
    private static final String COL_DUE = "due";
    
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DB_VER);
    }

	boolean addItem(ITodoItem item) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues vals = new ContentValues();
		vals.put(COL_TITLE, item.getTitle());
		if (item.getDueDate() != null)
			vals.put(COL_DUE, item.getDueDate().getTime());
		else
			vals.putNull(COL_DUE);
		long result = db.insert(TABLE_NAME, null, vals);
		db.close();
		return (result > -1);
	}

	
	
	public ITodoItem getItem(String title){
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, new String[] { COL_ID,
				COL_TITLE, COL_DUE }, COL_TITLE + "=?",
				new String[] { title }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
	    
		String isDateNull = cursor.getString(2);
		TodoItem item = new TodoItem(cursor.getString(1), (isDateNull != null) ? new Date(cursor.getLong(2)) : null);
		return item;
	}
	 
	public List<ITodoItem> getAllItems(){
		List<ITodoItem> allItems = new ArrayList<ITodoItem>();
	    String selectQuery = "SELECT  * FROM " + TABLE_NAME;
	 
	    SQLiteDatabase db = this.getWritableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    
	    if (cursor.moveToFirst()) {
	        do {
	        	String isDateNull = cursor.getString(2);
	            TodoItem item = new TodoItem(cursor.getString(1), (isDateNull != null) ? new Date(cursor.getLong(2)) : null);
	            // Adding contact to list
	            allItems.add(item);
	        }
	        while (cursor.moveToNext());
	    }
	    
	    return allItems;
	}
	
	public boolean updateItem(ITodoItem item){
		SQLiteDatabase db = this.getWritableDatabase();
		 
		ContentValues vals = new ContentValues();
		vals.put(COL_TITLE, item.getTitle());
		if (item.getDueDate() != null)
			vals.put(COL_DUE, item.getDueDate().getTime());
		else
			vals.putNull(COL_DUE);
	 
	    return (db.update(TABLE_NAME, vals, COL_TITLE + " = ?", new String[] { item.getTitle() }) > 0);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + 
				COL_TITLE + " TEXT," + COL_DUE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	 
	public boolean deleteItem(ITodoItem item){
		SQLiteDatabase db = this.getWritableDatabase();
		long result = db.delete(TABLE_NAME, COL_TITLE + " = ?", new String[] { item.getTitle() });
	    db.close();
	    return (result > 0);
	}
}
