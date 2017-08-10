/* 
 * 文件名：CheckUpgradeActivity.java
 * 版权：Copyright
 */
package com.miri.launcher.activity;

import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.miri.launcher.PersistData;
import com.miri.launcher.R;
import com.miri.launcher.market.DownloadService;
import com.miri.launcher.market.DownloadService.Downloader;
import com.miri.launcher.upgrade.Software;
import com.miri.launcher.upgrade.UpgradeHelper;
import com.miri.launcher.upgrade.UpgradeHelper.OnCheckVersionComplete;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.CustomBtnView;
import com.miri.launcher.view.dialog.AlertDialog;
import com.miri.launcher.view.dialog.AlertUtil;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-13
 */
public class CheckUpgradeActivity extends BaseActivity {

    //---------左侧
    private LinearLayout mCheckUpdateLayout;

    private ImageView mCheckUpdateIcon;

    private TextView mCheckUpdateText;

    private LinearLayout mDownloadingLayout;

    private ImageView mDownloadingIcon;

    private TextView mDownloadingText;

    //----------右侧检查版本层
    private LinearLayout mCheckUpdateContent;

    private TextView mUpgradeTip;

    private TextView mUpgradeConfim;

    private CustomBtnView mOkBtn;

    private CustomBtnView mCancelBtn;

    private LinearLayout mLogLayout;

    private TextView mLog;

    //----------右侧下载安装层
    private LinearLayout mDownloadingContent;

    private LinearLayout mLoadingLayout;

    private ProgressBar mLoadingProgress;

    private TextView mMsgTextView;

    private ProgressBar mprogreess;

    private TextView mPrecent;

    private TextView mFileSize;

    private TextView mRate;

    private CustomBtnView mOptBtn;

    private CustomBtnView mRemoveBtn;

    private DownloadService downloadService;

    private ServiceConnection serviceConnection;

    private Software mSoftware;

    private final int CHECK_FAIL = 100;

    private final int CHECK_SUCCESS = 101;

