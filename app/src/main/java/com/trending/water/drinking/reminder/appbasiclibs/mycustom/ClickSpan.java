package com.trending.water.drinking.reminder.appbasiclibs.mycustom;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class ClickSpan extends ClickableSpan {
    int text_color = -16776961;
    private boolean isUnderline = false;
    private OnClickListener mListener;

    public ClickSpan(OnClickListener listener) {
        this.mListener = listener;
    }

    public void onClick(View widget) {
        if (this.mListener != null) {
            this.mListener.onClick();
        }
    }

    public void setTextColor(int color) {
        this.text_color = color;
    }

    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(this.isUnderline);
        ds.bgColor = Color.parseColor("#FAFAFA");
        ds.setColor(this.text_color);
    }

    public interface OnClickListener {
        void onClick();
    }
}
