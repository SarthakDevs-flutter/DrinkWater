package com.trending.water.drinking.reminder.appbasiclibs.mycustom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.trending.water.drinking.reminder.R;

public class MaterialProgressBar extends View {
    private static final String TAG = "MaterialProgressBar";

    private static final int BAR_LENGTH = 16;
    private static final int BAR_MAX_LENGTH = 270;
    private static final long PAUSE_GROWING_TIME = 200;

    private int barColor = 0xAA0000FF; // Semi-transparent blue
    private int rimColor = ViewCompat.MEASURED_SIZE_MASK;
    private int barWidth = 4;
    private int rimWidth = 4;
    private int circleRadius = 28;

    private boolean fillRadius = false;
    private double barSpinCycleTime = 460.0d;
    private float spinSpeed = 230.0f;
    private boolean linearProgress = false;

    private float progress = 0.0f;
    private float targetProgress = 0.0f;
    private boolean isSpinning = false;

    private long lastTimeAnimated = 0;
    private long pausedTimeWithoutGrowing = 0;
    private double timeStartGrowing = 0.0d;
    private float barExtraLength = 0.0f;
    private boolean barGrowingFromFront = true;

    private final Paint barPaint = new Paint();
    private final Paint rimPaint = new Paint();
    private RectF circleBounds = new RectF();

    private boolean shouldAnimate = true;
    private ProgressCallback callback;

    public MaterialProgressBar(Context context) {
        super(context);
        initAnimationScale();
    }

