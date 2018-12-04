package com.example.youchengye.csci_310_project_mysmartusc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

public class ReceiveWordActivity extends AppCompatActivity {
    private static final String TAG = "ReceiveWordActivity";
    public boolean titleRead = false;
    public boolean titleImportant = false;
    public boolean titleStar = false;
    public boolean contentRead = false;
    public boolean contentImportant = false;
    public boolean contentStar = false;
    public boolean importantEmailAddress = false;
    public String sharedText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_word);
        System.out.println("In"+TAG);

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
                    sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                    if (sharedText != null) {
                        Log.i(TAG, "Shared text received: "+sharedText);

                    }
                }
            }

        }
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.TitleRead:
                titleRead = checked;
                break;
            case R.id.TitleImportant:
                titleImportant = checked;
                break;
            case R.id.TitleStar:
                titleStar = checked;
                break;
            case R.id.ContentRead:
                contentRead = checked;
                break;
            case R.id.ContentImportant:
                contentImportant = checked;
                break;
            case R.id.ContentStar:
                contentStar = checked;
                break;
            case R.id.ImportentEmailAddress:
                importantEmailAddress = checked;
                break;
        }
    }

    public void confirm(View view) {
        if(titleRead){
            UserInfo.getInstance().addTitleBlackList(sharedText);
        }
        if(titleImportant){
            UserInfo.getInstance().addTitleWhiteList(sharedText);
        }
        if(titleStar){
            UserInfo.getInstance().addContentStarList(sharedText);
        }
        if(contentRead){
            UserInfo.getInstance().addContentBlackList(sharedText);
        }
        if(contentImportant){
            UserInfo.getInstance().addContentWhiteList(sharedText);
        }
        if(contentStar){
            UserInfo.getInstance().addContentStarList(sharedText);
        }
        if(importantEmailAddress){
            UserInfo.getInstance().addImportantEmailAddressList(sharedText);
        }
        Intent gotoKeywordPage = new Intent(this, KeywordAddressModificationActivity.class);
        startActivity(gotoKeywordPage);
    }


}
