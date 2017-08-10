/*
 * 文件名：AppRecommendFragment.java
 * 描述：
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.miri.launcher.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.miri.launcher.PersistData;
import com.miri.launcher.R;
import com.miri.launcher.activity.AppsActivity;
import com.miri.launcher.activity.EditAppsActivity;
import com.miri.launcher.activity.LauncherActivity;
import com.miri.launcher.db.AppFavSettings.AppFavoritesColumns;
import com.miri.launcher.db.manage.AppFavoritesModel;
import com.miri.launcher.model.AppFavoritesInfo;
import com.miri.launcher.msg.model.AppNode;
import com.miri.launcher.msg.model.doc.AppRecommend;
import com.miri.launcher.service.AppRecommendService;
import com.miri.launcher.utils.IconUtil;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.CustomToast;
import com.miri.launcher.view.PagerPosterView;
import com.miri.launcher.view.PagerPosterView.OnReClickLisitener;
import com.miri.launcher.view.TabPageView;

/**
 * 首页应用推荐
 * @author penglin
 * @version 2013-6-24
 */
public class AppRecommendFragment extends BaseFragment {

    protected final Logger log = Logger.getLogger();

    private TextView mAppEntrance;

    private PagerPosterView mAdImage;

    private GridView mAppRecomm;

    private TextView mAppCount;

    private AppRecommendReceiver mAppRecommendReceiver;

    private AppListAdapter mAppListAdapter;

    private int mSelPosition;

    private LauncherActivity mParent;

    private TabPageView mTabPageView;

    public AppRecommendFragment() {
    }

    /**
     * 创建一个fragment的launcher版式
     * @return
     */
    public static AppRecommendFragment newInstance() {
        AppRecommendFragment fragment = new AppRecommendFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        log.e("fragment onAttach..." + activity);
        mTabPageView = (TabPageView) activity.findViewById(R.id.tab);
        if (activity instanceof LauncherActivity) {
            mParent = (LauncherActivity) activity;
        }
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log.e("fragment onCreate..." + getActivity());
        super.onCreate(savedInstanceState);
        // Register for changes to the favorites
        ContentResolver resolver = getActivity().getContentResolver();
        resolver.registerContentObserver(AppFavoritesColumns.getContentUri(false), true,
                mFavoritesObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log.e("fragment onCreateView..." + getActivity());
        View view = inflater.inflate(R.layout.app_recom, container, false);
        mAppEntrance = (TextView) view.findViewById(R.id.app_entrance);
        mAdImage = (PagerPosterView) view.findViewById(R.id.ad_image_bg);
        mAppRecomm = (GridView) view.findViewById(R.id.middle);
        mAppCount = (TextView) view.findViewById(R.id.bottom);
        Toolkit.disableOverScrollMode(mAppRecomm);

        mAppRecommendReceiver = new AppRecommendReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppRecommendService.RECOMMEND_ACTION);
        getActivity().registerReceiver(mAppRecommendReceiver, filter);

        return view;
    }

