package dyl.anjon.es.todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxTable;

import dyl.anjon.es.todo.fragments.ListFragment;
import dyl.anjon.es.todo.fragments.NavigationDrawerFragment;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

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
	 * Used for dropbox integration
	 */
	static final int REQUEST_LINK_TO_DBX = 0;
	private DbxAccountManager mAccountManager;
	private DbxAccount mAccount;
	private DbxDatastore mStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up dropbox
		mAccountManager = DbxAccountManager.getInstance(
				getApplicationContext(), getString(R.string.db_app_key),
				getString(R.string.db_app_secret));

		if (mAccountManager.hasLinkedAccount()) {
			mAccount = mAccountManager.getLinkedAccount();

			// mAccount data is available here
			try {
				mStore = DbxDatastore.openDefault(mAccount);
			} catch (DbxException e) {
				Log.e("ToDo", e.getMessage());
			}

			DbxTable tasksTbl = mStore.getTable("tasks");
			try {
				DbxTable.QueryResult results = tasksTbl.query();
				Log.i("ToDo", results.toString());
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * tasksTbl.insert().set("taskname", "Milk").set("completed",
			 * false); tasksTbl.insert().set("taskname",
			 * "Fresh Pasta").set("completed", false);
			 * tasksTbl.insert().set("taskname",
			 * "Toilet Paper").set("completed", false);
			 * tasksTbl.insert().set("taskname", "Aubergine").set("completed",
			 * false); tasksTbl.insert().set("taskname",
			 * "Shampoo").set("completed", false);
			 * tasksTbl.insert().set("taskname", "Bin Bags").set("completed",
			 * false); tasksTbl.insert().set("taskname",
			 * "Fresh Fish").set("completed", false);
			 * tasksTbl.insert().set("taskname", "Pesto").set("completed",
			 * false); tasksTbl.insert().set("taskname",
			 * "Bread").set("completed", false);
			 * tasksTbl.insert().set("taskname", "Crisps").set("completed",
			 * false); tasksTbl.insert().set("taskname",
			 * "Cheese").set("completed", false);
			 * tasksTbl.insert().set("taskname",
			 * "Bousin/Soft Cheese").set("completed", false);
			 * tasksTbl.insert().set("taskname", "Pasta").set("completed",
			 * false); tasksTbl.insert().set("taskname",
			 * "Thai jar").set("completed", false);
			 * tasksTbl.insert().set("taskname", "Sweetcorn").set("completed",
			 * false); tasksTbl.insert().set("taskname",
			 * "Chopped Tomatoes").set("completed", false);
			 * tasksTbl.insert().set("taskname", "Chives").set("completed",
			 * false); tasksTbl.insert().set("taskname",
			 * "Sprinkle Cereal").set("completed", false);
			 * tasksTbl.insert().set("taskname", "OJ").set("completed", false);
			 */
			try {
				mStore.sync();
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mStore.close();

		} else {
			mAccountManager.startLink((Activity) MainActivity.this,
					REQUEST_LINK_TO_DBX);
		}

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, ListFragment.newInstance("Shopping"))
				.commit();
	}

	public void onSectionAttached(String string) {
		mTitle = string;
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_LINK_TO_DBX) {
			if (resultCode == Activity.RESULT_OK) {
				mAccount = mAccountManager.getLinkedAccount();
				// mAccount data available here!
			} else {

			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

}
