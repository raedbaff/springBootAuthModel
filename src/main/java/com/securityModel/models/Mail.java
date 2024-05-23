package com.securityModel.models;

public class Mail {
    private String from;
    private String to;
    private String Subject;
    private String content;

    public Mail(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        Subject = subject;
        this.content = content;
    }

    public Mail() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
