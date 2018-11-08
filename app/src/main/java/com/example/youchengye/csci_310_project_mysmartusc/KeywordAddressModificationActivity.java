package com.example.youchengye.csci_310_project_mysmartusc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class KeywordAddressModificationActivity extends AppCompatActivity {

    private static Integer currentListID;
    private ProgressBar databaseLoadingSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_address_modification);
        databaseLoadingSpinner = (ProgressBar)findViewById(R.id.databaseProgressBar);
        displayAndStartDatabaseLoadingSpinner();
        populateSpinnerValues();
        currentListID = 0;

        if(getIntent()!=null){
            Bundle extras = getIntent().getExtras();
            if (extras!=null){
                String username = extras.getString("username");
                UserInfo.getInstance().Initialize(username, this);
            }
            Log.w("intent","not null");
        }
    }

    // Database Retrieval Waiting spinner
    public void displayAndStartDatabaseLoadingSpinner() {
        databaseLoadingSpinner.setIndeterminate(true);
        databaseLoadingSpinner.setVisibility(View.VISIBLE);
    }

    public void hideAndStopDatabaseLoadingSpinner() {
        databaseLoadingSpinner.setIndeterminate(false);
        databaseLoadingSpinner.setVisibility(View.GONE);
    }

    // List Selection Spinner
    private void populateSpinnerValues() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.keywords_modification_list_choices, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set OnItemSelectedListener
        spinner.setOnItemSelectedListener(new OnSpinnerListSelected());
    }

    private class OnSpinnerListSelected implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> adapterView, View view, int listIndex, long l) {
            KeywordAddressModificationActivity.currentListID = listIndex;
            updateList(false); // initially not in modification state
            Button modifyListButton = (Button)findViewById((R.id.modifyList));
            modifyListButton.setText("Modify List");
            Button addKeywordButton = (Button)findViewById(R.id.addKeyword);
            addKeywordButton.setVisibility(View.GONE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Nothing to do
        }
    }

    // List Modification Buttons
    public void onClickModifyList(View view) {
        ListView currentListView = (ListView)findViewById(R.id.currlist);

        if (((Button)view).getText().equals("Modify List")) {
            // Modifying list
            ((Button)view).setText("Done!");
            Button addKeywordButton = (Button)findViewById(R.id.addKeyword);
            addKeywordButton.setVisibility(View.VISIBLE);
            updateList(true);
        }
        else {
            // Done Modifying list
            ((Button)view).setText("Modify List");
            Button addKeywordButton = (Button)findViewById(R.id.addKeyword);
            addKeywordButton.setVisibility(View.GONE);
            // Write to Database
            UserInfo.getInstance().writeToDatabase();
            updateList(false);
        }

    }

    public void onClickAddKeyword(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(KeywordAddressModificationActivity.this).create();

        // Set Custom Title
        TextView title = new TextView(KeywordAddressModificationActivity.this);
        // Title Properties
        title.setText("+ Add New Keyword");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        EditText newKeyword = new EditText(KeywordAddressModificationActivity.this);
        newKeyword.setSingleLine();
        newKeyword.setId(R.id.new_keyword_edit_text);
        // Message Properties
        newKeyword.setGravity(Gravity.CENTER_HORIZONTAL);
        newKeyword.setTextColor(Color.BLACK);
        alertDialog.setView(newKeyword);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"ADD NEW KEYWORD", new OnKeywordAddedListener(newKeyword));

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Nothing to do
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.RED);
        cancelBT.setLayoutParams(negBtnLP);
    }

    // List Keywords Display/Update
    public void updateList(boolean showClickToRemove) {
        switch (currentListID) {
            case 0:
                updateList(UserInfo.getInstance().getTitleBlackList(), showClickToRemove);
                break;
            case 1:
                updateList(UserInfo.getInstance().getTitleWhiteList(), showClickToRemove);
                break;
            case 2:
                updateList(UserInfo.getInstance().getTitleStarList(), showClickToRemove);
                break;
            case 3:
                updateList(UserInfo.getInstance().getContentBlackList(), showClickToRemove);
                break;
            case 4:
                updateList(UserInfo.getInstance().getContentWhiteList(), showClickToRemove);
                break;
            case 5:
                updateList(UserInfo.getInstance().getContentStarList(), showClickToRemove);
                break;
            case 6:
                updateList(UserInfo.getInstance().getImportantEmailAddressList(), showClickToRemove);
                break;
        }
    }

    private void updateList(List<String> updatedList, boolean showClickToRemove) {
        ListView currentListView = (ListView)findViewById(R.id.currlist);
        updatedList = new ArrayList<String>((ArrayList<String>) updatedList); // make a deep copy to prevent modifying the list in UserInfo
        if (showClickToRemove) {
            currentListView.setOnItemClickListener(new OnDeleteWordRowListener());
            for (int i = 0; i < updatedList.size(); ++i)
                updatedList.set(i, updatedList.get(i) + " - click to remove");
        }
        else {
            currentListView.setOnItemClickListener(null);
        }
        ArrayAdapter<String> currListAdapter = new ArrayAdapter<String>(this, R.layout.single_word_row, updatedList);
        currentListView.setAdapter(currListAdapter);
    }

    // List Keywords Addition/Deletion
    private class OnKeywordAddedListener implements DialogInterface.OnClickListener {
        EditText newKeywordTextBox;
        public OnKeywordAddedListener(EditText nktb) {
            newKeywordTextBox = nktb;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String keyword = newKeywordTextBox.getText().toString();
            switch (currentListID) {
                case 0:
                    UserInfo.getInstance().addTitleBlackList(keyword);
                    break;
                case 1:
                    UserInfo.getInstance().addTitleWhiteList(keyword);
                    break;
                case 2:
                    UserInfo.getInstance().addTitleStarList(keyword);
                    break;
                case 3:
                    UserInfo.getInstance().addContentBlackList(keyword);
                    break;
                case 4:
                    UserInfo.getInstance().addContentWhiteList(keyword);
                    break;
                case 5:
                    UserInfo.getInstance().addContentStarList(keyword);
                    break;
                case 6:
                    UserInfo.getInstance().addImportantEmailAddressList(keyword);
                    break;
            }
            updateList(true);
        }
    }

    private class OnDeleteWordRowListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AlertDialog alertDialog = new AlertDialog.Builder(KeywordAddressModificationActivity.this).create();
            populatePromptForDeletion(alertDialog, view);

        }

        private void populatePromptForDeletion(AlertDialog alertDialog, View view) {
            // Set Custom Title
            TextView title = new TextView(KeywordAddressModificationActivity.this);
            // Title Properties
            title.setText("Confirm Deletion");
            title.setPadding(10, 10, 10, 10);   // Set Position
            title.setGravity(Gravity.CENTER);
            title.setTextColor(Color.BLACK);
            title.setTextSize(20);
            alertDialog.setCustomTitle(title);

            // Set Message
            TextView msg = new TextView(KeywordAddressModificationActivity.this);
            // Message Properties
            String keyword = ((TextView)view).getText().toString();
            keyword = keyword.substring(0, keyword.length() - 18);
            msg.setText("Are you sure you want to delete keyword " + keyword + "?");
            msg.setGravity(Gravity.CENTER_HORIZONTAL);
            msg.setTextColor(Color.BLACK);
            alertDialog.setView(msg);

            // Set Button
            // you can more buttons
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"YES! REMOVE IT FOR ME", new OnConfirmDeleteListener(keyword));

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"NO! KEEP IT", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Nothing to do
                }
            });

            new Dialog(getApplicationContext());
            alertDialog.show();

            // Set Properties for OK Button
            final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
            LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
            neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
            okBT.setPadding(50, 10, 10, 10);   // Set Position
            okBT.setTextColor(Color.BLUE);
            okBT.setLayoutParams(neutralBtnLP);

            final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
            negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
            cancelBT.setTextColor(Color.RED);
            cancelBT.setLayoutParams(negBtnLP);
        }

        private class OnConfirmDeleteListener implements DialogInterface.OnClickListener {
            String keyword;
            public OnConfirmDeleteListener(String keyword) {
                this.keyword = keyword;
            }
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (currentListID) {
                    case 0:
                        UserInfo.getInstance().removeTitleBlackList(keyword);
                        break;
                    case 1:
                        UserInfo.getInstance().removeTitleWhiteList(keyword);
                        break;
                    case 2:
                        UserInfo.getInstance().removeTitleStarList(keyword);
                        break;
                    case 3:
                        UserInfo.getInstance().removeContentBlackList(keyword);
                        break;
                    case 4:
                        UserInfo.getInstance().removeContentWhiteList(keyword);
                        break;
                    case 5:
                        UserInfo.getInstance().removeContentStarList(keyword);
                        break;
                    case 6:
                        UserInfo.getInstance().removeImportantEmailAddressList(keyword);
                        break;
                }
                KeywordAddressModificationActivity.this.updateList(true);
            }
        }
    }

}
