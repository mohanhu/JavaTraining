package com.codelooper.training.utils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for testing LiveData in unit tests.
 * Safely gets the value from a LiveData object synchronously.
 */
public class LiveDataTestUtil {

    /**
     * Observes a LiveData until it receives a value (up to 2 seconds), then returns the value.
     * 
     * @param liveData The LiveData object to observe.
     * @param <T> The type of data held by the LiveData.
     * @return The current value of the LiveData.
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(1);

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);

        // Wait up to 2 seconds for LiveData value to be posted
        if (!latch.await(2, TimeUnit.SECONDS)) {
            liveData.removeObserver(observer);
            return (T) data[0];
        }

        return (T) data[0];
    }
}
