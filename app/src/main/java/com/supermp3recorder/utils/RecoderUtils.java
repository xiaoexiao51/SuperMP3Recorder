package com.supermp3recorder.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.czt.mp3recorder.MP3Recorder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by MMM on 2017/6/7.
 */
public class RecoderUtils {

    //文件路径
    private String filePath;
    //文件夹路径
    private String FolderPath;

    private MP3Recorder mMediaRecorder;
    private final String TAG = "NetUtil";
    public static final int MAX_LENGTH = 1000 * 60;// 最大录音时长60s;

    /**
     * 文件存储默认sdcard/record
     */
    public RecoderUtils(Context context) {

        //默认保存路径为/sdcard/record/下
        this(Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/");
    }

    public RecoderUtils(String filePath) {

        File path = new File(filePath);
        if (!path.exists())
            path.mkdirs();

        this.FolderPath = filePath;
    }

    private long startTime;
    private long endTime;


    /**
     * 开始录音 使用amr格式
     * 录音文件
     *
     * @return
     */
    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        filePath = FolderPath + "voice_" + sdf.format(System.currentTimeMillis()) + ".mp3";
        if (mMediaRecorder == null)
            mMediaRecorder = new MP3Recorder(new File(filePath));
        try {
            /* ④开始 */
            mMediaRecorder.start();
            // AudioRecord audioRecord.
            /* 获取开始时间* */
            startTime = System.currentTimeMillis();
            updateMicStatus();
            Log.e(TAG, "startTime" + startTime);
        } catch (IllegalStateException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    /**
     * 停止录音
     */
    public long stopRecord() {
        if (mMediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();

        try {

            mMediaRecorder.stop();
            mMediaRecorder = null;

            mOnRecorderListener.onStop(filePath, endTime - startTime);
            filePath = "";

        } catch (RuntimeException e) {
            mMediaRecorder = null;

            File file = new File(filePath);
            if (file.exists())
                file.delete();

            filePath = "";

        }

        return endTime - startTime;
    }

    /**
     * 取消录音
     */
    public void cancelRecord() {

        try {

            mMediaRecorder.stop();
            mMediaRecorder = null;

        } catch (RuntimeException e) {
            mMediaRecorder = null;
        }

        File file = new File(filePath);
        if (file.exists())
            file.delete();

        filePath = "";

    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    private int BASE = 1;
    private int SPACE = 100;// 间隔取样时间

    /**
     * 更新麦克状态
     */
    private void updateMicStatus() {

        if (mMediaRecorder != null) {
            int volume = mMediaRecorder.getVolume();
            if (mOnRecorderListener != null) {
                mOnRecorderListener.onUpdate(volume, System.currentTimeMillis() - startTime);
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
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
