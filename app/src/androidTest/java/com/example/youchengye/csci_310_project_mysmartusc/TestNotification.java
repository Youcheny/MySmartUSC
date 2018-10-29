package com.example.youchengye.csci_310_project_mysmartusc;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Important !!!!!!!
 * Make sure you go to EmailTools class and change "private final static to" to tester's email address
 * The email should be the same as your sign in email
 */
@RunWith(AndroidJUnit4.class)
public class TestNotification {

    @Rule
    public ActivityTestRule activityRule = new AddKeywordsActivityTestRule(LoginActivity.class);
    public static UiDevice device;

    @Before
    public void setUp() throws Exception{
    }

    @Test
    public void testSendEmail(){
    }


}