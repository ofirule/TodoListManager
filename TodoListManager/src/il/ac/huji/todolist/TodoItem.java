package il.ac.huji.todolist;

import java.util.Date;

public class TodoItem implements ITodoItem {

	public TodoItem(String title, Date dueDate) {
		this.title = title;
		this.dueDate = dueDate;
	}
	@Override
	public String getTitle() {
		return this.title;
	}
	@Override
	public Date getDueDate() {
		return this.dueDate;
	}
	
	
	private String title;
	private Date dueDate;
	
}