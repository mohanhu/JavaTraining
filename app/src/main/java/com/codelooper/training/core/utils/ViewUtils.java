package com.codelooper.training.core.utils;

import android.view.View;

import java.util.function.Consumer;

public class ViewUtils {

    public static void setOnDebounceClickListener(
            View view,
            Consumer<View> onClick
    ) {
        view.setOnClickListener(new DebounceClickListener() {
            @Override
            void setOnDebounce(View view) {
                onClick.accept(view);
            }
        });
    }
}

