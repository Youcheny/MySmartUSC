package com.example.youchengye.csci_310_project_mysmartusc;


// This class is dedicated for the Gmail API
public class Header {
    public String from;
    public String subject;
    public String snippet;
    public String messageId;

    public Header(String from, String subject, String snippet, String messageId) {
        this.from = from;
        this.subject = subject;
        this.snippet = snippet;
        this.messageId = messageId;
    }
}