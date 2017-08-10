package com.miri.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.imageCache.ImageLoader;
import com.miri.launcher.imageCache.ImageLoader.ImgCallback;
import com.miri.launcher.msg.model.AppNode;
import com.miri.launcher.utils.IconUtil;
import com.miri.launcher.utils.Toolkit;

public class AppRecommendAdapter extends ArrayAdapter<AppNode> {

    private final Context mContext;

    private LayoutInflater mInflater = null;

    private final int mItemTheme;

    public AppRecommendAdapter(Context context, int itemTheme,
            List<AppNode> appNodes) {
        super(context, itemTheme, appNodes);
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

        AppNode recommend = getItem(position);
        ;
        // 判断应用是否已存在，存在则取应用图标；不存在则取自定义图标。
        String pkgName = recommend.getPkgName();
        boolean isLoadCustom = true;
        if (!Toolkit.isEmpty(pkgName)) {
            // 判断应用是否已安装
            boolean isExist = Toolkit.isExistApp(mContext, pkgName);
            if (isExist) {
                try {
                    PackageInfo pi = mContext.getPackageManager()
                            .getPackageInfo(pkgName, 0);
                    entity.appName.setText(pi.applicationInfo
                            .loadLabel(mContext.getPackageManager()));
                    entity.appIcon.setImageDrawable(pi.applicationInfo
                            .loadIcon(mContext.getPackageManager()));
                    isLoadCustom = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // 加载自定义的应用信息
        if (isLoadCustom) {
            entity.appName.setText(recommend.getName());
            boolean customIcon = recommend.isCustomIcon();
            if (customIcon) {
                // 本地默认图标
                Intent.ShortcutIconResource iconRes = new Intent.ShortcutIconResource();
                iconRes.packageName = mContext.getPackageName();
                iconRes.resourceName = mContext.getPackageName() + ":"
                        + recommend.getIconResource();
                entity.appIcon.setImageDrawable(IconUtil
                        .getShortcutIncoResource(mContext, iconRes));
            } else {
                // 网络图标
                ImageLoader.from(mContext).loadImg(entity.appIcon,
                        recommend.getPoster(), 0, new ImgCallback() {

                    @Override
                    public void refresh(Object view, Bitmap bitmap,
                            boolean isFromNet) {
                        ((ImageView) view).setImageBitmap(bitmap);

                    }
                });
            }

        }
        return convertView;
    }
}
