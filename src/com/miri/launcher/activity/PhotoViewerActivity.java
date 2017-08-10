package com.miri.launcher.activity;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miri.launcher.Constants;
import com.miri.launcher.R;
import com.miri.launcher.adapter.AnimationAdapter;
import com.miri.launcher.model.Folder;
import com.miri.launcher.model.Photo;
import com.miri.launcher.utils.ImageUtil;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.OptionButton;

/**
 * 照片浏览
 * @author zengjiantao
 */
public class PhotoViewerActivity extends BaseActivity {

    private static final float SCALE = 1.2f;

    private static final int OPTION_SELECTOR_LEFT = 42;

    private static final int ORIENTATION_WIDTH = 1280;

    private static final int ORIENTATION_HEIGHT = 720;

    private static final int NEXT_PHOTO = 100;

    private static final long NEXT_PHOTO_DELAY = 3000L;

    private static final int HIDE_CONTROL = 101;

    private static final long HIDE_CONTROL_DELAY = 5000L;

    private LinearLayout mTitleLayout;

    private FrameLayout mOptionLayout;

    private ImageView mPhotoCanvas;

    private TextView mPhotoNameTxt;

    private TextView mPhotoSizeTxt;

    private TextView mPhotoDateTxt;

    private OptionButton mPrevBtn;

    private OptionButton mNextBtn;

    private OptionButton mPlayBtn;

    private OptionButton mAmpfilyBtn;

    private OptionButton mReduceBtn;

    private OptionButton mFullscreenBtn;

    private OptionButton mBackBtn;

    private OptionButton mLastSelectedBtn;

    private ImageView mSelector;

    private List<Photo> mPhotos;

    private int mCurrPosition;

    private Handler mHandler;

    private boolean mIsPlayed = false;

    private Animation mAlphaIn;

    private Animation mAlphaOut;

    private Animation mPushUpIn;

    private Animation mPushUpOut;

    private Animation mPushDownIn;

    private Animation mPushDownOut;

