package com.example.youchengye.csci_310_project_mysmartusc;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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

    public List<Header> initialize(String accessToken){
//        singleInstance = new EmailList();
        this.accessToken = accessToken;
        Log.w(TAG, "accessToken: "+accessToken);
        this.credential = new GoogleCredential().setAccessToken(accessToken);

        this.service = new Gmail.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("MySmartUSC").build();

        // EXAMPLE OF EMAILLIST
        // There are 2 problems need to be fixed
        // 1. snippet is shortened content
        // 2. from includes name and email and email address
        try {
            List<Header> headers = EmailList.getInstance().listMessages();
            for(Header header: headers){
                Log.i(TAG, "subject: "+header.subject);
                Log.i(TAG, "from: "+header.from);
                Log.i(TAG, "snippet: "+header.snippet);
                Log.i(TAG, "messageId: "+header.messageId);
            }
            return headers;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public Message getMessage(String messageId) throws IOException {
        List<String> metadataHeaders = new ArrayList<>();
        metadataHeaders.add("From");
        metadataHeaders.add("Subject");
//		Message message = service.users().messages().get(userId, messageId).setFormat("metadata").setMetadataHeaders(metadataHeaders).execute();
        Message message = singleInstance.service.users().messages().get(singleInstance.userId, messageId).execute(); //SHOULD BE singleInstance.service.users().messages().get(singleInstance.userId, messageId).setFormat("raw").execute();
		if (message == null) {
			System.out.println("null message");
		}
		else {
			System.out.println("not null message");
			System.out.println(message.toPrettyString());
		}
		System.out.println("Message snippet: " + message.getSnippet());
        return message;
    }

    public List<Header> listMessages() throws IOException {

        ListMessagesResponse response = singleInstance.service.users().messages().list(singleInstance.userId).setMaxResults((long) 8).execute();

        List<Message> messages = new ArrayList<>();

        if (response.getMessages() != null) {
            messages.addAll(response.getMessages());
        }
		for (Message message : messages) {
			System.out.println(message.toPrettyString());
		}

        ArrayList<Header> headers = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            String messageId = messages.get(i).getId();
//			System.out.println(messageId);
            Message message = getMessage(messageId);
//			Message message = getMessage(messageId);
            int size = message.getPayload().getHeaders().size();
//			System.out.println("size " + size);
            String from = null;
            String subject = null;
//			String snippet = null;
            String snippet = message.getSnippet();
//            if (snippet.length() > 50) {
//                snippet = (snippet.substring(0, 45) + "...");
//            }
            for (int j = 0; j < size; j++) {
                String temp = message.getPayload().getHeaders().get(j).getName();
                if (temp.equals("From")) {
                    from = message.getPayload().getHeaders().get(j).getValue();
//                    int start = from.indexOf('<');
//                    int end = from.indexOf('>');
//                    Log.i(TAG, ""+start+" "+end);
//                    from = from.substring(start+1, end-start-1);
                    //WHEN I do the things above there is an error
                }
                else if (temp.equals("Subject")) {
                    subject = message.getPayload().getHeaders().get(j).getValue();
//                    if (subject.length() > 35) {
//                        subject = (subject.substring(0, 30) + "...");
//                    }
                }
            }
            headers.add(new Header(from, subject, snippet, messageId));
//			System.out.println("message index " + i + " from " + from + " subject " + subject + " snippet " + snippet);
        }

        return headers;
    }
}
