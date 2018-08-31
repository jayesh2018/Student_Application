package com.example.dell.student.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "cs_doc_details")
public class CS_Doc {

    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "document_name")
    private String document_name;

    @ColumnInfo(name = "document_type")
    private String document_type;

    @ColumnInfo(name = "date_added")
    private String date_added;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "download_link")
    private String download_link;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }


    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getDownload_link() {
        return download_link;
    }

    public void setDownload_link(String download_link) {
        this.download_link = download_link;
    }
}
