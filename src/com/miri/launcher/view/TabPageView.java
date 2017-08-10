/* 
 * 文件名：TabPageView.java
 * 版权：Copyright
 */
package com.miri.launcher.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.miri.launcher.R;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-4
 */
public class TabPageView extends LinearLayout {

    private TabPageIndicator tabPageIndicator;

    public TabPageView(Context context) {
        this(context, null);
    }

    public TabPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.tab_page, this, true);
        tabPageIndicator = (TabPageIndicator) view.findViewById(R.id.tab_indicator);

    }

    public TabPageIndicator getTabPageIndicator() {
        return tabPageIndicator;
    }

    public View getCurrTabView() {
        if (tabPageIndicator != null) {
            return tabPageIndicator.getCurrTabView();
        }
        return null;
    }

    public void resetTabWidth(int width) {
        if (width >= 0) {
            RelativeLayout.LayoutParams layParams = new RelativeLayout.LayoutParams(width,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            layParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            tabPageIndicator.setLayoutParams(layParams);
        }
    }

}
