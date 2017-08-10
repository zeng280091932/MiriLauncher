/* 
 * 文件名：AppFavoritesInfo.java
 */
package com.miri.launcher.model;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.miri.launcher.db.AppFavSettings.AppFavoritesColumns;
import com.miri.launcher.utils.IconUtil;

/**
 * 应用收藏快捷方式
 * @author penglin
 */
public class AppFavoritesInfo {

    public static final int NO_ID = -1;

    /**
     * The id in the settings database for this item
     */
    public int id = NO_ID;

    /**
     * TODO 定义收藏应用的类型
     */
    public int itemType = NO_ID;

    /**
     * The application name.
     */
    public CharSequence title;

    /**
     * The application packageName.
     */
    public CharSequence pkgName;

    /**
     * Indicates whether the icon comes from an application's resource (if false) or from a custom
     * Bitmap (if true.)
     */
    public boolean customIcon;

    /**
     * The application icon.
     */
    public Drawable icon;

    /**
     * If customIcon=false, this contains a reference to the shortcut icon as an application's
     * resource.
     */
    public Intent.ShortcutIconResource iconResource;

    /**
     * 启动应用的intent值，为null时，可通过url来下载应用 <br>
     * The intent used to start the application.
     */
    public Intent intent;

    /**
     * 定义收藏应用的下载路径
     */
    public CharSequence url;

    public AppFavoritesInfo() {

    }

    public void onAddToDatabase(ContentValues values) {
        String titleStr = title != null ? title.toString() : null;
        values.put(AppFavoritesColumns.TITLE, titleStr);

        String pkgNameStr = pkgName != null ? pkgName.toString() : null;
        values.put(AppFavoritesColumns.PKGNAME, pkgNameStr);

        values.put(AppFavoritesColumns.ITEM_TYPE, itemType);

        String uri = intent != null ? intent.toUri(0) : null;
        values.put(AppFavoritesColumns.INTENT, uri);

        String urlStr = url != null ? url.toString() : null;
        values.put(AppFavoritesColumns.URL, urlStr);

        if (customIcon) {
            values.put(AppFavoritesColumns.ICON_TYPE, AppFavoritesColumns.ICON_TYPE_BITMAP);
            values.put(AppFavoritesColumns.ICON, IconUtil.drawableToByte(icon));
        } else {
            values.put(AppFavoritesColumns.ICON_TYPE, AppFavoritesColumns.ICON_TYPE_RESOURCE);
            if (iconResource != null) {
                values.put(AppFavoritesColumns.ICON_PACKAGE, iconResource.packageName);
                values.put(AppFavoritesColumns.ICON_RESOURCE, iconResource.resourceName);
            }
        }
    }

    @Override
    public String toString() {
        return "AppFavoritesInfo [id=" + id + ", itemType=" + itemType + ", title=" + title
                + ", pkgName=" + pkgName + ", intent=" + intent + ", customIcon=" + customIcon
                + ", icon=" + icon + ", iconResource=" + iconResource + ", url=" + url + "]";
    }

}
