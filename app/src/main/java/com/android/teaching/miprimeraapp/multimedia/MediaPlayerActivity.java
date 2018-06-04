package com.android.teaching.miprimeraapp.multimedia;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.teaching.miprimeraapp.R;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity {

    private MediaPlayer myMediaPlayer;
    private boolean isPrepared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        try {
            myMediaPlayer = new MediaPlayer();
            myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            myMediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/miprimeraapp-db818.appspot.com/o/crowd-cheering.mp3?alt=media&token=1e1a8341-f108-460c-b3a7-8f19097040e3");
            myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPrepared = true;
                }
            });
            myMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        myMediaPlayer.release();
        myMediaPlayer = null;
    }

    public void onPlayerPlay(View view) {
        if (isPrepared) {
            myMediaPlayer.start();
        }
    }

    public void onPlayerPause(View view) {
        myMediaPlayer.pause();
    }
}
