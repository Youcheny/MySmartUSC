package com.example.youchengye.csci_310_project_mysmartusc;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */
public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        mStatusTextView = findViewById(R.id.status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);


        String serverClientId = getString(R.string.server_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope("https://mail.google.com/"))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);



    }

    @Override
    public void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        View rootView = getWindow().getDecorView().getRootView();

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String authCode = account.getServerAuthCode();
            String id = account.getId();
            String email = account.getEmail();
            if (email != null && !email.split("@")[1].equals("usc.edu")) {
                signOut();
                return;
            }
            EmailList.getInstance().setId(id);
            EmailList.getInstance().setLogin(this);
            EmailList.getInstance().setRefresh(authCode, getString(R.string.server_client_id), getString(R.string.client_secret));
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormEncodingBuilder()
                    .add("grant_type", "authorization_code")
                    .add("client_id", getString(R.string.server_client_id))
                    .add("client_secret", getString(R.string.client_secret))
                    .add("redirect_uri","")
                    .add("code", authCode)
                    .build();
            final Request request = new Request.Builder()
                    .url("https://www.googleapis.com/oauth2/v4/token")
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(final Request request, final IOException e) {
                    Log.e(TAG, e.toString());
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        final String message = jsonObject.toString(5);
                        final String access_token = jsonObject.getString("access_token");
//                        final String refresh_token = jsonObject.getString("refresh_token");
                        System.out.println("Access_token: "+access_token);
                        System.out.println(message);
//                        Log.w(TAG, "Refresh_token: "+refresh_token);
                        EmailList.getInstance().initialize(access_token, getString(R.string.channel_id));
//                        List<Header> headers = EmailList.getInstance().listMessages();
//                        createNotification(headers);

                    } catch (JSONException | MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });

                    // Signed in successfully, show authenticated UI.
            updateUI(account);
