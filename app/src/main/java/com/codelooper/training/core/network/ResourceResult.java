package com.codelooper.training.core.network;

public sealed abstract class ResourceResult<R>
        permits ResourceResult.Success,
                ResourceResult.Error,
                ResourceResult.Loading {

    private ResourceResult() {}

    public static final class Success<R>
            extends ResourceResult<R> {

        private final R data;

        public Success(R data) {
            this.data = data;
        }

        public R getData() {
            return data;
        }

        @Override
        public String toString() {
            return "Success[data=" + data + "]";
        }
    }

    public static final class Error<R>
            extends ResourceResult<R> {

        private final Throwable exception;

        public Error(Throwable exception) {
            this.exception = exception;
        }

        public Throwable getException() {
            return exception;
        }

        @Override
        public String toString() {
            return "Error[exception=" + exception + "]";
        }
    }

    public static final class Loading
            extends ResourceResult<Object> {

        public Loading() {
        }

        @Override
        public String toString() {
            return "Loading";
        }
    }

    public static <T> boolean succeeded(ResourceResult<T> result) {
        return result instanceof Success<?> success
                && success.getData() != null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T succeededResourceResult(
            ResourceResult<?> result
    ) {
        if (result instanceof Success<?> success) {
            return (T) success.getData();
        }
        return null;
    }
}