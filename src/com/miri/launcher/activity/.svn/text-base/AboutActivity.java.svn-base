package com.miri.launcher.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.miri.launcher.Constants;
import com.miri.launcher.PersistData;
import com.miri.launcher.R;

/**
 * 关于我们
 * @author zengjiantao
 */
public class AboutActivity extends BaseActivity {

    private TextView mVersionFlagText;

    private TextView mSoftwareNameTxt;

    private TextView mVersionNameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        mVersionFlagText = (TextView) findViewById(R.id.version_flag);
        mSoftwareNameTxt = (TextView) findViewById(R.id.software_name);
        mVersionNameTxt = (TextView) findViewById(R.id.version_name);

        if (PersistData.VERSION_TYPE == Constants.KONGGE) {
            mVersionFlagText.setText(R.string.kongge_desc);
            mVersionFlagText.setVisibility(View.VISIBLE);
        }

        mSoftwareNameTxt.setText(getResources().getString(R.string.software_name,
                getResources().getString(R.string.app_name)));
        mVersionNameTxt.setText(getResources().getString(R.string.version_name,
                getLocalVersion(this)));
    }

    /**
     * 获取本地版本字符串(AndroidManifest.xml中配置的版本信息)
     * @return
     */
    private String getLocalVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        String version = null;

        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            version = pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }
}
