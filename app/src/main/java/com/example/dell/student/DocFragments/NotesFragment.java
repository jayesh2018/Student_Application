package com.example.dell.student.DocFragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dell.student.DocAdapters.DocsAdapter;
import com.example.dell.student.Document_Info;
import com.example.dell.student.R;
import com.example.dell.student.constants.JApplication;
import com.example.dell.student.constants.MyBounceInterpolator;
import com.example.dell.student.room.StudentViewModel;
import com.example.dell.student.DocAdapters.JayeshViewHolder.RecyclerViewClickListener;

public class NotesFragment extends Fragment {

    //AppDatabase appDatabase;
    StudentViewModel studentViewModel;
    Document_Info[] docs;
    String subject;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notes, container, false);
        //appDatabase = AppDatabase.getAppDatabase(getContext());

        recyclerView = rootView.findViewById(R.id.notesrv);
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                switch (view.getId()) {
                    case R.id.imageButton:
                        Toast.makeText(getContext(), "Download Position " + position, Toast.LENGTH_SHORT).show();
                        JApplication.addDownloadURL(getContext(), Uri.parse(docs[position].getDownload_link()),docs[position].getDoc_name());
                        didTapButton(view);
                        break;
//                    case R.id.tDocname:
//                        Toast.makeText(getContext(), "Doc name Position " + position, Toast.LENGTH_SHORT).show();
//                        break;
                }
            }
        };
        final DocsAdapter docsAdapter = new DocsAdapter(getContext(),listener);
        recyclerView.setAdapter(docsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        subject = getArguments().getString("subject");
        studentViewModel.getTheseDoc("Notes", subject).observe(this, new Observer<Document_Info[]>() {
            @Override
            public void onChanged(@Nullable Document_Info[] document_infos) {
                docsAdapter.setDocs(document_infos);
                docs = document_infos;
                Log.i("tagg", "onChanged Called");
                Log.i("tagg", "document_infos.length " + document_infos.length);
            }
        });
        //docsAdapter.setDocs(studentViewModel.getTheseDoc("Notes",subject));

        //Document_Info[] document_info = appDatabase.subjectsDao().getTheseIT("Notes",subject);
        //recyclerView.setAdapter(new DocsAdapter(getActivity(), docs));
//        textView = rootView.findViewById(R.id.notess);
//        Log.i("tagg", " NotesFragment onCreateView");
//        if (getArguments() != null) {
//            subject = getArguments().getString("subject");
//            for (Document_Info document_info:appDatabase.subjectsDao().getTheseIT("Notes",subject)) {
//                temp += document_info.getDoc_name() + " "
//                        + document_info.getDate_added() + " "
//                        + document_info.getDownload_link() + "\n";
//            };
//
//        }
//        Log.i("tagg","temp" + temp);
//        textView.setText(temp);
        return rootView;
    }

    private void didTapButton(View view) {
        ImageButton button = (ImageButton) view;
        Animation myAnim = AnimationUtils.loadAnimation(getContext(),R.anim.bounce);
        MyBounceInterpolator myBounceInterpolator = new MyBounceInterpolator(0.2,20);
        myAnim.setInterpolator(myBounceInterpolator);
        button.startAnimation(myAnim);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
