package dyl.anjon.es.models;

import java.util.ArrayList;

public class ToDoList {

	private String name;
	private ArrayList<ToDoItem> items;

	public ToDoList(String name) {
		this.name = name;
		this.items = new ArrayList<ToDoItem>();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the items
	 */
	public ArrayList<ToDoItem> getItems() {
		return items;
	}

	/**
	 * @param item
	 *            the item to add
	 */
	public void add(ToDoItem item) {
		this.items.add(item);
	}

	/**
	 * @param string
	 *            the string to use to create a new item and add
	 */
	public void add(String string) {
		this.items.add(new ToDoItem(string));
	}

}
