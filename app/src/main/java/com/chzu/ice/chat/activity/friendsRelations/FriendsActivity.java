package com.chzu.ice.chat.activity.friendsRelations;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.R;
import com.chzu.ice.chat.activity.chat.ChatActivity;
import com.chzu.ice.chat.adapter.FriendsListAdapter;
import com.chzu.ice.chat.pojo.objectBox.FriendRelation;
import com.chzu.ice.chat.utils.ToastHelper;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class FriendsActivity extends AppCompatActivity implements IFriendsContract.View {
    private static final String TAG = FriendsActivity.class.getSimpleName();
    private RecyclerView friendsList;
    private IFriendsContract.Presenter friendsPresenter;
    private Toolbar friendsToolbar;
    private FrameLayout qrcodeBcg;
    private ImageView qrcodeImg;
    private String usr;
    private FriendsListAdapter friendsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        registerComponents();
        registerListener();
        new FriendsPresenter(this, new FriendsModel());
        initData();
        initSurface();
    }

    private void registerComponents() {
        friendsList = findViewById(R.id.friendsList);
        friendsToolbar = findViewById(R.id.friendsToolbar);
        qrcodeBcg = findViewById(R.id.qrcodeBcg);
        qrcodeImg = findViewById(R.id.qrcodeImg);
    }

    private void initData() {
        this.usr = ((App) getApplication()).getCurrentUserName();
        this.friendsListAdapter = new FriendsListAdapter();
        friendsListAdapter.setItemClickListener(new FriendsListAdapter.ItemClickListener() {
            @Override
            public void onClick(View v, int i, String s) {
                Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                intent.putExtra("nameTitle", s);
                startActivity(intent);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        friendsList.setLayoutManager(llm);

        friendsPresenter.loadAllFriends(usr, new FriendsModel.LoadFriendCallback() {
            @Override
            public void onCompleted(List<FriendRelation> relations) {
                friendsListAdapter.setFriendRelations(relations);
            }
        });

        friendsList.setAdapter(friendsListAdapter);
    }

    private void initSurface() {
        setSupportActionBar(friendsToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void registerListener() {
        qrcodeBcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrcodeBcg.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friends_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_add_friend) {
            new IntentIntegrator(this).initiateScan();
            return true;
        } else if (item.getItemId() == R.id.action_show_my_QRCode) {
            qrcodeBcg.setVisibility(View.VISIBLE);
            try {
                qrcodeImg.setImageBitmap(friendsPresenter.generateQRCode(((App) getApplication()).getCurrentUserName()));
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(IFriendsContract.Presenter presenter) {
        this.friendsPresenter = presenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                ToastHelper.showToast(this, "已取消", Toast.LENGTH_SHORT);
            } else {
                ToastHelper.showToast(this, "获取到的内容:" + result.getContents(), Toast.LENGTH_LONG);
                friendsPresenter.addFriend(((App) getApplication()).getCurrentUserName(), result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showNoSuchUser() {
        ToastHelper.showToast(this, "无此用户", Toast.LENGTH_SHORT);
    }

    @Override
    public void showHasAlreadyAdded() {
        ToastHelper.showToast(this, "不能重复添加好友", Toast.LENGTH_SHORT);
    }

    @Override
    public void showAddFriendSucceed() {
        ToastHelper.showToast(this, "添加好友成功", Toast.LENGTH_SHORT);
    }

    @Override
    public void showLoadSucceed(List<FriendRelation> relations) {

    }
}
