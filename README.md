# SuperMP3Recorder
Android MP3录音实现

![](https://github.com/xiaoexiao51/SuperMP3Recorder/raw/screenshot/img01)
![](https://github.com/xiaoexiao51/SuperMP3Recorder/raw/screenshot/img02)

代码实现：
mBtnRecorder.setOnRecorderListener(new RecorderButton.OnRecorderListener() {
            @Override
            public void onUpdate(int db, long time) {
               
            }

            @Override
            public void onStop(final String filePath, long time) {
            
                if (new File(filePath).exists()) {
                    File[] fileArray = new File[]{new File(filePath)};
                    // 图片上传操作
                }
            }
        });
        
这块儿欢迎大家来提宝贵意见。

MP3录音实现参考
yhirano/Mp3VoiceRecorderSampleForAndroid
日本人写的，感觉他的判断不完善，有点巧合编程的意思，也或许是我没看懂。
talzeus/AndroidMp3Recorder
比较严谨的代码。主要依据这个库进行的修改。
存在的问题：

AudioRecord传入参数很多没有按Android规定传入。如采样频率使用了22050Hz。
使用了自己构造的RingBuffer，看这有点头晕。 我在库里使用List来存储未编码的音频数据，更容易理解。
没有提供音量大小。
