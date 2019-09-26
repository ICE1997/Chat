package com.chzu.ice.chat.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chzu.ice.chat.R;
import com.chzu.ice.chat.db.Message;

import java.util.List;

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ChatViewHolder> {
    private static final String TAG = ChatViewAdapter.class.getSimpleName() ;
    private List<Message> msgs;

    public ChatViewAdapter(List<Message> msgs) {
        if (msgs != null) {
            this.msgs = msgs;
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        if (this.msgs != null) {
            holder.textView.setText(msgs.get(position).getMsg());
        }
    }

    @Override
    public int getItemCount() {
        return msgs == null ? 0 : msgs.size();
    }

    public void add(Message msg) {
        if(msgs != null) {
            this.msgs.add(msg);
            notifyDataSetChanged();
        }
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.test);
        }
    }
}
