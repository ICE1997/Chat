package com.chzu.ice.chat.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.chat.R;
import com.chzu.ice.chat.activity.friends.FriendsActivity;
import com.chzu.ice.chat.activity.login.LoginActivity;
import com.chzu.ice.chat.adapter.ChatViewAdapter;
import com.chzu.ice.chat.db.Message;
import com.chzu.ice.chat.utils.ObjectBoxHelper;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

import io.objectbox.Box;

public class MainActivity extends AppCompatActivity implements IMainView {
    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView chatView;
    private EditText input;
    private IMainPresenter mainPresenter;
    private Box<Message> messageBox;
    private ChatViewAdapter chatViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            this.mainPresenter = new MainPresenter(this);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        registerComponents();
        initData();

        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);

        chatView.smoothScrollToPosition((int) messageBox.count());

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("newMessage");
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Message message = new Message();
                message.setMsg(intent.getStringExtra("msg"));
                messageBox.put(message);
                chatViewAdapter.add(message);
                chatView.smoothScrollToPosition((int) messageBox.count());
            }
        },intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initData() {
        this.messageBox = ObjectBoxHelper.get().boxFor(Message.class);
        List<Message> msgs = messageBox.getAll();
        chatViewAdapter = new ChatViewAdapter(msgs);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        chatView.setLayoutManager(llm);
        chatView.setAdapter(chatViewAdapter);

        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "onKey: " + input.getText());
                    try {
                        mainPresenter.connect();
                        mainPresenter.publish(input.getText().toString());
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void registerComponents() {
        this.chatView = findViewById(R.id.chatView);
        this.input = findViewById(R.id.input);
    }

    @Override
    public void showPublishSucceed() {

    }

    @Override
    public void showPublishFailed() {

    }

    @Override
    public void showConnectSucceed() {

    }

    @Override
    public void showConncectFailed() {

    }
}
