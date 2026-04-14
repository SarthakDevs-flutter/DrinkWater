package com.trending.water.drinking.reminder.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class NonSwipeableViewPager extends ViewPager {
    private static final String TAG = "NonSwipeableViewPager";

    public NonSwipeableViewPager(@NonNull Context context) {
        super(context);
        initScroller();
    }

    public NonSwipeableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initScroller();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Disable swipe by intercepting nothing
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Disable swipe by handling nothing
        return false;
    }

    private void initScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, new CustomScroller(getContext()));
        } catch (Exception e) {
            Log.e(TAG, "Failed to set custom scroller", e);
        }
    }

    private static class CustomScroller extends Scroller {
        public CustomScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            // Fixed duration for smooth transition
            super.startScroll(startX, startY, dx, dy, 350);
        }
    }
}
