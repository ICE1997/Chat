package com.chzu.ice.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.chat.R;
import com.chzu.ice.chat.db.Friend;

import java.util.List;


public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListHolder> {
    private List<Friend> friends;
    private ItemClickListener itemClickListener;

    public FriendsListAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_list_card,parent,false);
        return new FriendsListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendsListHolder holder, final int position) {
        holder.textView.setText(friends.get(position).getFName());
        if(itemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(v,position,friends.get(position).getFName());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class FriendsListHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        FriendsListHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
        }
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void onClick(View v,int position,String s);
    }
}
