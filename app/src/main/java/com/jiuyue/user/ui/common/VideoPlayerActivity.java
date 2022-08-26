package com.jiuyue.user.ui.common;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jiuyue.user.R;
import com.jiuyue.user.base.BaseActivity;
import com.jiuyue.user.base.BasePresenter;
import com.jiuyue.user.databinding.ActivityVideoPlayerBinding;
import com.jiuyue.user.global.IntentKey;
import com.jiuyue.user.net.downFile.RetrofitUtils;
import com.tencent.qcloud.tuicore.component.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tuicore.util.DateTimeUtil;
import com.tencent.qcloud.tuicore.util.ScreenUtil;
import com.tencent.qcloud.tuikit.tuichat.component.video.UIKitVideoView;
import com.tencent.qcloud.tuikit.tuichat.component.video.proxy.IPlayer;
import com.tencent.qcloud.tuikit.tuichat.util.TUIChatLog;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.io.File;

public class VideoPlayerActivity extends BaseActivity<BasePresenter, ActivityVideoPlayerBinding> {
    private static final String TAG = VideoPlayerActivity.class.getSimpleName();
    private FrameLayout videoViewLayout;
    private UIKitVideoView videoView;
    private ImageView closeButton;
    private LinearLayout playControlLayout;
    private ImageView playButton;
    private SeekBar playSeekBar;
    private TextView timeBeginView, timeEndView;
    private ImageView pauseCenterView, snapImageView;
    private ProgressBar loadingView;
    private Handler durationHandler;
    private boolean mIsVideoPlay = false;
    private Runnable updateSeekBarTime;

    private String videoPath; //视频路径
    private String imagePath; //封面图

    @Override
    protected ActivityVideoPlayerBinding getViewBinding() {
        return ActivityVideoPlayerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void initStatusBar() {
        super.initStatusBar();
        UltimateBarX.statusBarOnly(this)
                .fitWindow(false)
                .colorRes(R.color.transparent)
                .light(false)
                .lvlColorRes(R.color.white)
                .apply();
    }

    @Override
    protected void init() {
        videoView = binding.videoPlayView;
        closeButton = binding.closeButton;
        playControlLayout = binding.playControlLayout;
        playButton = binding.playButton;
        playSeekBar = binding.playSeek;
        timeEndView = binding.timeEnd;
        timeBeginView = binding.timeBegin;
        pauseCenterView = binding.pauseButtonCenter;
        snapImageView = binding.contentImageIv;
        loadingView = binding.messageSendingPb;
        videoViewLayout = binding.videoViewLayout;

        // SeekBar 的触摸事件不被父 View 拦截
        playSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        videoPath = getIntent().getStringExtra(IntentKey.VIDEO_PATH);
        imagePath = getIntent().getStringExtra(IntentKey.VIDEO_COVER_PATH);
        if (videoPath != null && !videoPath.isEmpty()) {
            performVideoView();
        }
    }

    private void performVideoView() {
        videoView.setVisibility(View.VISIBLE);
        closeButton.setVisibility(View.VISIBLE);
        playControlLayout.setVisibility(View.GONE);
        pauseCenterView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        videoViewLayout.setVisibility(View.VISIBLE);

        mIsVideoPlay = false;
        playControlInit();
        loadVideoView();
    }

    private void loadVideoView() {
        snapImageView.setVisibility(View.VISIBLE);
        GlideEngine.loadImage(snapImageView, imagePath);
        File dir = new File(getFilesDir().getAbsolutePath() + "/" + "video" + "/" + "download");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final String localPath = dir + "/" + videoPath.substring(videoPath.lastIndexOf("/") + 1);
        final File videoFile = new File(localPath);
        if (videoFile.exists()) {//若存在本地文件则优先获取本地文件
            videoPath = localPath;
            playVideo();
        } else {
            getVideo(localPath);
        }
    }

    private void getVideo(String localPath) {
        RetrofitUtils.downLoadFile(videoPath, localPath, new RetrofitUtils.DownloadCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    videoPath = localPath;
                    playVideo();
                });
            }

