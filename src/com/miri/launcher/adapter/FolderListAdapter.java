package com.miri.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.model.Folder;
import com.miri.launcher.model.Photo;

public class FolderListAdapter extends ArrayAdapter<Folder> {

	private final Context mContext;

	private LayoutInflater mInflater = null;

	private final int mItemTheme;

	public FolderListAdapter(Context context, int itemTheme, List<Folder> folders) {
		super(context, itemTheme, folders);
		mContext = context;
		mItemTheme = itemTheme;
		mInflater = LayoutInflater.from(context);
	}

	public class Entity {

		public TextView folderName;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Entity entity;
		if (convertView == null) {
			entity = new Entity();
			convertView = mInflater.inflate(mItemTheme, null);
			entity.folderName = (TextView) convertView.findViewById(R.id.folder_name);
			convertView.setTag(entity);
		} else {
			entity = (Entity) convertView.getTag();
		}

		Folder folder = getItem(position);
		entity.folderName.setText(folder.getName());
		return convertView;
	}
}
