package com.chzu.ice.chat.activity.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.chat.App;
import com.chzu.ice.chat.R;
import com.chzu.ice.chat.activity.BaseActivity;
import com.chzu.ice.chat.adapter.ChatViewAdapter;
import com.chzu.ice.chat.db.Friend;
import com.chzu.ice.chat.db.Friend_;
import com.chzu.ice.chat.db.Message;
import com.chzu.ice.chat.db.Message_;
import com.chzu.ice.chat.utils.ObjectBoxHelper;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Date;
import java.util.List;

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
    private String nameTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        retrieveDataFromLastAct();
        registerComponents();
        registerInputListener();
        registerBroadcastReceiver();
        new ChatPresenter(this, new ChatModel());
        initData();
        initSurface();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void retrieveDataFromLastAct() {
        this.nameTitle = getIntent().getStringExtra("nameTitle");
    }

    private void initData() {
        this.messageBox = ObjectBoxHelper.get().boxFor(Message.class);
        List<Message> msgs = messageBox.query().equal(Message_.toU,nameTitle).build().find();
        chatViewAdapter = new ChatViewAdapter(msgs);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        chatView.setLayoutManager(llm);
        chatView.setAdapter(chatViewAdapter);


        if (nameTitle != null) {
            this.chatTitle.setText(nameTitle);
        }
    }

    private void initSurface() {
        chatView.scrollToPosition((int) messageBox.count());
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
        intentFilter.addAction("newMessage");
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Message message = new Message();
                message.setFromU(((App)getApplication()).getCurrentUserName());
                message.setToU(nameTitle);
                message.setTimestamp(new Date().getTime());
                message.setMsg(intent.getStringExtra("msg"));
                messageBox.put(message);
                chatViewAdapter.add(message);
                chatView.smoothScrollToPosition((int) messageBox.count());
            }
        }, intentFilter);
    }

    @Override
    public void showPublishSucceed() {

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

    private class InputMSGListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                Log.d(TAG, "onKey: " + input.getText());
                try {
                    Box<Friend> friendBox = ObjectBoxHelper.get().boxFor(Friend.class);
                    Friend friend = friendBox.query().equal(Friend_.FName,nameTitle).build().findFirst();
                    if(friend!=null) {
                        chatPresenter.publish(input.getText().toString(),friend.getFTopic());
                    }
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }
    }
}
