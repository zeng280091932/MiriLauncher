package com.miri.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.model.AppInfo;
import com.miri.launcher.utils.IconUtil;
import com.miri.launcher.utils.ScaleAnimEffect;
import com.miri.launcher.utils.Toolkit;

public class AppListAdapter extends ArrayAdapter<AppInfo> {

	private final Context mContext;

	private LayoutInflater mInflater = null;

	private final int mItemTheme;

	public AppListAdapter(Context context, int itemTheme, List<AppInfo> appInfos) {
		super(context, itemTheme, appInfos);
		mContext = context;
		mItemTheme = itemTheme;
		mInflater = LayoutInflater.from(context);
	}

	public class Entity {

		public ImageView appIcon;

		public TextView appName;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Entity entity;
		if (convertView == null) {
			entity = new Entity();
			convertView = mInflater.inflate(mItemTheme, null);
			entity.appIcon = (ImageView) convertView
					.findViewById(R.id.app_icon);
			entity.appName = (TextView) convertView.findViewById(R.id.app_name);
			convertView.setTag(entity);
		} else {
			entity = (Entity) convertView.getTag();
		}

		AppInfo appInfo = getItem(position);
		entity.appName.setText(appInfo.title);
		entity.appIcon.setImageBitmap(IconUtil.createIconBitmap(appInfo.icon));
		return convertView;
	}
}
