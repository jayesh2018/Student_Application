package com.example.dell.student.YrFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dell.student.Activities.Student_Activity;
import com.example.dell.student.R;

public class TwoFragment extends Fragment {

    Button sem3, sem4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        sem3 = view.findViewById(R.id.sem3);
        sem4 = view.findViewById(R.id.sem4);

        sem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Student_Activity) getActivity()).buttonClicked(3);
            }
        });
        sem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Student_Activity) getActivity()).buttonClicked(4);
            }
        });
        return view;
    }

}