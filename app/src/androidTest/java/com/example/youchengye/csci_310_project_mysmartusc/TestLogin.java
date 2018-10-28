package com.example.youchengye.csci_310_project_mysmartusc;

import android.content.Context;
import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

@RunWith(AndroidJUnit4.class)
public class TestLogin {
    @Rule
    public ActivityTestRule activityRule = new ActivityTestRule(LoginActivity.class);
    public static UiDevice device;

    @BeforeClass
    public static void setUp() throws Exception{
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void testLogin() throws UiObjectNotFoundException, IOException {
        UiObject sign_in_button = device.findObject(new UiSelector().clickable(true));
        sign_in_button.clickAndWaitForNewWindow();

        UiObject log_in = device.findObject(new UiSelector().textContains("@"));
        log_in.clickAndWaitForNewWindow();

        System.out.print("here");
    }

}
