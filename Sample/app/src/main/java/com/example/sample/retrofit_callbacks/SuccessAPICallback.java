package com.example.sample.retrofit_callbacks;

public interface SuccessAPICallback<T> {
    void onResponse(T t);
}
