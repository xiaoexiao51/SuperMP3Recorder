# SuperMP3Recorder
Android MP3录音实现

![](https://github.com/xiaoexiao51/SuperMP3Recorder/blob/master/screenshot/img01.png)

![](https://github.com/xiaoexiao51/SuperMP3Recorder/blob/master/screenshot/img02.png)

代码实现：

```java
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
                layoutParams.width = (int) (mMinItemWidhth + (mMaxItemWidhth / 60f * time / 1000));
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
        
        
欢迎大家来提宝贵意见。

MP3录音实现参考
yhirano/Mp3VoiceRecorderSampleForAndroid
日本人写的，感觉他的判断不完善。
talzeus/AndroidMp3Recorder
比较严谨的代码，主要依据这个库进行的完善。
