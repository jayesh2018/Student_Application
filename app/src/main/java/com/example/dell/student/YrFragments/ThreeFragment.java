package com.example.dell.student.YrFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dell.student.Activities.Student_Activity;
import com.example.dell.student.R;

public class ThreeFragment extends Fragment {

    Button sem5, sem6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        sem5 = view.findViewById(R.id.sem5);
        sem6 = view.findViewById(R.id.sem6);

        sem5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Student_Activity) getActivity()).buttonClicked(5);
            }
        });
        sem6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Student_Activity) getActivity()).buttonClicked(6);
            }
        });
        return view;
    }

}