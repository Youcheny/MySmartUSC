package com.example.youchengye.csci_310_project_mysmartusc;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailTools {
    public static final String password = "smartusc123!";
    private static String TITLE_MARK = "Title Mark";
    private static String TITLE_IMPORTANT = "Title Important";
    private static String TITLE_STAR = "Title Star";
    private static String CONTENT_MARK = "Content Mark";
    private static String CONTENT_IMPORTANT = "Content Important";
    private static String CONTENT_STAR = "Content Star";
    private static String IMPORTANT_EMAIL = "mysmartusc123@gmail.com";
    public static Boolean controller = false;


    public static void sendEmail(String from, String to) {
        final String FROM = from;
        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //get Session
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM, password);
                    }
                });

            createMessage(session, TITLE_MARK, "should be mark as read", from, to);
            createMessage(session, TITLE_IMPORTANT, "should be notified", from, to);
            createMessage(session,TITLE_STAR,"should be starred", from,to);
            createMessage(session,"Content Mark as Read", CONTENT_MARK, from, to);
            createMessage(session, "Content Important", CONTENT_IMPORTANT, from, to);
            createMessage(session, "Content Star", CONTENT_STAR, from, to);
            createMessage(session, "Important Email Address", IMPORTANT_EMAIL, from, to);
//        }

    }
    private static void createMessage(Session session, String subject, String content,String from, String to){
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(content, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }


}
