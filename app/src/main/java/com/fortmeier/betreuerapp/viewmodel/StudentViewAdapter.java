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

public class StudentViewAdapter extends ListAdapter<User, StudentViewAdapter.TutorHolder> {

    private OnItemClickListener listener;
    private List<User> tutors = new ArrayList<>();

    public StudentViewAdapter() {
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
    public TutorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tutor_item, parent, false);
        return new TutorHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull TutorHolder holder, int position) {
        User currentTutor = getItem(position);

        holder.tvFullName.setText(currentTutor.getName() + ", " + currentTutor.getFirstName());
        holder.tvEmail.setText(currentTutor.getEMail());
        holder.tvExpertises.setText(currentTutor.getExpertises());
    }

    @Override
    public int getItemCount() {
        return tutors.size();
    }

    public void setTutors(List<User> tutors) {
        this.tutors = tutors;
    }

    class TutorHolder extends RecyclerView.ViewHolder {

        private TextView tvFullName;
        private TextView tvEmail;
        private TextView tvExpertises;

        public TutorHolder(@NonNull View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tv_tutor_fullName);
            tvExpertises = itemView.findViewById(R.id.tv_tutor_expertises);
            tvEmail = itemView.findViewById(R.id.tv_tutor_email);

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
        void onItemClick(User tutor);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
