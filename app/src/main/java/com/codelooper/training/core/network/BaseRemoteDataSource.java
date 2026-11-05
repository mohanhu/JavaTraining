package com.codelooper.training.core.network;

import retrofit2.Call;
import retrofit2.Response;

abstract public class BaseRemoteDataSource {
    protected <T> NetworkResult<T> safeApiCall(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return new NetworkResult.Success<T>(response.body(), response.message());
            }
            return new NetworkResult.Error<>(response.message(), response.code());
        } catch (Exception e) {
            return new NetworkResult.Error<>(e.getMessage());
        }
    }
}
