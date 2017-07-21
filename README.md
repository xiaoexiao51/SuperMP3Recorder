# SuperMP3Recorder
Android MP3录音实现

![](https://github.com/xiaoexiao51/SuperMP3Recorder/screenshot/img01.png)
![](https://github.com/xiaoexiao51/SuperMP3Recorder/screenshot/img02.png)

代码实现：

```java
setOnRecorderListener(new RecorderButton.OnRecorderListener() {
            @Override
            public void onUpdate(int db, long time){
            
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
