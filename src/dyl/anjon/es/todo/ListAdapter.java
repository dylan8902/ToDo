package dyl.anjon.es.todo;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	private List<String> rowList;
	private LayoutInflater inflater = null;

	/**
	 * @param inflater
	 * @param journeys
	 */
	public ListAdapter(LayoutInflater inflater,
			ArrayList<String> items) {
		this.rowList = items;
		this.inflater = inflater;
	}

	public int getCount() {
		return rowList.size();
	}

	public String getItem(int position) {
		return rowList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.row_item, null);
		String item = rowList.get(position);
		TextView name = (TextView) v.findViewById(R.id.name);
		name.setText(item);
		return v;
	}
	
	public void refresh(ArrayList<String> items) {
		this.rowList = items;
		this.notifyDataSetChanged();
	}

}
