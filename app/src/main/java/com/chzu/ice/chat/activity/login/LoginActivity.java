package com.chzu.ice.chat.activity.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chzu.ice.chat.R;
import com.chzu.ice.chat.utils.ToastHelper;

public class LoginActivity extends AppCompatActivity implements ILoginView {
    private ILoginPresenter loginPresenter;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.loginPresenter = new LoginPresenter(this);
        this.loginPresenter.registerComponents();
    }

    @Override
    public void showWrongPasswordOrNoUser() {
        ToastHelper.showToast(getApplicationContext(), "密码错误或无此用户.", Toast.LENGTH_SHORT);
    }

    @Override
    public void registerComponents() {
        this.loginBtn = findViewById(R.id.loginBtn);
    }
}
