package com.example.zhen22.rxtest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhen22.rxtest.R;
import com.example.zhen22.rxtest.presenter.LoginPresenter;
import com.example.zhen22.rxtest.view.LoginView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,LoginView {
private EditText et_nickName,et_password;
private Button btn;
private LoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        loginPresenter=new LoginPresenter(this);
    }
    private void initView(){
        et_nickName=(EditText)findViewById(R.id.et_nickName);
        et_password=(EditText)findViewById(R.id.et_password);
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      switch (view.getId()){
          case R.id.btn:
              loginPresenter.login();
              break;
      }
    }

    @Override
    public String getUserName() {
        return et_nickName.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return et_password.getText().toString().trim();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}
