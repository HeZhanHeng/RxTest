package com.example.zhen22.rxtest.module;

/**
 * Created by zhen22 on 2019/11/28.
 */

public interface OnLoginListener {
    public void onUserNameError();
    public void onPasswordError();
    public void onSuccess();
    public void onFailure();
}
