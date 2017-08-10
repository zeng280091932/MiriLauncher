package com.miri.launcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class DlmListView extends ListView {

	public DlmListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DlmListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public DlmListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	/**
	 * To keep the focus off the whole list item, this method should always
	 * return true.
	 */
	@Override
	public boolean isInTouchMode() {
		return true;
	}
}
