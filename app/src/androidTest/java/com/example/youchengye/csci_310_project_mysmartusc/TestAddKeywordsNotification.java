package com.example.youchengye.csci_310_project_mysmartusc;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
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
    private String packagename = "com.example.youchengye.csci_310_project_mysmartusc";
    @Rule
    public ActivityTestRule activityRule = new AddKeywordsActivityTestRule(LoginActivity.class);
    public static UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());;

    @Test
    public void testEnterKeywords() throws UiObjectNotFoundException, InterruptedException {
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
        UiObject showList = device.findObject(new UiSelector().textContains("SHOW THIS LIST"));
        if (showList != null){
            showList.clickAndWaitForNewWindow();
        }
        UiObject modifyButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
        modifyButton.clickAndWaitForNewWindow();
        addKeyword(TITLE_MARK);

        UiObject listView = device.findObject(new UiSelector().className(ListView.class));
        device.wait(Until.hasObject(By.text(TITLE_MARK)), 2000);
        int old = listView.getChildCount();

        addKeyword(TITLE_MARK);
        UiObject newlistView = device.findObject(new UiSelector().className(ListView.class));
        int newNum = newlistView.getChildCount();
        assertEquals(old, newNum );

        UiObject done = device.findObject(new UiSelector().textContains("DONE!"));
        done.clickAndWaitForNewWindow();

    }

    @Test
    public void testNotificationReceived() throws UiObjectNotFoundException{
        device.wait(Until.hasObject(By.text("MySmartUSC")), 500000);
        device.openNotification();
        device.wait(Until.hasObject(By.textContains("Important")), 50000);

        assertTrue(device.hasObject(By.textContains("Important")));
        device.pressHome();
    }

    @Test
    public void testNotificationOnBackground(){
        device.wait(Until.hasObject(By.text("MySmartUSC")), 500000);
        device.openNotification();
        device.wait(Until.hasObject(By.textContains("Important")), 50000);
        device.pressHome();
        device.openNotification();
        assertTrue(device.hasObject(By.textContains("Important")));
        device.pressHome();
    }
    @Test
    public void testAddNewKeyword() {

        try {
            UiObject modifyListButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
            modifyListButton.clickAndWaitForNewWindow();
            UiObject addNewKeywordButton = device.findObject(new UiSelector().textContains("+ ADD NEW KEYWORD"));
            addNewKeywordButton.clickAndWaitForNewWindow();
            UiObject newKeywordEditText = device.findObject(new UiSelector().className("android.widget.EditText"));
            newKeywordEditText.waitForExists(100000);
            newKeywordEditText.setText("promotion");
            addNewKeywordButton = device.findObject(new UiSelector().textStartsWith("ADD NEW KEYWORD"));
            addNewKeywordButton.clickAndWaitForNewWindow();
            Thread.sleep(2000);

            assertTrue(device.hasObject(By.textContains("promotion")));
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testDeleteWord() throws UiObjectNotFoundException{
        UiObject showList = device.findObject(new UiSelector().textContains("SHOW THIS LIST"));
        if (showList != null){
            showList.clickAndWaitForNewWindow();
        }
        deleteWord();

        UiObject modifyButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
        modifyButton.clickAndWaitForNewWindow();

        addKeyword("1");
        addKeyword("2");

        UiObject listView = device.findObject(new UiSelector().className(ListView.class));
        int old = listView.getChildCount();
        if (old!=0){
            //click to remove a keyword
            UiObject remove = device.findObject(new UiSelector().textContains("remove"));
            remove.clickAndWaitForNewWindow();

            //confirm to delete
            UiObject confirm = device.findObject(new UiSelector().textContains("Yes"));
            confirm.clickAndWaitForNewWindow();

            UiObject done = device.findObject(new UiSelector().textContains("DONE!"));
            done.clickAndWaitForNewWindow();

            UiObject newlistView = device.findObject(new UiSelector().className(ListView.class));
            int newNum = newlistView.getChildCount();
            assertEquals(old, newNum+1);
        }
    }
    @Test
    public void testDeleteAllWords() throws UiObjectNotFoundException {
        if (device.hasObject(By.textContains("SHOW THIS LIST")) ==false){
            device.pressHome();
            openApp(packagename);
            login();
        }
        UiObject showList = device.findObject(new UiSelector().textContains("SHOW THIS LIST"));
        showList.clickAndWaitForNewWindow();

        deleteWord();
        UiObject listView = device.findObject(new UiSelector().className(ListView.class));
        int old = listView.getChildCount();
        assertEquals(0,old);
    }
    //able to add a lot of keywords and overload the listview we can see
    @Test
    public void testAddLotKeywords() throws UiObjectNotFoundException{
        try{
            UiObject modifyButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
            modifyButton.clickAndWaitForNewWindow();
        }catch (UiObjectNotFoundException e){
            device.pressHome();
            openApp(packagename);
            login();
            UiObject modifyButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
            modifyButton.clickAndWaitForNewWindow();
        }

        for (int i=0; i<10; i++){
            addKeyword(Integer.toString(i));
        }
        UiObject done = device.findObject(new UiSelector().textContains("DONE!"));
        done.clickAndWaitForNewWindow();
    }

    @Test
    public void testModifyThenShowList() throws UiObjectNotFoundException {

        UiObject modifyButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
        modifyButton.clickAndWaitForNewWindow();

        UiObject addNewKeywords = device.findObject(new UiSelector().textContains("+ ADD NEW KEYWORD"));
        assertTrue(addNewKeywords != null);
        UiObject showList = device.findObject(new UiSelector().textContains("SHOW THIS LIST"));
        showList.clickAndWaitForNewWindow();

        assertTrue(device.hasObject(By.textContains("+ ADD NEW KEYWORD")) == false);
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
        device.wait(Until.hasObject(By.textContains(lastList)),2000);
        UiObject spinner = device.findObject(new UiSelector().textContains(lastList));
        device.wait(Until.hasObject(By.textContains(lastList)),2000);
        spinner.clickAndWaitForNewWindow();

        device.wait(Until.hasObject(By.textContains(nextList)),2000);
        UiObject selectList = device.findObject(new UiSelector().textContains(nextList));
        device.wait(Until.hasObject(By.textContains(nextList)),2000);
        selectList.clickAndWaitForNewWindow();
        device.wait(Until.hasObject(By.textContains(nextList)),2000);

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

    private void addKeyword(String keyword) throws UiObjectNotFoundException {
        UiObject addKeyword = device.findObject(new UiSelector().textContains("+"));
        addKeyword.clickAndWaitForNewWindow();


        UiObject text = device.findObject(new UiSelector().className(EditText.class));
        text.setText(keyword);

        UiObject finishAdd = device.findObject(new UiSelector().text("ADD NEW KEYWORD"));
        finishAdd.clickAndWaitForNewWindow();
    }

    private void openApp(String packageName) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void login() throws UiObjectNotFoundException {
        UiObject sign_in_button = device.findObject(new UiSelector().clickable(true));
        sign_in_button.clickAndWaitForNewWindow();
        UiObject log_in = device.findObject(new UiSelector().textContains("@"));
        log_in.clickAndWaitForNewWindow();
    }

    private void deleteWord() throws UiObjectNotFoundException {
        UiObject modifyButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
        modifyButton.clickAndWaitForNewWindow();
        boolean remainingKeywords = true;
        while (remainingKeywords) {
            UiObject keywordToRemove = device.findObject(new UiSelector().textContains(" - click to remove"));
            remainingKeywords = keywordToRemove.exists();
            if (keywordToRemove.exists()) {
                keywordToRemove.click();
                UiObject confirmRemove = device.findObject(new UiSelector().textStartsWith("YES"));
                confirmRemove.waitForExists(10000);
                confirmRemove.click();
            }
        }
        UiObject done = device.findObject(new UiSelector().textContains("DONE!"));
        done.clickAndWaitForNewWindow();
    }
}

