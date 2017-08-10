/**
 * 
 */
package com.miri.launcher.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miri.launcher.MoretvConstants;
import com.miri.launcher.PersistData;
import com.miri.launcher.R;
import com.miri.launcher.adapter.AppListAdapter;
import com.miri.launcher.adapter.AppRecommendAdapter;
import com.miri.launcher.market.DownloadService;
import com.miri.launcher.model.AppInfo;
import com.miri.launcher.msg.model.AppNode;
import com.miri.launcher.msg.model.doc.AppRecommend;
import com.miri.launcher.service.AppRecommendService;
import com.miri.launcher.utils.ApkUtil;
import com.miri.launcher.utils.AppManager;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.dialog.AlertDialog.OnCancelBtnClickListener;
import com.miri.launcher.view.dialog.AlertDialog.OnOkBtnClickListener;
import com.miri.launcher.view.dialog.AlertUtil;

/**
 * 应用分类页面
 * 
 * @author zengjiantao
 * 
 */
public class AppsActivity extends BaseActivity {

    private final Logger logger = Logger.getLogger();

    private static final int UNINSTALL_REQUEST_CODE = 1000;

    private static final int COLUMNS = 7;

    private static final int COLUMN_WIDTH = 170;

    private static final int MARGIN_RECOMM_HIDE = 88;

    private static final int MARGIN_RECOMM_SHOW = 258;

    private static final int HEIGHT_RECOMM_HIDE = 546;

    private static final int HEIGHT_RECOMM_SHOW = 372;

    private LinearLayout mAppContainer;

    private GridView mAppGrid;

    private static ArrayList<AppInfo> mApplications;

    private AppListAdapter mAppAdapter;

    private LinearLayout mRecommContainer;

    private HorizontalScrollView mRecommScroller;

    private ImageView mRecommLeft;

    private ImageView mRecommRight;

    private GridView mAppRecommGrid;

    private List<AppNode> mAppRecomms;

    private AppRecommendAdapter mAppRecommAdapter;

    private TextView mAppCountTxt;

    private int mUninsallPostion;

    private boolean mUninsallRecomm = false;

    private AppRecommendReceiver mAppRecommendReceiver;

    private DownloadService mDownloadService;

