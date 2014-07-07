package dyl.anjon.es.models;

public class ToDoItem {

	private String name;
	private boolean isDone;
	public final static String ITEM_DELIMITTER = "\n";
	public final static String DONE = "✔ ";
	public final static String NOT_DONE = "- ";

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

	public String toString() {
		StringBuilder string = new StringBuilder();
		if (isDone()) {
			string.append(DONE);
		} else {
			string.append(NOT_DONE);
		}
		string.append(getName());
		string.append(ITEM_DELIMITTER);

		return string.toString();
	}

}
