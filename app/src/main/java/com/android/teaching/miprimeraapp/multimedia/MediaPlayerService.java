package com.android.teaching.miprimeraapp.multimedia;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.android.teaching.miprimeraapp.R;

import java.io.IOException;

public class MediaPlayerService extends Service {
    private MediaPlayer myMediaPlayer;

    public MediaPlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myMediaPlayer = MediaPlayer.create(this, R.raw.cancion);
        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopSelf();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (myMediaPlayer.isPlaying()) {
            myMediaPlayer.pause();
        } else {
            myMediaPlayer.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myMediaPlayer.release();
        myMediaPlayer = null;
    }
}
