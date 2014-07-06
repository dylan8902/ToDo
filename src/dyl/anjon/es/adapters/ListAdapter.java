package dyl.anjon.es.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
		final ToDoItem item = items.get(position);
		CheckBox name = (CheckBox) v.findViewById(R.id.name);
		name.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				item.setIsDone(isChecked);
			}
		});
		name.setText(item.getName());
		name.setChecked(item.isDone());
		return v;
	}

	public void refresh(ArrayList<ToDoItem> items) {
		this.items = items;
		this.notifyDataSetChanged();
	}

}
