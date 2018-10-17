package com.example.youchengye.csci_310_project_mysmartusc;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * A Singleton that contains all information about the current user
 */
public class UserInfo {
    // Constructor
    public UserInfo() {
        // configure firestore
        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        // before Initialization (retrieval from database) the UserInfo Object is not ready
        ready = false;
    }

    // Singleton Related Code
    private static UserInfo singleUserInfoInstance = null;

    public static UserInfo getInstance() {
        if (singleUserInfoInstance == null)
            singleUserInfoInstance = new UserInfo();
        return singleUserInfoInstance;
    }

    // Database (Firebase Firestore)
    FirebaseFirestore firestore;

    // Variable Initialization
    private String username;
    private List<String> titleBlackList = new ArrayList<String>();
    private List<String> titleWhiteList = new ArrayList<String>();
    private List<String> titleStarList = new ArrayList<String>();
    private List<String> contentBlackList = new ArrayList<String>();
    private List<String> contentWhiteList = new ArrayList<String>();
    private List<String> contentStarList = new ArrayList<String>();
    private List<String> importantEmailAddressList = new ArrayList<String>();
    private Set<String> titleBlackSet = new HashSet<String>();
    private Set<String> titleWhiteSet = new HashSet<String>();
    private Set<String> titleStarSet = new HashSet<String>();
    private Set<String> contentBlackSet = new HashSet<String>();
    private Set<String> contentWhiteSet = new HashSet<String>();
    private Set<String> contentStarSet = new HashSet<String>();
    private Set<String> importantEmailAddressSet = new HashSet<String>();
    public void Initialize(String username) {
        this.username = username;
        CollectionReference usersRef = firestore.collection("Users");
        // retrieve and initialize all the lists in UserData Object
        usersRef.whereEqualTo("username", username)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            HashMap<String, Object> databaseUserInfoRetrieval = (HashMap<String, Object>)document.getData();
                            for (String s: (ArrayList<String>) databaseUserInfoRetrieval.get("titleBlackList")) {
                                Log.d(TAG, "s1: " + s);
                            }
                            UserInfo.getInstance().setTitleBlackList((ArrayList<String>) databaseUserInfoRetrieval.get("titleBlackList"));
                            UserInfo.getInstance().setTitleWhiteList((ArrayList<String>) databaseUserInfoRetrieval.get("titleWhiteList"));
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            UserInfo.getInstance().setReady();
                            // information has been retrieved from database
                            // UserInfo object now ready
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });




    }
    // getters and setters
    public List<String> getTitleBlackList() {return titleBlackList;}
    public List<String> getTitleWhiteList() {return titleWhiteList;}
    public List<String> getTitleStarList() {return titleStarList;}
    public List<String> getContentBlackList() {return contentBlackList;}
    public List<String> getContentWhiteList() {return contentWhiteList;}
    public List<String> getContentStarList() {return contentStarList;}
    public List<String> getImportantEmailAddressList() {return importantEmailAddressList;}
    private void setListAndSet(String listName, List<String> toList) {

    }
    public void setTitleBlackList(List<String> newTitleBlackList) {
        if (newTitleBlackList == null) {
            titleBlackList.clear();
            titleBlackSet.clear();
        }
        else {
            titleBlackList = newTitleBlackList;
            titleBlackSet = new HashSet<String>(newTitleBlackList);
        }
    }
    public void setTitleWhiteList(List<String> newTitleWhiteList) {
        if (newTitleWhiteList == null) {
            titleWhiteList.clear();
            titleWhiteSet.clear();
        }
        else {
            titleWhiteList = newTitleWhiteList;
            titleWhiteSet = new HashSet<String>(newTitleWhiteList);
        }
    }
    public void setTitleStarList(List<String> newTitleStarList) {
        if (newTitleStarList == null) {
            titleStarList.clear();
            titleStarSet.clear();
        }
        else {
            titleStarList = newTitleStarList;
            titleStarSet = new HashSet<String>(newTitleStarList);
        }
    }
    public void setContentBlackList(List<String> newContentBlackList) {
        if (newContentBlackList == null) {
            contentBlackList.clear();
            contentBlackSet.clear();
        }
        else {
            contentBlackList = newContentBlackList;
            contentBlackSet = new HashSet<String>(newContentBlackList);
        }
    }
    public void setContentWhiteList(List<String> newContentWhiteList) {
        if (newContentWhiteList == null) {
            contentWhiteList.clear();
            contentWhiteSet.clear();
        }
        else {
            contentWhiteList = newContentWhiteList;
            contentWhiteSet = new HashSet<String>(newContentWhiteList);
        }
    }
    public void setContentStarList(List<String> newContentStarList) {
        if (newContentStarList == null) {
            contentStarList.clear();
            contentStarSet.clear();
        }
        else {
            contentStarList = newContentStarList;
            contentStarSet = new HashSet<String>(newContentStarList);
        }
    }
    public void setImportantEmailAddressList(List<String> newImportantEmailAddressList) {
        if (newImportantEmailAddressList == null) {
            importantEmailAddressList.clear();
            importantEmailAddressSet.clear();
        }
        else {
            importantEmailAddressList = newImportantEmailAddressList;
            importantEmailAddressSet = new HashSet<String>(newImportantEmailAddressList);
        }
    }

    // Database Retrieval Preparedness
    public boolean ready;
    public boolean isReady() {return ready;}
    public void setReady() {ready = true;}
}