    public MaterialProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MaterialProgressBar);
        parseAttributes(a);
        initAnimationScale();
    }

    private void initAnimationScale() {
        float scale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            scale = Settings.Global.getFloat(getContext().getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, 1.0f);
        } else {
            scale = Settings.System.getFloat(getContext().getContentResolver(), "animator_duration_scale", 1.0f);
        }
        this.shouldAnimate = scale != 0.0f;
    }

    private void parseAttributes(TypedArray a) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        barWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, barWidth, metrics);
        rimWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rimWidth, metrics);
        circleRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, circleRadius, metrics);

        circleRadius = (int) a.getDimension(R.styleable.MaterialProgressBar_matProg_circleRadius, circleRadius);
        fillRadius = a.getBoolean(R.styleable.MaterialProgressBar_matProg_fillRadius, false);
        barWidth = (int) a.getDimension(R.styleable.MaterialProgressBar_matProg_barWidth, barWidth);
        rimWidth = (int) a.getDimension(R.styleable.MaterialProgressBar_matProg_rimWidth, rimWidth);
        spinSpeed = a.getFloat(R.styleable.MaterialProgressBar_matProg_spinSpeed, spinSpeed / 360.0f) * 360f;
        barSpinCycleTime = a.getInt(R.styleable.MaterialProgressBar_matProg_barSpinCycleTime, (int) barSpinCycleTime);
        barColor = a.getColor(R.styleable.MaterialProgressBar_matProg_barColor, barColor);
        rimColor = a.getColor(R.styleable.MaterialProgressBar_matProg_rimColor, rimColor);
        linearProgress = a.getBoolean(R.styleable.MaterialProgressBar_matProg_linearProgress, false);

        if (a.getBoolean(R.styleable.MaterialProgressBar_matProg_progressIndeterminate, false)) {
            spin();
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewWidth = circleRadius + getPaddingLeft() + getPaddingRight();
        int viewHeight = circleRadius + getPaddingTop() + getPaddingBottom();

        int width = resolveSize(viewWidth, widthMeasureSpec);
        int height = resolveSize(viewHeight, heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupBounds(w, h);
        setupPaints();
        invalidate();
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
        setupPaints();
        if (!this.isSpinning) {
            invalidate();
        }
    }

    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Paint.Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);
    }



    private void setupBounds(int width, int height) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        if (!fillRadius) {
            int minValue = Math.min((width - paddingLeft) - paddingRight, (height - paddingTop) - paddingBottom);
            int circleDiameter = Math.min(minValue, (circleRadius * 2) - (barWidth * 2));
            int xOffset = ((((width - paddingLeft) - paddingRight) - circleDiameter) / 2) + paddingLeft;
            int yOffset = ((((height - paddingTop) - paddingBottom) - circleDiameter) / 2) + paddingTop;

            circleBounds = new RectF(xOffset + barWidth, yOffset + barWidth, xOffset + circleDiameter - barWidth, yOffset + circleDiameter - barWidth);
        } else {
            circleBounds = new RectF(paddingLeft + barWidth, paddingTop + barWidth, width - paddingRight - barWidth, height - paddingBottom - barWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);

        boolean mustInvalidate = false;

        if (!shouldAnimate) return;

        if (isSpinning) {
            mustInvalidate = true;
            long deltaTime = SystemClock.uptimeMillis() - lastTimeAnimated;
            updateBarLength(deltaTime);

            progress += (deltaTime * spinSpeed) / 1000.0f;
            if (progress > 360.0f) {
                progress -= 360.0f;
                runCallback(-1.0f);
            }
            lastTimeAnimated = SystemClock.uptimeMillis();

            float from = progress - 90;
            float length = barExtraLength + BAR_LENGTH;

            if (isInEditMode()) {
                from = 0;
                length = 135;
            }

            canvas.drawArc(circleBounds, from, length, false, barPaint);
        } else {
            float oldProgress = progress;
            if (progress != targetProgress) {
                mustInvalidate = true;
                this.progress = Math.min(progress + (spinSpeed * (((float) (SystemClock.uptimeMillis() - lastTimeAnimated)) / 1000.0f)), targetProgress);
                lastTimeAnimated = SystemClock.uptimeMillis();
            }

            if (oldProgress != progress) {
                runCallback();
            }

            float offset = 0.0f;
            float drawProgress = progress;
            if (!linearProgress) {
                float factor = 2.0f;
                offset = (float) (1.0f - Math.pow(1.0f - (progress / 360.0f), factor * 2.0f)) * 360.0f;
                drawProgress = (float) (1.0f - Math.pow(1.0f - (progress / 360.0f), factor)) * 360.0f;
            }

            if (isInEditMode()) drawProgress = 360;

            canvas.drawArc(circleBounds, offset - 90, drawProgress, false, barPaint);
        }

        if (mustInvalidate) {
            invalidate();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            lastTimeAnimated = SystemClock.uptimeMillis();
        }
    }

    private void updateBarLength(long deltaTime) {
        if (pausedTimeWithoutGrowing >= PAUSE_GROWING_TIME) {
            timeStartGrowing += deltaTime;
            if (timeStartGrowing > barSpinCycleTime) {
                timeStartGrowing -= barSpinCycleTime;
                pausedTimeWithoutGrowing = 0;
                barGrowingFromFront = !barGrowingFromFront;
            }

            float distance = (float) (Math.cos(((timeStartGrowing / barSpinCycleTime) + 1.0d) * Math.PI) / 2.0d) + 0.5f;
            float range = BAR_MAX_LENGTH - BAR_LENGTH;

            if (barGrowingFromFront) {
                barExtraLength = distance * range;
            } else {
                float newLength = (1.0f - distance) * range;
                progress += (barExtraLength - newLength);
                barExtraLength = newLength;
            }
        } else {
            pausedTimeWithoutGrowing += deltaTime;
        }
    }

    public void spin() {
        lastTimeAnimated = SystemClock.uptimeMillis();
        isSpinning = true;
        invalidate();
    }

    public void stopSpinning() {
        isSpinning = false;
        progress = 0;
        targetProgress = 0;
        invalidate();
    }

    public void setProgress(float progress) {
        if (isSpinning) {
            this.progress = 0;
            isSpinning = false;
            runCallback();
        }

        if (progress > 1.0f) progress -= 1.0f;
        else if (progress < 0.0f) progress = 0.0f;

        if (progress != targetProgress) {
            if (this.progress == targetProgress) {
                lastTimeAnimated = SystemClock.uptimeMillis();
            }
            targetProgress = Math.min(progress * 360.0f, 360.0f);
            invalidate();
        }
    }

    private void runCallback(float value) {
        if (callback != null) {
            callback.onProgressUpdate(value);
        }
    }

    private void runCallback() {
        if (callback != null) {
            callback.onProgressUpdate((float) Math.round((progress * 100) / 360) / 100.0f);
        }
    }

    public void setCallback(ProgressCallback callback) {
        this.callback = callback;
        if (!isSpinning) runCallback();
    }

    public float getProgress() {
        return isSpinning ? -1 : progress / 360.0f;
    }

    public void setInstantProgress(float progress) {
        if (isSpinning) {
            this.progress = 0;
            isSpinning = false;
        }

        if (progress > 1.0f) progress -= 1.0f;
        else if (progress < 0.0f) progress = 0.0f;

        if (progress != targetProgress) {
            targetProgress = Math.min(progress * 360.0f, 360.0f);
            this.progress = targetProgress;
            lastTimeAnimated = SystemClock.uptimeMillis();
            invalidate();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        WheelSavedState ss = new WheelSavedState(super.onSaveInstanceState());
        ss.progress = this.progress;
        ss.targetProgress = this.targetProgress;
        ss.isSpinning = this.isSpinning;
        ss.spinSpeed = this.spinSpeed;
        ss.barWidth = this.barWidth;
        ss.barColor = this.barColor;
        ss.rimWidth = this.rimWidth;
        ss.rimColor = this.rimColor;
        ss.circleRadius = this.circleRadius;
        ss.linearProgress = this.linearProgress;
        ss.fillRadius = this.fillRadius;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof WheelSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        WheelSavedState ss = (WheelSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.progress = ss.progress;
        this.targetProgress = ss.targetProgress;
        this.isSpinning = ss.isSpinning;
        this.spinSpeed = ss.spinSpeed;
        this.barWidth = ss.barWidth;
        this.barColor = ss.barColor;
        this.rimWidth = ss.rimWidth;
        this.rimColor = ss.rimColor;
        this.circleRadius = ss.circleRadius;
        this.linearProgress = ss.linearProgress;
        this.fillRadius = ss.fillRadius;
        this.lastTimeAnimated = SystemClock.uptimeMillis();
    }

    public interface ProgressCallback {
        void onProgressUpdate(float progress);
    }

    static class WheelSavedState extends BaseSavedState {
        float progress;
        float targetProgress;
        boolean isSpinning;
        float spinSpeed;
        int barWidth;
        int barColor;
        int rimWidth;
        int rimColor;
        int circleRadius;
        boolean linearProgress;
        boolean fillRadius;

        WheelSavedState(Parcelable superState) {
            super(superState);
        }

        private WheelSavedState(Parcel in) {
            super(in);
            this.progress = in.readFloat();
            this.targetProgress = in.readFloat();
            this.isSpinning = in.readByte() != 0;
            this.spinSpeed = in.readFloat();
            this.barWidth = in.readInt();
            this.barColor = in.readInt();
            this.rimWidth = in.readInt();
            this.rimColor = in.readInt();
            this.circleRadius = in.readInt();
            this.linearProgress = in.readByte() != 0;
            this.fillRadius = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.progress);
            out.writeFloat(this.targetProgress);
            out.writeByte((byte) (isSpinning ? 1 : 0));
            out.writeFloat(this.spinSpeed);
            out.writeInt(this.barWidth);
            out.writeInt(this.barColor);
            out.writeInt(this.rimWidth);
            out.writeInt(this.rimColor);
            out.writeInt(this.circleRadius);
            out.writeByte((byte) (linearProgress ? 1 : 0));
            out.writeByte((byte) (fillRadius ? 1 : 0));
        }

        public static final Creator<WheelSavedState> CREATOR = new Creator<WheelSavedState>() {
            public WheelSavedState createFromParcel(Parcel in) {
                return new WheelSavedState(in);
            }

            public WheelSavedState[] newArray(int size) {
                return new WheelSavedState[size];
            }
        };
    }
}
