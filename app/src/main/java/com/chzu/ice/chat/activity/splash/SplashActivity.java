package com.chzu.ice.chat.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.chzu.ice.chat.R;
import com.chzu.ice.chat.activity.friendsRelations.FriendsActivity;
import com.chzu.ice.chat.activity.login.LoginActivity;

public class SplashActivity extends AppCompatActivity implements ISplashContract.View {
    ISplashContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new SplashPresenter(this);
        init();
    }

    private void init() {
        if (!presenter.isFirstOpen() && presenter.hasSignedIn()) {
            Intent intent = new Intent(this, FriendsActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        this.finish();
    }

    @Override
    public void setPresenter(ISplashContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
