package dyl.anjon.es.models;

public class ToDoItem {

	private String name;
	private boolean isDone;

	public ToDoItem(String name) {
		this.name = name;
		this.isDone = false;
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
	 * @return the isDone
	 */
	public Boolean isDone() {
		return isDone;
	}

	/**
	 * @param isDone
	 *            the isDone to set
	 */
	public void setIsDone(boolean isDone) {
		this.isDone = isDone;
	}

}
