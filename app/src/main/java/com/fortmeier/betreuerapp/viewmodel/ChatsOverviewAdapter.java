package com.fortmeier.betreuerapp.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.fortmeier.betreuerapp.R;
import com.fortmeier.betreuerapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatsOverviewAdapter extends ListAdapter<User, ChatsOverviewAdapter.UserHolder> {

    private OnItemClickListener listener;
    private List<User> users = new ArrayList<>();

    public ChatsOverviewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getEMail() == newItem.getEMail();
        }

        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getEMail().equals(newItem.getEMail());
        }
    };


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatuser_item, parent, false);
        return new UserHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User currentUser = getItem(position);

        holder.tvFullName.setText(currentUser.getName() + ", " + currentUser.getFirstName());
        holder.tvLastMessage.setText(currentUser.getLastMessage());
        holder.tvTimeStamp.setText(currentUser.getTimeStamp());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;

    }

    public void setUser(User user){
        users.add(user);
    }

    class UserHolder extends RecyclerView.ViewHolder {

        private TextView tvFullName;
        private TextView tvLastMessage;
        private TextView tvTimeStamp;


        public UserHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tv_chatuser_fullName);
            tvLastMessage = itemView.findViewById(R.id.tv_last_message);
            tvTimeStamp = itemView.findViewById(R.id.tv_timeStamp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
