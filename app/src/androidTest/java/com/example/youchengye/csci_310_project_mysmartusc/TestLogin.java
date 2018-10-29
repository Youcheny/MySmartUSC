package com.example.youchengye.csci_310_project_mysmartusc;

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
import android.view.View;

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

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestLogin {
    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule(LoginActivity.class);
    public static UiDevice device;

    @BeforeClass
    public static void setUp() throws Exception{
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }


    @Test
    public void test01_testWrongEmail() throws UiObjectNotFoundException, IOException {
        UiObject sign_in_button = device.findObject(new UiSelector().clickable(true));
        sign_in_button.clickAndWaitForNewWindow();

        UiObject log_in = device.findObject(new UiSelector().textContains(".com"));
        log_in.waitForExists(100000);
        if(log_in.exists()){
            log_in.clickAndWaitForNewWindow();
//            assertTrue(device.hasObject(By.textContains("Sign in")));
        }




//        UiObject sign_in_button2 = device.findObject(new UiSelector().clickable(true));
//        sign_in_button2.clickAndWaitForNewWindow();
//
//        UiObject log_in2 = device.findObject(new UiSelector().textContains("@usc.edu"));
//        log_in2.clickAndWaitForNewWindow();

//        UiObject log_in = device.findObject(new UiSelector().textContains("Use another account"));
//        log_in.waitForExists(100000);
//        log_in.clickAndWaitForNewWindow();
//
//        UiObject accountInput = device.findObject(new UiSelector().focusable(true));
//        accountInput.waitForExists(100000);
//        accountInput.setText("mysmartusc123@gmail.com");
//
//        UiObject next = device.findObject(new UiSelector().textContains("Next"));
//        next.waitForExists(100000);
//        next.clickAndWaitForNewWindow();
//
//
//        UiObject passwordInput = device.findObject(new UiSelector().focusable(true));
//        passwordInput.waitForExists(100000);
//        passwordInput.setText("smartusc123!");
//
//        UiObject next2 = device.findObject(new UiSelector().textContains("Next"));
//        next2.waitForExists(100000);
//        next2.clickAndWaitForNewWindow();
//
//        UiObject agree = device.findObject(new UiSelector().textContains("I agree"));
//        agree.waitForExists(10000);
//        if (agree.exists()){
//            agree.clickAndWaitForNewWindow();
//        }
//
//        UiObject allow = device.findObject(new UiSelector().textContains("ALLOW"));
//        allow.waitForExists(10000);
//        if(allow.exists()){
//            allow.clickAndWaitForNewWindow();
//        }
    }

    @Test
    public void test02_testSignIn() throws UiObjectNotFoundException, IOException {
        UiObject sign_in_button2 = device.findObject(new UiSelector().clickable(true));
        sign_in_button2.clickAndWaitForNewWindow();

        UiObject log_in2 = device.findObject(new UiSelector().textContains("@usc.edu"));
        log_in2.clickAndWaitForNewWindow();

//        assertTrue(device.hasObject(By.textContains("Sign out")));
    }





}
