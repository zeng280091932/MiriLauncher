/*
 * 
 */
package com.miri.launcher.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;

import com.miri.launcher.utils.Logger;
import com.miri.launcher.view.CustomToast;

/**
 * 基础Acitvity，可在此类中添加基础公用方法
 * @author zengjiantao
 * @date 2013-5-19
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //        ResourceHelper.updateConfig(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Logger.getLogger().e("onCreate OptionsMenu!!!!!");
        // menu.add("menu");// 必须创建一项
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Logger.getLogger().e("menu opened !!!!!");
        // TODO 某些设备未执行onMenuOpened
        // startActivity(new Intent(this, MenuActivity.class));
        // return false;// 返回为true 则显示系统menu
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_MENU)) {
            Logger.getLogger().e("onKeyDown menu ,KEYCODE_MENU:" + KeyEvent.KEYCODE_MENU);
            startActivity(new Intent(this, MenuActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void showToast(String text) {
        //        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        CustomToast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}
