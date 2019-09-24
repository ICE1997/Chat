package com.chzu.ice.chat.activity.friends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.chzu.ice.chat.R;
import com.chzu.ice.chat.adapter.FriendsListAdpter;

public class FriendsActivity extends AppCompatActivity implements IFriendsView {
    private RecyclerView friendsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        registerComponents();
    }

    private void registerComponents() {
        friendsList = findViewById(R.id.friendsList);
        FriendsListAdpter friendsListAdpter = new FriendsListAdpter();
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        friendsList.setLayoutManager(llm);
        friendsList.setAdapter(friendsListAdpter);
    }
}
