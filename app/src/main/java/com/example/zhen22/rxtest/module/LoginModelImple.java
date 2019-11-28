package com.example.zhen22.rxtest.module;

/**
 * Created by zhen22 on 2019/11/28.
 */

public class LoginModelImple implements LoginPort {
    @Override
    public void login(String username, String password, OnLoginListener onLoginListener) {
        if(username.equals("admin")&&password.equals("123456")){
            onLoginListener.onSuccess();//登录成功
        }else if(!username.equals("admian")||!password.equals("123456")){
            onLoginListener.onUserNameError();//用户名或者密码错误
            onLoginListener.onPasswordError();
        }else {
            onLoginListener.onFailure();//登录失败
        }

    }
}
