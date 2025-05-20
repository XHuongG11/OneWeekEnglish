package com.example.oneweekenglish.util;

import android.content.Context;
import android.media.SoundPool;

import com.example.oneweekenglish.R;

public class MusicService {
    public static void clickButtonSound(Context context){
        SoundPool soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();

        int soundId = soundPool.load(context, R.raw.click_button_letter, 1);
        soundPool.setOnLoadCompleteListener((sp, id, status) -> {
            if (status == 0) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
        });
    }
    public static void loseSound(Context context){
        //Phat am thanh
        SoundPool soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .build();

        int soundId = soundPool.load(context, R.raw.lose_game_guess_word, 1);
        soundPool.setOnLoadCompleteListener((sp, id, status) -> {
            if (status == 0) {
                soundPool.play(soundId, 1, 1, 0, 0, 1);
            }
        });
    }
}
