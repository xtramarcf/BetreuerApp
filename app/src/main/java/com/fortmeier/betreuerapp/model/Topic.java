package com.fortmeier.betreuerapp.model;

import java.io.Serializable;

public class Topic implements Serializable {

    private String areaOfSubject;
    private String subject;
    private String subjectDescription;
    private String tutorEMail;
    private String tutorName;

    public Topic(String areaOfSubject, String subject, String subjectDescription, String tutorEMail, String tutorName) {
        this.areaOfSubject = areaOfSubject;
        this.subject = subject;
        this.subjectDescription = subjectDescription;
        this.tutorEMail = tutorEMail;
        this.tutorName = tutorName;
    }

    public Topic() {
    }

    public String getAreaOfSubject() {
        return areaOfSubject;
    }

    public void setAreaOfSubject(String areaOfSubject) {
        this.areaOfSubject = areaOfSubject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public String getTutorEMail() {
        return tutorEMail;
    }

    public void setTutorEMail(String tutorEMail) {
        this.tutorEMail = tutorEMail;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }
}