//            Thread.sleep(1000);
            Intent gotoKeywordsPage = new Intent(this, KeywordAddressModificationActivity.class);
            gotoKeywordsPage.putExtra("username", account.getEmail());
            startActivity(gotoKeywordsPage);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    public void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    public void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            /**
             * get the users important keywords, username to be change later
             */
            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName())+"\n Waiting for Emails...");
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    public void createNotification(List<Header> headers, List<Header> oldHeaders){
        List<Header> newEmails = checkNew(headers, oldHeaders);
        List<Header> importantEmails = checkEmail(newEmails);

        Log.w("importantEmails size", Integer.toString(importantEmails.size()));
        if (importantEmails!=null && importantEmails.size()!=0) {

            Intent notifyIntent = new Intent(this, OpenGmailActivity.class);
            // Set the Activity to start in a new, empty task
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Create the PendingIntent
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                    this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
            );



            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                    .setSmallIcon(R.drawable.ic_channel_icon)
                    .setContentTitle("Important Emails")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(notifyPendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            PowerManager powerManager = (PowerManager) this.getSystemService(POWER_SERVICE);

            if (!powerManager.isInteractive()){ // if screen is not already on, turn it on (get wake_lock for 10 seconds)
                PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MH24_SCREENLOCK");
                wl.acquire(10000);
                PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MH24_SCREENLOCK");
                wl_cpu.acquire(10000);
            }

            for (int i=importantEmails.size()-1; i>=0; i--){
                builder.setSmallIcon(R.drawable.usc)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .setSummaryText(importantEmails.get(i).from)
                                .setBigContentTitle(importantEmails.get(i).subject)
                                .bigText(importantEmails.get(i).content))
                        .setContentTitle(importantEmails.get(i).subject) // this should replace subject with sender in brief
                        .setContentText(importantEmails.get(i).content);


                notificationManager.notify(i, builder.build());
            }
        }
    }

    private String createContentText(Header header){
        String contentText = "From "+ header.from + ": "+header.snippet;
        return contentText;
    }


    public static List<Header> checkEmail(List<Header> headers){
        List<String> titleWhiteList = UserInfo.getInstance().getTitleWhiteList();
        List<String> contentWhiteList = UserInfo.getInstance().getContentWhiteList();
        List<String> importantEmailAddresses = UserInfo.getInstance().getImportantEmailAddressList();
        List<Header> importantEmails = new ArrayList<>();
        List<String> titleStarList = UserInfo.getInstance().getTitleStarList();
        List<String> cotentStarList = UserInfo.getInstance().getContentStarList();
        List<String>  titleBlackList = UserInfo.getInstance().getTitleBlackList();
        List<String> contentBlackList = UserInfo.getInstance().getContentBlackList();

        Set<Header> checkers = new HashSet<>();
        for (Header h:headers) {
            Boolean checked = false;
            for (String keyword:titleWhiteList){
                if (h.subject.toLowerCase().contains(keyword.toLowerCase())){
                    if (!checkers.contains(h) && checked==false){
                        importantEmails.add(h);
                        checkers.add(h);
                        checked = true;
                    }
                    break;
                }
            }

            for (String keyword: contentWhiteList){
                if (h.content.toLowerCase().contains(keyword.toLowerCase())){
                    if (!checkers.contains(h)&& checked==false){
                        importantEmails.add(h);
                        checkers.add(h);
                        checked = true;
                    }
                    break;
                }
            }

            for (String keyword: importantEmailAddresses){
                if (h.from.toLowerCase().contains(keyword.toLowerCase())){
                    if (!checkers.contains(h)&& checked==false){
                        importantEmails.add(h);
                        checkers.add(h);
                        checked = true;
                        break;
                    }
                }
            }

            // check if a email should be starred
            Boolean starred = false;
            for (String keyword : titleStarList){
                if(h.subject.toLowerCase().contains(keyword.toLowerCase())){
                    EmailList.getInstance().markAsStar(h.messageId);
                    starred = true;
                    break;
                }
            }

            if(!starred){ // check content for star keyword
                for(String keyword : cotentStarList) {
                    if(h.content.toLowerCase().contains(keyword.toLowerCase())) {
                        EmailList.getInstance().markAsStar(h.messageId);
                        starred = true;
                        break;
                    }
                }
            }

            // The email contains neither keywords in Whitelist nor Starlist,
            // but contains keywords in Blacklist. User will see the email marked as read
            if(!checkers.contains(h) && !starred){
                Boolean read = false;
                for(String keyword : titleBlackList){
                    if(h.subject.toLowerCase().contains(keyword.toLowerCase())){
                        EmailList.getInstance().markAsRead(h.messageId);
                        read = true;
                        break;
                    }
                }

                if(!read){ // check content for blacklist keyword
                    for(String keyword : contentBlackList){
                        if(h.content.toLowerCase().contains(keyword.toLowerCase())){
                            EmailList.getInstance().markAsRead(h.messageId);
                            break;
                        }
                    }
                }
            }

        }
        ImportantMailsActivity.addHeaders(importantEmails);
        return importantEmails;
    }

//    public List<Header> checkNew(List<Header> headers, List<Header> oldHeaders){
//        List<Header> newEmails = new ArrayList<>();
//        if (oldHeaders.size() == 0){
//            return headers;
//        }else{
//            for (Header h: headers){
//                if (!h.messageId.equals(oldHeaders.get(0).messageId)){
//                    newEmails.add(h);
//                }else{
//                    break;
//                }
//            }
//        }
//        return newEmails;
//    }

    public List<Header> checkNew(List<Header> headers, List<Header> oldHeaders){
        List<Header> newEmails = new ArrayList<>();
        if (oldHeaders.size() == 0){
            String newestMessageId = headers.get(0).messageId;
            EmailList.getInstance().setNewestMessageId(newestMessageId);
            return headers;
        }
        else{
            String newestMessageId = EmailList.getInstance().getNewestMessageId();
            int i = 0;
            for (Header h: headers){
                if(h.messageId.compareTo(newestMessageId) > 0){
                    newEmails.add(h);
                    if(i == 0){
                        EmailList.getInstance().setNewestMessageId(h.messageId);
                    }
                }
                else break;
                i++;
            }
        }



        return newEmails;
    }


}
