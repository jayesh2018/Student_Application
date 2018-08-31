package com.example.dell.student;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    String[] subjects;

    public SubjectAdapter(String[] subjects) {
        this.subjects = subjects;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@Nullable ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.subject, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final String title = subjects[position];
        Log.i("tagg", "onBindViewHolder");
        holder.subjectbtn.setText(title);
    }

    @Override
    public int getItemCount() {
        return subjects.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button subjectbtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            subjectbtn = itemView.findViewById(R.id.subjectbtn);
        }
    }
}
