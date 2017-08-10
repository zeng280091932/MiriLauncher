/* 
 * 文件名：DownItemActivity.java
 * 版权：Copyright
 */
package com.miri.launcher.activity;

import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.market.DownloadService;
import com.miri.launcher.utils.AppManager;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.CustomBtnView;
import com.miri.launcher.view.dialog.AlertDialog;
import com.miri.launcher.view.dialog.AlertUtil;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-12
 */
public class DownItemActivity extends BaseActivity {

    private DownloadManagerActivity mParentActivity;

    private View mCurrTabView;

    private TextView msgText;

    private ListView mListView;

    private DLListAdapter mListAdapter;

    private DownloadService downloadService;

    private ServiceConnection serviceConnection;

    private int mDownloadType = -1;

    private int mCurrPosition = 0;

    private UpdateHandler uh;

    private boolean isLastPos;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            final String urlStr = msg.getData().getString("urlStr");
            if (Toolkit.isEmpty(urlStr)) {
                return;
            }
            //            final Map<String, ?> dataSet = downloadService.getItem(urlStr);
            //            if (dataSet == null || dataSet.size() == 0) {
            //                return;
            //            }
            switch (msg.what) {
                case DownloadService.DOWNLOAD_INIT: {
                    //                    refreshList();
                    // Initiate the item's view when the corresponding download
                    // threads start.
                    final View child = getItemPosition(urlStr);
                    if (child == null) {
                        return;
                    }
                    ((TextView) child.findViewById(R.id.percent)).setText(msg.getData().getString(
                            "statusText"));
                    ((TextView) child.findViewById(R.id.size)).setText(msg.getData().getString(
                            "fileSizeText"));
                    break;
                }
                case DownloadService.DOWNLOAD_PROGRESS: {
                    // Update UI when the download progress changes.
                    final View child = getItemPosition(urlStr);
                    if (child == null) {
                        return;
                    }
                    ((TextView) child.findViewById(R.id.percent)).setText(msg.getData().getString(
                            "statusText"));
                    ((ProgressBar) child.findViewById(R.id.dl_progress)).setProgress(msg.getData()
                            .getInt("progress"));
                    ((TextView) child.findViewById(R.id.size)).setText(msg.getData().getString(
                            "fileSizeText"));
                    break;
                }
                case DownloadService.DOWNLOAD_FETCH_ICON: {
                    // Update UI when the download service has fetched the icon of a
                    // downloading application.
                    final View child = getItemPosition(urlStr);
                    if (child == null) {
                        return;
                    }
                    final Map<String, ?> dataSet = downloadService.getItem(urlStr);
                    if (dataSet == null || dataSet.size() == 0) {
                        return;
                    }
                    ((ImageView) child.findViewById(R.id.apk_icon)).setImageBitmap((Bitmap) dataSet
                            .get("icon"));
                    break;
                }
                case DownloadService.DOWNLOAD_COMPLETE: {
                    refreshList();
                    break;
                }
                case DownloadService.DOWNLOAD_DELETE: {
                    refreshList();
                    if (isLastPos && mListView.getCount() > 0) {
                        selListView(mListView.getCount() - 1);
                    }
                    break;
                }
                case DownloadService.DOWNLOAD_ERROR: {
                    refreshList();
                    break;
                }
                case DownloadService.DOWNLOAD_RETRY: {
                    refreshList();
                    break;
                }
            }
            if (!mCurrTabView.isFocused()) {
                final int count = mListView.getCount();
                if (count <= 0) {
                    mCurrTabView.requestFocus();
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_item);
        msgText = (TextView) findViewById(R.id.msg);
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setItemsCanFocus(true);
        /** 获取当前选中的tab页 */
        mParentActivity = (DownloadManagerActivity) getParent();
        TabHost tabHost = (TabHost) mParentActivity.findViewById(R.id.tab_host);
        mCurrTabView = tabHost.getCurrentTabView();
        mDownloadType = getIntent().getIntExtra("type", -1);
        serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                downloadService = ((DownloadService.MyBinder) service).getService();
                downloadService.setUIHandler(mHandler);
                initList();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        // Bind the DownloadService.
        getApplicationContext().bindService(new Intent(this, DownloadService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
        mCurrTabView.setOnKeyListener(mOnKey);
        uh = new UpdateHandler();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.miri.updatehandler.downitemacitvity");
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(uh, filter);
    }

    @Override
    protected void onDestroy() {
        if (downloadService != null) {
            downloadService.setUIHandler(null);
            // Unbind the service.
            getApplicationContext().unbindService(serviceConnection);
        }
        if (uh != null) {
            unregisterReceiver(uh);
        }
        super.onDestroy();
        Intent intent = new Intent("com.miri.updatehandler.checkUpgradeacitvity");
        sendBroadcast(intent);
    }

    private void initList() {
        List<Map<String, Object>> list;
        if (mDownloadType == DownloadService.TYPE_COMPLETED) {
            list = downloadService.getDoneItemsInfo();
        } else if (mDownloadType == DownloadService.TYPE_DOWNLOADING) {
            list = downloadService.getUnDoneItemsInfo();
        } else {
            //全部
            list = downloadService.getItemsInfo();
        }
        if (Toolkit.isEmpty(list)) {
            if (mDownloadType == DownloadService.TYPE_COMPLETED) {
                msgText.setText(R.string.no_downfinish_content);
            } else if (mDownloadType == DownloadService.TYPE_DOWNLOADING) {
                msgText.setText(R.string.no_downing_content);
            } else {
                msgText.setText(R.string.no_apk_content);
            }
            msgText.setVisibility(View.VISIBLE);
        } else {
            msgText.setVisibility(View.GONE);
            mListAdapter = new DLListAdapter(this, list, downloadService.getHandler());
            Toolkit.disableOverScrollMode(mListView);
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mCurrPosition = position;

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        mCurrTabView.requestFocus();
    }

    private void refreshList() {
        if (mListAdapter != null) {
            List<Map<String, Object>> list;
            if (mDownloadType == DownloadService.TYPE_COMPLETED) {
                list = downloadService.getDoneItemsInfo();
            } else if (mDownloadType == DownloadService.TYPE_DOWNLOADING) {
                list = downloadService.getUnDoneItemsInfo();
            } else {
                //全部
                list = downloadService.getItemsInfo();
            }
            mListAdapter.setAppData(list);
            mListAdapter.notifyDataSetChanged();
            if (Toolkit.isEmpty(list)) {
                if (mDownloadType == DownloadService.TYPE_COMPLETED) {
                    msgText.setText(R.string.no_downfinish_content);
                } else if (mDownloadType == DownloadService.TYPE_DOWNLOADING) {
                    msgText.setText(R.string.no_downing_content);
                } else {
                    msgText.setText(R.string.no_apk_content);
                }
                msgText.setVisibility(View.VISIBLE);
            } else {
                msgText.setVisibility(View.GONE);
            }
        } else {
            initList();
        }
    }

    private OnKeyListener mOnKey = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (v == mCurrTabView && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    requestFocus();
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                        || keyCode == KeyEvent.KEYCODE_ENTER) {
                    return true;
                }
            }
            return false;

        }
    };

