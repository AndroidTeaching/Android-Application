package com.android.teaching.miprimeraapp.multimedia;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.teaching.miprimeraapp.R;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity {

    private MediaPlayer myMediaPlayer;
    private boolean isPrepared = false;
    private ProgressBar audioProgressBar;
    private TextView textViewProgress;

    private Runnable audioProgressUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = myMediaPlayer.getCurrentPosition();
            audioProgressBar.setProgress(currentPosition);
            String secondsString = String.format("%02d", currentPosition / 1000);
            textViewProgress.setText("00:" + secondsString);
            audioProgressUpdateHandler.postDelayed(audioProgressUpdateRunnable, 50);
        }
    };
    private Handler audioProgressUpdateHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        audioProgressBar = findViewById(R.id.audio_progress_bar);
        textViewProgress = findViewById(R.id.text_view_progress);

        try {
            myMediaPlayer = new MediaPlayer();
            myMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            myMediaPlayer.setDataSource("https://firebasestorage.googleapis.com/v0/b/miprimeraapp-db818.appspot.com/o/crowd-cheering.mp3?alt=media&token=1e1a8341-f108-460c-b3a7-8f19097040e3");
            myMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPrepared = true;
                    audioProgressBar.setMax(mp.getDuration());
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
        audioProgressUpdateHandler.removeCallbacks(audioProgressUpdateRunnable);
        myMediaPlayer.release();
        myMediaPlayer = null;
    }

    public void onPlayerPlay(View view) {
        if (isPrepared) {
            myMediaPlayer.start();
            audioProgressUpdateHandler.post(audioProgressUpdateRunnable);
        }
    }

    public void onPlayerPause(View view) {
        myMediaPlayer.pause();
    }
}
