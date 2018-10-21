package com.example.youchengye.csci_310_project_mysmartusc;


// This class is dedicated for the Gmail API
public class Header {
    public String from;
    public String subject;
    public String snippet;
    public String messageId;
    public String content;

    public Header(String from, String subject, String snippet, String messageId, String content) {
        this.from = from;
        this.subject = subject;
        this.snippet = snippet;
        this.messageId = messageId;
        this.content = content;
    }


    public String toString(){
        super.toString();
        return("from "+ from +"\n" +
                        "subject " + subject + "\n"+
                        "snippet " + snippet +"\n" +
                        "messageid " + messageId +"\n" +
                        "content " + content +"\n\n" );
    }
}