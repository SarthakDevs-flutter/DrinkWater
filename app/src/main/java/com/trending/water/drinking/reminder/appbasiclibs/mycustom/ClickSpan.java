package com.trending.water.drinking.reminder.appbasiclibs.mycustom;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.NonNull;

public class ClickSpan extends ClickableSpan {
    private final boolean isUnderline = false;
    private final OnClickListener onClickListener;
    private int textColor = Color.BLUE;

    public ClickSpan(@NonNull OnClickListener listener) {
        this.onClickListener = listener;
    }

    @Override
    public void onClick(@NonNull View widget) {
        if (this.onClickListener != null) {
            this.onClickListener.onClick();
        }
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint textPaint) {
        textPaint.setUnderlineText(this.isUnderline);
        textPaint.bgColor = Color.parseColor("#FAFAFA");
        textPaint.setColor(this.textColor);
    }

    public interface OnClickListener {
        void onClick();
    }
}
