package com.android.teaching.miprimeraapp.multimedia;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.teaching.miprimeraapp.R;
import com.android.teaching.miprimeraapp.model.GameModel;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity {

    private VideoView myVideoView;
    private ProgressBar counterProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        counterProgressBar = findViewById(R.id.counter_progress_bar);

        myVideoView = findViewById(R.id.video_view);
        myVideoView.setVideoURI(Uri.parse("https://img-9gag-fun.9cache.com/photo/aBxGoNN_460sv.mp4"));
        myVideoView.start();

        MediaController myMediaController = new MediaController(this);
        myMediaController.setAnchorView(myVideoView);
        myVideoView.setMediaController(myMediaController);

        new ContadorAsyncTask().execute();
    }

    public void onPlayerPlay(View view) {
        Intent myServiceIntent = new Intent(this, MediaPlayerService.class);
        startService(myServiceIntent);
    }

    public void onPlayerPause(View view) {
        Intent myServiceIntent = new Intent(this, MediaPlayerService.class);
        stopService(myServiceIntent);
    }

    /**
     *  Crear clase AyncTask
     */
    private class ContadorAsyncTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            counterProgressBar.setMax(100);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int count;
            for(count = 0; count <= 100; count++) {
                publishProgress(count);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return 100;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            counterProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d("AsyncTask", "onPostExecute: " + integer.toString());
        }
    }


}











