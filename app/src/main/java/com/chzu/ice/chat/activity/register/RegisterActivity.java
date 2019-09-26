package com.chzu.ice.chat.activity.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chzu.ice.chat.R;
import com.chzu.ice.chat.utils.ToastHelper;

public class RegisterActivity extends AppCompatActivity implements IRegisterContract.View {
    private IRegisterContract.Presenter presenter;
    private Button registerBtn;
    private EditText usrnameEdt;
    private EditText pwdEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerComponents();
        registerListener();
        new RegisterPresenter(this, new RegisterModel());
    }

    @Override
    public void setPresenter(IRegisterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void registerComponents() {
        this.registerBtn = findViewById(R.id.registerBtn);
        this.usrnameEdt = findViewById(R.id.usrEdt);
        this.pwdEdt = findViewById(R.id.pwdEdt);
    }

    private void registerListener() {
        registerBtn.setOnClickListener(new RegisterBtnClickedListener());
    }

    @Override
    public void showRegisterSucceed() {
        ToastHelper.showToast(this,"注册成功!", Toast.LENGTH_SHORT);
    }

    @Override
    public void showAlreadyRegistered() {
        ToastHelper.showToast(this,"注册失败，用户已存在!", Toast.LENGTH_SHORT);
    }

    class RegisterBtnClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String usr = usrnameEdt.getText().toString();
            String pwd = pwdEdt.getText().toString();
            presenter.register(usr,pwd);
        }
    }
}
