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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.student.DocAdapters.JayeshViewHolder;
import com.example.dell.student.DocAdapters.DocsAdapter;
import com.example.dell.student.Document_Info;
import com.example.dell.student.R;
import com.example.dell.student.constants.JApplication;
import com.example.dell.student.constants.MyBounceInterpolator;
import com.example.dell.student.room.AppDatabase;
import com.example.dell.student.room.StudentViewModel;

public class OtherFragment extends Fragment {


    AppDatabase appDatabase;
    TextView textView;
    String subject, temp = " ";
    RecyclerView recyclerView;
    StudentViewModel studentViewModel;
    Document_Info[] docs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("tagg", " QstnFragment onCreate");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_others, container, false);

        recyclerView = rootView.findViewById(R.id.othersrv);
        JayeshViewHolder.RecyclerViewClickListener listener = new JayeshViewHolder.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

                switch (view.getId()) {
                    case R.id.imageButton:
                        Toast.makeText(getContext(), "Download Position " + position, Toast.LENGTH_SHORT).show();
                        JApplication.addDownloadURL(getContext(), Uri.parse(docs[position].getDownload_link()),docs[position].getDoc_name());
                        didTapButton(view);
                        break;
                }


            }
        };
        final DocsAdapter docsAdapter = new DocsAdapter(getContext(),listener);
        recyclerView.setAdapter(docsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        subject = getArguments().getString("subject");
        studentViewModel.getTheseDoc("Others", subject).observe(this, new Observer<Document_Info[]>() {
            @Override
            public void onChanged(@Nullable Document_Info[] document_infos) {
                docsAdapter.setDocs(document_infos);
                docs = document_infos;
                Log.i("tagg", "onChanged Called");
                Log.i("tagg", "document_infos.length " + document_infos.length);
            }
        });
        return rootView;
    }

    private void didTapButton(View view) {
        ImageButton button = (ImageButton) view;
        Animation myAnim = AnimationUtils.loadAnimation(getContext(),R.anim.bounce);
        MyBounceInterpolator myBounceInterpolator = new MyBounceInterpolator(0.2,20);
        myAnim.setInterpolator(myBounceInterpolator);
        button.startAnimation(myAnim);
    }
}