    private void requestFocus() {
        if (mListView != null && mListView.getCount() > 0) {
            selListView(mCurrPosition);
        } else {
            mCurrTabView.requestFocus();
        }
    }

    private void selListView(final int position) {
        if (mListView == null) {
            return;
        }
        int count = mListView.getCount();
        if (position >= 0 && count > 0 && position < count) {
            mListView.setSelection(position);
            mListView.post(new Runnable() {

                @Override
                public void run() {
                    final View child = mListView.getChildAt(position);
                    View optView = child.findViewById(R.id.btn_opt);
                    View deleteView = child.findViewById(R.id.btn_delete);
                    optView.requestFocus();
                }
            });
        }
    }

    private View getItemPosition(String urlStr) {
        View posView = null;
        int firstPos = mListView.getFirstVisiblePosition();
        int lastPos = mListView.getLastVisiblePosition();
        for (int i = firstPos; i <= lastPos; i++) {
            View child = mListView.getChildAt(i);
            if (child != null) {
                String url = (String) ((ProgressBar) child.findViewById(R.id.dl_progress)).getTag();
                if (url != null && url.equals(urlStr)) {
                    posView = child;
                    break;
                }
            }
        }
        return posView;
    }

    private class DLListAdapter extends BaseAdapter {

        private Context mContext;

        private LayoutInflater mInflater;

        private List<? extends Map<String, ?>> mAppData;

        private Handler mClickHandler;

        private OnClickListener mOnClickListener1, mOnClickListener2;

