package com.example.youchengye.csci_310_project_mysmartusc;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class OpenGmailActivity extends AppCompatActivity {
    String msg = "openGmail";
    boolean jumped = false;

    @Override
    protected void onCreate(Bundle b) {
        Log.d(msg, "The onCreate() event");
        super.onCreate(b);
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, 0);
        for(ResolveInfo info : resolveInfoList){
            if(info.activityInfo.packageName.equalsIgnoreCase("com.google.android.gm"))
            {
//                toast = Toast.makeText(MainActivity.this, "Gmail opened", Toast.LENGTH_LONG);
//                toast.show();
                launchApp(info.activityInfo.packageName, info.activityInfo.name);
                return;
            }
        }

    }

    private void launchApp(String packageName, String name)
    {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setComponent(new ComponentName(packageName, name));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onStart(){
        if(jumped){
            finish();
        }
        super.onStart();
        Log.d(msg, "The onStart() event");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(msg, "The onResume() event");
    }



    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");
        jumped = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
    }

}
