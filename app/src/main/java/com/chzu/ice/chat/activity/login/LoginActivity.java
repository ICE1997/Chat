package com.chzu.ice.chat.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chzu.ice.chat.R;
import com.chzu.ice.chat.activity.friends.FriendsActivity;
import com.chzu.ice.chat.utils.ToastHelper;

public class LoginActivity extends AppCompatActivity implements ILoginContract.View {
    private ILoginContract.Presenter loginPresenter;
    private Button loginBtn;
    private EditText usrEdt;
    private EditText pwdEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerComponents();
        registerListener();
        new LoginPresenter(this,new LoginModel());
    }

    @Override
    public void showWrongPasswordOrNoUser() {
        ToastHelper.showToast(getApplicationContext(), "密码错误或无此用户.", Toast.LENGTH_SHORT);
    }

    @Override
    public void showLoginSucceed() {
        ToastHelper.showToast(getApplicationContext(),"密码正确",Toast.LENGTH_SHORT);
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    public void registerComponents() {
        this.loginBtn = findViewById(R.id.loginBtn);
        this.usrEdt = findViewById(R.id.usrEdt);
        this.pwdEdt = findViewById(R.id.pwdEdt);
    }

    public void registerListener() {
        this.loginBtn.setOnClickListener(new loginOnClickListener());
    }

    @Override
    public void setPresenter(ILoginContract.Presenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    private class loginOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String usr = LoginActivity.this.usrEdt.getText().toString();
            String pwd = LoginActivity.this.pwdEdt.getText().toString();
            LoginActivity.this.loginPresenter.login(usr,pwd);
        }
    }
}
