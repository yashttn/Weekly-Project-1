package com.example.sample.RetrofitCallbacks;

public interface SuccessAPICallback<T> {
    void onResponse(T t);
}
