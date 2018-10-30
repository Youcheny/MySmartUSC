package com.example.youchengye.csci_310_project_mysmartusc;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.widget.EditText;
import android.widget.ListView;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    public void test0EnterKeywords() throws UiObjectNotFoundException, InterruptedException {
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

        clickModifyButton();
        addKeyword(TITLE_MARK);

        UiObject listView = device.findObject(new UiSelector().className(ListView.class));
        device.wait(Until.hasObject(By.text(TITLE_MARK)), 2000);
        int old = listView.getChildCount();

        addKeyword(TITLE_MARK);
        UiObject newlistView = device.findObject(new UiSelector().className(ListView.class));
        int newNum = newlistView.getChildCount();
        assertEquals(old, newNum );

        clickDone();

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
    public void test1AddNewKeyword() {

        try {
            clickModifyButton();
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

        clickModifyButton();

        addKeyword("1");
        addKeyword("2");

        UiObject listView = device.findObject(new UiSelector().className(ListView.class));
        int old = listView.getChildCount();
        if (old!=0){
            //click to remove a keyword
            clickButton("remove");

            //confirm to delete
            clickButton("yes");

            clickDone();

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
            clickModifyButton();
        }catch (UiObjectNotFoundException e){
            device.pressHome();
            openApp(packagename);
            login();
            clickModifyButton();
        }

        for (int i=0; i<10; i++){
            addKeyword(Integer.toString(i));
        }

        clickDone();
    }

    @Test
    public void testModifyThenShowList() throws UiObjectNotFoundException {

        clickModifyButton();

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

        clickModifyButton();

        clickButton("ADD NEW KEYWORD");

        UiObject text = device.findObject(new UiSelector().className(EditText.class));
        text.setText(keyword);

        UiObject finishAdd = device.findObject(new UiSelector().text("ADD NEW KEYWORD"));
        finishAdd.clickAndWaitForNewWindow();

        clickDone();
    }

    private void addKeyword(String keyword) throws UiObjectNotFoundException {
        clickButton("+");

        UiObject text = device.findObject(new UiSelector().className(EditText.class));
        text.setText(keyword);

        UiObject addNew = device.findObject(new UiSelector().text("ADD NEW KEYWORD"));
        addNew.waitForExists(5000);
        addNew.click();
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
        UiObject log_in = device.findObject(new UiSelector().textContains("@usc"));
        log_in.clickAndWaitForNewWindow();
    }

    private void deleteWord() throws UiObjectNotFoundException {
        clickModifyButton();
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
        clickDone();
    }

    private void clickModifyButton() throws UiObjectNotFoundException{
        UiObject modifyButton = device.findObject(new UiSelector().textContains("MODIFY LIST"));
        modifyButton.waitForExists(5000);
        modifyButton.click();
    }

    private void clickDone() throws  UiObjectNotFoundException{
        UiObject modifyButton = device.findObject(new UiSelector().textContains("DONE!"));
        modifyButton.waitForExists(5000);
        modifyButton.click();
    }

    private void clickButton(String button) throws UiObjectNotFoundException{
        UiObject modifyButton = device.findObject(new UiSelector().textContains(button));
        modifyButton.waitForExists(5000);
        modifyButton.click();
    }
}

