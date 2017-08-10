/* 
 * 文件名：AppFavoritesModel.java
 */
package com.miri.launcher.db.manage;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.miri.launcher.db.AppFavSettings.AppFavoritesColumns;
import com.miri.launcher.model.AppFavoritesInfo;
import com.miri.launcher.utils.IconUtil;

/**
 * @author penglin
 * @version 2013-6-24
 */
public class AppFavoritesModel {

    /**
     * Add an item to the database in a specified container.
     */
    public static void addAppFavToDatabase(Context context, AppFavoritesInfo item) {

        final ContentValues values = new ContentValues();
        final ContentResolver cr = context.getContentResolver();

        item.onAddToDatabase(values);

        Uri result = cr.insert(AppFavoritesColumns.getContentUri(true), values);

        if (result != null) {
            item.id = Integer.parseInt(result.getPathSegments().get(1));
        }
    }

    /**
     * Update an item to the database in a specified container.
     */
    public static void updateAppFavInDatabase(Context context, AppFavoritesInfo item) {
        final ContentValues values = new ContentValues();
        final ContentResolver cr = context.getContentResolver();

        item.onAddToDatabase(values);

        cr.update(AppFavoritesColumns.getContentUri(item.id, true), values, null, null);
    }

    /**
     * Removes the specified item from the database
     * @param context
     * @param id
     */
    public static void deleteAppFavFromDatabase(Context context, int id) {
        final ContentResolver cr = context.getContentResolver();
        final Uri uriToDelete = AppFavoritesColumns.getContentUri(id, true);
        cr.delete(uriToDelete, null, null);
    }

    /**
     * Removes the specified item from the database
     * @param context
     * @param pkgName
     */
    public static void deleteAppFavFromDatabase(Context context, String pkgName, int itemType) {
        final ContentResolver cr = context.getContentResolver();
        final Uri uriToDelete = AppFavoritesColumns.getContentUri(true);
        cr.delete(uriToDelete, "packageName=? and itemType=?",
                new String[] {pkgName, String.valueOf(itemType)});
    }

    /**
     * Returns collections if the shortcuts already exists in the database. we identify a shortcut
     * @param context
     * @return
     */
    public static List<AppFavoritesInfo> queryAppFavorites(Context context, int itemType) {
        List<AppFavoritesInfo> result = new ArrayList<AppFavoritesInfo>();
        final ContentResolver contentResolver = context.getContentResolver();
        final Cursor c = contentResolver.query(AppFavoritesColumns.getContentUri(false), null,
                "itemType=?", new String[] {itemType + ""}, null);
        if (c != null) {
            try {
                final int idIndex = c.getColumnIndexOrThrow(BaseColumns._ID);
                final int titleIndex = c.getColumnIndexOrThrow(AppFavoritesColumns.TITLE);
                final int pkgNameIndex = c.getColumnIndexOrThrow(AppFavoritesColumns.PKGNAME);
                final int intentIndex = c.getColumnIndexOrThrow(AppFavoritesColumns.INTENT);
                final int itemTypeIndex = c.getColumnIndexOrThrow(AppFavoritesColumns.ITEM_TYPE);
                final int iconTypeIndex = c.getColumnIndexOrThrow(AppFavoritesColumns.ICON_TYPE);
                final int iconIndex = c.getColumnIndexOrThrow(AppFavoritesColumns.ICON);
                final int iconPackageIndex = c
                        .getColumnIndexOrThrow(AppFavoritesColumns.ICON_PACKAGE);
                final int iconResourceIndex = c
                        .getColumnIndexOrThrow(AppFavoritesColumns.ICON_RESOURCE);
                final int urlIndex = c.getColumnIndexOrThrow(AppFavoritesColumns.URL);

                while (c.moveToNext()) {
                    AppFavoritesInfo info = new AppFavoritesInfo();
                    info.id = c.getInt(idIndex);
                    info.title = c.getString(titleIndex);
                    info.pkgName = c.getString(pkgNameIndex);
                    info.itemType = c.getInt(itemTypeIndex);

                    String intentStr = c.getString(intentIndex);
                    if (intentStr != null && intentStr.length() > 0) {
                        try {
                            info.intent = Intent.parseUri(c.getString(intentIndex), 0);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    int iconType = c.getInt(iconTypeIndex);
                    if (iconType == AppFavoritesColumns.ICON_TYPE_RESOURCE) {
                        info.customIcon = false;
                        Intent.ShortcutIconResource iconRes = new Intent.ShortcutIconResource();
                        iconRes.packageName = c.getString(iconPackageIndex);
                        iconRes.resourceName = c.getString(iconResourceIndex);
                        info.iconResource = iconRes;
                    } else {
                        info.customIcon = true;
                        info.icon = IconUtil.byteToDrawable(c.getBlob(iconIndex));
                    }

                    info.url = c.getString(urlIndex);
                    //                    Logger.getLogger().d("app favorites info->" + info.toString());
                    result.add(info);
                }
            } finally {
                c.close();
            }
        }
        return result;
    }

    /**
     * Returns true if the AppFav already exists in the database. we identify a shortcut by its id.
     */
    public static int existAppFav(Context context, int id) {
        final ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(AppFavoritesColumns.getContentUri(id, false), null, null, null, null);
        int _id = AppFavoritesInfo.NO_ID;
        boolean result = false;
        try {
            final int idIndex = c.getColumnIndexOrThrow(BaseColumns._ID);
            result = c.moveToFirst();
            if (result) {
                _id = c.getInt(idIndex);
            }
        } finally {
            c.close();
        }
        return _id;
    }

    /**
     * Returns true if the AppFav already exists in the database. we identify a shortcut by its
     * pkgName.
     */
    public static List<Integer> existAppFav(Context context, String pkgName, int itemType) {
        List<Integer> ids = new ArrayList<Integer>();
        final ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(AppFavoritesColumns.getContentUri(false), new String[] {"_id",
                "packageName", "intent"}, "packageName=? and itemType=?", new String[] {pkgName,
                String.valueOf(itemType)}, null);
        int _id = AppFavoritesInfo.NO_ID;
        try {
            final int idIndex = c.getColumnIndexOrThrow(BaseColumns._ID);
            while (c.moveToNext()) {
                _id = c.getInt(idIndex);
                ids.add(_id);
            }
        } finally {
            c.close();
        }
        return ids;
    }

    public static int countAppFavorites(Context context, int itemType) {
        List<AppFavoritesInfo> result = queryAppFavorites(context, itemType);
        if (result != null) {
            return result.size();
        }
        return 0;
    }
}