        public DLListAdapter(Context context, List<? extends Map<String, ?>> list, Handler handler) {
            mContext = context;
            mAppData = list;
            mClickHandler = handler;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mOnClickListener1 = new ClickListener();
            mOnClickListener2 = new ClickListener();
        }

        @Override
        public int getCount() {
            return mAppData.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setAppData(List<? extends Map<String, ?>> appData) {
            this.mAppData = appData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            View rowView = convertView;
            if (rowView == null) {
                holder = new ViewHolder();
                rowView = this.mInflater.inflate(R.layout.down_list_item, null);
                holder.iconView = (ImageView) rowView.findViewById(R.id.apk_icon);
                holder.apkName = (TextView) rowView.findViewById(R.id.apk_name);
                holder.size = (TextView) rowView.findViewById(R.id.size);
                holder.percent = (TextView) rowView.findViewById(R.id.percent);
                holder.progress = (ProgressBar) rowView.findViewById(R.id.dl_progress);
                holder.optView = (CustomBtnView) rowView.findViewById(R.id.btn_opt);
                holder.deleteView = (CustomBtnView) rowView.findViewById(R.id.btn_delete);
                rowView.setTag(holder);
                holder.optView.setOnClickListener(mOnClickListener1);
                holder.deleteView.setOnClickListener(mOnClickListener2);

                holder.optView.setOnKeyListener(new KeyListener(holder, position));
                holder.deleteView.setOnKeyListener(new KeyListener(holder, position));

                holder.optView.setOnFocusChangeListener(new FocusChangeListener(position));
                holder.deleteView.setOnFocusChangeListener(new FocusChangeListener(position));
            } else {
                holder = (ViewHolder) rowView.getTag();
            }
            Map<String, ?> dataSet = mAppData.get(position);
            createViewFromResource(dataSet, holder);

            return rowView;
        }

        private void createViewFromResource(Map<String, ?> dataSet, ViewHolder holder) {
            holder.apkName.setText((String) dataSet.get("appName"));
            holder.size.setText((String) dataSet.get("fileSizeText"));
            //bind Progress
            holder.progress.setTag(dataSet.get("urlStr"));
            holder.progress.setMax(100);
            Integer progress = (Integer) dataSet.get("progress");
            if (progress != null) {
                holder.progress.setProgress(progress);
            }
            //bind percent
            holder.percent.setText((String) dataSet.get("statusText"));
            // Bind icon
            final int type = (Integer) dataSet.get("type");
            Object icon = dataSet.get("icon");
            if (icon != null) {
                if (icon instanceof Drawable) {
                    holder.iconView.setImageDrawable((Drawable) icon);
                } else if (icon instanceof Bitmap) {
                    holder.iconView.setImageBitmap((Bitmap) icon);
                }
            } else {
                // If the icon is null, use the system default application icon instead.
                holder.iconView.setImageResource(android.R.drawable.sym_def_app_icon);
            }

            //bind btn
            if (type == DownloadService.TYPE_DOWNLOADING) {
                if ((Boolean) dataSet.get("isError")) {
                    holder.optView.setText(R.string.btn_retry);
                    holder.optView.setIconDrawable(R.drawable.down_start_icon);
                } else {
                    holder.optView.setText((Boolean) dataSet.get("isPause") ? R.string.btn_continue
                            : R.string.btn_pause);
                    holder.optView
                            .setIconDrawable((Boolean) dataSet.get("isPause") ? R.drawable.down_start_icon
                                    : R.drawable.down_pause_icon);
                }
            } else if (type == DownloadService.TYPE_COMPLETED) {
                if (AppManager.isInstall(mContext, (String) dataSet.get("filePath"))) {
                    holder.optView.setText(R.string.btn_installed);
                } else {
                    holder.optView.setText(R.string.btn_install);
                }
                holder.optView.setIconDrawable(R.drawable.down_install_icon);
            }
            holder.optView.setTag(dataSet);
            holder.deleteView.setTag(dataSet);
        }

        private class FocusChangeListener implements OnFocusChangeListener {

            private int position;

            public FocusChangeListener(int position) {
                this.position = position;
            }

            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (v.getId() == R.id.btn_opt || v.getId() == R.id.btn_delete) {
                    if (hasFocus) {
                        //                        mListView.setSelection(position);
                        //                        mListView.invalidateViews();
                        //                        mListView.post(new Runnable() {
                        //
                        //                            @Override
                        //                            public void run() {
                        //                                v.requestFocus();
                        //                            }
                        //
                        //                        });
                    }
                }

            }

        }

