package com.supermp3recorder.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.supermp3recorder.R;
import com.supermp3recorder.utils.RecoderUtils;

/**
 * Created by MMM on 2017/6/7.
 */

public class RecorderButton extends Button {

    private Context mContext;
    private int startY = 0;
    private int CANCLE_LENGTH = -100;// 下滑取消距离
    private long startTime;
    private boolean canRecord;
    private boolean isDownTime;
    private int mDownTime = 10;

    RecorderDialog mRecorderDialog;
    private ImageView mImageView;
    private TextView mTextView;
    private RecoderUtils mRecoderUtils;

    public RecorderButton(Context context) {
        this(context, null);
    }

    public RecorderButton(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, R.attr.buttonStyle);
        this.mContext = context;
//        ButterKnife.bind(this, View.inflate(context, R.layout.recorder_button, this));
    }

    public RecorderButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRecoderUtils = new RecoderUtils(mContext);
        mRecorderDialog = new RecorderDialog(getContext());
        mImageView = mRecorderDialog.getRecordIcon();
        mTextView = mRecorderDialog.getRecordTime();

        initTouchListener();
        initRecordListener();
    }

    private void initRecordListener() {
        mRecoderUtils.setOnRecorderListener(new RecoderUtils.OnRecorderListener() {
            @Override
            public void onUpdate(int db, long time) {
                // 设置音量
                mImageView.getDrawable().setLevel((int) (10000 * db / 2000));
                // 监听倒计时间
                mDownTime = (int) (60 - time / 1000);
                if (mDownTime <= 10) {
                    isDownTime = true;
                    mTextView.setText("还可以说 " + mDownTime + " 秒");
                }
                if (mDownTime == 0) {
                    mRecoderUtils.stopRecord();
                    mRecorderDialog.dismiss();
                }

                // 抛出监听
                if (mOnRecorderListener != null) {
                    mOnRecorderListener.onUpdate(db, time);
                }
            }

            @Override
            public void onStop(String filePath, long time) {
                // 抛出监听
                if (mOnRecorderListener != null) {
                    mOnRecorderListener.onStop(filePath, time);
                }
            }
        });
    }

    public void initTouchListener() {

        RecorderButton.this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (canRecord) {
                            startY = (int) event.getY();
                            startTime = System.currentTimeMillis();
                            mRecorderDialog.show();
                            mTextView.setText("手指上滑，取消发送");
                            RecorderButton.this.setText("松开结束");
                            mRecoderUtils.startRecord();
                        } else {
                            //请求获取语音权限
                            ActivityCompat.requestPermissions((Activity) mContext,
                                    new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                            canRecord = true;
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        if (canRecord) {
                            int endY = (int) event.getY();
                            if (startY < 0)
                                return true;
                            if (endY - startY < CANCLE_LENGTH) {
                                if (mDownTime > 0) {
                                    mRecoderUtils.cancelRecord();
                                }
                            } else {
                                long intervalTime = System.currentTimeMillis() - startTime;
                                if (intervalTime < 1000) {
                                    mRecoderUtils.cancelRecord();
                                    Toast.makeText(mContext, "录音时间过短", Toast.LENGTH_SHORT).show();
                                } else {
                                    mRecoderUtils.stopRecord();
//                                    mFlBubble.setVisibility(View.INVISIBLE);
                                }
                            }
                            mRecorderDialog.dismiss();
                            RecorderButton.this.setText("按住说话");
                            mTextView.setText("手指上滑，取消发送");
                            mTextView.setBackgroundResource(R.color.transparent);
                            isDownTime = false;
                            mDownTime = 10;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (canRecord) {
                            int tempY = (int) event.getY();
                            if (startY < 0)
                                return true;
                            if (tempY - startY < CANCLE_LENGTH) {
                                RecorderButton.this.setText("松开手指，取消发送");
                                mTextView.setText("松开手指，取消发送");
                                mTextView.setBackgroundResource(R.color.transparent_red);
                            } else {
                                if (!isDownTime) {
                                    RecorderButton.this.setText("松开结束");
                                    mTextView.setText("手指上滑，取消发送");
                                    mTextView.setBackgroundResource(R.color.transparent);
                                }
                            }
                        }
                        break;
                }

                return true;
            }
        });
    }


    private OnRecorderListener mOnRecorderListener;

    public void setOnRecorderListener(OnRecorderListener recorderListener) {
        mOnRecorderListener = recorderListener;
    }

    public interface OnRecorderListener {
        public void onUpdate(int db, long time);

        public void onStop(String filePath, long time);
    }

}
