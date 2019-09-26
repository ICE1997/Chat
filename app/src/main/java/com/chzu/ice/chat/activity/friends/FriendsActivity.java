package com.chzu.ice.chat.activity.friends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.chat.R;
import com.chzu.ice.chat.activity.chat.ChatActivity;
import com.chzu.ice.chat.adapter.FriendsListAdapter;

public class FriendsActivity extends AppCompatActivity implements IFriendsContract.View {
    private static final String TAG = FriendsActivity.class.getSimpleName();
    private RecyclerView friendsList;
    private IFriendsContract.Presenter friendsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        registerComponents();
        new FriendsPresenter(this, new FriendsModel());
    }

    private void registerComponents() {
        friendsList = findViewById(R.id.friendsList);
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter(new FriendsListAdapter.ItemClickListener() {
            @Override
            public void onClick(View v, int i, String s) {
                Log.d(TAG, "onClick: you Clicked me!" + s);
                Intent intent = new Intent(FriendsActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        friendsList.setLayoutManager(llm);
        friendsList.setAdapter(friendsListAdapter);
    }

    @Override
    public void setPresenter(IFriendsContract.Presenter presenter) {
        this.friendsPresenter = presenter;
    }
}