        /**
         * 点击事件
         * @author penglin
         * @version TVLAUNCHER001, 2013-9-17
         */
        private class ClickListener implements OnClickListener {

            @Override
            public void onClick(final View v) {
                Logger.getLogger().e("v :" + v);
                if (v.getId() == R.id.btn_opt) {
                    final Map<String, ?> dataSet = (Map<String, ?>) v.getTag();
                    final int type = (Integer) dataSet.get("type");
                    if (type == DownloadService.TYPE_DOWNLOADING) {
                        if ((Boolean) dataSet.get("isError")) {
                            Message msg = new Message();
                            msg.what = DownloadService.DOWNLOAD_RETRY;
                            msg.getData().putString("urlStr", (String) dataSet.get("urlStr"));
                            ((CustomBtnView) v).setText(R.string.btn_retry);
                            ((CustomBtnView) v).setIconDrawable(R.drawable.down_start_icon);
                            mClickHandler.sendMessage(msg);
                        } else {
                            boolean isPause = (Boolean) dataSet.get("isPause");
                            Message msg = new Message();
                            msg.what = DownloadService.DOWNLOAD_PAUSE;
                            msg.getData().putString("urlStr", (String) dataSet.get("urlStr"));
                            msg.getData().putBoolean("isPause", !isPause);
                            ((CustomBtnView) v).setText(isPause ? R.string.btn_pause
                                    : R.string.btn_continue);
                            ((CustomBtnView) v)
                                    .setIconDrawable(isPause ? R.drawable.down_pause_icon
                                            : R.drawable.down_start_icon);
                            mHandler.removeMessages(DownloadService.DOWNLOAD_PROGRESS);
                            mClickHandler.removeMessages(DownloadService.DOWNLOAD_PAUSE);
                            mClickHandler.sendMessageDelayed(msg, 500);
                        }
                    } else if (type == DownloadService.TYPE_COMPLETED) {
                        downloadService.install((String) dataSet.get("filePath"));
                    }
                } else if (v.getId() == R.id.btn_delete) {
                    isLastPos = mListView.getSelectedItemPosition() == mListView.getCount() - 1;
                    AlertUtil.showAlert(mContext, R.string.download_delete_confirm,
                            R.string.download_delete_message, mContext.getString(R.string.ok),
                            new AlertDialog.OnOkBtnClickListener() {

                                @Override
                                public void onClick() {
                                    final Map<String, ?> dataSet = (Map<String, ?>) v.getTag();
                                    Message msg = new Message();
                                    msg.what = DownloadService.DOWNLOAD_DELETE;
                                    msg.getData().putString("urlStr",
                                            (String) dataSet.get("urlStr"));
                                    mClickHandler.sendMessage(msg);
                                }
                            }, mContext.getString(R.string.cancel), null);

                }
            }

        }

        /**
         * @author penglin
         * @version TVLAUNCHER001, 2013-9-17
         */
        private class KeyListener implements OnKeyListener {

            private ViewHolder holder;

            private int position;

            public KeyListener(ViewHolder holder, int position) {
                this.holder = holder;
                this.position = position;
            }

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                /** 焦点在列表第一个，阻止焦点向上 */
                if ((position <= 0) && (event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
                    return true;
                }
                /** 最左侧的item点击left,选中当前tab页 */
                if ((v.getId() == R.id.btn_opt) && (event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)) {
                    mCurrTabView.requestFocus();
                    return true;
                }
                /** 焦点在列表第最后一个，阻止焦点向下 */
                if ((position == getCount() - 1) && (event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)) {
                    return true;
                }
                return false;
            }

        }

        class ViewHolder {

            public ImageView iconView;

            public TextView apkName;

            public TextView size;

            public TextView rate;

            public TextView percent;

            public ProgressBar progress;

            public CustomBtnView optView;

            public CustomBtnView deleteView;

        }

    }

    class UpdateHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.miri.updatehandler.downitemacitvity")) {
                if (downloadService != null) {
                    downloadService.setUIHandler(mHandler);
                }
            }
            if (Intent.ACTION_PACKAGE_CHANGED.equals(action)
                    || Intent.ACTION_PACKAGE_REMOVED.equals(action)
                    || Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                refreshList();
                if (!mCurrTabView.isFocused()) {
                    final int count = mListView.getCount();
                    if (count <= 0) {
                        mCurrTabView.requestFocus();
                    }
                }
            }
        }
    }

}
