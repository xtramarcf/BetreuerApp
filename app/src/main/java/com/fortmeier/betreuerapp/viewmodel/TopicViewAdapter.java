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
import com.fortmeier.betreuerapp.model.Topic;
import com.fortmeier.betreuerapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class TopicViewAdapter extends ListAdapter<Topic, TopicViewAdapter.TopicHolder> {

    private OnItemClickListener listener;
    private List<Topic> topics = new ArrayList<>();

    public TopicViewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Topic> DIFF_CALLBACK = new DiffUtil.ItemCallback<Topic>() {
        @Override
        public boolean areItemsTheSame(@NonNull Topic oldItem, @NonNull Topic newItem) {
            return oldItem.getTutorEMail() == newItem.getTutorEMail();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Topic oldItem, @NonNull Topic newItem) {
            return oldItem.getTutorEMail().equals(newItem.getTutorEMail());
        }
    };


    @NonNull
    @Override
    public TopicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topic_item, parent, false);
        return new TopicHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull TopicHolder holder, int position) {
        Topic currentTopic = getItem(position);

        holder.tvAreaOfSubject.setText(currentTopic.getAreaOfSubject());
        holder.tvSubject.setText(currentTopic.getSubject());
        holder.tvDescription.setText(currentTopic.getSubjectDescription());
        holder.tvTutorName.setText(currentTopic.getTutorName());
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    class TopicHolder extends RecyclerView.ViewHolder {

        private TextView tvAreaOfSubject;
        private TextView tvSubject;
        private TextView tvDescription;
        private TextView tvTutorName;

        public TopicHolder(@NonNull View itemView) {
            super(itemView);
            tvAreaOfSubject = itemView.findViewById(R.id.tv_topic_area_of_subject);
            tvSubject = itemView.findViewById(R.id.tv_topic_subject);
            tvDescription = itemView.findViewById(R.id.tv_topic_description);
            tvTutorName = itemView.findViewById(R.id.tv_topic_full_name_tutor);



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
        void onItemClick(Topic topic);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
