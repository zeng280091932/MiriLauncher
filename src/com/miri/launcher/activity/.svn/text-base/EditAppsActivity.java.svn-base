package com.miri.launcher.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.adapter.AppEditAdapter;
import com.miri.launcher.adapter.AppEditAdapter.Entity;
import com.miri.launcher.db.AppFavSettings.AppFavoritesColumns;
import com.miri.launcher.db.manage.AppFavoritesModel;
import com.miri.launcher.model.AppFavoritesInfo;
import com.miri.launcher.model.AppInfo;
import com.miri.launcher.utils.AppManager;
import com.miri.launcher.utils.Logger;

/**
 * 编辑应用页面
 * @author zengjiantao
 */
public class EditAppsActivity extends BaseActivity {

    private final Logger logger = Logger.getLogger();

    private GridView mAppGrid;

    private static ArrayList<AppInfo> mApplications;

    private AppEditAdapter mAppAdapter;

    private TextView mAppCountTxt;

    private List<AppFavoritesInfo> mSelectedApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_app);

        initWidget();
        initData();
        fillWidgetContent();
    }

    /**
     * 初始化页面控件
     */
    private void initWidget() {
        mAppGrid = (GridView) findViewById(R.id.app_grid);
        mAppGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                logger.d("position:" + position);
                AppEditAdapter.Entity entity = (Entity) view.getTag();
                AppInfo appInfo = mAppAdapter.getItem(position);
                final String packageName = appInfo.intent.getComponent().getPackageName();
                if (entity.selectIcon.isShown()) {
                    entity.selectIcon.setVisibility(View.GONE);
                    AppFavoritesModel.deleteAppFavFromDatabase(EditAppsActivity.this, packageName,
                            AppFavoritesColumns.ITEM_TYPE_USER);
                    Iterator<AppFavoritesInfo> iterator = mSelectedApps.iterator();
                    while (iterator.hasNext()) {
                        AppFavoritesInfo info = iterator.next();
                        if (packageName.equals(info.pkgName)) {
                            iterator.remove();
                            break;
                        }
                    }
                } else {
                    entity.selectIcon.setVisibility(View.VISIBLE);
                    AppFavoritesInfo info = new AppFavoritesInfo();
                    info.title = appInfo.title;
                    info.intent = appInfo.intent;
                    info.pkgName = packageName;
                    info.itemType = AppFavoritesColumns.ITEM_TYPE_USER;
                    info.customIcon = true;
                    info.icon = appInfo.icon;
                    AppFavoritesModel.addAppFavToDatabase(EditAppsActivity.this, info);
                    mSelectedApps.add(info);
                }
            }
        });
        mAppCountTxt = (TextView) findViewById(R.id.app_count);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        loadApplications();
        mSelectedApps = AppFavoritesModel.queryAppFavorites(this,
                AppFavoritesColumns.ITEM_TYPE_USER);
    }

    /**
     * 填充控件的内容
     */
    private void fillWidgetContent() {
        mAppAdapter = new AppEditAdapter(this, R.layout.app_grid_item, mApplications);
        mAppAdapter.setSelectedApps(mSelectedApps);
        mAppGrid.setAdapter(mAppAdapter);
        mAppGrid.requestFocus();
        mAppGrid.setSelection(0);
        mAppCountTxt.setText(getResources().getString(R.string.app_count, mApplications.size()));
    }

    /**
     * 加载系统所有应用程序
     * @param isLaunching
     */
    private void loadApplications() {
        mApplications = AppManager.findAllApps(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
