package com.example.youchengye.csci_310_project_mysmartusc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();


    }

    public void onClickLogin(View view) {
        Intent gotoLoginPage = new Intent(this, LoginActivity.class);
        startActivity(gotoLoginPage);
    }

    // --- for testing only!
    public void onClickKeywordsModification(View view) {
        Intent gotoKeywordsModificationPage = new Intent(this, KeywordAddressModificationActivity.class);
        startActivity(gotoKeywordsModificationPage);
    }
    // --- remove before release!


    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.channel_id),
                    name,
                    NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            channel.setLockscreenVisibility(4);
            channel.shouldShowLights();

            //Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
