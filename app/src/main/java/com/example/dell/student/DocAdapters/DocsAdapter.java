package com.example.dell.student.DocAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dell.student.Document_Info;
import com.example.dell.student.R;
import com.example.dell.student.DocAdapters.JayeshViewHolder.RecyclerViewClickListener;

public class DocsAdapter extends RecyclerView.Adapter<JayeshViewHolder> {

    private final Context context;
    private final LayoutInflater mInflater;
    private Document_Info data[], document_info;
    private RecyclerViewClickListener mListener;

    public DocsAdapter(Context context, RecyclerViewClickListener listener) {
        this.context = context;
        mListener = listener;
        mInflater = LayoutInflater.from(context);

        Log.i("tagg", "Constructor  ");
    }

    @NonNull
    @Override
    public JayeshViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.doc_item, parent, false);
        return new JayeshViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull JayeshViewHolder holder, int position) {
        if (data != null) {
            document_info = data[position];
            holder.docname.setText(document_info.getDoc_name());
            Log.i("tagg", "position");
            Log.i("tagg", "Myadapter" + document_info.getDoc_name());
            holder.dateadded.setText(document_info.getDate_added());
//            holder.downloadbtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //Log.i("tagg", "" + get);
//
//                    Toast.makeText(context, "Download Clicked", Toast.LENGTH_SHORT).show();
//                    //JApplication.addDownloadURL(context,Uri.parse(document_info.getDownload_link()),document_info.getDoc_name());
//                }
//            });
        } else {
            holder.docname.setText("No Documents Uploaded Yet");
            Log.i("tagg", "No Documents Uploaded Yet");
        }
    }

    @Override
    public int getItemCount() {
        //Log.i("tagg","data.length  " + data.length);
        if (data != null)
            return data.length;
        else return 0;
    }

    public void setDocs(Document_Info[] docs) {
        data = docs;
        notifyDataSetChanged();
        //Log.i("tagg","data = " + data[0].getDoc_name());
    }
}
