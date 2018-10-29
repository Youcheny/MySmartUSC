package com.example.youchengye.csci_310_project_mysmartusc;

import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.hamcrest.core.AllOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
@RunWith(AndroidJUnit4.class)
public class TestAddKeywordsNotification {
    private String TITLE_MARK = "Title Mark";
    private String TITLE_IMPORTANT = "Title Important";
    private String TITLE_STAR = "Title Star";
    private String CONTENT_MARK = "Content Mark";
    private String CONTENT_IMPORTANT = "Content Important";
    private String CONTENT_STAR = "Content Star";
    private String IMPORTANT_EMAIL = "mysmartusc123@gmail.com";

    @Rule
    public ActivityTestRule activityRule = new AddKeywordsActivityTestRule(LoginActivity.class);
    public static UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());;

    @Test
    public void testEnterKeywords() throws UiObjectNotFoundException {
        populateKeywordList();
    }

    @Test
    public void testShowList() throws UiObjectNotFoundException {
        UiObject showList = device.findObject(new UiSelector().textContains("SHOW THIS LIST"));
        if (showList != null){
            showList.clickAndWaitForNewWindow();
        }
        assertTrue(device.findObject(new UiSelector().textContains(TITLE_MARK))!=null);
    }

    @Test
    public void testRepeatedKeywords() throws UiObjectNotFoundException, InterruptedException {
        UiObject listView = device.findObject(new UiSelector().className(ListView.class));
        int old = listView.getChildCount();

        UiObject showList = device.findObject(new UiSelector().textContains("SHOW THIS LIST"));
        if (showList != null){
            showList.clickAndWaitForNewWindow();
        }
        populateEachList("Title Mark As Read List", "Title Mark As Read List", TITLE_MARK);
        UiObject newlistView = device.findObject(new UiSelector().className(ListView.class));
        int newNum = newlistView.getChildCount();
        assertEquals(old, newNum );

    }

    @Test
    public void testNotificationReceived() throws UiObjectNotFoundException{
        device.wait(Until.hasObject(By.text("MySmartUSC")), 500000);
        device.openNotification();
        device.wait(Until.hasObject(By.textContains("Important")), 50000);

        assertTrue(device.hasObject(By.textContains("Important")));
    }

    @Test
    public void testNotificationOnBackground(){
        device.wait(Until.hasObject(By.text("MySmartUSC")), 500000);
        device.openNotification();
        device.wait(Until.hasObject(By.textContains("Important")), 50000);
        device.pressHome();
        device.openNotification();
        assertTrue(device.hasObject(By.textContains("Important")));
    }

    @Test
    public void testDeleteWord() throws UiObjectNotFoundException{
        //populate all lists, in case the list is empty
        populateKeywordList();

        UiObject showList = device.findObject(new UiSelector().textContains("SHOW THIS LIST"));
        if (showList != null){
            showList.clickAndWaitForNewWindow();
        }

        UiObject modifyButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
        modifyButton.clickAndWaitForNewWindow();

        UiObject listView = device.findObject(new UiSelector().className(ListView.class));
        int old = listView.getChildCount();
        if (old!=0){
            //click to remove a keyword
            UiObject remove = device.findObject(new UiSelector().textContains("remove"));
            remove.clickAndWaitForNewWindow();

            //confirm to delete
            UiObject confirm = device.findObject(new UiSelector().textContains("Yes"));
            confirm.clickAndWaitForNewWindow();

            UiObject newlistView = device.findObject(new UiSelector().className(ListView.class));
            int newNum = newlistView.getChildCount();
            assertEquals(old, newNum+1);
        }

    }
    @Test
    public void testModifyThenSHowList() throws UiObjectNotFoundException {

        UiObject modifyButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
        modifyButton.clickAndWaitForNewWindow();

        UiObject addNewKeywords = device.findObject(new UiSelector().textContains("+ ADD NEW KEYWORD"));
        Rect rect = addNewKeywords.getBounds();

        UiObject showList = device.findObject(new UiSelector().textContains("SHOW THIS LIST"));
        if (showList != null){
            showList.clickAndWaitForNewWindow();
        }

        UiObject addNewKeywordsChanged = device.findObject(new UiSelector().textContains("+ ADD NEW KEYWORD"));
        Rect rectNew = addNewKeywordsChanged.getBounds();
        assertEquals(rect.bottom, rectNew.bottom);
        assertEquals(rect.top, rectNew.top);

    }
    private void populateKeywordList() throws UiObjectNotFoundException {
        populateEachList("Title Mark As Read List", "Title Mark As Read List", TITLE_MARK);
        populateEachList("Title Mark As Read List", "Title Important List", TITLE_IMPORTANT);
        populateEachList("Title Important List", "Title Star List", TITLE_STAR);
        populateEachList("Title Star List", "Content Mark As Read List", CONTENT_MARK);
        populateEachList("Content Mark As Read List", "Content Important List", CONTENT_IMPORTANT);
        populateEachList("Content Important List", "Content Star List", CONTENT_STAR);
        populateEachList("Content Star List", "Important Email Addresses List", IMPORTANT_EMAIL);
    }

    private void populateEachList(String lastList, String nextList, String keyword) throws UiObjectNotFoundException {
        UiObject spinner = device.findObject(new UiSelector().textContains(lastList));
        spinner.clickAndWaitForNewWindow();

        UiObject selectList = device.findObject(new UiSelector().textContains(nextList));
        selectList.clickAndWaitForNewWindow();

        UiObject modifyButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
        modifyButton.clickAndWaitForNewWindow();

        UiObject addKeyword = device.findObject(new UiSelector().textContains("+ ADD NEW KEYWORD"));
        addKeyword.clickAndWaitForNewWindow();


        UiObject text = device.findObject(new UiSelector().className(EditText.class));
        text.setText(keyword);

        UiObject finishAdd = device.findObject(new UiSelector().text("ADD NEW KEYWORD"));
        finishAdd.clickAndWaitForNewWindow();

        UiObject done = device.findObject(new UiSelector().textContains("DONE!"));
        done.clickAndWaitForNewWindow();
    }
}

