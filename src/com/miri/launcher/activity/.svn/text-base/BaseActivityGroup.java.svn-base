/* 
 * 文件名：BaseActivityGroup.java
 * 版权：Copyright
 */
package com.miri.launcher.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;

import com.miri.launcher.utils.Logger;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-13
 */
@SuppressWarnings("deprecation")
public class BaseActivityGroup extends ActivityGroup {

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
        //        menu.add("menu");// 必须创建一项
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Logger.getLogger().e("menu opened !!!!!");
        //TODO 某些设备未执行onMenuOpened
        //        startActivity(new Intent(this, MenuActivity.class));
        //        return false;// 返回为true 则显示系统menu
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

}
