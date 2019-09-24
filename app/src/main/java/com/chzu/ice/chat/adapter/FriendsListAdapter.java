package com.chzu.ice.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.chat.R;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListHolder> {
    private String[] names = {"张三","李四","王五","你猜","张三","李四","王五","你猜","张三","李四","王五","你猜","张三","李四","王五","你猜"};
    @NonNull
    @Override
    public FriendsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_card,parent,false);
        return new FriendsListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListHolder holder, int position) {
        holder.textView.setText(names[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    class FriendsListHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        FriendsListHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
        }
    }
}
