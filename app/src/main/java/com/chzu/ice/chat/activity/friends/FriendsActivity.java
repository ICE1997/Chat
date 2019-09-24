package com.chzu.ice.chat.activity.friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.chzu.ice.chat.R;
import com.chzu.ice.chat.activity.chat.ChatActivity;
import com.chzu.ice.chat.adapter.FriendsListAdapter;

public class FriendsActivity extends AppCompatActivity implements IFriendsContract.View {
    private RecyclerView friendsList;
    private IFriendsContract.Presenter friendsPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        registerComponents();
        new FriendsPresenter(this,new FriendsModel());
        Intent intent = new Intent(this,ChatActivity.class);
        startActivity(intent);
    }

    private void registerComponents() {
        friendsList = findViewById(R.id.friendsList);
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter();
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        friendsList.setLayoutManager(llm);
        friendsList.setAdapter(friendsListAdapter);
    }

    @Override
    public void setPresenter(IFriendsContract.Presenter presenter) {
        this.friendsPresenter = presenter;
    }
}
