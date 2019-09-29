package com.chzu.ice.chat.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.R;
import com.chzu.ice.chat.activity.friendsRelations.FriendsActivity;
import com.chzu.ice.chat.activity.register.RegisterActivity;
import com.chzu.ice.chat.utils.ToastHelper;

public class LoginActivity extends AppCompatActivity implements ILoginContract.View {
    private ILoginContract.Presenter loginPresenter;
    private Button loginBtn;
    private Button toRegisterBtn;
    private EditText usrEdt;
    private EditText pwdEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerComponents();
        registerListener();
        new LoginPresenter(this, new LoginModel());
    }

    @Override
    public void showNoSuchUser() {
        ToastHelper.showToast(getApplicationContext(), "无此用户.", Toast.LENGTH_SHORT);
    }

    @Override
    public void showWrongPassword() {
        ToastHelper.showToast(getApplicationContext(), "密码错误.", Toast.LENGTH_SHORT);
    }

    @Override
    public void showLoginSucceed() {
        ToastHelper.showToast(getApplicationContext(), "密码正确", Toast.LENGTH_SHORT);


        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
        ((App) getApplication()).setCurrentUserName(usrEdt.getText().toString());


        Intent intent2 = new Intent("SubscribeSignal");
        intent2.putExtra("username", usrEdt.getText().toString());
        LocalBroadcastManager.getInstance(App.getApplication()).sendBroadcast(intent2);

        this.finish();

    }

    public void registerComponents() {
        this.loginBtn = findViewById(R.id.registerBtn);
        this.usrEdt = findViewById(R.id.usrEdt);
        this.pwdEdt = findViewById(R.id.pwdEdt);
        this.toRegisterBtn = findViewById(R.id.toRegisterBtn);
    }

    public void registerListener() {
        this.loginBtn.setOnClickListener(new LoginOnClickListener());
        this.toRegisterBtn.setOnClickListener(new ToRegister());
    }

    @Override
    public void setPresenter(ILoginContract.Presenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    private class LoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String usr = LoginActivity.this.usrEdt.getText().toString();
            String pwd = LoginActivity.this.pwdEdt.getText().toString();
            LoginActivity.this.loginPresenter.login(usr, pwd);
        }
    }

    private class ToRegister implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}
