package com.miri.launcher.db.manage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.format.DateFormat;

import com.miri.launcher.model.Photo;
import com.miri.launcher.model.Video;
import com.miri.launcher.utils.Toolkit;

public class MediaManager {

	/**
	 * 获取外部存储中所有的视频
	 * 
	 * @param context
	 * @return
	 */
	public static List<Video> queryVideos(Context context) {
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] {
				MediaStore.Video.Media.DISPLAY_NAME,
				MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION };
		Cursor cursor = contentResolver.query(
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
		if (cursor == null) {
			return Collections.emptyList();
		}
		try {
			List<Video> result = new ArrayList<Video>();
			final int nameIndex = cursor
					.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
			final int pathIndex = cursor
					.getColumnIndex(MediaStore.Video.Media.DATA);
			final int durationIndex = cursor
					.getColumnIndex(MediaStore.Video.Media.DURATION);

			while (cursor.moveToNext()) {
				int duration = cursor.getInt(durationIndex);
				result.add(new Video(cursor.getString(nameIndex), cursor
						.getString(pathIndex), Toolkit.parseDuration(duration)));
			}
			return result;
		} finally {
			cursor.close();
		}
	}

	/**
	 * 获取外部存储中所有的照片
	 * 
	 * @param context
	 * @return
	 */
	public static List<Photo> queryPhotos(Context context) {
		ContentResolver contentResolver = context.getContentResolver();
		String[] projection = new String[] {
				MediaStore.Images.Media.DISPLAY_NAME,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.DATE_ADDED,
				MediaStore.Images.Media.DATE_MODIFIED };
		Cursor cursor = contentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
				null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
		if (cursor == null) {
			return Collections.emptyList();
		}
		try {
			List<Photo> result = new ArrayList<Photo>();
			final int nameIndex = cursor
					.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);

			final int pathIndex = cursor
					.getColumnIndex(MediaStore.Images.Media.DATA);

			final int dateAddIndex = cursor
					.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);

			final int dateModIndex = cursor
					.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED);

			while (cursor.moveToNext()) {
				long dateMod = cursor.getLong(dateModIndex) * 1000l;
				long dateAdd = cursor.getLong(dateAddIndex) * 1000l;

				String dateModStr = DateFormat.format("yyyy/MM/dd",
						new Date(dateMod)).toString();
				String dateAddStr = DateFormat.format("yyyy/MM/dd",
						new Date(dateAdd)).toString();
				result.add(new Photo(cursor.getString(nameIndex), cursor
						.getString(pathIndex), dateModStr, dateAddStr));
			}
			return result;
		} finally {
			cursor.close();
		}
	}
}
