package com.chzu.ice.chat.activity.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.R;
import com.chzu.ice.chat.activity.BaseActivity;
import com.chzu.ice.chat.adapter.ChatViewAdapter;
import com.chzu.ice.chat.config.AppConfig;
import com.chzu.ice.chat.config.MQTTConfig;
import com.chzu.ice.chat.pojo.mqtt.MTQQMessage;
import com.chzu.ice.chat.pojo.objectBox.FriendRelation;
import com.chzu.ice.chat.pojo.objectBox.FriendRelation_;
import com.chzu.ice.chat.pojo.objectBox.Message;
import com.chzu.ice.chat.pojo.objectBox.Message_;
import com.chzu.ice.chat.pojo.objectBox.UserAccount;
import com.chzu.ice.chat.pojo.objectBox.UserAccount_;
import com.chzu.ice.chat.utils.ObjectBoxHelper;
import com.chzu.ice.chat.utils.RSAUtil;
import com.chzu.ice.chat.utils.SPHelper;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.objectbox.Box;

public class ChatActivity extends BaseActivity implements IChatContract.View {
    private final String TAG = ChatActivity.class.getSimpleName();
    private RecyclerView chatView;
    private EditText input;
    private IChatContract.Presenter chatPresenter;
    private Box<Message> messageBox;
    private ChatViewAdapter chatViewAdapter;
    private Toolbar chatToolbar;
    private TextView chatTitle;
    private String friendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        registerComponents();
        registerInputListener();
        retrieveDataFromLastAct();
        registerBroadcastReceiver();
        new ChatPresenter(this, new ChatModel());
        initData();
        initSurface();
        new Handler().post(new Thread(this::exchangePublicKey));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void exchangePublicKey() {
        try {
            KeyPair keyPair = RSAUtil.getKeyPair();
            String publicKeyStr = RSAUtil.getPublicKey(keyPair);
            String privateKeyStr = RSAUtil.getPrivateKey(keyPair);
            Box<UserAccount> userAccountBox = ObjectBoxHelper.get().boxFor(UserAccount.class);
            UserAccount userAccount = userAccountBox.query().equal(UserAccount_.UName, ((App) getApplication()).getSignedInUsername()).build().findFirst();
            if (userAccount != null) {
                userAccount.setPrivateKey(privateKeyStr);
                userAccountBox.put(userAccount);
            }
            sendMessage(publicKeyStr, friendName, MTQQMessage.TYPE_PUBLIC_KEY);
        } catch (MqttException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private void retrieveDataFromLastAct() {
        this.friendName = getIntent().getStringExtra("f_name");
        Log.d(TAG, "retrieveDataFromLastAct: FriendName:" + friendName);
    }

    private void initData() {
        this.messageBox = ObjectBoxHelper.get().boxFor(Message.class);
        String usr = new SPHelper(getApplication(), AppConfig.SP_CONFIG_ADDRESS_LOGIN_INFO).getString(AppConfig.SP_CONFIG_KEY_SIGNED_IN_USER, "");
        List<Message> msgs = messageBox.query().equal(Message_.toU, usr).build().find();
        chatViewAdapter = new ChatViewAdapter(msgs);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        chatView.setLayoutManager(llm);
        chatView.setAdapter(chatViewAdapter);

        if (friendName != null) {
            this.chatTitle.setText(friendName);
        }
    }

    private void initSurface() {
        chatView.scrollToPosition((int) messageBox.count() - 1);
        setBackArrow(this.chatToolbar);
    }

    private void registerInputListener() {
        input.setOnKeyListener(new InputMSGListener());
    }

    private void registerComponents() {
        this.chatView = findViewById(R.id.chatView);
        this.input = findViewById(R.id.input);
        this.chatToolbar = findViewById(R.id.chatViewToolbar);
        this.chatTitle = findViewById(R.id.chatViewTitle);
    }

    private void registerBroadcastReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MQTTConfig.SIGNAL_RECEIVE_MESSAGE);
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Message message = new Message();
                message.setToU((((App) getApplication()).getSignedInUsername()));
                message.setFromU("1");
                message.setMsg(intent.getStringExtra(MQTTConfig.EXTRA_RECEIVE_MESSAGE_MESSAGE));
                chatViewAdapter.add(message);
                chatView.smoothScrollToPosition((int) messageBox.count());
            }
        }, intentFilter);
    }

    @Override
    public void showPublishSucceed() {
        this.input.setText("");
    }

    @Override
    public void showPublishFailed() {

    }

    @Override
    public void setPresenter(IChatContract.Presenter friendsPresenter) {
        this.chatPresenter = friendsPresenter;
    }

    @Override
    public void baseFinish() {
        this.finish();
    }

    private void sendMessage(String msg, String friendName, String type) throws MqttException {
        Box<FriendRelation> friendBox = ObjectBoxHelper.get().boxFor(FriendRelation.class);
        FriendRelation friendRelation = friendBox.query().equal(FriendRelation_.FName, friendName).build().findFirst();
        if (friendRelation != null) {
            MTQQMessage message = new MTQQMessage(friendRelation.getMName(), friendRelation.getFName(), type, msg);
            message.setSenderTopic(((App) getApplication()).getSignedInUserTopic());
            message.setReceiverTopic(friendRelation.getFTopic());
            chatPresenter.publish(message);
        } else {
            Log.e(TAG, "onKey: FriendRelation is NULL");
        }
    }

    private class InputMSGListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                Log.d(TAG, "onKey: " + input.getText());
                try {
                    String msgOrg = input.getText().toString();
                    Box<FriendRelation> friendBox = ObjectBoxHelper.get().boxFor(FriendRelation.class);
                    FriendRelation friendRelation = friendBox.query().equal(FriendRelation_.FName, friendName).build().findFirst();
                    if (friendRelation != null) {
                        String publicKeyStr = friendRelation.getPublicKey();
                        PublicKey publicKey = RSAUtil.string2PublicKey(publicKeyStr);
                        byte[] publicEncrypt = RSAUtil.publicEncrypt(msgOrg.getBytes(), publicKey);
                        String msgDecrypted = RSAUtil.byte2Base64(publicEncrypt);
                        sendMessage(msgDecrypted, friendName, MTQQMessage.TYPE_PERSON);
                    }

                } catch (MqttException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | InvalidKeySpecException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
    }
}
