package com.example.oneweekenglish.dao;

import java.util.List;

public interface OnGetAllListener <T>{
    void onGetAll(List<T> t);
    void onGetFailed(Exception e);
}
