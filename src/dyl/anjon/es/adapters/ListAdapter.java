package dyl.anjon.es.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dyl.anjon.es.models.ToDoItem;
import dyl.anjon.es.todo.R;

public class ListAdapter extends BaseAdapter {

	private List<ToDoItem> items;
	private LayoutInflater inflater = null;

	/**
	 * @param inflater
	 * @param items
	 */
	public ListAdapter(LayoutInflater inflater, ArrayList<ToDoItem> items) {
		this.items = items;
		this.inflater = inflater;
	}

	public int getCount() {
		return items.size();
	}

	public ToDoItem getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_item, null);
		ToDoItem item = items.get(position);
		TextView name = (TextView) v.findViewById(R.id.name);
		name.setText(item.getName());
		if (item.isDone()) {
			ImageView done = (ImageView) v.findViewById(R.id.done);
			done.setVisibility(View.VISIBLE);
		} else {
			ImageView notDone = (ImageView) v.findViewById(R.id.not_done);
			notDone.setVisibility(View.VISIBLE);		}
		return v;
	}

	public void refresh(ArrayList<ToDoItem> items) {
		this.items = items;
		this.notifyDataSetChanged();
	}

}
