/*
 * 文件名：PagerPosterView.java
 * 版权：Copyright
 */
package com.miri.launcher.view;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.imageCache.ImageLoader;
import com.miri.launcher.imageCache.ImageLoader.ImgCallback;
import com.miri.launcher.moretv.MediaInfoUtil;
import com.miri.launcher.moretv.model.MediaInfo;
import com.miri.launcher.msg.model.AppNode;
import com.miri.launcher.utils.IconUtil;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.Toolkit;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-6
 */
public class PagerPosterView extends FrameLayout implements OnClickListener {

    private OnReClickLisitener mOnReClick;

    private static final int delay = 5;

    private static final int period = 5;

    private LinearLayout mDotLinear;

    private ViewPager mViewPager;

    private List<View> mPageViews;

    private List<String> mTitleStr;

    private List<Spanned> mDescStr;

    // 展示图片的页码（一排点标志展示哪张图）
    private List<ImageView> mDotImgs;

    private boolean isContinue = true;

    private TextView mTitle;

    private TextView mDesc;

    private LinearLayout mInfoLayout;

    private int currentItem = 0; // 当前图片的索引号

    private ScheduledExecutorService service;

    // 是否显示页码标志点
    private boolean isPointOut = true;

    // 是否显示文字
    private boolean isShowTitle = true;

    private final int maxPage = 0;// 最大页码

