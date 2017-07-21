package com.supermp3recorder.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.supermp3recorder.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by MMM on 2017/6/7.
 */

public class RecorderDialog extends Dialog {

    @Bind(R.id.iv_record_icon)
    ImageView mIvRecordingIcon;
    @Bind(R.id.tv_record_time)
    TextView mTvRecordingTime;
    private Context mContext;
    private View mView;

    public RecorderDialog(Context context) {
        this(context, 0);
        this.mContext = context;
    }

    public RecorderDialog(Context context, int themeResId) {
        super(context, R.style.MediaDialogStyle);
        this.mContext = context;
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_recorder, null);
        ButterKnife.bind(this, mView);
        setContentView(mView);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.x = 0;
        params.y = 0;
        WindowManager manager = ((Activity) mContext).getWindowManager();
        Display d = manager.getDefaultDisplay();
//        params.width = (int) (d.getWidth() * 1);
        dialogWindow.setAttributes(params);

        RecorderDialog.this.setCanceledOnTouchOutside(false);

    }

    public ImageView getRecordIcon() {
        return mIvRecordingIcon;
    }

    public TextView getRecordTime() {
        return mTvRecordingTime;
    }

}
