package com.example.dell.student.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "it_subjects")
public class IT_Subjects {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "subject")
    private String subject;

    @ColumnInfo(name = "year")
    private String year;

    @ColumnInfo(name = "semester")
    private String semester;

    public IT_Subjects(String subject, String year, String semester) {
        this.subject = subject;
        this.year = year;
        this.semester = semester;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

}
