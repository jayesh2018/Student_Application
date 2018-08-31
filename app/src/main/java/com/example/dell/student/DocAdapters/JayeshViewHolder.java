package com.example.dell.student.DocAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dell.student.R;

public class JayeshViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView docname, dateadded;
    ImageButton downloadbtn;
    private RecyclerViewClickListener mListener;

    public JayeshViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        docname = itemView.findViewById(R.id.tDocname);
        dateadded = itemView.findViewById(R.id.tDocDate);
        downloadbtn = itemView.findViewById(R.id.imageButton);
        mListener = listener;
        downloadbtn.setOnClickListener(this);
        dateadded.setOnClickListener(this);
        docname.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mListener.onClick(view, getAdapterPosition());
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
}
