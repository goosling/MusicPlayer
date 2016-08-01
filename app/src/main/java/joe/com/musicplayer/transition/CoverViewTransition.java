package joe.com.musicplayer.transition;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import joe.com.musicplayer.R;
import joe.com.musicplayer.view.CoverView;

/**
 * Created by JOE on 2016/7/31.
 */
public class CoverViewTransition extends Transition {

    private static final String PROPNAME_RADIUS = CoverViewTransition.class.getName() + ":radius";
    private static final String PROPNAME_TRACK_ALPHA = CoverViewTransition.class.getName() + ":trackAlpha";
    private static final String[] sTransitionProperties = {PROPNAME_RADIUS, PROPNAME_TRACK_ALPHA};

    private static final Property<CoverView, Float> RADIUS_PROPERTY =
            new Property<CoverView, Float>(Float.class, "radius") {
                @Override
                public Float get(CoverView object) {
                    return object.getTransitionRadius();
                }

                @Override
                public void set(CoverView object, Float value) {
                    object.setTransitionRadius(value);
                }
            };

    private static final Property<CoverView, Integer> TRACK_ALPHA_PROPERTY =
            new Property<CoverView, Integer>(Integer.class, "trackAlpha") {
                @Override
                public Integer get(CoverView object) {
                    return object.getTrackAlpha();
                }

                @Override
                public void set(CoverView object, Integer value) {
                    object.setTrackAlpha(value);
                }
            };
    private final int mShape;

    public CoverViewTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoverView);
        int shape = a.getInt(R.styleable.CoverView_shape, CoverView.SHAPE_RECTANGLE);
        a.recycle();
        mShape = shape;
    }

    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        // Add fake value to force calling of createAnimator method
        captureValues(transitionValues, "start");
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        // Add fake value to force calling of createAnimator method
        captureValues(transitionValues, "end");
    }

    private void captureValues(TransitionValues transitionValues, Object value) {
        if (transitionValues.view instanceof CoverView) {
            transitionValues.values.put(PROPNAME_RADIUS, value);
            transitionValues.values.put(PROPNAME_TRACK_ALPHA, value);
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if(endValues == null || !(endValues.view instanceof CoverView)) {
            return null;
        }

        CoverView view = (CoverView)endValues.view;
        final float minRadius = view.getMinRadius();
        final float maxRadius = view.getMaxRadius();

        float startRadius, endRadius;
        int startTrackAlpha, endTrackAlpha;

        if(mShape == CoverView.SHAPE_RECTANGLE) {
            startRadius = maxRadius;
            endRadius = minRadius;
            startTrackAlpha = CoverView.TACK_ALPHA_TRANSPARENT;
            endTrackAlpha = CoverView.TACK_ALPHA_OPAQUE;
        } else {
            startRadius = minRadius;
            endRadius = maxRadius;
            startTrackAlpha = CoverView.TACK_ALPHA_OPAQUE;
            endTrackAlpha = CoverView.TACK_ALPHA_TRANSPARENT;
        }

        List<Animator> animatorList = new ArrayList<>();

        view.setTransitionRadius(startRadius);
        animatorList.add(ObjectAnimator.ofFloat(view, RADIUS_PROPERTY, startRadius, endRadius));

        view.setTrackAlpha(startTrackAlpha);
        animatorList.add(ObjectAnimator.ofInt(view, TRACK_ALPHA_PROPERTY, startTrackAlpha, endTrackAlpha));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorList);
        return animatorSet;

    }
}
