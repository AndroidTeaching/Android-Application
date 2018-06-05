package com.android.teaching.miprimeraapp.multimedia;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.teaching.miprimeraapp.R;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity {

    private VideoView myVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        myVideoView = findViewById(R.id.video_view);
        myVideoView.setVideoURI(Uri.parse("https://img-9gag-fun.9cache.com/photo/aBxGoNN_460sv.mp4"));
        myVideoView.start();
    }

    public void onPlayerPlay(View view) {
        Intent myServiceIntent = new Intent(this, MediaPlayerService.class);
        startService(myServiceIntent);
    }

    public void onPlayerPause(View view) {
        Intent myServiceIntent = new Intent(this, MediaPlayerService.class);
        stopService(myServiceIntent);
    }
}
