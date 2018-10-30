package com.example.youchengye.csci_310_project_mysmartusc;


import com.google.firebase.FirebaseApp;

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
    private Header mTestingHeader11;
    private Header mTestingHeader12;
    private Header mTestingHeader13;
    private Header mTestingHeader14;
    private Header mTestingHeader15;
    private Header mTestingHeader16;


    private LoginActivity testClass; // access and test checkNew and CheckEmail in this class



    @Before
    public void setUp() throws Exception {
        /*
         * The following TestingHeader simulate 10 emails in an inbox. mTestingHeader1 is the
         * oldest, and TestingHeader10 is the newest
         */
        mTestingHeader1 = new Header("testing@gmail.com", "testing1",
                "snippet1", "1", "cwl1");
        mTestingHeader2 = new Header("testing@gmail.com", "twl1",
                "snippet2", "2", "content1");
        mTestingHeader3 = new Header("testing@gmail.com", "testing2",
                "snippet3", "3", "cwl2");
        mTestingHeader4 = new Header("testing@gmail.com", "twl2",
                "snippet4", "4", "cwl3");
        mTestingHeader5 = new Header("testing@gmail.com", "twl3",
                "snippet5", "5", "content2");
        mTestingHeader6 = new Header("testing@gmail.com", "testing3",
                "snippet6", "6", "cwl4");
        mTestingHeader7 = new Header("testing@gmail.com", "twl4",
                "snippet7", "7", "content3");
        mTestingHeader8 = new Header("testing@gmail.com", "testing4",
                "snippet8", "8", "cwl5");
        mTestingHeader9 = new Header("testing@gmail.com", "twl5",
                "snippet9", "9", "content4");
        mTestingHeader10 = new Header("testing@gmail.com", "testing5",
                "snippet10", "10", "content5");
        mTestingHeader11 = new Header("testing@gmail.com", "testing6",
                "snippet10", "11", "content6");
        mTestingHeader12 = new Header("testing@gmail.com", "testing7",
                "snippet10", "12", "content7");
        mTestingHeader13 = new Header("testing@gmail.com", "testing8",
                "snippet10", "13", "content8");
        mTestingHeader14 = new Header("testing@gmail.com", "testing9",
                "snippet10", "14", "content9");
        mTestingHeader15 = new Header("testing@gmail.com", "testing10",
                "snippet10", "15", "content10");
        mTestingHeader16 = new Header("testing@gmail.com", "testing11",
                "snippet10", "16", "content11");


        testClass = new LoginActivity();

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
     * Check if new Headers will be identified and returned in newest to oldest order
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
        newHeaders.add(mTestingHeader16);
        newHeaders.add(mTestingHeader15);
        newHeaders.add(mTestingHeader14);
        newHeaders.add(mTestingHeader13);
        newHeaders.add(mTestingHeader12);
        newHeaders.add(mTestingHeader11);
        newHeaders.add(mTestingHeader10);
        newHeaders.add(mTestingHeader9);
        List<Header> expetced = new ArrayList<>();
        expetced.add(mTestingHeader16);
        expetced.add(mTestingHeader15);
        expetced.add(mTestingHeader14);
        expetced.add(mTestingHeader13);
        expetced.add(mTestingHeader12);
        expetced.add(mTestingHeader11);
        expetced.add(mTestingHeader10);
        expetced.add(mTestingHeader9);
        List<Header> actual = testClass.checkNew(newHeaders, oldHeaders);
        assertEquals(expetced, actual);
    }

    /*
     * Check that old headers will not be returned when we have six new headers
     */
    @Test
    public void test_4(){
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
     * Check that old headers will not be returned when the first old header is deleted
     */
    @Test
    public void test_5(){
        List<Header> oldHeaders = new ArrayList<>();
        oldHeaders.add(mTestingHeader9);
        oldHeaders.add(mTestingHeader8);
        oldHeaders.add(mTestingHeader7);
        oldHeaders.add(mTestingHeader6);
        oldHeaders.add(mTestingHeader5);
        oldHeaders.add(mTestingHeader4);
        oldHeaders.add(mTestingHeader3);
        oldHeaders.add(mTestingHeader2);
        List<Header> newHeaders = new ArrayList<>();
        newHeaders.add(mTestingHeader8);
        newHeaders.add(mTestingHeader7);
        newHeaders.add(mTestingHeader6);
        newHeaders.add(mTestingHeader5);
        newHeaders.add(mTestingHeader4);
        newHeaders.add(mTestingHeader3);
        newHeaders.add(mTestingHeader2);
        newHeaders.add(mTestingHeader1);
        List<Header> expetced = new ArrayList<>();
        List<Header> actual = testClass.checkNew(newHeaders, oldHeaders);
        assertEquals(expetced, actual);
    }

}
