package dyl.anjon.es.models;

import java.util.ArrayList;

public class ToDoList {

	private String name;
	private ArrayList<String> items;

	public ToDoList(String name) {
		this.name = name;
		this.items = new ArrayList<String>();
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
	public ArrayList<String> getItems() {
		return items;
	}

	/**
	 * @param string
	 *            the string to add
	 */
	public void add(String string) {
		this.items.add(string);
	}

}
