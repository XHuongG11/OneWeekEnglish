package com.example.oneweekenglish.util;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class Sound {
    private TextToSpeech textToSpeech;
    public Sound(Context context){
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("TTS","Ngôn ngữ không được hỗ trợ");
                    }else{
                        Log.e("TTS","Khởi tạo thất bại");
                    }
                }
            }
        });
    }
    public void readText(String text){
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
    }
}