package com.example.youchengye.csci_310_project_mysmartusc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickLogin(View view) {
        Intent gotoLoginPage = new Intent(this, LoginActivity.class);
        startActivity(gotoLoginPage);

    }
}
