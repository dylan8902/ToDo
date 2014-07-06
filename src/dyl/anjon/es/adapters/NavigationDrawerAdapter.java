package dyl.anjon.es.adapters;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dyl.anjon.es.models.ToDoList;
import dyl.anjon.es.todo.R;

public class NavigationDrawerAdapter extends BaseAdapter {

	private ArrayList<ToDoList> lists;
	private LayoutInflater inflater = null;

	/**
	 * @param inflater
	 * @param items
	 */
	public NavigationDrawerAdapter(LayoutInflater inflater,
			ArrayList<ToDoList> lists) {
		this.lists = lists;
		this.inflater = inflater;
	}

	public int getCount() {
		return lists.size();
	}

	public String getItem(int position) {
		return lists.get(position).getName();
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_navigation_drawer_item, null);
		ToDoList list = lists.get(position);
		TextView name = (TextView) v.findViewById(R.id.name);
		name.setText(list.getName());
		return v;
	}

	public void refresh(ArrayList<ToDoList> lists) {
		this.lists = lists;
		this.notifyDataSetChanged();
	}

}
