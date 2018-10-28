package com.example.youchengye.csci_310_project_mysmartusc;

import android.app.Instrumentation;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.app.DialogFragment;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;

import static org.junit.Assert.*;

/**
 * Important !!!!!!!
 * Make sure you go to EmailTools class and change "private final static to" to tester's email address
 * The email should be the same as your sign in email
 */
@RunWith(AndroidJUnit4.class)
public class TestNotification {

    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule(LoginActivity.class);
    public static UiDevice device;

    @BeforeClass
    public static void setUp() throws Exception{
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        device = UiDevice.getInstance(instrumentation);

        Instrumentation.ActivityMonitor activityMonitor= new Instrumentation.ActivityMonitor();
//        activityMonitor.getResult()

    }

    //test if emails can be successfully send
    @Test
    public static void testSendEmail(){

        EmailTools.sendEmail("test emails can be successfully send", "if not received, make sure change 'private final static to' to tester's email address ");
    }


}