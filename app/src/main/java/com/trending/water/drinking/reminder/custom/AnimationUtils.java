package com.trending.water.drinking.reminder.custom;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class AnimationUtils {
    public static void expand(final View v) {
        v.measure(View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            /* access modifiers changed from: protected */
            public void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1.0f ? -2 : (int) (((float) targetHeight) * interpolatedTime);
                v.requestLayout();
            }

            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((long) ((int) (((float) targetHeight) / v.getContext().getResources().getDisplayMetrics().density)));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            /* access modifiers changed from: protected */
            public void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1.0f) {
                    v.setVisibility(View.GONE);
                    return;
                }
                v.getLayoutParams().height = initialHeight - ((int) (((float) initialHeight) * interpolatedTime));
                v.requestLayout();
            }

            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((long) ((int) (((float) initialHeight) / v.getContext().getResources().getDisplayMetrics().density)));
        v.startAnimation(a);
    }
}
