package com.example.sample;

import android.content.Context;

import java.net.ConnectException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Response;

public class ResponseHandler<T extends BaseResponseModel> implements retrofit2.Callback<T> {
    private SuccessAPICallback<T> successAPICallback = null;
    private FailureAPICallback failureAPICallback = null;
    private Context context;
    private Call<T> mCall;

    public ResponseHandler(Context context) {
        this.context = context;
    }

    public ResponseHandler(Context context, SuccessAPICallback<T> successAPICallback, FailureAPICallback failureAPICallback) {
        this.context = context;
        this.failureAPICallback = failureAPICallback;
        this.successAPICallback = successAPICallback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            if (successAPICallback != null)
                successAPICallback.onResponse(response.body());
        } else {
            if (failureAPICallback != null) {
                failureAPICallback.onFailure(response.body(), response.body());
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        String errorMessage = throwable.toString();
        if (throwable instanceof UnknownHostException || throwable instanceof ConnectException) {
            //showNetworkDialog();
        }
    }
}