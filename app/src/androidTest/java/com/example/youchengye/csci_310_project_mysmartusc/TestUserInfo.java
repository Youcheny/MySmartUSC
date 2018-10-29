package com.example.youchengye.csci_310_project_mysmartusc;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.Assert.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class TestUserInfo {
    public void initialize(String username) {
        UserInfo.getInstance().Initialize(username);

    }

    /**
     * initialization should be completed in 10 seconds
     */
    @Test
    public void test_01_testInitializeInTimeLimit() {
        initialize("test_user_01@usc.edu");
        // wait 10 seconds for database retrieval
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(!UserInfo.getInstance().getTitleBlackList().isEmpty());
        assertTrue(!UserInfo.getInstance().getTitleWhiteList().isEmpty());
        assertTrue(!UserInfo.getInstance().getTitleStarList().isEmpty());
    }

    /**
     * initialization should create an exact copy of the user's information in the database on the UserInfo Singleton object
     */
    public void test_02_testInitializeCorrectness() {
        String[] titleBlackListExpected = {"tbl1", "tbl2", "tbl3", "tbl4", "tbl5"};
        assertArrayEquals(UserInfo.getInstance().getTitleBlackList().toArray(), titleBlackListExpected);
        String[] titleWhiteListExpected = {"twl1", "twl2", "twl3", "twl4", "twl5"};
        assertArrayEquals(UserInfo.getInstance().getTitleWhiteList().toArray(), titleWhiteListExpected);
        String[] titleStarListExpected = {"tsl1", "tsl2", "tsl3", "tsl4", "tsl5"};
        assertArrayEquals(UserInfo.getInstance().getTitleStarList().toArray(), titleStarListExpected);
        String[] contentBlackListExpected = {"cbl1", "cbl2", "cbl3", "cbl4", "cbl5"};
        assertArrayEquals(UserInfo.getInstance().getContentBlackList().toArray(), contentBlackListExpected);
        String[] contentWhiteListExpected = {"cwl1", "cwl2", "cwl3", "cwl4", "cwl5"};
        assertArrayEquals(UserInfo.getInstance().getContentWhiteList().toArray(), contentWhiteListExpected);
        String[] contentStarListExpected = {"csl1", "csl2", "csl3", "csl4", "csl5"};
        assertArrayEquals(UserInfo.getInstance().getContentStarList().toArray(), contentStarListExpected);
        String[] importantEmailAddressListExpected = {"iel1", "iel2", "iel3", "iel4", "iel5"};
        assertArrayEquals(UserInfo.getInstance().getImportantEmailAddressList().toArray(), importantEmailAddressListExpected);
    }

    /**
     * for every keyword retrieved from the database, check*List(keyword) should return true
     */
    public void test_03_testCheckKeywordsTrue() {
        String[] titleBlackListExpected = {"tbl1", "tbl2", "tbl3", "tbl4", "tbl5"};
        for (String keyword : titleBlackListExpected)
            assertTrue(UserInfo.getInstance().checkTitleBlackList(keyword));
        String[] titleWhiteListExpected = {"twl1", "twl2", "twl3", "twl4", "twl5"};
        for (String keyword : titleWhiteListExpected)
            assertTrue(UserInfo.getInstance().checkTitleWhiteList(keyword));
        String[] titleStarListExpected = {"tsl1", "tsl2", "tsl3", "tsl4", "tsl5"};
        for (String keyword : titleStarListExpected)
            assertTrue(UserInfo.getInstance().checkTitleStarList(keyword));
        String[] contentBlackListExpected = {"cbl1", "cbl2", "cbl3", "cbl4", "cbl5"};
        for (String keyword : contentBlackListExpected)
            assertTrue(UserInfo.getInstance().checkContentBlackList(keyword));
        String[] contentWhiteListExpected = {"cwl1", "cwl2", "cwl3", "cwl4", "cwl5"};
        for (String keyword : contentWhiteListExpected)
            assertTrue(UserInfo.getInstance().checkContentWhiteList(keyword));
        String[] contentStarListExpected = {"csl1", "csl2", "csl3", "csl4", "csl5"};
        for (String keyword : contentStarListExpected)
            assertTrue(UserInfo.getInstance().checkContentStarList(keyword));
        String[] importantEmailAddressListExpected = {"iel1", "iel2", "iel3", "iel4", "iel5"};
        for (String keyword : importantEmailAddressListExpected)
            assertTrue(UserInfo.getInstance().checkImportantEmailAddressList(keyword));
    }
    /**
     * for a set of keywords not in the database, check*List(keyword) should return false for every one of them
     */
    public void test_03_testCheckKeywordsFalse() {
        String[] titleBlackListUnexpected = {"tbl6", "tbl7", "tbl8", "tbl9", "tbl10"};
        for (String keyword : titleBlackListUnexpected)
            assertFalse(UserInfo.getInstance().checkTitleBlackList(keyword));
        String[] titleWhiteListUnexpected = {"twl6", "twl7", "twl8", "twl9", "twl10"};
        for (String keyword : titleWhiteListUnexpected)
            assertFalse(UserInfo.getInstance().checkTitleWhiteList(keyword));
        String[] titleStarListUnexpected = {"tsl6", "tsl7", "tsl8", "tsl9", "tsl10"};
        for (String keyword : titleStarListUnexpected)
            assertFalse(UserInfo.getInstance().checkTitleStarList(keyword));
        String[] contentBlackListUnexpected = {"cbl6", "cbl7", "cbl8", "cbl9", "cbl10"};
        for (String keyword : contentBlackListUnexpected)
            assertFalse(UserInfo.getInstance().checkContentBlackList(keyword));
        String[] contentWhiteListUnexpected = {"cwl6", "cwl7", "cwl8", "cwl9", "cwl10"};
        for (String keyword : contentWhiteListUnexpected)
            assertFalse(UserInfo.getInstance().checkContentWhiteList(keyword));
        String[] contentStarListUnexpected = {"csl6", "csl7", "csl8", "csl9", "csl10"};
        for (String keyword : contentStarListUnexpected)
            assertFalse(UserInfo.getInstance().checkContentStarList(keyword));
        String[] importantEmailAddressListUnexpected = {"iel6", "iel7", "iel8", "iel9", "iel10"};
        for (String keyword : importantEmailAddressListUnexpected)
            assertFalse(UserInfo.getInstance().checkImportantEmailAddressList(keyword));
    }

    /**
     * every keyword added should make check*List on that keyword true
     */
    public void test_04_testAddKeywords() {
        String[] titleBlackListToAdd = {"tbl6", "tbl7", "tbl8", "tbl9", "tbl10"};
        for (String keyword : titleBlackListToAdd)
            UserInfo.getInstance().addTitleBlackList(keyword);
        for (String keyword : titleBlackListToAdd)
            assertTrue(UserInfo.getInstance().checkTitleBlackList(keyword));
        String[] titleWhiteListToAdd = {"twl6", "twl7", "twl8", "twl9", "twl10"};
        for (String keyword : titleWhiteListToAdd)
            UserInfo.getInstance().addTitleWhiteList(keyword);
        for (String keyword : titleWhiteListToAdd)
            assertTrue(UserInfo.getInstance().checkTitleWhiteList(keyword));
        String[] titleStarListToAdd = {"tsl6", "tsl7", "tsl8", "tsl9", "tsl10"};
        for (String keyword : titleStarListToAdd)
            UserInfo.getInstance().addTitleStarList(keyword);
        for (String keyword : titleStarListToAdd)
            assertTrue(UserInfo.getInstance().checkTitleStarList(keyword));
        String[] contentBlackListToAdd = {"cbl6", "cbl7", "cbl8", "cbl9", "cbl10"};
        for (String keyword : contentBlackListToAdd)
            UserInfo.getInstance().addContentBlackList(keyword);
        for (String keyword : contentBlackListToAdd)
            assertTrue(UserInfo.getInstance().checkContentBlackList(keyword));
        String[] contentWhiteListToAdd = {"cwl6", "cwl7", "cwl8", "cwl9", "cwl10"};
        for (String keyword : contentWhiteListToAdd)
            UserInfo.getInstance().addContentWhiteList(keyword);
        for (String keyword : contentWhiteListToAdd)
            assertTrue(UserInfo.getInstance().checkContentWhiteList(keyword));
        String[] contentStarListToAdd = {"csl6", "csl7", "csl8", "csl9", "csl10"};
        for (String keyword : contentStarListToAdd)
            UserInfo.getInstance().addContentStarList(keyword);
        for (String keyword : contentStarListToAdd)
            assertTrue(UserInfo.getInstance().checkContentStarList(keyword));
        String[] importantEmailAddressListUnexpected = {"iel6", "iel7", "iel8", "iel9", "iel10"};
        for (String keyword : importantEmailAddressListUnexpected)
            UserInfo.getInstance().addImportantEmailAddressList(keyword);
        for (String keyword : importantEmailAddressListUnexpected)
            assertTrue(UserInfo.getInstance().checkImportantEmailAddressList(keyword));
    }
    /**
     * removes every keyword just added, then check that check*List(keyword) for every such keyword is false
     */
    public void testRemoveKeyword() {
        String[] titleBlackListToRemove = {"tbl6", "tbl7", "tbl8", "tbl9", "tbl10"};
        for (String keyword : titleBlackListToRemove)
            UserInfo.getInstance().removeTitleBlackList(keyword);
        for (String keyword : titleBlackListToRemove)
            assertFalse(UserInfo.getInstance().checkTitleBlackList(keyword));
        String[] titleWhiteListToRemove = {"twl6", "twl7", "twl8", "twl9", "twl10"};
        for (String keyword : titleWhiteListToRemove)
            UserInfo.getInstance().removeTitleWhiteList(keyword);
        for (String keyword : titleWhiteListToRemove)
            assertFalse(UserInfo.getInstance().checkTitleWhiteList(keyword));
        String[] titleStarListToRemove = {"tsl6", "tsl7", "tsl8", "tsl9", "tsl10"};
        for (String keyword : titleStarListToRemove)
            UserInfo.getInstance().removeTitleStarList(keyword);
        for (String keyword : titleStarListToRemove)
            assertFalse(UserInfo.getInstance().checkTitleStarList(keyword));
        String[] contentBlackListToRemove = {"cbl6", "cbl7", "cbl8", "cbl9", "cbl10"};
        for (String keyword : contentBlackListToRemove)
            UserInfo.getInstance().removeContentBlackList(keyword);
        for (String keyword : contentBlackListToRemove)
            assertFalse(UserInfo.getInstance().checkContentBlackList(keyword));
        String[] contentWhiteListToRemove = {"cwl6", "cwl7", "cwl8", "cwl9", "cwl10"};
        for (String keyword : contentWhiteListToRemove)
            UserInfo.getInstance().removeContentWhiteList(keyword);
        for (String keyword : contentWhiteListToRemove)
            assertFalse(UserInfo.getInstance().checkContentWhiteList(keyword));
        String[] contentStarListToRemove = {"csl6", "csl7", "csl8", "csl9", "csl10"};
        for (String keyword : contentStarListToRemove)
            UserInfo.getInstance().removeContentStarList(keyword);
        for (String keyword : contentStarListToRemove)
            assertFalse(UserInfo.getInstance().checkContentStarList(keyword));
        String[] importantEmailAddressListToRemove = {"iel6", "iel7", "iel8", "iel9", "iel10"};
        for (String keyword : importantEmailAddressListToRemove)
            UserInfo.getInstance().removeImportantEmailAddressList(keyword);
        for (String keyword : importantEmailAddressListToRemove)
            assertFalse(UserInfo.getInstance().checkImportantEmailAddressList(keyword));
    }
}
