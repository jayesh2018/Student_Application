package com.example.dell.student;

public class Document_Info {

    String document_name, date_added, download_link;

    public String getDoc_name() {
        return document_name;
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

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }
}
