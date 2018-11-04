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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Document;

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
    public String username;
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
    private boolean titleBlackListChanged;
    private boolean titleWhiteListChanged;
    private boolean titleStarListChanged;
    private boolean contentBlackListChanged;
    private boolean contentWhiteListChanged;
    private boolean contentStarListChanged;
    private boolean importantEmailAddressListChanged;

    private class OnDatabaseRetrievalCompleteListener implements OnCompleteListener {
        KeywordAddressModificationActivity ui;
        public OnDatabaseRetrievalCompleteListener(KeywordAddressModificationActivity ui) {
            this.ui = ui;
        }
        @Override
        public void onComplete(@NonNull Task task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = (DocumentSnapshot)task.getResult();
                HashMap<String, Object> databaseUserInfoRetrieval = (HashMap<String, Object>)document.getData();
                if (databaseUserInfoRetrieval == null) databaseUserInfoRetrieval = new HashMap<String, Object>(); // empty hashmap to prevent crash
                setTitleBlackList((ArrayList<String>) databaseUserInfoRetrieval.get("titleBlackList"));
                setTitleWhiteList((ArrayList<String>) databaseUserInfoRetrieval.get("titleWhiteList"));
                setTitleStarList((ArrayList<String>) databaseUserInfoRetrieval.get("titleStarList"));
                setContentBlackList((ArrayList<String>) databaseUserInfoRetrieval.get("contentBlackList"));
                setContentWhiteList((ArrayList<String>) databaseUserInfoRetrieval.get("contentWhiteList"));
                setContentStarList((ArrayList<String>) databaseUserInfoRetrieval.get("contentStarList"));
                setImportantEmailAddressList((ArrayList<String>) databaseUserInfoRetrieval.get("importantEmailAddressList"));
                Log.d(TAG, document.getId() + " => " + document.getData());
                // information has been retrieved from database
                ui.updateList(false);
            }
        }
    }
    public void Initialize(String username, KeywordAddressModificationActivity ui) {
        this.username = username;
        titleBlackListChanged = false;
        titleWhiteListChanged = false;
        titleStarListChanged = false;
        contentBlackListChanged = false;
        contentWhiteListChanged = false;
        contentStarListChanged = false;
        importantEmailAddressListChanged = false;
        // retrieve and initialize all the lists in UserData Object
        DocumentReference userRef = firestore.collection("Users").document(username);
        userRef.get().addOnCompleteListener(new OnDatabaseRetrievalCompleteListener(ui));
    }
    // getters and setters
    public List<String> getTitleBlackList() {return titleBlackList;}
    public List<String> getTitleWhiteList() {return titleWhiteList;}
    public List<String> getTitleStarList() {return titleStarList;}
    public List<String> getContentBlackList() {return contentBlackList;}
    public List<String> getContentWhiteList() {return contentWhiteList;}
    public List<String> getContentStarList() {return contentStarList;}
    public List<String> getImportantEmailAddressList() {return importantEmailAddressList;}
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

    // Keywords Modification
    public void addTitleBlackList(String keyword) {if (!titleBlackSet.contains(keyword)){titleBlackList.add(keyword); titleBlackSet.add(keyword); titleBlackListChanged = true;}}
    public void addTitleWhiteList(String keyword) {if (!titleWhiteSet.contains(keyword)){titleWhiteList.add(keyword); titleWhiteSet.add(keyword); titleWhiteListChanged = true;}}
    public void addTitleStarList(String keyword) {if (!titleStarSet.contains(keyword)){titleStarList.add(keyword); titleStarSet.add(keyword); titleStarListChanged = true;}}
    public void addContentBlackList(String keyword) {if (!contentBlackSet.contains(keyword)){contentBlackList.add(keyword); contentBlackSet.add(keyword); contentBlackListChanged = true;}}
    public void addContentWhiteList(String keyword) {if (!contentWhiteSet.contains(keyword)){contentWhiteList.add(keyword); contentWhiteSet.add(keyword); contentWhiteListChanged = true;}}
    public void addContentStarList(String keyword) {if (!contentStarSet.contains(keyword)){contentStarList.add(keyword); contentStarSet.add(keyword); contentStarListChanged = true;}}
    public void addImportantEmailAddressList(String keyword) {if (!importantEmailAddressSet.contains(keyword)){importantEmailAddressList.add(keyword); importantEmailAddressSet.add(keyword); importantEmailAddressListChanged = true;}}
    public void removeTitleBlackList(String keyword) {titleBlackList.remove(keyword); titleBlackSet.remove(keyword); titleBlackListChanged = true;}
    public void removeTitleWhiteList(String keyword) {titleWhiteList.remove(keyword); titleWhiteSet.remove(keyword); titleWhiteListChanged = true;}
    public void removeTitleStarList(String keyword) {titleStarList.remove(keyword); titleStarSet.remove(keyword); titleStarListChanged = true;}
    public void removeContentBlackList(String keyword) {contentBlackList.remove(keyword); contentBlackSet.remove(keyword); contentBlackListChanged = true;}
    public void removeContentWhiteList(String keyword) {contentWhiteList.remove(keyword); contentWhiteSet.remove(keyword); contentWhiteListChanged = true;}
    public void removeContentStarList(String keyword) {contentStarList.remove(keyword); contentStarSet.remove(keyword); contentStarListChanged = true;}
    public void removeImportantEmailAddressList(String keyword) {importantEmailAddressList.remove(keyword); importantEmailAddressSet.remove(keyword); importantEmailAddressListChanged = true;}

    // Keyword Checks
    public boolean checkTitleBlackList(String keyword) {return titleBlackSet.contains(keyword);}
    public boolean checkTitleWhiteList(String keyword) {return titleWhiteSet.contains(keyword);}
    public boolean checkTitleStarList(String keyword) {return titleStarSet.contains(keyword);}
    public boolean checkContentBlackList(String keyword) {return contentBlackSet.contains(keyword);}
    public boolean checkContentWhiteList(String keyword) {return contentWhiteSet.contains(keyword);}
    public boolean checkContentStarList(String keyword) {return contentStarSet.contains(keyword);}
    public boolean checkImportantEmailAddressList(String keyword) {return importantEmailAddressSet.contains(keyword);}

    // Write to database
    public void writeToDatabase() {
        Map<String, Object> changes = new HashMap<String, Object>();
        if (titleBlackListChanged)
            changes.put("titleBlackList", titleBlackList);
        if (titleWhiteListChanged)
            changes.put("titleWhiteList", titleWhiteList);
        if (titleStarListChanged)
            changes.put("titleStarList", titleStarList);
        if (contentBlackListChanged)
            changes.put("contentBlackList", contentBlackList);
        if (contentWhiteListChanged)
            changes.put("contentWhiteList", contentWhiteList);
        if (contentStarListChanged)
            changes.put("contentStarList", contentStarList);
        if (importantEmailAddressListChanged)
            changes.put("importantEmailAddressList", importantEmailAddressList);
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(username);
        userRef.set(changes, SetOptions.merge());
    }

    // Clear function
    public void clear() {
        titleBlackList.clear();
        titleWhiteList.clear();
        titleStarList.clear();
        contentBlackList.clear();
        contentWhiteList.clear();
        contentStarList.clear();
        importantEmailAddressList.clear();
        titleBlackSet.clear();
        titleWhiteSet.clear();
        titleStarSet.clear();
        contentBlackSet.clear();
        contentWhiteSet.clear();
        contentStarSet.clear();
        importantEmailAddressSet.clear();
        titleBlackListChanged = false;
        titleWhiteListChanged = false;
        titleStarListChanged = false;
        contentBlackListChanged = false;
        contentWhiteListChanged = false;
        contentStarListChanged = false;
        importantEmailAddressListChanged = false;
        username = "";
    }
}