    private UpdateHandler uh;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            // The index of the changed item in the list.
            String urlStr = msg.getData().getString("urlStr");
            if (urlStr == null || mSoftware == null || !urlStr.equals(mSoftware.getUrl())) {
                return;
            }
            final Map<String, ?> dataSet = downloadService.getItem(urlStr);
            mOptBtn.setTag(dataSet);
            mRemoveBtn.setTag(dataSet);
            switch (msg.what) {
                case DownloadService.DOWNLOAD_INIT: {
                    // Initiate the item's view when the corresponding download
                    // threads start.
                    mPrecent.setText(msg.getData().getString("statusText"));
                    mFileSize.setText(msg.getData().getString("fileSizeText"));
                    break;
                }
                case DownloadService.DOWNLOAD_PROGRESS: {
                    // Update UI when the download progress changes.
                    mFileSize.setText(msg.getData().getString("fileSizeText"));
                    mPrecent.setText(msg.getData().getString("statusText"));
                    mprogreess.setProgress(msg.getData().getInt("progress"));
                    break;
                }
                case DownloadService.DOWNLOAD_FETCH_ICON: {
                }
                case DownloadService.DOWNLOAD_COMPLETE: {
                    // Update UI when a download finishes.
                    showDownloadMsg(getString(R.string.dl_finish, getString(R.string.app_name)));
                    mFileSize.setText(msg.getData().getString("fileSizeText"));
                    mPrecent.setText(msg.getData().getString("statusText"));
                    mprogreess.setProgress(msg.getData().getInt("progress"));
                    mOptBtn.setText(R.string.btn_install);
                    mOptBtn.setBackgroundResource(R.drawable.btn_backgrund_green);
                    break;
                }
                case DownloadService.DOWNLOAD_DELETE: {
                    // Update UI when a downloading or completed item is deleted.
                    // mListView.removeViews(index, 1);
                    finish();
                    break;
                }
                case DownloadService.DOWNLOAD_ERROR: {
                    dimssLoading();
                    mFileSize.setText(msg.getData().getString("fileSizeText"));
                    mPrecent.setText(msg.getData().getString("statusText"));
                    mprogreess.setProgress(msg.getData().getInt("progress"));
                    mOptBtn.setText(R.string.btn_retry);
                    mOptBtn.setBackgroundResource(R.drawable.btn_backgrund_orange);
                    break;
                }
                case DownloadService.DOWNLOAD_RETRY: {
                    dimssLoading();
                    mPrecent.setText(msg.getData().getString("statusText"));
                    mOptBtn.setText(R.string.btn_retry);
                    mOptBtn.setBackgroundResource(R.drawable.btn_backgrund_orange);
                    break;
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_upgrade);
        mCheckUpdateLayout = (LinearLayout) findViewById(R.id.check_upgrade_layout);
        mCheckUpdateIcon = (ImageView) findViewById(R.id.check_upgrade_icon);
        mCheckUpdateText = (TextView) findViewById(R.id.check_version_text);
        mDownloadingLayout = (LinearLayout) findViewById(R.id.download_layout);
        mDownloadingIcon = (ImageView) findViewById(R.id.downloading_icon);
        mDownloadingText = (TextView) findViewById(R.id.downloading_text);

        mCheckUpdateContent = (LinearLayout) findViewById(R.id.check_upgrade_content);
        mUpgradeTip = (TextView) findViewById(R.id.check_upgrading);
        mUpgradeConfim = (TextView) findViewById(R.id.upgrade_confirm);
        mOkBtn = (CustomBtnView) findViewById(R.id.okBtn);
        mCancelBtn = (CustomBtnView) findViewById(R.id.cancelBtn);
        mLogLayout = (LinearLayout) findViewById(R.id.upgrade_log_layout);
        mLog = (TextView) findViewById(R.id.upgrade_log);

        mDownloadingContent = (LinearLayout) findViewById(R.id.downloading_content);
        mLoadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        mLoadingProgress = (ProgressBar) findViewById(R.id.progress);
        mMsgTextView = (TextView) findViewById(R.id.msg);
        mprogreess = (ProgressBar) findViewById(R.id.dl_progress);
        mPrecent = (TextView) findViewById(R.id.percent);
        mFileSize = (TextView) findViewById(R.id.size);
        mRate = (TextView) findViewById(R.id.rate);
        mOptBtn = (CustomBtnView) findViewById(R.id.optBtn);
        mRemoveBtn = (CustomBtnView) findViewById(R.id.removeBtn);

        selectCheckUpgradLayout();
        checkingVersion();
        bindListener();
        serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Logger.getLogger().d("serviceConnection");
                downloadService = ((DownloadService.MyBinder) service).getService();
                downloadService.setUIHandler(mHandler);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        // Bind the DownloadService.
        getApplicationContext().bindService(new Intent(this, DownloadService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
        uh = new UpdateHandler();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.miri.updatehandler.checkUpgradeacitvity");
        registerReceiver(uh, filter);
    }

    private OnClickListener mOnClick = new OnClickListener() {

        @Override
        public void onClick(final View v) {
            if (v == mOptBtn) {
                final Map<String, ?> dataSet = (Map<String, ?>) v.getTag();
                if (dataSet == null) {
                    return;
                }
                final int type = (Integer) dataSet.get("type");
                if (type == DownloadService.TYPE_DOWNLOADING) {
                    if ((Boolean) dataSet.get("isError")) {
                        showLoading();
                        ((CustomBtnView) v).setText(R.string.silent);
                        Message msg = new Message();
                        msg.what = DownloadService.DOWNLOAD_RETRY;
                        msg.getData().putString("urlStr", (String) dataSet.get("urlStr"));
                        downloadService.getHandler().sendMessage(msg);
                    } else {
                        finish();
                    }
                } else if (type == DownloadService.TYPE_COMPLETED) {
                    downloadService.install((String) dataSet.get("filePath"));
                }

            } else if (v == mRemoveBtn) {
                AlertUtil.showAlert(CheckUpgradeActivity.this, R.string.download_delete_confirm,
                        R.string.download_delete_message, getString(R.string.ok),
                        new AlertDialog.OnOkBtnClickListener() {

                            @Override
                            public void onClick() {
                                final Map<String, ?> dataSet = (Map<String, ?>) v.getTag();
                                if (dataSet == null) {
                                    return;
                                }
                                Message msg = new Message();
                                msg.what = DownloadService.DOWNLOAD_DELETE;
                                msg.getData().putString("urlStr", (String) dataSet.get("urlStr"));
                                downloadService.getHandler().sendMessage(msg);
                            }
                        }, getString(R.string.cancel), null);
            } else if (v == mOkBtn) {
                selectDownloadLayout();
                downloadSoft(mSoftware.getUrl(), mSoftware.getVersion());
            } else if (v == mCancelBtn) {
                Integer tag = (Integer) mCancelBtn.getTag();
                if (tag == CHECK_SUCCESS) {
                    finish();
                } else if (tag == CHECK_FAIL) {
                    //重新检查
                    checkingVersion();
                }
            }
        }
    };

    private void bindListener() {
        mOptBtn.setOnClickListener(mOnClick);
        mRemoveBtn.setOnClickListener(mOnClick);
        mOkBtn.setOnClickListener(mOnClick);
        mCancelBtn.setOnClickListener(mOnClick);
    }

    /**
     * 选中左侧检查版本视图层
     */
    private void selectCheckUpgradLayout() {
        mCheckUpdateLayout.setBackgroundResource(R.drawable.upgrade_left_bg);
        mCheckUpdateIcon.setImageResource(R.drawable.check_upgrade_selected);
        mCheckUpdateText.setTextColor(0xffffffff);
        mDownloadingLayout.setBackgroundResource(android.R.color.transparent);
        mDownloadingIcon.setImageResource(R.drawable.downloading_unselected);
        mDownloadingText.setTextColor(0xb2ffffff);
    }

    /**
     * 选中左侧下载安装视图层
     */
    private void selectDownloadLayout() {
        mDownloadingLayout.setBackgroundResource(R.drawable.upgrade_left_bg);
        mDownloadingIcon.setImageResource(R.drawable.downloading_selected);
        mDownloadingText.setTextColor(0xffffffff);
        mCheckUpdateLayout.setBackgroundResource(android.R.color.transparent);
        mCheckUpdateIcon.setImageResource(R.drawable.check_upgrade_unselected);
        mCheckUpdateText.setTextColor(0xb2ffffff);
        mOptBtn.requestFocus();
    }

    /**
     * 正在检查新版本
     */
    private void checkingVersion() {
        mCheckUpdateContent.setVisibility(View.VISIBLE);
        mDownloadingContent.setVisibility(View.GONE);
        mUpgradeTip.setText(R.string.check_upgrading);
        mUpgradeTip.setVisibility(View.VISIBLE);
        mUpgradeConfim.setVisibility(View.GONE);
        mOkBtn.setVisibility(View.GONE);
        mCancelBtn.setVisibility(View.GONE);
        mLogLayout.setVisibility(View.GONE);
        new UpgradeHelper().doUpgrade(this, PersistData.getUpgradeUrl(), true,
                new OnCheckVersionComplete() {

                    @Override
                    public void onComplete(final int exType, final Software software) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (exType == UpgradeHelper.IS_LASTEST_VERSION) {
                                    noVersionUpgrade();
                                } else if (exType == UpgradeHelper.EX_NETWORK_ERROR) {
                                    showCheckUpgradeError(R.string.network_timeout);
                                } else if (exType == UpgradeHelper.EX_PARSEFILE_FAIL) {
                                    showCheckUpgradeError(R.string.parse_upgrade_file_error);
                                } else if (exType == UpgradeHelper.EXIST_UPGRADE_VERSION) {
                                    String versionName = software.getVersion();
                                    String log = software.getLog();
                                    hasVersionUpgrade(versionName == null ? "" : versionName,
                                            log == null ? "" : log);
                                    mSoftware = software;
                                }

                            }
                        });

                    }
                });
    }

    /**
     * 没有新版本，当前版本已是最新
     */
    private void noVersionUpgrade() {
        mCheckUpdateContent.setVisibility(View.VISIBLE);
        mDownloadingContent.setVisibility(View.GONE);
        mUpgradeTip.setText(R.string.check_upgradie_finish);
        mUpgradeTip.setVisibility(View.VISIBLE);
        mUpgradeConfim.setText(getString(R.string.version_lastest, Toolkit.getLocalVersion(this)));
        mUpgradeConfim.setVisibility(View.VISIBLE);
        mOkBtn.setVisibility(View.GONE);
        mCancelBtn.setVisibility(View.VISIBLE);
        mCancelBtn.setText(R.string.back);
        mCancelBtn.setBackgroundResource(R.drawable.btn_backgrund_green);
        mCancelBtn.setTag(CHECK_SUCCESS);
        mCancelBtn.requestFocus();
        mLogLayout.setVisibility(View.GONE);
    }

    /**
     * 显示检查版本了出错消息
     * @param resId
     */
    private void showCheckUpgradeError(int resId) {
        mCheckUpdateContent.setVisibility(View.VISIBLE);
        mDownloadingContent.setVisibility(View.GONE);
        mUpgradeTip.setText(R.string.check_upgradie_finish);
        mUpgradeTip.setVisibility(View.VISIBLE);
        mUpgradeConfim.setText(resId);
        mUpgradeConfim.setVisibility(View.VISIBLE);
        mOkBtn.setVisibility(View.GONE);
        mCancelBtn.setVisibility(View.VISIBLE);
        mCancelBtn.setText(R.string.btn_retry);
        mCancelBtn.setBackgroundResource(R.drawable.btn_backgrund_orange);
        mCancelBtn.setTag(CHECK_FAIL);
        mCancelBtn.requestFocus();
        mLogLayout.setVisibility(View.GONE);
    }

    /**
     * 检查到有新版本
     * @param versionNo
     * @param log
     */
    private void hasVersionUpgrade(String versionNo, String log) {
        mCheckUpdateContent.setVisibility(View.VISIBLE);
        mDownloadingContent.setVisibility(View.GONE);
        mUpgradeTip.setText(R.string.check_upgradie_finish);
        mUpgradeTip.setVisibility(View.VISIBLE);
        mUpgradeConfim.setText(getString(R.string.upgrade_confirm, Toolkit.getLocalVersion(this),
                versionNo));
        mUpgradeConfim.setVisibility(View.VISIBLE);
        mOkBtn.setVisibility(View.VISIBLE);
        mOkBtn.requestFocus();
        mCancelBtn.setVisibility(View.VISIBLE);
        mCancelBtn.setText(R.string.cancel);
        mCancelBtn.setBackgroundResource(R.drawable.btn_backgrund_orange);
        mCancelBtn.setTag(CHECK_SUCCESS);
        if (!Toolkit.isEmpty(log)) {
            mLogLayout.setVisibility(View.VISIBLE);
            mLog.setText(log);
            Logger.getLogger().d("log:" + log);
            //TODO 需解决转义字符的问题
            Spanned text = Html.fromHtml(log);
            mLog.setText(text);
            //            SpannableStringBuilder spannable = new SpannableStringBuilder(mLog);
            //            mLogText.setText(spannable, BufferType.SPANNABLE);
        } else {
            mLogLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 下载安装
     */
    private void downloadSoft(String urlStr, String versionName) {
        mCheckUpdateContent.setVisibility(View.GONE);
        mDownloadingContent.setVisibility(View.VISIBLE);
        showLoading();
        boolean isDownFinish = downloadService.isDownFinish(urlStr);
        if (isDownFinish) {
            final Map<String, ?> dataSet = downloadService.getItem(urlStr);
            Message msg = new Message();
            msg.getData().putString("urlStr", urlStr);
            msg.what = DownloadService.DOWNLOAD_COMPLETE;
            msg.getData().putString("filePath", (String) dataSet.get("filePath"));
            msg.getData().putInt("progress", 100);
            msg.getData().putString("statusText", "100%");
            msg.getData().putString("fileSizeText", (String) dataSet.get("fileSizeText"));
            mHandler.sendMessage(msg);
            return;
        }
        final Map<String, Object> map = downloadService.getItem(urlStr);
        if (map != null) {
            String filePath = (String) map.get("fileName");
            String appName = (String) map.get("appName");
            String iconPath = (String) map.get("iconPath");
            //            downloadService.createDownload(urlStr, filePath, appName, iconPath, false, false);
            final boolean isPause = (Boolean) map.get("isPause");
            if (isPause) {
                ((Downloader) map.get("downloader")).resume();
            }
        } else {
            downloadService.createDownload(urlStr, null, getString(R.string.app_name), null, true,
                    false);
        }
    }

    /**
     * 显示下载加载条
     */
    private void showLoading() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingProgress.setVisibility(View.VISIBLE);
        mMsgTextView.setText(R.string.upgrade_downloading);
    }

    /**
     * 显示下载信息
     * @param msg
     */
    private void showDownloadMsg(String msg) {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingProgress.setVisibility(View.INVISIBLE);
        mMsgTextView.setText(msg);
    }

    private void dimssLoading() {
        mLoadingLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (uh != null) {
            unregisterReceiver(uh);
        }
        if (downloadService != null) {
            downloadService.setUIHandler(null);
            // Unbind the service.
            getApplicationContext().unbindService(serviceConnection);
        }
        super.onDestroy();
        Intent intent = new Intent("com.miri.updatehandler.downitemacitvity");
        sendBroadcast(intent);

    }

    class UpdateHandler extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (downloadService != null) {
                downloadService.setUIHandler(mHandler);
            }
        }
    }

}
