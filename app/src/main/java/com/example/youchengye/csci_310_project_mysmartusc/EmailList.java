package com.example.youchengye.csci_310_project_mysmartusc;

import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.model.ModifyMessageRequest;

import java.io.ByteArrayInputStream;

import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.jsoup.Jsoup;
public class EmailList {
    private static final String TAG = "EmailList";
    private GoogleCredential credential;
    private Gmail service;
    private String userId;
    private String accessToken;
    private static EmailList singleInstance;

    private EmailList(){
        this.credential = null;
        this.service = null;
        this.accessToken = null;
        this.userId = "me";
    }

    public void initialize(String accessToken) throws IOException, MessagingException {
        this.accessToken = accessToken;
        this.credential = new GoogleCredential().setAccessToken(accessToken);
        this.service = new Gmail.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("MySmartUSC").build();

        try {
            List<Header> headers = EmailList.getInstance().listMessages();
            for(Header header:headers){
//                Log.i(TAG, "subject: "+header.subject);
//                Log.i(TAG, "from: "+header.from);
//                Log.i(TAG, "snippet: "+header.snippet);
//                Log.i(TAG, "messageId: "+header.messageId);
//                Log.i(TAG, "content: "+header.content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id){
        this.userId = id;
    }

    public static EmailList getInstance(){
        if(singleInstance!=null){
            return singleInstance;
        }
        else{
            singleInstance = new EmailList();
            return singleInstance;
        }
    }


    /**
     * get 8 newest emails
     * @return a hashmap of messageId and header
     * header includes from subject messageId content and snippet
     * @throws IOException throw ioexception
     * @throws MessagingException throw messageexception
     */
    public List<Header> listMessages() throws IOException, MessagingException {

        ListMessagesResponse response = singleInstance.service.users().messages().list(singleInstance.userId).setMaxResults((long) 8).execute();
        List<Message> messages = new ArrayList<>();



        if (response.getMessages() != null) {
            messages.addAll(response.getMessages());
        }

        List<Header> headers = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String messageId = messages.get(i).getId();
            Message message = getMessage(messageId);

            MimeMessage mimeMessage = getMimeMessage(messageId);
            String content = getTextFromMessage(mimeMessage);

            int size = message.getPayload().getHeaders().size();
            String from = null;
            String subject = null;
            String snippet = message.getSnippet();

            for (int j = 0; j < size; j++) {
                String temp = message.getPayload().getHeaders().get(j).getName();
                if (temp.equals("From")) {
                    from = message.getPayload().getHeaders().get(j).getValue();
                }
                else if (temp.equals("Subject")) {
                    subject = message.getPayload().getHeaders().get(j).getValue();
                }
            }
            headers.add( new Header(from, subject, snippet, messageId,content));
        }

        return headers;
    }

    /**
     * function called by listEmails to get each email according to messageId
     * @param messageId id of each message
     * @return a email message
     * @throws IOException
     */
    public Message getMessage(String messageId) throws IOException {
        List<String> metadataHeaders = new ArrayList<>();
        metadataHeaders.add("From");
        metadataHeaders.add("Subject");
        Message message = singleInstance.service.users().messages().get(singleInstance.userId, messageId).execute();
        return message;
    }


    private MimeMessage getMimeMessage(String messageId)
            throws IOException, MessagingException {
        Message message = singleInstance.service.users().messages().get(userId, messageId).setFormat("raw").execute();
        //parse the return message
        Base64 base64Url = new Base64(true);
        byte[] emailBytes = base64Url.decodeBase64(message.getRaw());

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));
        return email;
    }

    private String getTextFromMessage(MimeMessage message) throws IOException, MessagingException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException {
        int count = mimeMultipart.getCount();
        if (count == 0)
            throw new MessagingException("Multipart with no body parts not supported.");
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (multipartAlt)
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        String result = "";
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        return result;
    }

    private String getTextFromBodyPart(BodyPart bodyPart) throws IOException, MessagingException {
        String result = "";
        if (bodyPart.isMimeType("text/plain")) {
            result = (String) bodyPart.getContent();
        } else if (bodyPart.isMimeType("text/html")) {
            String html = (String) bodyPart.getContent();
            result = org.jsoup.Jsoup.parse(html).text();
        } else if (bodyPart.getContent() instanceof MimeMultipart){
            result = getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
        }
        return result;
    }

    public void markAsRead(String messageId){
        List<String> labelsToRemove = new ArrayList<String>();
        labelsToRemove.add("UNREAD");
        ModifyMessageRequest mods = new ModifyMessageRequest().setRemoveLabelIds(labelsToRemove);
        try{
            Message message = service.users().messages().modify(userId, messageId, mods).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void markAsStar(String messageId){
        List<String> labelsToAdd = new ArrayList<String>();
        labelsToAdd.add("STARRED");
        ModifyMessageRequest mods = new ModifyMessageRequest().setAddLabelIds(labelsToAdd);
        try{
            Message message = service.users().messages().modify(userId, messageId, mods).execute();
            System.out.println("Message id: " + message.getId());
            System.out.println(message.toPrettyString());
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