    private ServiceConnection mDlSvcConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps);

        initWidget();
        initData();
        fillWidgetContent();
    }

    /**
     * 初始化页面控件
     */
    private void initWidget() {
        mAppContainer = (LinearLayout) findViewById(R.id.app_container);

        mAppGrid = (GridView) findViewById(R.id.app_grid);
        mAppGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                logger.d("position:" + position);
                AppInfo app = mAppAdapter.getItem(position);
                try {
                    startActivity(app.intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mAppGrid.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                    int position, long id) {
                logger.d("position:" + position);
                AppInfo app = mAppAdapter.getItem(position);
                ComponentName component = app.intent.getComponent();
                if (component != null) {
                    String packageName = component.getPackageName();
                    logger.d(packageName);
                    if (!Toolkit.isEmpty(packageName)) {
                        mUninsallRecomm = false;
                        mUninsallPostion = position;
                        uninstall(app.title.toString(), packageName);
                    }
                }
                return true;
            }
        });
        mAppGrid.setOnKeyListener(mOnKey);
        mAppCountTxt = (TextView) findViewById(R.id.app_count);

        mRecommContainer = (LinearLayout) findViewById(R.id.recomm_container);
        mRecommLeft = (ImageView) findViewById(R.id.recomm_left);
        mRecommRight = (ImageView) findViewById(R.id.recomm_right);
        mRecommScroller = (HorizontalScrollView) findViewById(R.id.horizontal_scroll);
        // 绑定鼠标事件监听器
        mRecommScroller.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int scrollX = mRecommScroller.getScrollX();
                // 不可见子项的数量
                int invisibleNum = mAppRecommAdapter.getCount() - COLUMNS;
                // Log.d(TAG, "invisibleNum" + invisibleNum);
                if (invisibleNum <= 0) {
                    return false;
                }
                if (scrollX < COLUMN_WIDTH) {
                    mRecommLeft.setVisibility(View.INVISIBLE);
                    mRecommRight.setVisibility(View.VISIBLE);
                } else if ((scrollX >= COLUMN_WIDTH)
                        && (scrollX < (COLUMN_WIDTH * invisibleNum))) {
                    mRecommLeft.setVisibility(View.VISIBLE);
                    mRecommRight.setVisibility(View.VISIBLE);
                } else if (scrollX >= (COLUMN_WIDTH * invisibleNum)) {
                    mRecommLeft.setVisibility(View.VISIBLE);
                    mRecommRight.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        mAppRecommGrid = (GridView) findViewById(R.id.recomm_grid);
        mAppRecommGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                AppNode appRecommend = mAppRecommAdapter.getItem(position);
                startApp(AppsActivity.this, appRecommend.getPkgName(),
                        appRecommend.getUrl(), appRecommend.getName(),
                        appRecommend.getPoster());
            }
        });
        mAppRecommGrid
        .setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                    View view, int position, long id) {
                AppNode appRecommend = mAppRecommAdapter
                        .getItem(position);
                String packageName = appRecommend.getPkgName();
                logger.d(packageName);
                boolean isExist = Toolkit.isExistApp(AppsActivity.this,
                        packageName);
                if (isExist) {
                    mUninsallRecomm = true;
                    uninstall(appRecommend.getName(), packageName);
                } else {
                    showToast("还没有安装该应用！");
                }
                return true;
            }
        });
        mAppRecommGrid.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                // 超出显示范围时，mHorizontalScrollView平滑左右滚动，并控制左右指示箭头的显示与隐藏。
                int scrollXBefore = mRecommScroller.getScrollX();

                if (position >= (COLUMNS - 1)) {
                    int scrollXAfter = COLUMN_WIDTH
                            * ((position - COLUMNS) + 1);
                    if (scrollXBefore < scrollXAfter) {
                        mRecommScroller.smoothScrollTo(scrollXAfter, 0);
                    } else if ((scrollXBefore - scrollXAfter) / COLUMN_WIDTH >= COLUMNS) {
                        mRecommScroller.smoothScrollTo(scrollXBefore
                                - COLUMN_WIDTH, 0);
                    }
                } else if ((scrollXBefore / COLUMN_WIDTH) > position) {
                    int scrollXAfter = COLUMN_WIDTH * position;
                    mRecommScroller.smoothScrollTo(scrollXAfter, 0);
                }

                int count = mAppRecommAdapter.getCount();
                if (count > COLUMNS) {
                    if (position == 0) {
                        mRecommLeft.setVisibility(View.INVISIBLE);
                        mRecommRight.setVisibility(View.VISIBLE);
                    } else if (position == count - 1) {
                        mRecommLeft.setVisibility(View.VISIBLE);
                        mRecommRight.setVisibility(View.INVISIBLE);
                    } else {
                        mRecommLeft.setVisibility(View.VISIBLE);
                        mRecommRight.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAppRecommGrid.setOnKeyListener(mOnKey);

        mAppRecommendReceiver = new AppRecommendReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppRecommendService.RECOMMEND_ACTION);
        registerReceiver(mAppRecommendReceiver, filter);

        mDlSvcConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mDownloadService = ((DownloadService.MyBinder) service)
                        .getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        // Bind the DownloadService.
        bindService(new Intent(this, DownloadService.class), mDlSvcConnection,
                Context.BIND_AUTO_CREATE);
    }

    private final OnKeyListener mOnKey = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (v.getId() == R.id.app_grid
                    && event.getAction() == KeyEvent.ACTION_DOWN
                    && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                GridView grid = (GridView) v;
                int currentPos = grid.getSelectedItemPosition();
                if (currentPos < COLUMNS) {
                    showRecomm(true);
                }
            }
            if (v.getId() == R.id.recomm_grid
                    && event.getAction() == KeyEvent.ACTION_DOWN
                    && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                showRecomm(false);
            }
            return false;
        }
    };

    /**
     * 显示推荐应用
     * 
     * @param isShow
     */
    private void showRecomm(boolean isShow) {
        LayoutParams params = (LayoutParams) mAppContainer.getLayoutParams();
        LinearLayout.LayoutParams gridParams = (LinearLayout.LayoutParams) mAppGrid
                .getLayoutParams();
        if (isShow) {
            mRecommContainer.setVisibility(View.VISIBLE);
            params.topMargin = Toolkit.px2dip(AppsActivity.this,
                    MARGIN_RECOMM_SHOW);
            gridParams.height = HEIGHT_RECOMM_SHOW;
        } else {
            mRecommContainer.setVisibility(View.GONE);
            params.topMargin = Toolkit.px2dip(AppsActivity.this,
                    MARGIN_RECOMM_HIDE);
            gridParams.height = HEIGHT_RECOMM_HIDE;
        }
        mAppContainer.setLayoutParams(params);
        mAppGrid.setLayoutParams(gridParams);
    }

    /**
     * 卸载应用
     */
    private void uninstall(final String appName, final String packageName) {
        AlertUtil.showAlert(
                this,
                getResources().getString(R.string.uninstall_title),
                getResources().getString(R.string.uninstall_msg, appName),
                getResources().getString(R.string.ok),
                new OnOkBtnClickListener() {

                    @Override
                    public void onClick() {
                        Uri packageURI = Uri.parse("package:" + packageName);
                        Intent uninstallIntent = new Intent(
                                Intent.ACTION_DELETE, packageURI);
                        startActivityForResult(uninstallIntent,
                                UNINSTALL_REQUEST_CODE);
                    }
                }, getResources().getString(R.string.cancel),
                new OnCancelBtnClickListener() {

                    @Override
                    public void onClick() {
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            refreshData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        loadApplications();
        mAppAdapter = new AppListAdapter(this, R.layout.app_grid_item,
                mApplications);
        mAppGrid.setAdapter(mAppAdapter);
        int size = mApplications.size();
        mAppCountTxt
        .setText(getResources().getString(R.string.app_count, size));
        if (!mUninsallRecomm) {
            mAppGrid.requestFocus();
            if (size > 0) {
                if (mUninsallPostion > size - 1) {
                    mAppGrid.setSelection(size - 1);
                } else {
                    mAppGrid.setSelection(mUninsallPostion);
                }
            }
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        loadApplications();
        mAppRecomms = getAppRecommendData();
    }

    /**
     * 填充控件的内容
     */
    private void fillWidgetContent() {
        int count = mAppRecomms.size();
        if (count > COLUMNS) {
            mRecommRight.setVisibility(View.VISIBLE);
        }
        int columnWidth = Toolkit.px2dip(this, COLUMN_WIDTH);
        int width = count * columnWidth;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mAppRecommGrid
                .getLayoutParams();
        params.width = width;
        mAppRecommGrid.setLayoutParams(params);
        mAppRecommGrid.setNumColumns(count);
        mAppRecommAdapter = new AppRecommendAdapter(this,
                R.layout.app_grid_item, mAppRecomms);
        mAppRecommGrid.setAdapter(mAppRecommAdapter);
        mAppRecommGrid.requestFocus();
        mAppRecommGrid.setSelection(0);

        mAppAdapter = new AppListAdapter(this, R.layout.app_grid_item,
                mApplications);
        mAppGrid.setAdapter(mAppAdapter);
        mAppCountTxt.setText(getResources().getString(R.string.app_count,
                mApplications.size()));
    }

    private void updateAppRecommGrid() {
        List<AppNode> data = getAppRecommendData();
        if (!Toolkit.isEmpty(data)) {
            mAppRecommAdapter.clear();
            if (Build.VERSION.SDK_INT >= 11) {
                mAppRecommAdapter.addAll(data);
            } else {
                for (AppNode appNode : data) {
                    mAppRecommAdapter.add(appNode);
                }
            }
        } else {
            clearListView();
        }
    }

    private void clearListView() {
        if (mAppRecommAdapter != null && mAppRecommAdapter.getCount() > 0) {
            mAppRecommAdapter.clear();
        }
    }

    /**
     * @param context
     * @param pkgName
     *            包名
     * @param url
     *            应用下载路径
     * @param appName
     *            应用名称
     * @param iconPath
     *            应用图标路径
     */
    protected void startApp(Context context, String pkgName, String url,
            String appName, String iconPath) {
        boolean isNeedInstall = false;
        if (!Toolkit.isEmpty(pkgName)) {
            // 判断应用是否已安装
            boolean isExist = Toolkit.isExistApp(context, pkgName);
            if (isExist) {
                // 启动app
                AppManager.runApp(context, pkgName);
            } else {
                isNeedInstall = true;
            }
        } else {
            isNeedInstall = true;
        }

        if (isNeedInstall) {
            boolean installExist = false;
            if (!Toolkit.isEmpty(pkgName)
                    && pkgName.equals(MoretvConstants.PACKAGE_NAME)) {
                installExist = ApkUtil.installExist(context,
                        MoretvConstants.FILE_NAME);
            }
            Log.d("Debug", "installExist:" + installExist);
            // TODO 去下载安装
            if (!installExist && mDownloadService != null) {
                mDownloadService.createDownload(url, null, appName, iconPath,
                        true, true);
            }
        }

    }

    /**
     * 加载系统所有应用程序
     * 
     * @param isLaunching
     */
    private void loadApplications() {
        mApplications = AppManager.findAllApps(this);
    }

    /**
     * 取推荐应用数据
     * 
     * @return
     */
    private List<AppNode> getAppRecommendData() {
        AppRecommend mainRec = PersistData.mAppRecommend;
        List<AppNode> recommends = mainRec.getAppRecommends();
        if (mainRec != null && !Toolkit.isEmpty(recommends)) {
            return recommends;
        }
        return Collections.emptyList();
    }

    @Override
    protected void onDestroy() {
        unbindService(mDlSvcConnection);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 接收应用更新广播
     * 
     * @author penglin
     * @version HDMNV100R001, 2013-6-24
     */
    class AppRecommendReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null
                    && action.equals(AppRecommendService.RECOMMEND_ACTION)) {
                Logger.getLogger().d("receive app recommend!");
                updateAppRecommGrid();
            }
        }
    }
}
