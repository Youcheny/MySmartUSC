package com.example.youchengye.csci_310_project_mysmartusc;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.widget.Button;
import android.widget.EditText;

public class AddKeywordsActivityTestRule extends ActivityTestRule {
    private String TITLE_MARK = "Title Mark";
    private String TITLE_IMPORTANT = "Title Important";
    private String TITLE_STAR = "Title Star";
    private String CONTENT_MARK = "Content Mark";
    private String CONTENT_IMPORTANT = "Content Important";
    private String CONTENT_STAR = "Content Star";
    private String IMPORTANT_EMAIL = "mysmartusc123@gmail.com";

    UiDevice device;
    public AddKeywordsActivityTestRule(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();

    }

    @Override
    protected Intent getActivityIntent() {
        Intent customIntent = new Intent();
        // add some custom extras and stuff
        return customIntent;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();
        // maybe you want to do something here
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        device = UiDevice.getInstance(instrumentation);
        try {
            UiObject sign_in_button = device.findObject(new UiSelector().clickable(true));
            sign_in_button.clickAndWaitForNewWindow();
            UiObject log_in = device.findObject(new UiSelector().textContains("@"));
            log_in.clickAndWaitForNewWindow();


        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void afterActivityFinished() {
    }


}
