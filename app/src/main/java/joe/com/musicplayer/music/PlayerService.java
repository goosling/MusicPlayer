package joe.com.musicplayer.music;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by JOE on 2016/7/27.
 */
public class PlayerService extends Service {

    private static final String TAG = PlayerService.class.getSimpleName();
    private static final int DURATION = 335;

    private final IBinder mBinder = new LocalBinder();

    private Worker mWorker;

    public PlayerService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(mWorker != null) {
            mWorker.interrupt();
        }
        return super.onUnbind(intent);
    }

    public void play() {
        if(mWorker == null ) {
            mWorker = new Worker();
            mWorker.start();
        } else {
            mWorker.doResume();
        }
    }

    public boolean isPlaying() {
        return mWorker != null && mWorker.isPlaying();
    }

    public void pause() {
        if(mWorker != null) {
            mWorker.doPause();
        }
    }

    public int getPosition() {
        if(mWorker != null) {
            return mWorker.getPosition();
        }
        return 0;
    }

    public int getDuration() {
        return DURATION;
    }

    private static class Worker extends Thread {

        boolean paused = false;
        int position = 0;

        @Override
        public void run() {
            try{
                while(position < DURATION) {
                    sleep(1000);
                    if(!paused) {
                        position++;
                    }
                }
            }catch(InterruptedException e) {
                Log.d(TAG, "Player unbounded");
            }
        }

        void doResume() {
            paused = false;
        }

        void doPause() {
            paused = true;
        }

        boolean isPlaying() {
            return !paused;
        }

        int getPosition() {
            return position;
        }
    }

    public class LocalBinder extends Binder {
        public PlayerService getService() {
            //return the instance of PlayerService so clients can call public methods
            return PlayerService.this;
        }
    }
}
