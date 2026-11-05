package com.codelooper.training.core;

import android.app.Application;
import android.content.Context;

import androidx.test.runner.AndroidJUnitRunner;

import dagger.hilt.android.testing.HiltTestApplication;

public class HiltAndroidRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(
            ClassLoader classLoader,
            String className,
            Context context
    ) throws InstantiationException,
            IllegalAccessException,
            ClassNotFoundException {

        return super.newApplication(
                classLoader,
                HiltTestApplication.class.getName(),
                context
        );
    }
}
