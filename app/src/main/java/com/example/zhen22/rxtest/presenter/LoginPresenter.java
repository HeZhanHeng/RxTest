package com.example.zhen22.rxtest.presenter;

import com.example.zhen22.rxtest.module.LoginModelImple;
import com.example.zhen22.rxtest.module.LoginPort;
import com.example.zhen22.rxtest.module.OnLoginListener;
import com.example.zhen22.rxtest.view.LoginView;

/**
 * Created by zhen22 on 2019/11/28.
 */

public class LoginPresenter implements OnLoginListener {
    private LoginPort loginPort;
    private LoginView loginView;
    public LoginPresenter(LoginView loginView){
        this.loginView=loginView;
        loginPort=new LoginModelImple();
    }
    public void login(){
        String username = loginView.getUserName();
        String password = loginView.getPassword();
        loginPort.login(username,password,this);
    }
    @Override
    public void onUserNameError() {
        loginView.showToast("用户名或者密码错误");
    }

    @Override
    public void onPasswordError() {
        loginView.showToast("用户名或者密码错误");
    }

    @Override
    public void onSuccess() {
        loginView.showToast("登录成功！");
    }

    @Override
    public void onFailure() {
        loginView.showToast("异常错误！");
    }
}
