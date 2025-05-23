package com.example.oneweekenglish.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oneweekenglish.R;
import com.example.oneweekenglish.model.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 2;
    private ArrayList<Message> messageList;

    public MessageAdapter(ArrayList<Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isBot() ? VIEW_TYPE_BOT : VIEW_TYPE_USER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_message, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_bot_message, parent, false);
            return new BotMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(message);
        } else {
            ((BotMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ViewHolder cho tin nhắn của người dùng
    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserMessage;

        UserMessageViewHolder(View itemView) {
            super(itemView);
            textViewUserMessage = itemView.findViewById(R.id.textViewUserMessage);
        }

        void bind(Message message) {
            textViewUserMessage.setText(message.getContent());
        }
    }

    // ViewHolder cho tin nhắn của bot
    static class BotMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBotMessage;

        BotMessageViewHolder(View itemView) {
            super(itemView);
            textViewBotMessage = itemView.findViewById(R.id.textViewBotMessage);
        }

        void bind(Message message) {
            textViewBotMessage.setText(message.getContent());
        }
    }
}