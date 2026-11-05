package com.codelooper.training.core.network;

public sealed class NetworkResult<T>
        permits NetworkResult.Loading,
                NetworkResult.Success,
                NetworkResult.Error,
                NetworkResult.NoInternet,
                NetworkResult.UnAuthorized
{

    public String message;
    public T data;
    Integer code;

    NetworkResult(
            T data,
            String message,
            Integer code
    ) {
        this.message = message;
        this.data = data;
        this.code = code;
    }


    public final class Loading extends NetworkResult<T> {
        public Loading() {
            super(null, null, null);
        }
    }
    public static final class Success<T> extends NetworkResult<T> {
        public Success(
                T data,
                String message
        ) {
            super(data, message, null);
        }
    }
    public static final class Error<T> extends NetworkResult<T> {
        public Error(
                String message,
                Integer code
        ) {
            super(null, message, code);
        }

        public Error(
                String message
        ) {
            super(null, message, null);
        }
    }

    public static final class NoInternet<T> extends NetworkResult<T> {
        public NoInternet(
                String message,
                Integer code
        ) {
            super(null, message, code);
        }
    }

    public static final class UnAuthorized<T> extends NetworkResult<T> {
        public UnAuthorized(
                String message,
                Integer code
        ) {
            super(null, message, code);
        }
    }

}