package dyl.anjon.es.todo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.dropbox.sync.android.DbxAccountManager;

import dyl.anjon.es.models.ToDoList;
import dyl.anjon.es.todo.fragments.ListFragment;
import dyl.anjon.es.todo.fragments.NavigationDrawerFragment;
import dyl.anjon.es.todo.utils.FileUtils;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,
		ListFragment.ListCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	/**
	 * Used to store the lists of todo lists
	 */
	private ArrayList<ToDoList> lists = new ArrayList<ToDoList>();;

	/**
	 * Used to store the currently selected list
	 */
	private int selectedListIndex;

	/**
	 * Dropbox bits and bobs
	 */
	static final int REQUEST_LINK_TO_DBX = 0;
	private DbxAccountManager mDbxAcctMgr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// set up Dropbox
		mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(),
				getString(R.string.db_app_key),
				getString(R.string.db_app_secret));

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		openLists();

		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout), lists);

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		selectedListIndex = position;
		ToDoList list = lists.get(selectedListIndex);
		fragmentManager.beginTransaction()
				.replace(R.id.container, new ListFragment(list)).commit();
		mTitle = list.getName();
	}

	@Override
	public void onListChanged(ToDoList list) {
		lists.set(selectedListIndex, list);
		saveLists();
	}

	@Override
	public void onListsChanged(ArrayList<ToDoList> lists) {
		this.lists = lists;
		saveLists();
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.list, menu);
		if (mNavigationDrawerFragment.isDrawerOpen()) {
			mTitle = getString(R.string.app_name);
		} else if (selectedListIndex > 0) {
			ToDoList list = lists.get(selectedListIndex);
			mTitle = list.getName();

		}
		restoreActionBar();
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem action = menu.findItem(R.id.action_link_db);
		MenuItem profile = menu.findItem(R.id.db_profile);
		if ((action != null) && (profile != null)) {
			if (mDbxAcctMgr.hasLinkedAccount()) {
				action.setVisible(false);
				profile.setVisible(true);
				String name = mDbxAcctMgr.getLinkedAccount().getAccountInfo().displayName;
				profile.setTitle(name);
			} else {
				profile.setVisible(false);
			}
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent().setClass(getApplicationContext(),
					SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_link_db:
			mDbxAcctMgr.startLink((Activity) this, REQUEST_LINK_TO_DBX);
			return true;
		case R.id.action_add_list:
			final EditText name = new EditText(this);
			name.setHint("Enter name for new list");
			new AlertDialog.Builder(this)
					.setTitle("New List")
					.setView(name)
					.setNegativeButton("Cancel", null)
					.setPositiveButton("Create",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									ToDoList list = new ToDoList(name
											.getEditableText().toString());
									lists.add(list);
									mNavigationDrawerFragment.adapter
											.refresh(lists);
									saveLists();
								}
							}).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LINK_TO_DBX) {
			if (resultCode == Activity.RESULT_OK) {
				mDbxAcctMgr = DbxAccountManager.getInstance(
						getApplicationContext(),
						getString(R.string.db_app_key),
						getString(R.string.db_app_secret));
				openLists();
			} else {

			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setTitle("Quit")
				.setMessage("Are you sure you want to quit?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}
						}).setNegativeButton("No", null).show();
	}

	public void saveLists() {
		if (mDbxAcctMgr.hasLinkedAccount()) {
			FileUtils.saveListsForAccount(lists, mDbxAcctMgr);
		}
	}

	public void openLists() {
		if (mDbxAcctMgr.hasLinkedAccount()) {
			lists = FileUtils.openListsForAccount(mDbxAcctMgr);
		}
	}

}
