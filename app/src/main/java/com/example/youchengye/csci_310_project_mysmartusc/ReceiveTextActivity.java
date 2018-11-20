package com.example.youchengye.csci_310_project_mysmartusc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

@SuppressLint("Registered")
public class ReceiveTextActivity extends Activity {
    private static final String TAG = "ReceiveTextActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(EmailList.getInstance().accessToken == null){
            Intent gotoLoginPage = new Intent(this, LoginActivity.class);
            EmailList.getInstance().setIntent(gotoLoginPage);
            startActivity(gotoLoginPage);
        }
        else{
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                    handleSendText(intent); // Handle text being sent
                }
            }

        }

    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            Log.i(TAG, "Shared text received: "+sharedText);
            UserInfo.getInstance().addContentWhiteList(sharedText);
            Intent gotoKeywordPage = new Intent(this, KeywordAddressModificationActivity.class);
            intent.putExtra("sharedText", sharedText);
            startActivity(gotoKeywordPage);
        }
    }


}
