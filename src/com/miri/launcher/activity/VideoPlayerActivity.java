package com.miri.launcher.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.miri.launcher.Constants;
import com.miri.launcher.R;
import com.miri.launcher.adapter.AnimationAdapter;
import com.miri.launcher.db.manage.MediaManager;
import com.miri.launcher.model.Video;
import com.miri.launcher.player.CustomVideoView;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.OptionButton;
import com.miri.launcher.view.dialog.AlertDialog;
import com.miri.launcher.view.dialog.AlertUtil;

/**
 * 视频播放页面
 * 
 * @author zengjiantao
 * 
 */
public class VideoPlayerActivity extends BaseActivity {

	private static final Logger logger = Logger.getLogger();

	private static final int OPTION_SELECTOR_LEFT = 42;

	private static final int SET_PROGRESS = 100;

	private static final int RESET_PROGRESS = 101;

	private static final long SET_PROGRESS_DELAY = 1000L;

	private static final int HIDE_CONTROL = 102;

	private static final long HIDE_CONTROL_DELAY = 5000L;

	private static final int SEEKEND = 103;

	/**
	 * SEEKEND延迟<br>
	 * 此值需根据实际开发板的信号间隔进行设置
	 */
	protected static long seekEndDelay = 1000L;

	private CustomVideoView mPlayer;

	private LinearLayout mTitleLayout;

	private FrameLayout mOptionLayout;

	private TextView mVideoNameTxt;

	private TextView mVideoTimeTxt;

	private OptionButton mFullScreenBtn;

	private OptionButton mPrevBtn;

	private OptionButton mNextBtn;

	private OptionButton mPlayBtn;

	private OptionButton mSpeedInBtn;

	private OptionButton mSpeedBackBtn;

	private OptionButton mBackBtn;

	private ImageView mSelector;

	private SeekBar mSeekBar;

	private OptionButton mLastOptionBtn;

	private List<Video> mVideos;

	private int mCurrPosition;

	private TextView mSeekTimeTxt;

	private TextView mDurationTxt;
	
	private ImageView mPausePop;

	private Handler mHandler;

	private AudioManager mAudioManager;

	protected boolean mDragging;

	protected int timeBeforeSeek;
	
	private Animation mPushUpIn;

	private Animation mPushUpOut;

	private Animation mPushDownIn;

