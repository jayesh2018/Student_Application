package com.example.dell.student.YrFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dell.student.Activities.Student_Activity;
import com.example.dell.student.R;

public class OneFragment extends Fragment {

    Button sem1, sem2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        sem1 = view.findViewById(R.id.sem1);
        sem2 = view.findViewById(R.id.sem2);

        sem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Student_Activity) getActivity()).buttonClicked(1);
            }
        });
        sem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Student_Activity) getActivity()).buttonClicked(2);
            }
        });
        return view;
    }

}

