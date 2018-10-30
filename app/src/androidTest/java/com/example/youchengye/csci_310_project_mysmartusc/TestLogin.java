package com.example.youchengye.csci_310_project_mysmartusc;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLogin {
    @Rule
    public ActivityTestRule activityRule = new AddKeywordsActivityTestRule(LoginActivity.class);
    public UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());


    @Test
    public void testWrongEmail() throws UiObjectNotFoundException, IOException {
        UiObject sign_in_button = device.findObject(new UiSelector().clickable(true));
        sign_in_button.clickAndWaitForNewWindow();

        UiObject log_in = device.findObject(new UiSelector().textContains(".com"));

        log_in.waitForExists(5000);
        if(log_in.exists()) {
            log_in.clickAndWaitForNewWindow();
        }
        else{
            UiObject log_in2 = device.findObject(new UiSelector().textContains("Use another account"));
            log_in2.waitForExists(100000);
            log_in2.clickAndWaitForNewWindow();

            UiObject accountInput = device.findObject(new UiSelector().className(EditText.class));
            accountInput.waitForExists(100000);
            accountInput.setText("mysmartusc123@gmail.com");

            UiObject next = device.findObject(new UiSelector().className(Button.class).textContains("Next"));
            next.waitForExists(100000);
            next.clickAndWaitForNewWindow();

            device.wait(Until.hasObject(By.textContains("password")), 20000);
            UiObject passwordInput = device.findObject(new UiSelector().className(EditText.class));
            passwordInput.waitForExists(100000);
            passwordInput.setText("smartusc123!");

            UiObject next2 = device.findObject(new UiSelector().textContains("Next"));
            next2.waitForExists(100000);
            next2.clickAndWaitForNewWindow();

            UiObject agree = device.findObject(new UiSelector().textContains("I agree").className(Button.class));
            agree.waitForExists(10000);
            if (agree.exists()){
                agree.clickAndWaitForNewWindow();
            }

            UiObject allow = device.findObject(new UiSelector().textContains("ALLOW"));
            allow.waitForExists(10000);
            if(allow.exists()){
                allow.clickAndWaitForNewWindow();
            }
        }
    }

    @Test
    public void testSignIn() throws UiObjectNotFoundException, IOException {
        UiObject sign_in_button2 = device.findObject(new UiSelector().clickable(true));
        sign_in_button2.clickAndWaitForNewWindow();

        UiObject log_in2 = device.findObject(new UiSelector().textContains("@usc.edu"));
        log_in2.clickAndWaitForNewWindow();
    }

    @Test
    public void testGrabNewEmail() throws UiObjectNotFoundException {
        device.wait(Until.hasObject(By.text("SHOW THIS LIST")), 500000);

        device.openNotification();
        device.wait(Until.hasObject(By.textContains("Title Important")), 50000);
        device.wait(Until.hasObject(By.textContains("Something should not appear here")), 50000);
        List<Header> oldHeaders = EmailList.getInstance().getOldHeader();
        device.wait(Until.hasObject(By.textContains("Something should not appear here")), 50000);
        List<Header> newHeaders = EmailList.getInstance().getOldHeader();

        assertTrue(oldHeaders.get(0).messageId != newHeaders.get(0).messageId);
    }

    @Test
    public void testEightEmails() throws UiObjectNotFoundException {
        device.wait(Until.hasObject(By.text("SHOW THIS LIST")), 500000);
        device.openNotification();
        device.wait(Until.hasObject(By.textContains("Title Important")), 50000);
        device.wait(Until.hasObject(By.textContains("Something should not appear here")), 50000);
        List<Header> header = EmailList.getInstance().getOldHeader();
        assertEquals(8, header.size());
    }

}