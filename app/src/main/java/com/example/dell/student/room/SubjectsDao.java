package com.example.dell.student.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.dell.student.Document_Info;

@Dao
public interface SubjectsDao {

    @Query("SELECT COUNT(*) FROM it_subjects")
    int getITRowsno();

    @Query("SELECT COUNT(*) FROM cs_subjects")
    int getCSRowsno();

    @Query("SELECT subject FROM it_subjects WHERE semester = :sem")
    String[] getITSubjects(int sem);

    @Query("SELECT subject FROM cs_subjects WHERE semester = :sem")
    String[] getCSSubjects(int sem);

    @Insert
    void insertITSubjects(IT_Subjects... itSubjects);

    @Insert
    void insertCSSubjects(CS_Subjects... csSubjects);

    @Insert
    void insertITDocs(IT_Doc... it_docs);

    @Insert
    void insertCSDocs(CS_Doc... cs_docs);

    @Query("SELECT * FROM it_doc_details")
    LiveData<IT_Doc[]> getITDocs();

    @Query("SELECT * FROM cs_doc_details")
    LiveData<CS_Doc[]> getCSDocs();

    @Query("SELECT MAX(date_added) from it_doc_details")
    String getITTimestamp();

    @Query("SELECT MAX(date_added) from cs_doc_details")
    String getCSTimestamp();

    @Query("SELECT document_name,date_added,download_link FROM it_doc_details WHERE document_type = :doc_type AND subject = :subject UNION SELECT document_name,date_added,download_link FROM cs_doc_details WHERE document_type = :doc_type AND subject = :subject")
    LiveData<Document_Info[]> getTheseDoc(String doc_type, String subject);

}
