package com.example.youchengye.csci_310_project_mysmartusc;


import android.content.Intent;
import android.content.SyncRequest;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.model.ModifyMessageRequest;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.mail.Session;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class EmailList {
    private static final String TAG = "EmailList";
    private GoogleCredential credential;
    private Gmail service;
    private String userId;
    private String accessToken;
    private static EmailList singleInstance;
    private final int NOTIFICATION_ID = 1;
    private String channel_id;
    private LoginActivity login;
    private List<Header> oldHeaders = new ArrayList<>();
    private String mNewestMessageId = "";
    public String authCode;
//    private int fetchCount;
    public String server_client_id;
    public String client_secret;
    public Intent gotoLoginPage;
    private ScheduledExecutorService executor;

    public String getNewestMessageId(){
        return mNewestMessageId;
    }

    public void setNewestMessageId(String newestMessageId){
        mNewestMessageId = newestMessageId;
    }

    private EmailList() {
        this.credential = null;
        this.service = null;
        this.accessToken = null;
        this.userId = "me";
//        this.fetchCount = 0;
        this.authCode = null;
    }

    public void setLogin(final LoginActivity login){
        this.login = login;
    }
    public void setRefresh(String authCode, String server_client_id, String client_secret){
        this.authCode = authCode;
        this.server_client_id = server_client_id;
        this.client_secret = client_secret;
    }
    public void setService(String accessToken){
        this.accessToken = accessToken;
        System.out.println("new access token: "+this.accessToken);
        this.credential = new GoogleCredential().setAccessToken(accessToken);
        this.service = new Gmail.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("MySmartUSC").build();
    }
    public void setIntent(Intent gotoLoginPage){
        this.gotoLoginPage = gotoLoginPage;
    }
    public void refresh(){
        this.login.signOut();
        this.login.startActivity(this.gotoLoginPage);
        this.login.signIn();
    }

    public void initialize(String accessToken, String channel_id) throws IOException, MessagingException {
        this.channel_id = channel_id;
        this.login = login;
        this.setService(accessToken);
//        if(this.executor!=null) return;
        this.executor =
                Executors.newSingleThreadScheduledExecutor();

        Runnable periodicTask = new Runnable() {
            int fetchCount = 0;
            public void run() {
                // Invoke method(s) to do the work
                try {
                    List<Header> headers = listMessages();
                    System.out.println(headers.size());
                    login.createNotification(headers, oldHeaders);
                    oldHeaders = headers;
                } catch (IOException | MessagingException e) {
                    e.printStackTrace();
                }
                fetchCount++;
                System.out.println("Fetch count: "+fetchCount);
                if(fetchCount%2==0){
                    EmailList.getInstance().refresh();
                }

            }
        };

        executor.scheduleAtFixedRate(periodicTask, 0, 5, TimeUnit.SECONDS);

    }

    public void setId(String id){
        this.userId = id;
    }

    public static EmailList getInstance(){
        if(singleInstance!=null){
//            Log.w(TAG, "NOT EMPTY!!!");
//            Log.w(TAG, "accessToken: "+singleInstance.accessToken);
            return singleInstance;
        }
        else{
//            Log.w(TAG, "EMPTY!!!");
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
//        this.credential = new GoogleCredential().setAccessToken(accessToken);
//        service = new Gmail.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
//                .setApplicationName("MySmartUSC").build();
//        initialize(accessToken);
//        Log.w(TAG, "service: "+service);
//        Log.w(TAG, "userId: "+userId);
        ListMessagesResponse response = service.users().messages().list(userId).setMaxResults((long) 8).execute();
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
        Message message = service.users().messages().get(userId, messageId).execute();
        return message;
    }


    private MimeMessage getMimeMessage(String messageId)
            throws IOException, MessagingException {
        Message message = service.users().messages().get(userId, messageId).setFormat("raw").execute();
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
        List<String> labelsToRemove = new ArrayList<>();
        labelsToRemove.add("UNREAD");
        ModifyMessageRequest mods = new ModifyMessageRequest().setRemoveLabelIds(labelsToRemove);
        try{
            Message message = service.users().messages().modify(userId, messageId, mods).execute();

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public void markAsStar(String messageId){
        List<String> labelsToAdd = new ArrayList<>();
        labelsToAdd.add("STARRED");
        ModifyMessageRequest mods = new ModifyMessageRequest().setAddLabelIds(labelsToAdd);
        try{
            Message message = service.users().messages().modify(userId, messageId, mods).execute();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public List<Header> getOldHeader(){
        return oldHeaders;
    }
}