    private boolean mIsAnimation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_viewer);

        Intent intent = getIntent();
        Folder folder = (Folder) intent.getSerializableExtra(Constants.KEY_FOLDER);
        mPhotos = folder.getPhotos();
        mCurrPosition = intent.getIntExtra(Constants.KEY_PHOTO_POSITION, 0);
        initAnimation();
        initWidget();
        initHandler();
        mHandler.sendEmptyMessageDelayed(HIDE_CONTROL, HIDE_CONTROL_DELAY);
    }

    private void initWidget() {
        mPhotoCanvas = (ImageView) findViewById(R.id.photo_canvas);

        mTitleLayout = (LinearLayout) findViewById(R.id.viewer_title);
        mOptionLayout = (FrameLayout) findViewById(R.id.viewer_option);

        mPhotoNameTxt = (TextView) findViewById(R.id.photo_name);
        mPhotoSizeTxt = (TextView) findViewById(R.id.photo_size);
        mPhotoDateTxt = (TextView) findViewById(R.id.photo_date);
        showPhoto(mPhotos.get(mCurrPosition));

        mSelector = (ImageView) findViewById(R.id.option_selector);

        mPrevBtn = (OptionButton) findViewById(R.id.option_prev);
        mNextBtn = (OptionButton) findViewById(R.id.option_next);
        mPlayBtn = (OptionButton) findViewById(R.id.option_play);
        mAmpfilyBtn = (OptionButton) findViewById(R.id.option_ampfily);
        mReduceBtn = (OptionButton) findViewById(R.id.option_reduce);
        mFullscreenBtn = (OptionButton) findViewById(R.id.option_fullscreen);
        mBackBtn = (OptionButton) findViewById(R.id.option_back);
        mPrevBtn.requestFocus();

        clickHandler(mPrevBtn, mNextBtn, mPlayBtn, mAmpfilyBtn, mReduceBtn, mFullscreenBtn,
                mBackBtn);
        focusHandler(mPrevBtn, mNextBtn, mPlayBtn, mAmpfilyBtn, mReduceBtn, mFullscreenBtn,
                mBackBtn);
    }

    private void initHandler() {
        mHandler = new Handler(new Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case NEXT_PHOTO:
                        next();
                        if (mCurrPosition < mPhotos.size()) {
                            mHandler.sendEmptyMessageDelayed(NEXT_PHOTO, NEXT_PHOTO_DELAY);
                        }
                        break;

                    case HIDE_CONTROL:
                        hideControl();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void showPhoto(Photo photo) {
        Display display = getWindowManager().getDefaultDisplay();
        Bitmap bitmap = ImageUtil.decodeSampledBitmapFromFile(new File(photo.getPath()),
                display.getWidth(), display.getHeight());

        resetCanvas();
        mPhotoCanvas.setImageBitmap(bitmap);
        mPhotoNameTxt.setText(photo.getName());
        if (bitmap != null) {
            mPhotoSizeTxt.setText(bitmap.getWidth() + "*" + bitmap.getHeight());
        }
        mPhotoDateTxt.setText(photo.getDateModify());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mOptionLayout.isShown() && mTitleLayout.isShown()) {
            mHandler.removeMessages(HIDE_CONTROL);
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                mHandler.sendEmptyMessage(HIDE_CONTROL);
            } else {
                mHandler.sendEmptyMessageDelayed(HIDE_CONTROL, HIDE_CONTROL_DELAY);
            }
            return false;
        } else {
            if (keyCode == KeyEvent.KEYCODE_MENU) {
                showControl();
                if (mLastSelectedBtn != null) {
                    mLastSelectedBtn.requestFocus();
                }
                mHandler.sendEmptyMessageDelayed(HIDE_CONTROL, HIDE_CONTROL_DELAY);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                prev();
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                next();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示控制层
     */
    private void showControl() {
        mTitleLayout.startAnimation(mPushUpIn);
        mOptionLayout.startAnimation(mPushDownIn);
    }

    /**
     * 隐藏控制层
     */
    private void hideControl() {
        mTitleLayout.startAnimation(mPushUpOut);
        mOptionLayout.startAnimation(mPushDownOut);
    }

    private void clickHandler(View... views) {
        for (View view: views) {
            clickHandler(view);
        }
    }

    private void clickHandler(View view) {
        view.setOnClickListener(mOnClickListener);
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            v.requestFocus();
            v.requestFocusFromTouch();
            mHandler.removeMessages(HIDE_CONTROL);
            mHandler.sendEmptyMessageDelayed(HIDE_CONTROL, HIDE_CONTROL_DELAY);
            switch (v.getId()) {
                case R.id.option_prev:
                    prev();
                    break;
                case R.id.option_next:
                    next();
                    break;
                case R.id.option_play:
                    playOrPause();
                    break;
                case R.id.option_ampfily:
                    ampfily();
                    break;
                case R.id.option_reduce:
                    reduce();
                    break;
                case R.id.option_fullscreen:
                    mHandler.removeMessages(HIDE_CONTROL);
                    mHandler.sendEmptyMessage(HIDE_CONTROL);
                    break;
                case R.id.option_back:
                    finish();
                    break;

                default:
                    break;
            }
        }
    };

    private void initAnimation() {
        mAlphaIn = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
        mAlphaIn.setAnimationListener(new AnimationAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                mPhotoCanvas.setVisibility(View.VISIBLE);
                mIsAnimation = false;
                super.onAnimationEnd(animation);
            }

        });
        mAlphaOut = AnimationUtils.loadAnimation(this, R.anim.alphat_out);
        mAlphaOut.setAnimationListener(new AnimationAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                mPhotoCanvas.setVisibility(View.GONE);
                showPhoto(mPhotos.get(mCurrPosition));
                mPhotoCanvas.startAnimation(mAlphaIn);
                super.onAnimationEnd(animation);
            }

        });

        mPushUpIn = AnimationUtils.loadAnimation(this, R.anim.push_up_in);
        mPushUpIn.setAnimationListener(new AnimationAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                mTitleLayout.setVisibility(View.VISIBLE);
                super.onAnimationEnd(animation);
            }
        });
        mPushUpOut = AnimationUtils.loadAnimation(this, R.anim.push_up_out);
        mPushUpOut.setAnimationListener(new AnimationAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                mTitleLayout.setVisibility(View.GONE);
                super.onAnimationEnd(animation);
            }
        });
        mPushDownIn = AnimationUtils.loadAnimation(this, R.anim.push_down_in);
        mPushDownIn.setAnimationListener(new AnimationAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                mOptionLayout.setVisibility(View.VISIBLE);
                super.onAnimationEnd(animation);
            }
        });
        mPushDownOut = AnimationUtils.loadAnimation(this, R.anim.push_down_out);
        mPushDownOut.setAnimationListener(new AnimationAdapter() {

            @Override
            public void onAnimationEnd(Animation animation) {
                mOptionLayout.setVisibility(View.GONE);
                super.onAnimationEnd(animation);
            }
        });
    }

    /**
     * 上一个
     */
    private void prev() {
        if (!mIsAnimation) {
            if (--mCurrPosition < 0) {
                mCurrPosition = 0;
                showToast("已经是第一张图片了！");
            } else {
                // showPhoto(mPhotos.get(mCurrPosition));
                mIsAnimation = true;
                mPhotoCanvas.startAnimation(mAlphaOut);
            }
        }
    }

    /**
     * 下一个
     */
    private void next() {
        if (!mIsAnimation) {
            if (++mCurrPosition >= mPhotos.size()) {
                mCurrPosition = mPhotos.size() - 1;
                showToast("已经是最后一张图片了！");
            } else {
                // showPhoto(mPhotos.get(mCurrPosition));
                mIsAnimation = true;
                mPhotoCanvas.startAnimation(mAlphaOut);
            }
        }
    }

    /**
     * 暂停或者播放
     */
    private void playOrPause() {
        mHandler.removeMessages(NEXT_PHOTO);
        if (!mIsPlayed) {
            mHandler.sendEmptyMessageDelayed(NEXT_PHOTO, NEXT_PHOTO_DELAY);
        }
        mIsPlayed = !mIsPlayed;
        updatePauseLayout();
    }

    private void updatePauseLayout() {
        if (mIsPlayed) {
            mPlayBtn.setImageResource(R.drawable.option_pause_bg);
            mPlayBtn.setText("幻灯片暂停");
        } else {
            mPlayBtn.setImageResource(R.drawable.option_play_bg);
            mPlayBtn.setText("幻灯片播放");
        }
    }

    /**
     * 放大
     */
    private void ampfily() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPhotoCanvas.getLayoutParams();
        int width = params.width;
        int height = params.height;
        params.width = Math.round(width * SCALE);
        params.height = Math.round(height * SCALE);
        mPhotoCanvas.setLayoutParams(params);
    }

    /**
     * 缩小
     */
    private void reduce() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPhotoCanvas.getLayoutParams();
        int width = params.width;
        int height = params.height;
        params.width = Math.round(width / SCALE);
        params.height = Math.round(height / SCALE);
        mPhotoCanvas.setLayoutParams(params);
    }

    /**
     * 重置画布
     */
    private void resetCanvas() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPhotoCanvas.getLayoutParams();
        params.width = ORIENTATION_WIDTH;
        params.height = ORIENTATION_HEIGHT;
        mPhotoCanvas.setLayoutParams(params);
    }

    /**
     * 修正焦点，一次可传入多个View
     * @param views
     */
    private void focusHandler(View... views) {
        for (View v: views) {
            this.focusHandler(v);
        }
    }

    /**
     * 修正焦点
     * @param view
     */
    private void focusHandler(View view) {
        view.setOnFocusChangeListener(mOnFocus);
    }

    private final OnFocusChangeListener mOnFocus = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                int left = v.getLeft();
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mSelector
                        .getLayoutParams();
                params.leftMargin = left
                        - Toolkit.px2dip(PhotoViewerActivity.this, OPTION_SELECTOR_LEFT);
                if (params.leftMargin < 0) {
                    params.leftMargin = 0;
                }
                mSelector.setLayoutParams(params);
                mLastSelectedBtn = (OptionButton) v;
            }
        }

    };
}
