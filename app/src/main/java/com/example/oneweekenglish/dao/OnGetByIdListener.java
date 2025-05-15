package com.example.oneweekenglish.dao;

public interface OnGetByIdListener<T> {
    void onGetByID(T t);
    void onGetFailed(Exception e);
}
