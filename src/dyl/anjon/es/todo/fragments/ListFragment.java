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
import dyl.anjon.es.todo.MainActivity;
import dyl.anjon.es.todo.R;

public class ListFragment extends Fragment {

	public ListAdapter adapter;
	private ToDoList list;

	private static final String ARG_LIST_NAME = "List";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ListFragment newInstance(ToDoList list) {
		ListFragment fragment = new ListFragment(list);
		Bundle args = new Bundle();
		args.putString(ARG_LIST_NAME, list.getName());
		fragment.setArguments(args);
		return fragment;
	}

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
		addItem.setImeActionLabel("Add Item", KeyEvent.KEYCODE_ENTER);
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
		((MainActivity) activity).onSectionAttached(getArguments().getString(
				ARG_LIST_NAME));
	}
}