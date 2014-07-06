package dyl.anjon.es.models;

import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxPath.InvalidPathException;

public class ToDoList {

	private String name;
	private ArrayList<ToDoItem> items;
	private final static String LIST_NAME_DELIMITTER = "\n--------------------\n";
	private final static String LIST_DELIMITTER = "\n\n";
	private final static String FILENAME = "todo.txt";
	private final static String LOG_TAG = "ToDo";

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

	public String toString() {
		StringBuilder string = new StringBuilder(getName());
		string.append(LIST_NAME_DELIMITTER);
		for (int i = 0; i < getItems().size(); i++) {
			string.append(getItems().get(i).toString());
		}
		string.append(LIST_DELIMITTER);

		return string.toString();
	}

	public static boolean saveListsForAccount(ArrayList<ToDoList> lists,
			DbxAccountManager mDbxAcctMgr) {

		StringBuilder string = new StringBuilder();
		for (int i = 0; i < lists.size(); i++) {
			string.append(lists.get(i).toString());
		}

		DbxFile testFile = null;
		try {
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());
			testFile = dbxFs.create(new DbxPath(FILENAME));
			testFile.writeString(string.toString());
		} catch (Unauthorized e) {
			Log.e(LOG_TAG, e.getMessage());
		} catch (InvalidPathException e) {
			Log.e(LOG_TAG, e.getMessage());
		} catch (DbxException e) {
			Log.e(LOG_TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage());
		} finally {
			if (testFile != null) {
				testFile.close();
			}
		}
		return true;
	}

	public static ArrayList<ToDoList> openListsForAccount(DbxAccountManager mDbxAcctMgr) {
		ArrayList<ToDoList> lists = new ArrayList<ToDoList>();

		DbxFile testFile = null;
		String contents = "";
		try {
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());
			testFile = dbxFs.open(new DbxPath(FILENAME));
			contents = testFile.readString();
			Log.d("Dropbox Test", "File contents: " + contents);
		} catch (InvalidPathException e) {
			Log.e(LOG_TAG, e.getMessage());
		} catch (DbxException e) {
			Log.e(LOG_TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(LOG_TAG, e.getMessage());
		} finally {
			if (testFile != null) {
				testFile.close();
			}
		}
		
		String listStrings[] = contents.split(LIST_DELIMITTER);
		for(int i = 0; i < listStrings.length; i++) {
			String listComponents[] = listStrings[i].split(LIST_NAME_DELIMITTER);
			ToDoList list = new ToDoList(listComponents[0]);
			String listItems[] = listComponents[1].split(ToDoItem.ITEM_DELIMITTER);
			for(int j = 0; j < listItems.length; j++) {
				String itemString = listItems[j].substring(2);
				Log.i(LOG_TAG, itemString);
				ToDoItem item = new ToDoItem(itemString);
				if (listItems[j].startsWith(ToDoItem.DONE)) {
					item.setIsDone(true);
				} else if(listItems[j].startsWith(ToDoItem.NOT_DONE)) {
					item.setIsDone(false);
				}
				list.add(item);
			}
			lists.add(list);
		}

		return lists;
	}

}
