package com.example.youchengye.csci_310_project_mysmartusc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
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

import com.example.youchengye.csci_310_project_mysmartusc.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
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
import java.util.HashMap;
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
    private final int NOTIFICATION_ID = 1;

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
//        Log.w(TAG, serverClientId);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope("https://mail.google.com/"))
//                .requestIdToken(serverClientId)
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // [END customize_button]
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
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
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String authCode = account.getServerAuthCode();
//            String idToken = account.getIdToken();
            String id = account.getId();
            Log.w(TAG, "id: "+id);
            EmailList.getInstance().setId(id);
//            Log.w(TAG, "idToken: "+idToken);
            Log.w(TAG, "authCode: "+authCode);
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
//                        Log.i(TAG, message);
                        final String access_token = jsonObject.getString("access_token");
//                        Log.i(TAG, "access_token: "+access_token);
                        EmailList.getInstance().initialize(access_token);
                        List<Header> headers = EmailList.getInstance().listMessages();
                        createNotification(headers);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            // Signed in successfully, show authenticated UI.
            updateUI(account);
            // jump to KeywordsAddressModification page
            Thread.sleep(1000);
            Intent gotoKeywordsPage = new Intent(this, KeywordAddressModificationActivity.class);
            startActivity(gotoKeywordsPage);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
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

    public void createNotification(List<Header> headers){
        List<Header> importantEmails = checkEmail(headers);

        if (importantEmails!=null && importantEmails.size()!=0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_id))
                    .setSmallIcon(R.drawable.ic_channel_icon)
                    .setContentTitle("Important Emails")
                    .setContentText(createContentText(importantEmails.get(0)))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            PowerManager powerManager = (PowerManager) this.getSystemService(POWER_SERVICE);

            if (!powerManager.isInteractive()){ // if screen is not already on, turn it on (get wake_lock for 10 seconds)
                PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MH24_SCREENLOCK");
                wl.acquire(10000);
                PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MH24_SCREENLOCK");
                wl_cpu.acquire(10000);
            }

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    private String createContentText(Header header){
        String contentText = "From "+ header.from + ": "+header.snippet;
        return contentText;
    }

    private void deleteNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.deleteNotificationChannel(getString(R.string.channel_id));
        }
    }

    private List<Header> checkEmail(List<Header> headers){
        List<String> titleWhiteList = UserInfo.getInstance().getTitleWhiteList();
        List<String> contentWhiteList = UserInfo.getInstance().getContentWhiteList();
        List<String> importantEmailAddresses = UserInfo.getInstance().getImportantEmailAddressList();
        List<Header> importantEmails = new ArrayList<>();
        List<String> titleStarList = UserInfo.getInstance().getTitleStarList();
        List<String> cotentStarList = UserInfo.getInstance().getContentStarList();
        List<String>  titleBlackList = UserInfo.getInstance().getTitleBlackList();
        List<String> contentBlackList  =UserInfo.getInstance().getContentBlackList();

        Log.w("titlewhiltelist", titleWhiteList.toString());
        Log.w("contentWhiteList", contentWhiteList.toString());
        Set<Header> checkers = new HashSet<>();
        for (Header h:headers) {
            Log.w("headers", h.from);
            for (String keyword:titleWhiteList){
                if (h.subject.toLowerCase().contains(keyword.toLowerCase())){
                    if (!checkers.contains(h)){
                        importantEmails.add(h);
                        checkers.add(h);
                    }
                    break;
                }
            }

            for (String keyword: contentWhiteList){
                if (h.content.toLowerCase().contains(keyword.toLowerCase())){
                    if (!checkers.contains(h)){
                        importantEmails.add(h);
                        checkers.add(h);
                    }
                    break;
                }
            }

            for (String keyword: importantEmailAddresses){
                if (h.from.toLowerCase().contains(keyword.toLowerCase())){
                    if (!checkers.contains(h)){
                        importantEmails.add(h);
                        checkers.add(h);
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
        return importantEmails;
    }

    // for testing notification
    public void onClickNotification(View view) throws IOException, MessagingException {
//        try {
//            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//            if (account == null){
//                Log.w("account","account is null");
//            }else{
//                Log.w("account", account.getServerAuthCode());
//            }
//            String authCode = account.getServerAuthCode();
////            String idToken = account.getIdToken();
//            String id = account.getId();
//            Log.w(TAG, "id: "+id);
//            EmailList.getInstance().setId(id);
////            Log.w(TAG, "idToken: "+idToken);
//            Log.w(TAG, "authCode: "+authCode);
//            OkHttpClient client = new OkHttpClient();
//            RequestBody requestBody = new FormEncodingBuilder()
//                    .add("grant_type", "authorization_code")
//                    .add("client_id", getString(R.string.server_client_id))
//                    .add("client_secret", getString(R.string.client_secret))
//                    .add("redirect_uri","")
//                    .add("code", authCode)
//                    .build();
//            final Request request = new Request.Builder()
//                    .url("https://www.googleapis.com/oauth2/v4/token")
//                    .post(requestBody)
//                    .build();
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(final Request request, final IOException e) {
//                    Log.e(TAG, e.toString());
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        final String message = jsonObject.toString(5);
////                        Log.i(TAG, message);
//                        final String access_token = jsonObject.getString("access_token");
////                        Log.i(TAG, "access_token: "+access_token);
//                        EmailList.getInstance().initialize(access_token);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (MessagingException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            // Signed in successfully, show authenticated UI.
//            updateUI(account);
//        }

        List<Header> headers = EmailList.getInstance().listMessages();
        createNotification(headers);
    }

}
