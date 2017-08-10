/* 
 * 文件名：LauncherSettings.java
 */
package com.miri.launcher.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Settings related utilities.
 * @author penglin
 * @version 2013-6-3
 */
public class AppFavSettings {

    public static final class AppFavoritesColumns implements BaseColumns {

        /**
         * The content:// style URL for this table
         */
        private static final Uri CONTENT_URI = Uri.parse("content://" + AppFavProvider.AUTHORITY
                + "/" + AppFavProvider.TABLE_APP_FAVORITES + "?" + AppFavProvider.PARAMETER_NOTIFY
                + "=true");

        /**
         * The content:// style URL for this table. When this Uri is used, no notification is sent
         * if the content changes.
         */
        private static final Uri CONTENT_URI_NO_NOTIFICATION = Uri.parse("content://"
                + AppFavProvider.AUTHORITY + "/" + AppFavProvider.TABLE_APP_FAVORITES + "?"
                + AppFavProvider.PARAMETER_NOTIFY + "=false");

        public static Uri getContentUri(boolean notify) {
            return notify ? CONTENT_URI : CONTENT_URI_NO_NOTIFICATION;
        }

        /**
         * The content:// style URL for a given row, identified by its id.
         * @param id The row id.
         * @param notify True to send a notification is the content changes.
         * @return The unique content URL for the specified row.
         */
        public static Uri getContentUri(long id, boolean notify) {
            return Uri.parse("content://" + AppFavProvider.AUTHORITY + "/"
                    + AppFavProvider.TABLE_APP_FAVORITES + "/" + id + "?"
                    + AppFavProvider.PARAMETER_NOTIFY + "=" + notify);
        }

        /**
         * Descriptive name of the gesture that can be displayed to the user.
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String TITLE = "title";

        public static final String PKGNAME = "packageName";

        /**
         * The Intent URL of the gesture, describing what it points to. This value is given to
         * {@link android.content.Intent#parseUri(String, int)} to create an Intent that can be
         * launched.
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String INTENT = "intent";

        /**
         * The type of the gesture
         * <P>
         * Type: INTEGER
         * </P>
         */
        public static final String ITEM_TYPE = "itemType";

        /**
         * The gesture is an user
         */
        public static final int ITEM_TYPE_USER = 0;

        /**
         * The gesture is created recommend
         */
        public static final int ITEM_TYPE_RECOMMEND = 1;

        /**
         * The icon type.
         * <P>
         * Type: INTEGER
         * </P>
         */
        public static final String ICON_TYPE = "iconType";

        /**
         * The icon is a resource identified by a package name and an integer id.
         */
        public static final int ICON_TYPE_RESOURCE = 0;

        /**
         * The icon is a bitmap.
         */
        public static final int ICON_TYPE_BITMAP = 1;

        /**
         * The icon package name, if icon type is ICON_TYPE_RESOURCE.
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String ICON_PACKAGE = "iconPackage";

        /**
         * The icon resource id, if icon type is ICON_TYPE_RESOURCE.
         * <P>
         * Type: TEXT
         * </P>
         */
        public static final String ICON_RESOURCE = "iconResource";

        /**
         * The custom icon bitmap, if icon type is ICON_TYPE_BITMAP.
         * <P>
         * Type: BLOB
         * </P>
         */
        public static final String ICON = "icon";

        /**
         * The ITEM_TYPE is ITEM_TYPE_RECOMMEND.otherwise is null
         * <P>
         * Type: String
         * </P>
         */
        public static final String URL = "url";

    }

}
