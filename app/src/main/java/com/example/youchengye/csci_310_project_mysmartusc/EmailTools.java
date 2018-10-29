package com.example.youchengye.csci_310_project_mysmartusc;


import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailTools {
    public static final String from = "mysmartusc123@gmail.com";
    public static final String password = "smartusc123!";
    private static String TITLE_MARK = "Title Mark";
    private static String TITLE_IMPORTANT = "Title Important";
    private static String TITLE_STAR = "Title Star";
    private static String CONTENT_MARK = "Content Mark";
    private static String CONTENT_IMPORTANT = "Content Important";
    private static String CONTENT_STAR = "Content Star";
    private static String IMPORTANT_EMAIL = "mysmartusc123@gmail.com";
    public static Boolean controller = false;
    /**
     * important!!!!!!!!
     * change to tester's email address
     */
    public static String to = "ruoxijia@usc.edu";


    public static void sendEmail() {
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
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

            createMessage(session, TITLE_MARK, "should be mark as read");
            createMessage(session, TITLE_IMPORTANT, "should be notified");
            createMessage(session,TITLE_STAR,"should be starred");
            createMessage(session,"Content Mark as Read", CONTENT_MARK);
            createMessage(session, "Content Important", CONTENT_IMPORTANT);
            createMessage(session, "Content Star", CONTENT_STAR);
            createMessage(session, "Important Email Address", IMPORTANT_EMAIL);
//        }
    }

    private static void createMessage(Session session, String subject, String content){
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
