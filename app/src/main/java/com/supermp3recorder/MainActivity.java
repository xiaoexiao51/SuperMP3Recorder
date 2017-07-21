package com.supermp3recorder;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.supermp3recorder.utils.MediaManager;
import com.supermp3recorder.widget.RecorderButton;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tv_bubble)
    TextView mTvBubble;
    @Bind(R.id.btn_recorder)
    RecorderButton mBtnRecorder;
    @Bind(R.id.iv_bubble)
    ImageView mIvBubble;
    @Bind(R.id.fl_bubble)
    FrameLayout mFlBubble;

    private boolean isPlaying;
    private int mMinItemWidhth;
    private int mMaxItemWidhth;
    private int mWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // 获取屏幕宽
        WindowManager windowManager = getWindowManager();
        mWidth = windowManager.getDefaultDisplay().getWidth();
        mMaxItemWidhth = (int) (mWidth * 0.6f);
        mMinItemWidhth = (int) (mWidth * 0.3f);

        initRecorder();
    }

    private void initRecorder() {
        mBtnRecorder.setOnRecorderListener(new RecorderButton.OnRecorderListener() {
            /**
             * @param db 声音分贝
             * @param time 当前时长
             */
            @Override
            public void onUpdate(int db, long time) {
                mTvBubble.setText(time / 1000 + "s");
            }

            /**
             * @param filePath 录音文件
             * @param time 录音时长
             */
            @Override
            public void onStop(final String filePath, long time) {
                mTvBubble.setText(time / 1000 + "s");
                // 语音泡泡条的长度
                ViewGroup.LayoutParams layoutParams = mFlBubble.getLayoutParams();
                layoutParams.width = (int) (mMinItemWidhth + mMaxItemWidhth / 60f * time / 1000);
                mFlBubble.setLayoutParams(layoutParams);

                mFlBubble.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isPlaying) {
                            if (!MediaManager.isPause) {
                                MediaManager.pause();
                                ((AnimationDrawable) mIvBubble.getDrawable()).stop();
                            } else {
                                MediaManager.resume();
                                ((AnimationDrawable) mIvBubble.getDrawable()).run();
                            }
                        } else {
                            isPlaying = true;
                            // 播放帧动画
                            mIvBubble.setImageResource(R.drawable.record_animlist);
                            ((AnimationDrawable) mIvBubble.getDrawable()).start();
                            MediaManager.playSound(filePath, new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    isPlaying = false;
                                    ((AnimationDrawable) mIvBubble.getDrawable()).stop();
                                    mIvBubble.setImageResource(R.drawable.ic_record03);
                                }
                            });
                        }
                    }
                });

                if (new File(filePath).exists()) {
                    File[] fileArray = new File[]{new File(filePath)};
                    // 图片上传操作
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }
}
