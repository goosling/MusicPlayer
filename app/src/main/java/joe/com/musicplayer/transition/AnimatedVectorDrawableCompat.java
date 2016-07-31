package joe.com.musicplayer.transition;

import android.os.Handler;
import android.os.Message;

/**
 * Created by JOE on 2016/7/30.
 */
public class AnimatedVectorDrawableCompat {

    private static final Handler sHandler = new AnimatedVectorDrawableHandler();

    private static class AnimatedVectorDrawableHandler extends Handler {
        private static final int MSG_START = 0;
        private static final int MSG_STOP = 1;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }
}
