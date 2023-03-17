package com.fortmeier.betreuerapp.viewmodel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fortmeier.betreuerapp.R;
import com.fortmeier.betreuerapp.model.ChatMessage;

import java.util.ArrayList;

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ChatMessage> list;

    public ChatViewAdapter(Context context, ArrayList<ChatMessage> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(list.get(position).getUserEmail());
        holder.message.setText(list.get(position).getMessage());
        holder.dateTime.setText(list.get(position).getDateTime());
    }

    public void addMessage(ChatMessage msg){
        list.add(0, msg);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username, message, dateTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.tv_msg_userEmail);
            message = itemView.findViewById(R.id.tv_msg_message);
            dateTime = itemView.findViewById(R.id.tv_msg_dateTime);
        }
    }
}