    private void bindListener() {
        mAppEntrance.setOnKeyListener(mOnKey);
        mAppEntrance.setOnFocusChangeListener(mOnFocus);
        mAppEntrance.setOnClickListener(mOnClick);

        mAdImage.setOnKeyListener(mOnKey);
        mAdImage.setOnReClickLisitener(new OnReClickLisitener() {

            @Override
            public void onClick(View currView) {
                Object tag = currView.getTag();
                if (tag != null) {
                    if (tag instanceof AppNode) {
                        AppNode appRecommend = (AppNode) tag;
                        startApp(getActivity(), appRecommend.getPkgName(), appRecommend.getUrl(),
                                appRecommend.getName(), null);
                    }
                }
            }
        });

        mAppRecomm.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mAppRecomm.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object tag = mAppListAdapter.getItem(position);

                if (tag instanceof AppFavoritesInfo) {
                    AppFavoritesInfo appFav = (AppFavoritesInfo) tag;
                    int itemType = appFav.itemType;
                    if (itemType == AppFavoritesColumns.ITEM_TYPE_USER) {
                        Intent intent = appFav.intent;
                        log.i("intent = " + intent);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            log.e(e.getMessage());
                            CustomToast
                            .makeText(getActivity(), R.string.no_app, Toast.LENGTH_SHORT)
                            .show();
                        }
                    }
                }

            }
        });

        mAppRecomm.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), EditAppsActivity.class));
                return true;
            }
        });
    }

    private final OnFocusChangeListener mOnFocus = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            View view = (View) v.getParent();
            if (view != null) {
                if (hasFocus) {
                    view.bringToFront();
                    view.setBackgroundResource(R.drawable.select_bg);
                } else {
                    view.setBackgroundDrawable(null);
                }
            }

        }
    };

    private final OnKeyListener mOnKey = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            boolean upView = (v == mAppEntrance) || (v == mAdImage);
            if (upView && (event.getAction() == KeyEvent.ACTION_DOWN)) {
                if ((keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
                    if (mTabPageView != null && mTabPageView.getCurrTabView() != null) {
                        mTabPageView.getCurrTabView().requestFocus();
                    }
                } else if ((keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
                    if (mAppRecomm != null && mAppRecomm.getCount() > 0) {
                        mAppRecomm.setSelection(mSelPosition);
                        mAppRecomm.requestFocus();
                        mAppRecomm.invalidateViews();
                    }
                }

            }
            return false;
        }
    };

    private final OnClickListener mOnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.app_entrance:
                Intent intent = new Intent(getActivity(), AppsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        log.e("fragment onActivityCreated..." + getActivity());
        super.onActivityCreated(savedInstanceState);
        createPagerPosterView();
        //        createOrUpdateAppGridView();
        initAppGridView();
        bindListener();

    }

    private void createPagerPosterView() {
        List<AppNode> data = getMarketRecommendData();
        mAdImage.setInitAppRecommend(data, false, false);
    }

    /**
     * 取推荐电子市场apk数据
     * @return
     */
    private List<AppNode> getMarketRecommendData() {
        AppRecommend mainRec = PersistData.mAppRecommend;
        if (mainRec != null && !Toolkit.isEmpty(mainRec.getMarketRecommends())) {
            return mainRec.getMarketRecommends();
        }
        return Collections.emptyList();
    }

    /**
     * 创建或更新应用列表数据
     */
    private void createOrUpdateAppGridView() {
        List<AppFavoritesInfo> appFavData = getAppFavoritesData();
        mAppCount.setText(String.format(getString(R.string.app_count), appFavData.size()));

        appFavData.add(createAddAppFavData());
        if (mAppListAdapter == null) {
            mAppListAdapter = new AppListAdapter(getActivity(), appFavData);
            mAppRecomm.setAdapter(mAppListAdapter);
        } else {
            mAppListAdapter.setData(appFavData);
            mAppListAdapter.notifyDataSetChanged();
        }
    }

    private void initAppGridView() {
        List<AppFavoritesInfo> appFavData = getAppFavoritesData();
        mAppCount.setText(String.format(getString(R.string.app_count), appFavData.size()));
        appFavData.add(createAddAppFavData());
        mAppListAdapter = new AppListAdapter(getActivity(), appFavData);
        mAppRecomm.setAdapter(mAppListAdapter);
    }

    private void updateAppGridView() {
        List<AppFavoritesInfo> appFavData = getAppFavoritesData();
        mAppCount.setText(String.format(getString(R.string.app_count), appFavData.size()));
        appFavData.add(createAddAppFavData());
        mAppListAdapter.setData(appFavData);
        mAppListAdapter.notifyDataSetChanged();
    }

    /**
     * 取应用数据
     * @return
     */
    private List<AppFavoritesInfo> getAppFavoritesData() {
        List<AppFavoritesInfo> result = new ArrayList<AppFavoritesInfo>();
        List<AppFavoritesInfo> infos = AppFavoritesModel.queryAppFavorites(getActivity(),
                AppFavoritesColumns.ITEM_TYPE_USER);
        if (!Toolkit.isEmpty(infos)) {
            result = infos;
        }
        return result;
    }

    /**
     * 添加应用图标数据
     * @return
     */
    private AppFavoritesInfo createAddAppFavData() {
        AppFavoritesInfo info = new AppFavoritesInfo();
        info.title = getString(R.string.add_app);
        info.itemType = AppFavoritesColumns.ITEM_TYPE_USER;
        info.intent = new Intent(getActivity(), EditAppsActivity.class);
        info.customIcon = false;
        ShortcutIconResource sir = new ShortcutIconResource();
        sir.packageName = getActivity().getPackageName();
        sir.resourceName = getResources().getResourceName(R.drawable.add_app_shortcut);
        info.iconResource = sir;
        return info;
    }

    @Override
    public void onDestroyView() {
        log.e("fragement onDestroyView...getActivity:" + getActivity());
        if (mAppRecommendReceiver != null) {
            getActivity().unregisterReceiver(mAppRecommendReceiver);
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        log.e("fragement ondestory...getActivity:" + getActivity());
        ContentResolver resolver = getActivity().getContentResolver();
        resolver.unregisterContentObserver(mFavoritesObserver);
        super.onDestroy();
        log.e("fragement ondestory after...getActivity:" + getActivity());
    }

    @Override
    public void onDetach() {
        log.e("fragement onDetach...getActivity:" + getActivity());
        super.onDetach();
        log.e("fragement onDetach after...getActivity:" + getActivity());
    }

    /**
     * @author penglin
     * @version TVLAUNCHER001, 2013-9-5
     */
    private class AppListAdapter extends BaseAdapter {

        private final Context mContext;

        private LayoutInflater mInflater = null;

        private List<AppFavoritesInfo> data;

        public AppListAdapter(Context context, List<AppFavoritesInfo> objects) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            data = objects;
        }

        public void setData(List<AppFavoritesInfo> objects) {
            data = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Entity entity;
            if (convertView == null) {
                entity = new Entity();
                convertView = mInflater.inflate(R.layout.app_recomm_item, null);
                entity.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
                entity.appTitle = (TextView) convertView.findViewById(R.id.app_title);
                convertView.setTag(entity);
            } else {
                entity = (Entity) convertView.getTag();
            }

            AppFavoritesInfo item = (AppFavoritesInfo) getItem(position);
            if (item instanceof AppFavoritesInfo) {
                AppFavoritesInfo appFav = item;
                entity.appTitle.setText(appFav.title);
                if (appFav.customIcon) {
                    entity.appIcon.setImageDrawable(appFav.icon);
                } else {
                    entity.appIcon.setImageDrawable(IconUtil.getShortcutIncoResource(getActivity(),
                            appFav.iconResource));
                }
                entity.appIcon.setScaleType(ScaleType.FIT_XY);
            }
            return convertView;
        }

        @Override
        public int getCount() {
            if (Toolkit.isEmpty(data)) {
                return 0;
            }
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            if (Toolkit.isEmpty(data)) {
                return null;
            }
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Entity {

            public ImageView appIcon;

            public TextView appTitle;

        }

    }

    /**
     * 接收应用更新广播
     * @author penglin
     * @version HDMNV100R001, 2013-6-24
     */
    class AppRecommendReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(AppRecommendService.RECOMMEND_ACTION)) {
                Logger.getLogger().d("receive app recommend!");
                createPagerPosterView();
            }
        }
    }

    /**
     * Receives notifications whenever the user favorites have changed.
     */
    private final ContentObserver mFavoritesObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            log.w("favorites db change-->" + selfChange);
            //            createOrUpdateAppGridView();
            updateAppGridView();
        }
    };

}
