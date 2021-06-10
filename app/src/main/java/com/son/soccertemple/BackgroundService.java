package com.son.soccertemple;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundService extends Service {
    private static final String TAG = null;
    MediaPlayer player;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.drump);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return flags;
    }

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    public void onPause() {
        player.stop();
    }

    public void onPlay() {
        player.start();
    }
}
