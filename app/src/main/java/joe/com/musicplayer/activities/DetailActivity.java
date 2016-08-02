package joe.com.musicplayer.activities;

import android.os.Bundle;
import android.transition.Transition;
import android.view.View;

import joe.com.musicplayer.R;
import joe.com.musicplayer.view.CoverView;
import joe.com.musicplayer.view.TransitionAdapter;

/**
 * Created by JOE on 2016/8/1.
 */
public class DetailActivity extends PlayerActivity {

    private CoverView mCoverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_detail);

        mCoverView = (CoverView)findViewById(R.id.cover);
        mCoverView.setCallbacks(new CoverView.Callbacks() {
            @Override
            public void onStopAnimation() {
                supportFinishAfterTransition();
            }
        });

        getWindow().getSharedElementEnterTransition().addListener(new TransitionAdapter() {
            @Override
            public void onTransitionEnd(Transition transition) {
                play();
                mCoverView.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        onFabClick(null);
    }

    public void onFabClick(View view) {
        pause();
        mCoverView.stop();
    }
}
