package com.example.youchengye.csci_310_project_mysmartusc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class KeywordAddressModificationActivity extends AppCompatActivity {

    private static Integer currentListID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_address_modification);
        // Populate the spinner with default values
        populateSpinnerValues();
        currentListID = 0;
        // wait while the list values are retrieved from the database
        UserInfo.getInstance().Initialize("youcheny"); // "youcheny" to be changed later

//        ListView mainListView = (ListView) findViewById( R.id.here );
//
//        // Create and populate a List of planet names.
//        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
//                "Jupiter", "Saturn", "Uranus", "Neptune"};
//        ArrayList<String> planetList = new ArrayList<String>();
//        planetList.addAll( Arrays.asList(planets) );
//
//        // Create ArrayAdapter using the planet list.
//        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
//
//        // Add more planets. If you passed a String[] instead of a List<String>
//        // into the ArrayAdapter constructor, you must not add more items.
//        // Otherwise an exception will occur.
//        listAdapter.add( "Ceres" );
//        listAdapter.add( "Pluto" );
//        listAdapter.add( "Haumea" );
//        listAdapter.add( "Makemake" );
//        listAdapter.add( "Eris" );
//
//        // Set the ArrayAdapter as the ListView's adapter.
//        mainListView.setAdapter( listAdapter );

    }

    protected void populateSpinnerValues() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.keywords_modification_list_choices, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    protected void updateList() {
        switch (currentListID) {
            case 0:
                updateList(UserInfo.getInstance().getTitleBlackList());
        }
    }

    protected void updateList(List<String> updatedList) {
        ListView currentListView = (ListView)findViewById(R.id.currlist);
        ArrayAdapter<String> currListAdapter = new ArrayAdapter<String>(this, R.layout.single_word_row, updatedList);
        currentListView.setAdapter(currListAdapter);
    }

    public void onClickShowList(View view) {
        updateList();
    }

    public void onClickModifyList(View view) {

    }
}
