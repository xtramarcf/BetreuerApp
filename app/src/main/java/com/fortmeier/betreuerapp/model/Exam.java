package com.fortmeier.betreuerapp.model;


import java.io.Serializable;

public class Exam implements Serializable {

    private String theme;
    private String subject;
    private String studentName;
    private String studentEMail;
    private String status;
    private String secondAssessorName;
    private String secondAssessorFirstName;
    private String billStatus;
    private String tutorEMail;
    private String tutorFullName;
    private String id;

    public Exam() {
    }

    public Exam(String theme, String subject, String studentName, String studentEMail, String status, String secondAssessorName, String secondAssessorFirstName, String billStatus, String tutorEMail, String tutorFullName) {
        this.theme = theme;
        this.subject = subject;
        this.studentName = studentName;
        this.studentEMail = studentEMail;
        this.status = status;
        this.secondAssessorName = secondAssessorName;
        this.secondAssessorFirstName = secondAssessorFirstName;
        this.billStatus = billStatus;
        this.tutorEMail = tutorEMail;
        this.tutorFullName = tutorFullName;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEMail() {
        return studentEMail;
    }

    public void setStudentEMail(String studentEMail) {
        this.studentEMail = studentEMail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSecondAssessorName() {
        return secondAssessorName;
    }

    public void setSecondAssessorName(String secondAssessorName) {
        this.secondAssessorName = secondAssessorName;
    }

    public String getSecondAssessorFirstName() {
        return secondAssessorFirstName;
    }

    public void setSecondAssessorFirstName(String secondAssessorFirstName) {
        this.secondAssessorFirstName = secondAssessorFirstName;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getTutorEMail() {
        return tutorEMail;
    }

    public void setTutorEMail(String tutorEMail) {
        this.tutorEMail = tutorEMail;
    }

    public String getTutorFullName() {
        return tutorFullName;
    }

    public void setTutorFullName(String tutorFullName) {
        this.tutorFullName = tutorFullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
