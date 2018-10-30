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

import java.util.List;

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


    /**
     * add one keyword to each list to prepare for other tests
     * @throws UiObjectNotFoundException
     */
    @Test
    public void test_01_EnterKeywords() throws UiObjectNotFoundException {
        populateKeywordList();
    }

    /**
     * test add one keyword
     */
    @Test
    public void test_02_AddNewKeyword() {

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


    /**
     * test if the listview will appear after press show this list
     * @throws UiObjectNotFoundException
     */
    @Test
    public void test_03_ShowList() throws UiObjectNotFoundException {
        UiObject showList = device.findObject(new UiSelector().textContains("SHOW THIS LIST"));
        if (showList != null){
            showList.clickAndWaitForNewWindow();
        }
        assertTrue(device.findObject(new UiSelector().textContains(TITLE_MARK))!=null);
    }

    /**
     * test if repeated keywords will be added to listview
     * @throws UiObjectNotFoundException
     * @throws InterruptedException
     */
    @Test
    public void test_04_RepeatedKeywords() throws UiObjectNotFoundException, InterruptedException {
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

    /**
     * test if notification can be revceived and show in notification status bar
     * @throws UiObjectNotFoundException
     */
    @Test
    public void test_05_NotificationReceived() throws UiObjectNotFoundException{
        device.wait(Until.hasObject(By.text("MySmartUSC")), 500000);
        device.openNotification();
        device.wait(Until.hasObject(By.textContains("Important")), 50000);

        assertTrue(device.hasObject(By.textContains("Important")));
        device.pressHome();
    }

    /**
     * test if notification can be received when the app is in the background
     */
    @Test
    public void test_08_NotificationOnBackground(){
        device.wait(Until.hasObject(By.text("MySmartUSC")), 500000);
        device.openNotification();
        device.wait(Until.hasObject(By.textContains("Important")), 50000);
        device.pressHome();
        device.openNotification();
        assertTrue(device.hasObject(By.textContains("Important")));
        device.pressHome();
    }


    /**
     * test if delete one word works
     * @throws UiObjectNotFoundException
     */
    @Test
    public void test_09_DeleteWord() throws UiObjectNotFoundException{
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

    /**
     * test if we add a lot of keywords, the listview should not be overloaded and crush
     * @throws UiObjectNotFoundException
     */
    @Test
    public void test_10_AddLotKeywords() throws UiObjectNotFoundException{
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

    /**
     * test if we can delete all keywords from one list
     * @throws UiObjectNotFoundException
     */
    @Test
    public void test_11_DeleteAllWords() throws UiObjectNotFoundException {
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

    /**
     * test if the "+ add new keyword" button will disappear after the "show this list" button is pressed
     * @throws UiObjectNotFoundException
     */
    @Test
    public void test_12_ModifyThenShowList() throws UiObjectNotFoundException {

        clickModifyButton();

        UiObject addNewKeywords = device.findObject(new UiSelector().textContains("+ ADD NEW KEYWORD"));
        assertTrue(addNewKeywords != null);
        UiObject showList = device.findObject(new UiSelector().textContains("SHOW THIS LIST"));
        showList.clickAndWaitForNewWindow();

        assertTrue(device.hasObject(By.textContains("+ ADD NEW KEYWORD")) == false);
    }

    /**
     * test first clearing every list, then adding everyone's names and deleting everyone's names
     */
    @Test
    public void test_13_AddKeywordsToThenClearEachList() {
        try {
            clearEachList("Title Mark As Read List", "Title Mark As Read List");
            clearEachList("Title Mark As Read List", "Title Important List");
            clearEachList("Title Important List", "Title Star List");
            clearEachList("Title Star List", "Content Mark As Read List");
            clearEachList("Content Mark As Read List", "Content Important List");
            clearEachList("Content Important List", "Content Star List");
            clearEachList("Content Star List", "Important Email Addresses List");
            String[] programmersList = {"Ruoxi Jia", "Youcheng Ye", "Qiusi Li", "Tianli Yu", "Tuling Zhao", "Thank you for using our app!"};
            populateEachListWithStringArray("Important Email Addresses List", "Title Mark As Read List", programmersList);
            populateEachListWithStringArray("Title Mark As Read List", "Title Important List", programmersList);
            populateEachListWithStringArray("Title Important List", "Title Star List", programmersList);
            populateEachListWithStringArray("Title Star List", "Content Mark As Read List", programmersList);
            populateEachListWithStringArray("Content Mark As Read List", "Content Important List", programmersList);
            populateEachListWithStringArray("Content Important List", "Content Star List", programmersList);
            populateEachListWithStringArray("Content Star List", "Important Email Addresses List", programmersList);
            clearEachList("Important Email Addresses List", "Title Mark As Read List");
            clearEachList("Title Mark As Read List", "Title Important List");
            clearEachList("Title Important List", "Title Star List");
            clearEachList("Title Star List", "Content Mark As Read List");
            clearEachList("Content Mark As Read List", "Content Important List");
            clearEachList("Content Important List", "Content Star List");
            clearEachList("Content Star List", "Important Email Addresses List");

        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
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
        spinner.click();

        device.wait(Until.hasObject(By.textContains(nextList)),2000);
        UiObject selectList = device.findObject(new UiSelector().textContains(nextList));
        device.wait(Until.hasObject(By.textContains(nextList)),2000);
        selectList.click();
        device.wait(Until.hasObject(By.textContains(nextList)),2000);

        clickModifyButton();

        clickButton("ADD NEW KEYWORD");

        UiObject text = device.findObject(new UiSelector().className(EditText.class));
        text.setText(keyword);

        UiObject finishAdd = device.findObject(new UiSelector().text("ADD NEW KEYWORD"));
        finishAdd.clickAndWaitForNewWindow();

        clickDone();
    }

    private void populateEachListWithStringArray (String lastList, String nextList, String[] keywords) throws UiObjectNotFoundException {
        device.wait(Until.hasObject(By.textContains(lastList)),2000);
        UiObject spinner = device.findObject(new UiSelector().textContains(lastList));
        device.wait(Until.hasObject(By.textContains(lastList)),2000);
        spinner.click();

        device.wait(Until.hasObject(By.textContains(nextList)),2000);
        UiObject selectList = device.findObject(new UiSelector().textContains(nextList));
        device.wait(Until.hasObject(By.textContains(nextList)),2000);
        selectList.click();
        device.wait(Until.hasObject(By.textContains(nextList)),2000);

        clickModifyButton();
        for (String keyword : keywords) {
            clickButton("ADD NEW KEYWORD");
            UiObject text = device.findObject(new UiSelector().className(EditText.class));
            text.waitForExists(5000);
            text.setText(keyword);
            UiObject finishAdd = device.findObject(new UiSelector().text("ADD NEW KEYWORD"));
            finishAdd.waitForExists(5000);
            finishAdd.click();
        }



        clickDone();
    }

    private void clearEachList(String lastList, String nextList) throws UiObjectNotFoundException {
        device.wait(Until.hasObject(By.textContains(lastList)),2000);
        UiObject spinner = device.findObject(new UiSelector().textContains(lastList));
        device.wait(Until.hasObject(By.textContains(lastList)),2000);
        spinner.click();

        device.wait(Until.hasObject(By.textContains(nextList)),2000);
        UiObject selectList = device.findObject(new UiSelector().textContains(nextList));
        device.wait(Until.hasObject(By.textContains(nextList)),2000);
        selectList.click();
        device.wait(Until.hasObject(By.textContains(nextList)),2000);

        deleteWord();
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