    // 切换当前显示的图片
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(android.os.Message msg) {
            mViewPager.setCurrentItem(currentItem);// 切换当前显示的图片
        };
    };

    public PagerPosterView(Context context) {
        this(context, null);
    }

    public PagerPosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        View view = LayoutInflater.from(context).inflate(
                R.layout.flipper_poster_view, this, true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);
        setOnClickListener(this);
        mViewPager = (ViewPager) view.findViewById(R.id.flipper);
        mTitle = (CustomTextView) view.findViewById(R.id.title);
        mDesc = (TextView) view.findViewById(R.id.desc);
        mInfoLayout = (LinearLayout) view.findViewById(R.id.info_layout);
        mDotLinear = (LinearLayout) view.findViewById(R.id.dots);
        mViewPager.setFocusable(false);
        mViewPager.setFocusableInTouchMode(false);
        mPageViews = new ArrayList<View>();
        // 设置viewpager的适配器和监听事件
        mViewPager.setAdapter(new PageAdapter());
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        // 按下时不继续定时滑动,弹起时继续定时滑动
        setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    isContinue = false;
                    break;
                case MotionEvent.ACTION_UP:
                    isContinue = true;
                    break;
                default:
                    isContinue = true;
                    break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(new ScrollTask(), delay, period,
                TimeUnit.SECONDS);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (service != null) {
            service.shutdown();
        }
        super.onDetachedFromWindow();
    }

    /**
     * 换行切换任务
     * 
     * @author Administrator
     */
    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (mViewPager) {
                if (isContinue) {
                    if (mPageViews.size() > 1) {
                        currentItem = (currentItem + 1) % mPageViews.size();
                        // Logger.getLogger()
                        // .d("Pager Poster View currentItem: "
                        // + currentItem);
                        handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
                    }
                }
            }
        }

    }

    public void setInitAppRecommend(List<AppNode> appRecommends,
            boolean pointOut, boolean showTitle) {
        setInit(appRecommends, pointOut, showTitle);
    }

    public void setInitMediaInfo(List<MediaInfo> mediaInfos, boolean pointOut,
            boolean showTitle) {
        setInit(mediaInfos, pointOut, showTitle);
    }

    @SuppressWarnings("rawtypes")
    private void setInit(List objects, boolean pointOut, boolean showTitle) {
        if (Toolkit.isEmpty(objects)) {
            return;
        }
        this.isPointOut = pointOut;
        this.isShowTitle = showTitle;

        mPageViews = new ArrayList<View>();

        if (isShowTitle) {
            mTitleStr = new ArrayList<String>();
            mDescStr = new ArrayList<Spanned>();
            // mInfoLayout.setVisibility(View.VISIBLE);
        } else {
            // mInfoLayout.setVisibility(View.GONE);
        }

        if (isPointOut && objects.size() > 1) {
            mDotLinear.removeAllViews();
            mDotImgs = new ArrayList<ImageView>();
        }
        ImageView posterImg;
        ImageView dotImg;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(7, 0, 7, 0);
        for (int i = 0; i < objects.size(); i++) {
            Object obj = objects.get(i);
            if (obj == null) {
                continue;
            }
            if (isPointOut && objects.size() > 1) {
                // 设置圆点
                dotImg = new ImageView(getContext());
                dotImg.setLayoutParams(params);
                dotImg.setScaleType(ScaleType.FIT_XY);
                dotImg.setBackgroundResource((i == 0) ? R.drawable.circle_selected
                        : R.drawable.circle_normal);
                mDotImgs.add(dotImg);
                mDotLinear.addView(dotImg);
                mDotLinear.setVisibility(View.VISIBLE);
            } else {
                mDotLinear.setVisibility(View.GONE);
            }

            // 设置ViewPager显示的页面内容
            posterImg = new ImageView(getContext());
            posterImg.setLayoutParams(new LayoutParams(MATCH_PARENT,
                    MATCH_PARENT, Gravity.CENTER));
            posterImg.setScaleType(ScaleType.FIT_XY);
            String poster = "";
            String title = "";
            Spanned desc = Html.fromHtml("");
            if (obj instanceof AppNode) {
                AppNode recommend = (AppNode) obj;
                poster = recommend.getPoster();
                title = recommend.getName();
                boolean customIcon = recommend.isCustomIcon();
                if (customIcon) {
                    Intent.ShortcutIconResource iconRes = new Intent.ShortcutIconResource();
                    iconRes.packageName = getContext().getPackageName();
                    iconRes.resourceName = getContext().getPackageName() + ":"
                            + recommend.getIconResource();
                    posterImg.setImageDrawable(IconUtil
                            .getShortcutIncoResource(getContext(), iconRes));
                } else {
                    ImageLoader.from(getContext()).loadImg(posterImg, poster,
                            0, new ImgCallback() {

                        @Override
                        public void refresh(Object view, Bitmap bitmap,
                                boolean isFromNet) {
                            ((ImageView) view).setImageBitmap(bitmap);

                        }
                    });
                }
            } else if (obj instanceof MediaInfo) {
                MediaInfo mediaInfo = (MediaInfo) obj;
                poster = mediaInfo.getImage1();
                title = mediaInfo.getTitle();
                desc = MediaInfoUtil.createDesc(mediaInfo);
                boolean customIcon = mediaInfo.isCustomIcon();
                if (customIcon) {
                    Intent.ShortcutIconResource iconRes = new Intent.ShortcutIconResource();
                    iconRes.packageName = getContext().getPackageName();
                    iconRes.resourceName = getContext().getPackageName() + ":"
                            + mediaInfo.getIconResource();
                    posterImg.setImageDrawable(IconUtil
                            .getShortcutIncoResource(getContext(), iconRes));
                } else {
                    ImageLoader.from(getContext()).loadImg(posterImg, poster,
                            0, new ImgCallback() {

                        @Override
                        public void refresh(Object view, Bitmap bitmap,
                                boolean isFromNet) {
                            ((ImageView) view).setImageBitmap(bitmap);

                        }
                    });
                }

            }
            mPageViews.add(posterImg);
            posterImg.setTag(obj);

            if (isShowTitle) {
                mTitleStr.add(title);
                mDescStr.add(desc);
            }
        }
        if (isShowTitle && mTitleStr != null && mTitleStr.size() > 0) {
            setTitleText(mTitleStr.get(0));
        }
        if (isShowTitle && mDescStr != null && mDescStr.size() > 0) {
            setDescText(mDescStr.get(0));
        }
    }

    private void setTitleText(CharSequence text) {
        if (mTitle != null) {
            if (!TextUtils.isEmpty(text)) {
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(text);
            } else {
                mTitle.setVisibility(View.GONE);
            }
            invalidate();
        }
    }

    private void setDescText(CharSequence text) {
        if (mDesc != null) {
            if (!TextUtils.isEmpty(text)) {
                mDesc.setVisibility(View.VISIBLE);
                mDesc.setText(text);
            } else {
                mDesc.setVisibility(View.GONE);
            }
            invalidate();
        }
    }

    @Override
    protected void onFocusChanged(boolean hasFocus, int direction,
            Rect previouslyFocusedRect) {
        super.onFocusChanged(hasFocus, direction, previouslyFocusedRect);
        if (hasFocus) {
            bringToFront();
            Drawable drawable = getBackground();
            if (drawable == null) {
                setBackgroundResource(R.drawable.select_bg);
            }
            if (isShowTitle) {
                mInfoLayout.setVisibility(View.VISIBLE);
            }
        } else {
            setBackgroundDrawable(null);
            if (isShowTitle) {
                mInfoLayout.setVisibility(View.GONE);
            }
        }
        invalidate();
    }

    class PageAdapter extends PagerAdapter implements IconPagerAdapter {

        // 销毁position位置的界面
        @Override
        public void destroyItem(View v, int position, Object arg2) {
            // 由于需要它循环滚动，所以不能将其清除掉。
            ((ViewPager) v).removeView(mPageViews.get(position
                    % mPageViews.size()));

        }

        @Override
        public int getCount() {
            return mPageViews.size();
            // 设置成最大值以便循环滑动
            // int cont = ((mPageViews == null || mPageViews.size() == 0) ? 0 :
            // Integer.MAX_VALUE);
            // maxPage = cont;
            // return cont;
        }

        // 初始化position位置的界面
        @Override
        public Object instantiateItem(View v, int position) {
            if (mPageViews.size() > 0) {
                View view = mPageViews.get(position % mPageViews.size());
                ((ViewPager) v).addView(view);
                return view;
            } else {
                return null;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getIconResId(int index) {
            return R.drawable.perm_dots;
        }

    }

    class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            // 设置标题
            if (isShowTitle && mTitleStr != null && mTitleStr.size() > 0) {
                setTitleText(mTitleStr.get(position % mTitleStr.size()));
                setDescText(mDescStr.get(position % mTitleStr.size()));
            }

            if (isPointOut) {
                // 选中小圆点
                if (mDotImgs != null && mDotImgs.size() > 0) {
                    for (int i = 0; i < mDotImgs.size(); i++) {
                        // 不是当前选中的page，其小圆点设置为未选中的状态
                        mDotImgs.get(i)
                        .setBackgroundResource(
                                (position % mDotImgs.size() != i) ? R.drawable.circle_normal
                                        : R.drawable.circle_selected);
                    }
                }
            }

        }

    }

    @Override
    public void onClick(View v) {
        int position = mViewPager.getCurrentItem();
        Logger.getLogger().d(
                "Pager Poster View Click currentItem:" + currentItem);
        View currView = mPageViews.get(position);
        mOnReClick.onClick(currView);
    }

    public void setOnReClickLisitener(OnReClickLisitener onReClick) {
        mOnReClick = onReClick;
    }

    public interface OnReClickLisitener {

        public void onClick(View currView);
    }

}
