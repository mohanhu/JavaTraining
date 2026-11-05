package com.codelooper.training.core.scoped;

import java.util.function.Consumer;

public final class ScopedFuncExt {
    public static <T> T apply(T obj, Consumer<T> consumer) {
         consumer.accept(obj);
         return obj;
    }

}
