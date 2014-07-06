package dyl.anjon.es.todo;

import java.util.ArrayList;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxTable;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListFragment extends Fragment {

	public ListAdapter adapter;

	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ListFragment newInstance(int sectionNumber) {
		ListFragment fragment = new ListFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public ListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list, container,
				false);
		ListView list = (ListView) rootView.findViewById(R.id.list);

		ArrayList<String> items = new ArrayList<String>();

		DbxAccountManager mAccountManager = DbxAccountManager.getInstance(
				getActivity().getApplicationContext(),
				getString(R.string.db_app_key),
				getString(R.string.db_app_secret));

		if (mAccountManager.hasLinkedAccount()) {
			DbxAccount mAccount = mAccountManager.getLinkedAccount();
			DbxDatastore mStore = null;
			try {
				mStore = DbxDatastore.openDefault(mAccount);
			} catch (DbxException e) {
				Log.e("ToDo", e.getMessage());
			}

			DbxTable tasksTbl = mStore.getTable("tasks");
			try {
				DbxTable.QueryResult results = tasksTbl.query();
				Log.i("ToDo", results.toString());
				for (int i = 0; i < results.count(); i++) {
					String taskname = results.asList().get(i)
							.getString("taskname");
					items.add(taskname);
				}
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				mStore.close();
			}

		}

		adapter = new ListAdapter(inflater, items);
		list.setAdapter(adapter);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
}