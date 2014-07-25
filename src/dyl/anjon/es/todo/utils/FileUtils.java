package dyl.anjon.es.todo.utils;

import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxPath.InvalidPathException;

import dyl.anjon.es.models.ToDoItem;
import dyl.anjon.es.models.ToDoList;

public class FileUtils {

	public static boolean saveListsForAccount(ArrayList<ToDoList> lists,
			DbxAccountManager mDbxAcctMgr) {

		StringBuilder string = new StringBuilder();
		for (int i = 0; i < lists.size(); i++) {
			string.append(lists.get(i).toString());
		}

		Log.d(Utils.LOG_TAG, "Saving file:");
		Log.d(Utils.LOG_TAG, string.toString());

		DbxFile testFile = null;
		try {
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());
			DbxPath filePath = new DbxPath(ToDoList.FILENAME);
			if (dbxFs.exists(filePath)) {
				testFile = dbxFs.open(filePath);
			} else {
				testFile = dbxFs.create(filePath);
			}
			testFile.writeString(string.toString());
		} catch (Unauthorized e) {
			Log.e(Utils.LOG_TAG, e.getMessage());
		} catch (InvalidPathException e) {
			Log.e(Utils.LOG_TAG, e.getMessage());
		} catch (DbxException e) {
			Log.e(Utils.LOG_TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(Utils.LOG_TAG, e.getMessage());
		} finally {
			if (testFile != null) {
				testFile.close();
			}
		}

		return true;
	}

	public static ArrayList<ToDoList> openListsForAccount(
			DbxAccountManager mDbxAcctMgr) {
		DbxFile testFile = null;
		String contents = "";
		try {
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mDbxAcctMgr
					.getLinkedAccount());
			DbxPath filePath = new DbxPath(ToDoList.FILENAME);
			if (dbxFs.exists(filePath)) {
				testFile = dbxFs.open(filePath);
			} else {
				testFile = dbxFs.create(filePath);
			}
			contents = testFile.readString();
		} catch (InvalidPathException e) {
			Log.e(Utils.LOG_TAG, e.getMessage());
		} catch (DbxException e) {
			Log.e(Utils.LOG_TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(Utils.LOG_TAG, e.getMessage());
		} finally {
			if (testFile != null) {
				testFile.close();
			}
		}

		return parseLists(contents);
	}

	private static ArrayList<ToDoList> parseLists(String contents) {
		ArrayList<ToDoList> lists = new ArrayList<ToDoList>();
		String listStrings[] = contents.split(ToDoList.LIST_DELIMITTER);
		for (int i = 0; i < listStrings.length; i++) {
			lists.add(parseList(listStrings[i].trim()));
		}

		return lists;
	}

	private static ToDoList parseList(String contents) {
		String listComponents[] = contents.split(ToDoList.LIST_NAME_DELIMITTER);
		ToDoList list = new ToDoList(listComponents[0]);
		if (listComponents.length > 1) {
			String listItems[] = listComponents[1]
					.split(ToDoItem.ITEM_DELIMITTER);
			for (int i = 0; i < listItems.length; i++) {
				String itemString = listItems[i].substring(2);
				ToDoItem item = new ToDoItem(itemString);
				if (listItems[i].startsWith(ToDoItem.DONE)) {
					item.setIsDone(true);
				} else if (listItems[i].startsWith(ToDoItem.NOT_DONE)) {
					item.setIsDone(false);
				}
				list.add(item);
			}
		}

		return list;
	}

}
