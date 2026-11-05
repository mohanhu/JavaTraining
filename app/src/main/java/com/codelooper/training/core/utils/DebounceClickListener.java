package com.codelooper.training.core.utils;

import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class DebounceClickListener implements View.OnClickListener {
    private final Map<View,Long> lastClickTime;

    DebounceClickListener () {
        lastClickTime = new WeakHashMap<>();
    };

    abstract void setOnDebounce(View view);

    @Override
    public void onClick(View v) {
        Long lastClickValue = lastClickTime.get(v);
        long lastClick = lastClickValue != null ? lastClickValue : 0L;

        long now = System.currentTimeMillis();
        long debounceTime = 1000;
        lastClickTime.put(v,now);
        if (lastClick==0L) {
            setOnDebounce(v);
            return;
        }
        if (now - lastClick > debounceTime) {
            setOnDebounce(v);
        }
    }
}

