package com.example.youchengye.csci_310_project_mysmartusc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "onCreate: "+UserInfo.getInstance().MainActivityStarted);

        if(!UserInfo.getInstance().MainActivityStarted){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            createNotificationChannel();
            UserInfo.getInstance().MainActivityStarted = true;
        }
        else{
            /* When collapsed notification is clicked, a new MainActivity will be created, we use
             * jump to Gmail at this time
             */
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, 0);
            for(ResolveInfo info : resolveInfoList){
                if(info.activityInfo.packageName.equalsIgnoreCase("com.google.android.gm"))
                {
//                toast = Toast.makeText(MainActivity.this, "Gmail opened", Toast.LENGTH_LONG);
//                toast.show();
                    Intent sintent = new Intent("android.intent.action.MAIN");
                    sintent.addCategory("android.intent.category.LAUNCHER");
                    sintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    sintent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(sintent);
                    return;
                }
            }
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("MainActivity", "onResume");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("MainActivity", "onStart: "+UserInfo.getInstance().MainActivityStarted);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("MainActivity", "onStop");
    }



    public void onClickLogin(View view) {
        Intent gotoLoginPage = new Intent(this, LoginActivity.class);
        EmailList.getInstance().setIntent(gotoLoginPage);

        startActivity(gotoLoginPage);
    }


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