            @Override
            public void onFailure(Throwable t) {
            }

            @Override
            public void onProgress(int progress) {
            }
        });
    }

    private void playVideo() {
        android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
        mmr.setDataSource(videoPath);
        String width = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);//宽
        String height = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);//高
        updateVideoView(Integer.parseInt(width),Integer.parseInt(height));

        Uri videoUri = Uri.parse(videoPath);

        playControlLayout.setVisibility(View.VISIBLE);
        pauseCenterView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);

        if (videoUri == null) {
            TUIChatLog.e(TAG, "playVideo videoUri == null");
            return;
        }

        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(new IPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IPlayer mediaPlayer) {
                TUIChatLog.e(TAG, "onPrepared()");
                videoView.start();
                videoView.pause();
                playButton.setImageResource(R.drawable.ic_play_icon);
                pauseCenterView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
                updateVideoViewSize();
                mIsVideoPlay = false;
                if (durationHandler != null) {
                    durationHandler = null;
                }
                if (updateSeekBarTime != null) {
                    updateSeekBarTime = null;
                }
                durationHandler = new Handler();
                updateSeekBarTime = new Runnable() {
                    public void run() {
                        //TUIChatLog.e(TAG, "mIsVideoPlay = " + mIsVideoPlay);
                        //get current position
                        int timeElapsed = videoView.getCurrentPosition();

                        if (playSeekBar.getProgress() >= playSeekBar.getMax()) {
                            TUIChatLog.e(TAG, "getProgress() >= getMax()");
                            resetVideo();
                            return;
                        }
                        //set seekbar progress
                        //TUIChatLog.e(TAG, "Runnable playSeekBar setProgress = " + (int) timeElapsed/1000);
                        playSeekBar.setProgress(Math.round(timeElapsed * 1.0f / 1000));

                        String durations = DateTimeUtil.formatSecondsTo00(Math.round(videoView.getCurrentPosition() * 1.0f / 1000));
                        timeBeginView.setText(durations);
                        //repeat yourself that again in 100 miliseconds
                        if (mIsVideoPlay) {
                            durationHandler.postDelayed(this, 100);
                        }
                    }
                };

                //获取音乐的总时长
                int duration = Math.round(mediaPlayer.getDuration() * 1.0f / 1000);
                int progress = Math.round(mediaPlayer.getCurrentPosition() * 1.0f / 1000);
                //将进度条设置最大值为：音乐的总时长
                playSeekBar.setMax(duration);
                playSeekBar.setProgress(progress);
                String durations = DateTimeUtil.formatSecondsTo00(duration);
                timeEndView.setText(durations);
                durationHandler.postDelayed(updateSeekBarTime, 100);
            }
        });
        videoView.setOnSeekCompleteListener(new IPlayer.OnSeekCompleteListener() {
            @Override
            public void OnSeekComplete(IPlayer mp) {

            }
        });
    }

    public void resetVideo() {
        videoView.stop();
        videoView.resetVideo();
        playButton.setImageResource(R.drawable.ic_play_icon);
        pauseCenterView.setVisibility(View.VISIBLE);
        snapImageView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        playSeekBar.setProgress(0);
        mIsVideoPlay = false;
        String durations = DateTimeUtil.formatSecondsTo00(0);
        timeBeginView.setText(durations);
    }

    private void playControlInit() {
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView != null) {
                    videoView.stop();
                } else {
                    TUIChatLog.e(TAG, "videoView is null");
                }
                finish();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickPlayVideo();
            }
        });

        //给进度条设置滑动的监听
        playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String durations = DateTimeUtil.formatSecondsTo00(progress);
                timeBeginView.setText(durations);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                TUIChatLog.i(TAG, "onStartTrackingTouch progress == " + progress);
                if (videoView != null) {
                    //进度条在当前位置播放
                    videoView.seekTo(progress * 1000);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                TUIChatLog.i(TAG, "onStopTrackingTouch progress == " + progress);
                if (videoView != null && videoView.isPlaying()) {
                    //进度条在当前位置播放
                    videoView.seekTo(progress * 1000);
                    videoView.start();
                } else if (videoView != null) {
                    //进度条在当前位置播放
                    videoView.seekTo(progress * 1000);
                }
            }
        });

        pauseCenterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlayVideo();
            }
        });
    }

    private void updateVideoViewSize() {
        int videoWidth = snapImageView.getWidth();
        int videoHeight = snapImageView.getHeight();
        updateVideoView(videoWidth, videoHeight);
    }

    private void clickPlayVideo() {
        if (!videoView.isPrepared()) {
            mIsVideoPlay = false;
            TUIChatLog.e(TAG, "!videoView.isPrepared()");
            return;
        }
        if (videoView.isPlaying()) {
            TUIChatLog.d(TAG, "videoView.isPlaying()");
            videoView.pause();
            playButton.setImageResource(com.tencent.qcloud.tuikit.tuichat.R.drawable.ic_play_icon);
            pauseCenterView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
            mIsVideoPlay = false;
        } else {
            //获取音乐的总时长
            float times = videoView.getDuration() * 1.0f / 1000;
            if (times <= 0) {
                TUIChatLog.e(TAG, "onClick, downloading video");
                //ToastUtil.toastShortMessage("downloading video");
                pauseCenterView.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                resetVideo();
                return;
            }

            //获取音乐的总时长
            int duration = Math.round(videoView.getDuration() * 1.0f / 1000);
            int progress = Math.round(videoView.getCurrentPosition() * 1.0f / 1000);
            TUIChatLog.d(TAG, "onClick playSeekBar duration == " + duration + " playSeekBar progress = " + progress);
            if (playSeekBar.getProgress() >= duration) {
                TUIChatLog.e(TAG, "getProgress() >= duration");
                resetVideo();
                return;
            }
            videoView.start();
            playButton.setImageResource(com.tencent.qcloud.tuikit.tuichat.R.drawable.ic_pause_icon);
            pauseCenterView.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
            snapImageView.setVisibility(View.GONE);
            mIsVideoPlay = true;


            //将进度条设置最大值为：音乐的总时长
            playSeekBar.setMax(duration);
            playSeekBar.setProgress(progress);
            String durations = DateTimeUtil.formatSecondsTo00(duration);
            timeEndView.setText(durations);
            if (durationHandler != null) {
                durationHandler.postDelayed(updateSeekBarTime, 100);
            }
        }
    }

    private void updateVideoView(int videoWidth, int videoHeight) {
        TUIChatLog.i(TAG, "updateVideoView videoWidth: " + videoWidth + " videoHeight: " + videoHeight);
        if (videoWidth <= 0 && videoHeight <= 0) {
            return;
        }
        boolean isLandscape = true;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            isLandscape = false;
        }

        int deviceWidth;
        int deviceHeight;
        if (isLandscape) {
            deviceWidth = Math.max(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
            deviceHeight = Math.min(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
        } else {
            deviceWidth = Math.min(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
            deviceHeight = Math.max(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
        }
        int[] scaledSize = ScreenUtil.scaledSize(deviceWidth, deviceHeight, videoWidth, videoHeight);
        TUIChatLog.i(TAG, "scaled width: " + scaledSize[0] + " height: " + scaledSize[1]);
        ViewGroup.LayoutParams params = videoView.getLayoutParams();
        params.width = scaledSize[0];
        params.height = scaledSize[1];
        videoView.setLayoutParams(params);
        if (snapImageView.getVisibility() == View.VISIBLE) {
            snapImageView.setLayoutParams(params);
        }
    }

    public void destroyView() {
        if (videoView != null) {
            videoView.stop();
        }
        if (playSeekBar != null) {
            playSeekBar.setProgress(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyView();
    }
}
