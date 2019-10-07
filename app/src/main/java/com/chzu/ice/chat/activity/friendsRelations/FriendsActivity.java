package com.chzu.ice.chat.activity.friendsRelations;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.R;
import com.chzu.ice.chat.activity.chat.ChatActivity;
import com.chzu.ice.chat.adapter.FriendsListAdapter;
import com.chzu.ice.chat.config.AppConfig;
import com.chzu.ice.chat.config.MQTTConfig;
import com.chzu.ice.chat.pojo.objectBox.FriendRelation;
import com.chzu.ice.chat.utils.SPHelper;
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
    private FrameLayout QRCodeBcg;
    private ImageView QRCodeImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        registerComponents();
        registerListener();
        registerConnectSucceedSucceedSignalReceiver();
        new FriendsPresenter(this, new FriendsModel());
        initData();
        initSurface();

        Intent connect = new Intent(MQTTConfig.SIGNAL_CONNECT);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(connect);

    }

    private void registerComponents() {
        friendsList = findViewById(R.id.friendsList);
        friendsToolbar = findViewById(R.id.friendsToolbar);
        QRCodeBcg = findViewById(R.id.qrcodeBcg);
        QRCodeImg = findViewById(R.id.qrcodeImg);
    }

    private void registerConnectSucceedSucceedSignalReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplication());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_CONNECT_SUCCEED);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent subscribe = new Intent(MQTTConfig.SIGNAL_SUBSCRIBE);
                subscribe.putExtra(MQTTConfig.EXTRA_SUBSCRIBE_TOPIC, ((App) getApplication()).getSignedInUserTopic());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(subscribe);
            }
        }, intentFilter);
    }

    private void initData() {
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter();
        friendsListAdapter.setItemClickListener(new FriendsListAdapter.ItemClickListener() {
            @Override
            public void onClick(View v, int i, String s) {
                Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                intent.putExtra("f_name", s);
                startActivity(intent);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        friendsList.setLayoutManager(llm);

        final Handler handler = new LoadFriendsHandler(friendsListAdapter, friendsList);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FriendRelation> relations = friendsPresenter.loadAllFriends();
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = relations;
                handler.sendMessage(message);
            }
        }).start();

    }

    private void initSurface() {
        setSupportActionBar(friendsToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void registerListener() {
        QRCodeBcg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeBcg.setVisibility(View.GONE);
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
            QRCodeBcg.setVisibility(View.VISIBLE);
            try {
                String usr = new SPHelper(getApplication(), AppConfig.SP_CONFIG_ADDRESS_LOGIN_INFO).getString(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER, "");
                QRCodeImg.setImageBitmap(friendsPresenter.generateQRCode(usr));
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
//                friendsPresenter.addFriend(((App) getApplication()).getCurrentUserName(), result.getContents());
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

    private static class LoadFriendsHandler extends Handler {
        private FriendsListAdapter adapter;
        private RecyclerView list;

        private LoadFriendsHandler(FriendsListAdapter adapter, RecyclerView list) {
            this.adapter = adapter;
            this.list = list;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                @SuppressWarnings("unchecked")
                List<FriendRelation> relations = (List<FriendRelation>) msg.obj;
                for (FriendRelation r : relations
                ) {
                    System.out.println(r.getFName());
                }
                adapter.setFriendRelations(relations);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
