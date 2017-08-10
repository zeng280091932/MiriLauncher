package com.miri.launcher.adapter;

import com.miri.launcher.R;
import com.miri.launcher.adapter.PhotoListAdapter.Entity;
import com.miri.launcher.model.Photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CharacterAdapter extends BaseAdapter {

	private static final String[] CHARACTERS = { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9" };

	private Context mContext;

	private LayoutInflater mInflater = null;

	public CharacterAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public class Entity {

		public TextView charTxt;

	}

	@Override
	public int getCount() {
		return CHARACTERS.length;
	}

	@Override
	public Object getItem(int position) {
		return CHARACTERS[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Entity entity;
		if (convertView == null) {
			entity = new Entity();
			convertView = mInflater.inflate(R.layout.char_grid_item, null);
			entity.charTxt = (TextView) convertView
					.findViewById(R.id.char_text);
			convertView.setTag(entity);
		} else {
			entity = (Entity) convertView.getTag();
		}

		String character = getItem(position).toString();
		entity.charTxt.setText(character);

		return convertView;
	}

}