	private Animation mPushDownOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.video_player);

		initAnimation();
		initWidget();
		initHandler();
		initData();

	}

	private void initWidget() {
		mPlayer = (CustomVideoView) findViewById(R.id.player);
		mPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				Log.d("Debug", "duration:" + mp.getDuration());
				String duration = Toolkit.parseDuration(mp.getDuration());
				mVideoTimeTxt.setText(duration);
				mDurationTxt.setText(duration);
				setProgress();
				int pos = mPlayer.getCurrentPosition();
				mHandler.sendEmptyMessageDelayed(SET_PROGRESS,
						SET_PROGRESS_DELAY - (pos % SET_PROGRESS_DELAY));
			}
		});
		mPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				logger.d("what: " + what);
				logger.d("extra: " + extra);
				stopPlay();
				showToast("播放视频出错！" + "what: " + what + "extra: " + extra);
				return true;
			}
		});
		mPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				logger.d("onCompletion");
				stopPlay();
				mHandler.sendEmptyMessageDelayed(RESET_PROGRESS, SET_PROGRESS_DELAY);
			}
		});

		mTitleLayout = (LinearLayout) findViewById(R.id.player_title);
		mOptionLayout = (FrameLayout) findViewById(R.id.player_option);

		mVideoNameTxt = (TextView) findViewById(R.id.video_name);
		mVideoTimeTxt = (TextView) findViewById(R.id.video_time);

		mSelector = (ImageView) findViewById(R.id.option_selector);

		mFullScreenBtn = (OptionButton) findViewById(R.id.video_fullscreen);
		mPrevBtn = (OptionButton) findViewById(R.id.video_prev);
		mNextBtn = (OptionButton) findViewById(R.id.video_next);
		mPlayBtn = (OptionButton) findViewById(R.id.video_play);
		mSpeedInBtn = (OptionButton) findViewById(R.id.video_speed_in);
		mSpeedBackBtn = (OptionButton) findViewById(R.id.video_speed_back);
		mBackBtn = (OptionButton) findViewById(R.id.video_back);
		mFullScreenBtn.requestFocus();

		mSeekBar = (SeekBar) findViewById(R.id.video_seekbar);
		mSeekBar.setMax(1000);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStartTrackingTouch(SeekBar bar) {
				// Log.d(TAG, "startChangeSeekBar");
				// show(0);
				mDragging = true;

				timeBeforeSeek = mPlayer.getCurrentPosition();

				if (mPlayer.isPlaying()) {
					mPlayer.pause();
					updatePausePlay();
				}

			}

			/**
			 * 注意：用遥控器操作时，只触发这个函数，其他两个函数不触发
			 */
			@Override
			public void onProgressChanged(SeekBar bar, int progress,
					boolean fromuser) {

				if (!fromuser) {
					return;
				}
				// show(0);

				mDragging = true;
				// 拖动前先暂停
				if (mPlayer.isPlaying()) {
					mPlayer.pause();
					updatePausePlay();
				}
				doSeekAction();
				updateCurrentTime();
			}

			@Override
			public void onStopTrackingTouch(SeekBar bar) {
				doSeekAction();
			}
		});
		mSeekTimeTxt = (TextView) findViewById(R.id.seek_time);
		mDurationTxt = (TextView) findViewById(R.id.total_time);
		mPausePop = (ImageView) findViewById(R.id.pause_pop);

		clickHandler(mFullScreenBtn, mPrevBtn, mNextBtn, mPlayBtn, mSpeedInBtn,
				mSpeedBackBtn, mBackBtn);
		focusHandler(mFullScreenBtn, mPrevBtn, mNextBtn, mPlayBtn, mSpeedInBtn,
				mSpeedBackBtn, mBackBtn, mSeekBar);

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
	}

	private void initHandler() {
		mHandler = new Handler(new Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case SET_PROGRESS:
					if (isControlShown() && !mDragging) {
						setProgress();
						int pos = mPlayer.getCurrentPosition();
						mHandler.sendEmptyMessageDelayed(SET_PROGRESS,
								SET_PROGRESS_DELAY - (pos % SET_PROGRESS_DELAY));
					}
					break;
				case RESET_PROGRESS:
					mHandler.removeMessages(SET_PROGRESS);
					mSeekBar.setProgress(0);
					break;
				case HIDE_CONTROL:
					hideControl();
					break;
				case SEEKEND:// 执行Seek操作
					int progress = mSeekBar.getProgress();
					long duration = mPlayer.getDuration();
					// 用long防止溢出
					long newposition = (duration * progress) / 1000L;
					Log.d("Debug", "=======seekto newposition:" + newposition);
					// 设置 seekComplete后立即调用start
					// if (Product.isCH_MT5396()) {
					// mPlayer.playAfterSeek(false);
					// } else {
					// mPlayer.playAfterSeek(true);
					// }
					mPlayer.start();
					mPlayer.seekTo((int) newposition);

					// Log.d(TAG,
					// "--------seekTo() 被调用 in CutomMediaController---------");
					mDragging = false;
					// Log.d(TAG, "mSeekHandler:mDragging--->" + mDragging);
					updatePausePlay();
					int pos = mPlayer.getCurrentPosition();
					mHandler.sendEmptyMessageDelayed(SET_PROGRESS,
							SET_PROGRESS_DELAY - (pos % SET_PROGRESS_DELAY));
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	private void initData() {
		Intent intent = getIntent();
		mCurrPosition = intent.getIntExtra(Constants.KEY_VIDEO_POSITION, 0);
		mVideos = MediaManager.queryVideos(this);
		Video video = mVideos.get(mCurrPosition);
		play(video);
		mHandler.sendEmptyMessageDelayed(HIDE_CONTROL, HIDE_CONTROL_DELAY);
	}

	private void play(Video video) {
		mVideoNameTxt.setText(video.getName());

		stopPlay();
		Uri uri = Uri.parse(video.getPath());
		mPlayer.setVideoURI(uri);
		mPlayer.start();
		// Log.d("Debug", "duration:" + mPlayer.getDuration());
		// mVideoTimeTxt.setText(Toolkit.parseDuration(mPlayer.getDuration()));
	}

	/**
	 * 停止播放
	 */
	private void stopPlay() {
		if (mPlayer.isPlaying()) {
			mPlayer.stopPlayback();
			updatePausePlay();
		}
	}

	private void setProgress() {

		if ((mPlayer == null)) {
			return;
		}

		int position = mPlayer.getCurrentPosition();
		int duration = mPlayer.getDuration();

		// 确定Seek位置
		if (timeBeforeSeek != 0 && Math.abs(position - timeBeforeSeek) < 2000) {
			return;
		}

		mSeekTimeTxt.setText(Toolkit.parseDuration(position));

		if (mSeekBar != null) {
			if (duration > 0) {
				// use long to avoid overflow
				long pos = (1000L * position) / duration;
				mSeekBar.setProgress((int) pos);
			}
		}

	}

	private void clickHandler(View... views) {
		for (View view : views) {
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
			case R.id.video_prev:
				prev();
				break;
			case R.id.video_next:
				next();
				break;
			case R.id.video_play:
				doPauseResume();
				break;
			case R.id.video_speed_in:
				doSeekForwardAction();
				break;
			case R.id.video_speed_back:
				doSeekBackAction();
				break;
			case R.id.video_fullscreen:
				mHandler.removeMessages(HIDE_CONTROL);
				mHandler.sendEmptyMessage(HIDE_CONTROL);
				break;
			case R.id.video_back:
				exit();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 上一个
	 */
	private void prev() {
		if (--mCurrPosition < 0) {
			mCurrPosition = 0;
			showToast("已经是第一个视频了！");
		} else {
			stopPlay();
			play(mVideos.get(mCurrPosition));
		}
	}

	/**
	 * 下一个
	 */
	private void next() {
		if (++mCurrPosition >= mVideos.size()) {
			mCurrPosition = mVideos.size() - 1;
			showToast("已经是最后一个视频了！");
		} else {
			stopPlay();
			play(mVideos.get(mCurrPosition));
		}
	}

	/**
	 * 播放/暂停切换
	 */
	private void doPauseResume() {
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
			mPausePop.setVisibility(View.VISIBLE);
		} else {
			mPlayer.start();
			mPausePop.setVisibility(View.GONE);
		}
		updatePausePlay();
	}

	private void updatePausePlay() {
		if (mPlayer.isPlaying()) {
			mPlayBtn.setImageResource(R.drawable.option_pause_bg);
			mPlayBtn.setText("暂停");
		} else {
			mPlayBtn.setImageResource(R.drawable.option_play_bg);
			mPlayBtn.setText("播放");
		}
	}

	/**
	 * 快退
	 */
	private void doSeekBackAction() {
		mDragging = true;
		mPlayer.pause();
		updatePausePlay();
		timeBeforeSeek = mPlayer.getCurrentPosition();
		setSeekBackProgress(50);
		doSeekAction();
	}

	/**
	 * 快进
	 */
	private void doSeekForwardAction() {
		mDragging = true;
		mPlayer.pause();
		updatePausePlay();
		timeBeforeSeek = mPlayer.getCurrentPosition();
		setSeekForwardProgress(50);
		doSeekAction();
	}

	// 减小进度
	protected void setSeekBackProgress(int interval) {
		if (mPlayer != null) {
			// 当前进度条减少1%(max:1000)
			mSeekBar.setProgress(mSeekBar.getProgress() - interval);
			// Log.i(TAG, "减小进度-->" + mProgress.getProgress());
			updateCurrentTime();
		}
	}

	/**
	 * 视频跳到seek位置
	 */
	protected void doSeekAction() {

		// 移除消息
		mHandler.removeMessages(SEEKEND);
		// 重新发送消息
		mHandler.sendEmptyMessageDelayed(SEEKEND, seekEndDelay);

		updateCurrentTime();
	}

	// 增加进度
	protected void setSeekForwardProgress(int interval) {
		if (mPlayer != null) {
			// 当前进度条加上1%(max:1000)
			mSeekBar.setProgress(mSeekBar.getProgress() + interval);
			// Log.i(TAG, "增加进度-->" + mProgress.getProgress());
			updateCurrentTime();
		}
	}

	/**
	 * 更新当前播放时间
	 */
	private void updateCurrentTime() {
		long duration = mPlayer.getDuration();
		long newposition = (duration * mSeekBar.getProgress()) / 1000L;
		if (mSeekTimeTxt != null) {
			mSeekTimeTxt.setText(Toolkit.parseDuration((int) newposition));
			// Log.d(TAG, stringForTime((int) newposition));
		}
	}

	private void exit() {
		AlertUtil.showAlert(this, R.string.exit_title, R.string.exit_msg,
				R.string.ok, new AlertDialog.OnOkBtnClickListener() {

					@Override
					public void onClick() {
						stopPlay();
						finish();
					}

				}, R.string.cancel, new AlertDialog.OnCancelBtnClickListener() {

					@Override
					public void onClick() {
					}
				});
	}

	@Override
	public void onBackPressed() {
		exit();
	}

	/**
	 * 修正焦点，一次可传入多个View
	 * 
	 * @param views
	 */
	private void focusHandler(View... views) {
		for (View v : views) {
			this.focusHandler(v);
		}
	}

	/**
	 * 修正焦点
	 * 
	 * @param view
	 */
	private void focusHandler(View view) {
		view.setOnFocusChangeListener(mOnFocus);
		view.setOnKeyListener(mOnkey);
	}

	private final OnFocusChangeListener mOnFocus = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			if (hasFocus) {
				if (v.getId() == R.id.video_seekbar) {
					mSelector.setVisibility(View.GONE);
				} else {
					int left = v.getLeft();
					FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mSelector
							.getLayoutParams();
					params.leftMargin = left
							- Toolkit.px2dip(VideoPlayerActivity.this,
									OPTION_SELECTOR_LEFT);
					if (params.leftMargin < 0) {
						params.leftMargin = 0;
					}
					mSelector.setLayoutParams(params);
					mSelector.setVisibility(View.VISIBLE);
					mLastOptionBtn = (OptionButton) v;
				}
			}
		}

	};

	private final OnKeyListener mOnkey = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (v.getId() == R.id.video_seekbar) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
					if (mLastOptionBtn != null) {
						mLastOptionBtn.requestFocus();
					} else {
						mFullScreenBtn.requestFocus();
					}
				}
			} else {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_DPAD_UP) {
					mLastOptionBtn = (OptionButton) v;
				}
			}
			return false;
		}

	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (isControlShown()) {
			mHandler.removeMessages(HIDE_CONTROL);
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				mHandler.sendEmptyMessage(HIDE_CONTROL);
				return true;
			} else {
				mHandler.sendEmptyMessageDelayed(HIDE_CONTROL,
						HIDE_CONTROL_DELAY);
				return false;
			}

		} else {
			if (keyCode == KeyEvent.KEYCODE_MENU) {
				showControl();
				if (mLastOptionBtn != null) {
					mLastOptionBtn.requestFocus();
				} else {
					mFullScreenBtn.requestFocus();
				}
				mHandler.sendEmptyMessageDelayed(HIDE_CONTROL,
						HIDE_CONTROL_DELAY);
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT
					|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				showControl();
				mSeekBar.requestFocus();
				mHandler.sendEmptyMessageDelayed(HIDE_CONTROL,
						HIDE_CONTROL_DELAY);
				return false;
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_RAISE,
						AudioManager.FX_FOCUS_NAVIGATION_UP);
				return false;
			} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_LOWER,
						AudioManager.FX_FOCUS_NAVIGATION_UP);
				return false;
			} else if (keyCode == KeyEvent.KEYCODE_ENTER
					|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
				doPauseResume();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	};
	
	private void initAnimation() {
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
				mHandler.sendEmptyMessage(SET_PROGRESS);
				super.onAnimationEnd(animation);
			}
		});
		mPushDownOut = AnimationUtils.loadAnimation(this, R.anim.push_down_out);
		mPushDownOut.setAnimationListener(new AnimationAdapter() {
			@Override
			public void onAnimationEnd(Animation animation) {
				mOptionLayout.setVisibility(View.GONE);
				mHandler.removeMessages(SET_PROGRESS);
				super.onAnimationEnd(animation);
			}
		});
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

	/**
	 * 判断控制层是否显示
	 * 
	 * @return
	 */
	private boolean isControlShown() {
		return mTitleLayout.isShown() && mOptionLayout.isShown();
	}
}
