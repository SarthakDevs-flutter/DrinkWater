package com.trending.water.drinking.reminder.appbasiclibs.mylistener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    private ClickListener clickListener;
    private GestureDetector gestureDetector;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener2) {
        this.clickListener = clickListener2;
        this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener2 != null) {
                    clickListener2.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }

    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child == null || this.clickListener == null || !this.gestureDetector.onTouchEvent(e)) {
            return false;
        }
        this.clickListener.onClick(child, rv.getChildPosition(child));
        return false;
    }

    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public interface ClickListener {
        void onClick(View view, int i);

        void onLongClick(View view, int i);
    }
}
