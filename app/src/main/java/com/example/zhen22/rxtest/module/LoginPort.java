package com.example.zhen22.rxtest.module;

/**
 * Created by zhen22 on 2019/11/28.
 */

public interface LoginPort {
    void login(String username,String password,OnLoginListener onLoginListener);
}
