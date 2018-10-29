package com.example.youchengye.csci_310_project_mysmartusc;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class TestNotification {

    private Header mTestingHeader1;
    private Header mTestingHeader2;
    private Header mTestingHeader3;
    private Header mTestingHeader4;
    private Header mTestingHeader5;
    private Header mTestingHeader6;
    private Header mTestingHeader7;
    private Header mTestingHeader8;
    private Header mTestingHeader9;
    private Header mTestingHeader10;

    private LoginActivity testClass; // access and test checkNew and CheckEmail in this class

    @Before
    public void setUp() throws Exception {
        /*
         * The following TestingHeader simulate 10 emails in an inbox. mTestingHeader1 is the
         * oldest, and TestingHeader10 is the newest
         */
        mTestingHeader1 = new Header("testing@gmail.com", "testing1",
                "snippet1", "1", "content1");
        mTestingHeader2 = new Header("testing@gmail.com", "testing2",
                "snippet2", "2", "content2");
        mTestingHeader3 = new Header("testing@gmail.com", "testing3",
                "snippet3", "3", "content3");
        mTestingHeader4 = new Header("testing@gmail.com", "testing4",
                "snippet4", "4", "content4");
        mTestingHeader5 = new Header("testing@gmail.com", "testing5",
                "snippet5", "5", "content5");
        mTestingHeader6 = new Header("testing@gmail.com", "testing6",
                "snippet6", "6", "content6");
        mTestingHeader7 = new Header("testing@gmail.com", "testing7",
                "snippet7", "7", "content7");
        mTestingHeader8 = new Header("testing@gmail.com", "testing8",
                "snippet8", "8", "content8");
        mTestingHeader9 = new Header("testing@gmail.com", "testing9",
                "snippet9", "9", "content9");
        mTestingHeader10 = new Header("testing@gmail.com", "testing10",
                "snippet10", "10", "content10");

        testClass = new LoginActivity();
        UserInfo.getInstance().Initialize("test_user_01@usc.edu");
        Thread.sleep(10000); // wait for Firebase initialization

    }


    /*
     * Check if all headers can be detected as new headers when we don't have any information
     * of existing headers
     */
    @Test
    public void test_1(){
        List<Header> oldHeaders = new ArrayList<>();
        List<Header> newHeaders = new ArrayList<>();
        newHeaders.add(mTestingHeader8);
        newHeaders.add(mTestingHeader7);
        newHeaders.add(mTestingHeader6);
        newHeaders.add(mTestingHeader5);
        newHeaders.add(mTestingHeader4);
        newHeaders.add(mTestingHeader3);
        newHeaders.add(mTestingHeader2);
        newHeaders.add(mTestingHeader1);
        List<Header> expected = newHeaders;
        List<Header> actual = testClass.checkNew(newHeaders, oldHeaders);
        assertEquals(expected, actual);
    }

    /*
     * Check if new headers can be identified as unchanged when we don't have new information of
     * headers
     */
    @Test
    public void test_2(){
        List<Header> oldHeaders = new ArrayList<>();
        oldHeaders.add(mTestingHeader8);
        oldHeaders.add(mTestingHeader7);
        oldHeaders.add(mTestingHeader6);
        oldHeaders.add(mTestingHeader5);
        oldHeaders.add(mTestingHeader4);
        oldHeaders.add(mTestingHeader3);
        oldHeaders.add(mTestingHeader2);
        oldHeaders.add(mTestingHeader1);
        List<Header> newHeaders = new ArrayList<>();
        newHeaders.add(mTestingHeader8);
        newHeaders.add(mTestingHeader7);
        newHeaders.add(mTestingHeader6);
        newHeaders.add(mTestingHeader5);
        newHeaders.add(mTestingHeader4);
        newHeaders.add(mTestingHeader3);
        newHeaders.add(mTestingHeader2);
        newHeaders.add(mTestingHeader1);
        List<Header> expected = new ArrayList<Header>();
        List<Header> actual = testClass.checkNew(newHeaders, oldHeaders);
        assertEquals(expected, actual);
    }

    /*
     * Check if new emails will be identified and returned in newest to oldest order
     */
    @Test
    public void test_3(){
        List<Header> oldHeaders = new ArrayList<>();
        oldHeaders.add(mTestingHeader8);
        oldHeaders.add(mTestingHeader7);
        oldHeaders.add(mTestingHeader6);
        oldHeaders.add(mTestingHeader5);
        oldHeaders.add(mTestingHeader4);
        oldHeaders.add(mTestingHeader3);
        oldHeaders.add(mTestingHeader2);
        oldHeaders.add(mTestingHeader1);
        List<Header> newHeaders = new ArrayList<>();
        newHeaders.add(mTestingHeader10);
        newHeaders.add(mTestingHeader9);
        newHeaders.add(mTestingHeader8);
        newHeaders.add(mTestingHeader7);
        newHeaders.add(mTestingHeader6);
        newHeaders.add(mTestingHeader5);
        newHeaders.add(mTestingHeader4);
        newHeaders.add(mTestingHeader3);
        List<Header> expetced = new ArrayList<>();
        expetced.add(mTestingHeader10);
        expetced.add(mTestingHeader9);
        List<Header> actual = testClass.checkNew(newHeaders, oldHeaders);
        assertEquals(expetced, actual);
    }

    /*
     * Check if email with whitelist keyword in title can be returned
     */
    @Test
    public void test_4(){

    }

    /*
     * Check if email with whitelist keyword in content can be returned
     */
    @Test
    public void test_5(){

    }

    /*
     * Check if email sent from important address can be returned
     */
    @Test
    public void test_6(){

    }







}
