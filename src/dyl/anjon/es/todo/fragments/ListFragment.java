package dyl.anjon.es.todo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import dyl.anjon.es.adapters.ListAdapter;
import dyl.anjon.es.models.ToDoList;
import dyl.anjon.es.todo.R;

public class ListFragment extends Fragment {

	public ListAdapter adapter;
	private ToDoList list;
	private ListCallbacks mCallbacks;

	public ListFragment(ToDoList list) {
		this.list = list;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list, container,
				false);
		ListView v = (ListView) rootView.findViewById(R.id.list);
		adapter = new ListAdapter(inflater, list.getItems());
		v.setAdapter(adapter);

		final EditText addItem = (EditText) rootView.findViewById(R.id.add_item);
		addItem.setImeActionLabel("Add", KeyEvent.KEYCODE_ENTER);
		addItem.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					String item = addItem.getText().toString();
					if (!item.isEmpty()) {
						list.add(item);
					}
					addItem.setText("");
					adapter.refresh(list.getItems());
					if (mCallbacks != null) {
						mCallbacks.onListChanged(list);
					}
					return false;
				}
				return false;
			}
		});

		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (ListCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement ListCallbacks.");
		}
	}
	
	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface ListCallbacks {
		/**
		 * Called when the list changes.
		 */
		void onListChanged(ToDoList list);
	}

}