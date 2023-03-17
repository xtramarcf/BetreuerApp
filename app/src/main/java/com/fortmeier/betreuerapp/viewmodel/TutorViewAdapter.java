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
import com.fortmeier.betreuerapp.model.Exam;

import java.util.ArrayList;
import java.util.List;

public class TutorViewAdapter extends ListAdapter<Exam, TutorViewAdapter.ExamHolder> {

    private OnItemCLickListener listener;
    private List<Exam> exams = new ArrayList<>();


    public TutorViewAdapter(){
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Exam> DIFF_CALLBACK = new DiffUtil.ItemCallback<Exam>() {
        @Override
        public boolean areItemsTheSame(@NonNull Exam oldItem, @NonNull Exam newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Exam oldItem, @NonNull Exam newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.exam_item, parent, false);
        return new ExamHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamHolder holder, int position) {
        Exam currentExam = getItem(position);

        showThemeInMultipleLines(holder, currentExam);
        holder.tvExamSubject.setText(currentExam.getSubject());
        holder.tvExamStudentName.setText(currentExam.getStudentName());
        holder.tvExamStudentEMail.setText(currentExam.getStudentEMail());
        holder.tvExamStatus.setText(currentExam.getStatus());
        holder.tvExamTutor.setText(currentExam.getTutorFullName());
        holder.tvExamSecondAssessor.setText(currentExam.getSecondAssessorName()+", "+currentExam.getSecondAssessorFirstName());
        holder.tvExamBillStatus.setText(currentExam.getBillStatus());
    }

    private void showThemeInMultipleLines(@NonNull ExamHolder holder, Exam currentExam) {
        holder.tvExamTheme1.setText(currentExam.getTheme());
    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    public void setExams(List<Exam> exams){
        this.exams = exams;
    }

    class ExamHolder extends RecyclerView.ViewHolder {

        private TextView tvExamTheme1;
        private TextView tvExamTheme2;
        private TextView tvExamTheme3;
        private TextView tvExamSubject;
        private TextView tvExamStudentName;
        private TextView tvExamStudentEMail;
        private TextView tvExamStatus;
        private TextView tvExamTutor;
        private TextView tvExamSecondAssessor;
        private TextView tvExamBillStatus;

        public ExamHolder(@NonNull View itemView) {
            super(itemView);
            tvExamTheme1 = itemView.findViewById(R.id.tv_exam_theme_1);
            tvExamSubject = itemView.findViewById(R.id.tv_exam_subject);
            tvExamStudentName = itemView.findViewById(R.id.tv_exam_student_name);
            tvExamStudentEMail = itemView.findViewById(R.id.tv_exam_student_email);
            tvExamStatus = itemView.findViewById(R.id.tv_exam_status);
            tvExamTutor = itemView.findViewById(R.id.tv_exam_tutor);
            tvExamSecondAssessor = itemView.findViewById(R.id.tv_exam_second_assessor);
            tvExamBillStatus = itemView.findViewById(R.id.tv_exam_bill);

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

    public interface OnItemCLickListener{
        void onItemClick(Exam exam);
    }

    public void setOnItemClickListener(OnItemCLickListener listener){
        this.listener = listener;
    }
}
