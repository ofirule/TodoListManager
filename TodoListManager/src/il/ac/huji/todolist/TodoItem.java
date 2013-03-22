package il.ac.huji.todolist;

import java.util.Date;

public class TodoItem {

	public TodoItem(String title, Date dueDate) {
		this.title = title;
		this.dueDate = dueDate;
	}
	
	public String title;
	public Date dueDate;
}