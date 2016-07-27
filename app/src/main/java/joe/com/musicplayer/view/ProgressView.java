package joe.com.musicplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import joe.com.musicplayer.R;

/**
 * Created by JOE on 2016/7/26.
 */
public class ProgressView extends View {

    private static final float STROKE_SIZE = 4;
    private static final float START_ANGLE = 135;
    private static final float MIDDLE_ANGLE = 270;
    private static final float GAP_ANGLE = 90;
    private static final float FULLPROGRESS_ANGLE = 360 - GAP_ANGLE;

    private final RectF mBounds = new RectF();
    private final Paint mPaint = new Paint();

    private final float mStrokeSize;
    private final int mBackgroundColor;
    private final int mForegroundColor;

    private int mProgress;
    private int mMax;
    private float mMorph;

    public ProgressView(Context context) {
        this(context, null, 0);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final float density = getResources().getDisplayMetrics().density;
        mStrokeSize = STROKE_SIZE * density;

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeSize);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, outValue, true);
        mForegroundColor = outValue.data;
        context.getTheme().resolveAttribute(R.attr.colorControlHighlight, outValue, true);
        mBackgroundColor = outValue.data;

        mProgress = 0;
        mMax = 100;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {

        if (progress < 0) {
            progress = 0;
        }

        if (progress > mMax) {
            progress = mMax;
        }

        if (progress != mProgress) {
            mProgress = progress;
            invalidate();
        }
    }

    public int getMax() {
        return mMax;
    }

    public void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != mMax) {
            mMax = max;
            if (mProgress > max) {
                mProgress = max;
            }
            invalidate();
        }
    }

    public float getMorph() {
        return mMorph;
    }

    public void setMorph(float morph) {
        if (morph != mMorph) {
            mMorph = morph;
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = Math.max(getSuggestedMinimumWidth(), (int)mStrokeSize);
        int height = Math.max(getSuggestedMinimumHeight(), (int)mStrokeSize);

        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        final int measureWidth = resolveSizeAndState(width, widthMeasureSpec, 0);
        final int measureHeight = resolveSizeAndState(height, heightMeasureSpec, 0);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float xpad = mStrokeSize / 2;

        mBounds.top = getPaddingTop() + xpad;
        mBounds.bottom = h - getPaddingBottom() - xpad;
        mBounds.left = getPaddingLeft() + xpad;
        mBounds.right = w - getPaddingRight() - xpad;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float scale = mMax > 0 ? mProgress / (float) mMax : 0;
        float startAngle = MIDDLE_ANGLE - START_ANGLE * mMorph;
        float sweepAngle = FULLPROGRESS_ANGLE * mMorph;

        if (mBounds.height() <= mStrokeSize) {

            // Draw the line

            mPaint.setColor(mBackgroundColor);
            canvas.drawLine(mBounds.left, mBounds.top, mBounds.right, mBounds.top, mPaint);

            float stopX = mBounds.width() * scale + mBounds.left;

            mPaint.setColor(mForegroundColor);
            canvas.drawLine(mBounds.left, mBounds.top, stopX, mBounds.top, mPaint);

        } else if (startAngle < 180f) {

            // Draw the arc

            mPaint.setColor(mBackgroundColor);
            canvas.drawArc(mBounds, startAngle, sweepAngle, false, mPaint);

            mPaint.setColor(mForegroundColor);
            canvas.drawArc(mBounds, startAngle, sweepAngle * scale, false, mPaint);

        } else {

            // Draw the semi-arc

            mPaint.setColor(mBackgroundColor);
            canvas.drawArc(mBounds, 180f, 180f, false, mPaint);

            mPaint.setColor(mForegroundColor);
            canvas.drawArc(mBounds, 180f, 180f * scale, false, mPaint);

        }

    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.progress = mProgress;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setProgress(ss.progress);
    }

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        int progress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }
    }

}