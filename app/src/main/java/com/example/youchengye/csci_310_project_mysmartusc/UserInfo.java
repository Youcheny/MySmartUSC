package com.example.youchengye.csci_310_project_mysmartusc;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;

public class UserInfo {
    // A Singleton that contains all information about the current user
    public UserInfo() {
        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
    }
    // Singleton
    private static UserInfo singleUserInfoInstance = null;

    public static UserInfo getInstance() {
        if (singleUserInfoInstance == null)
            singleUserInfoInstance = new UserInfo();
        return singleUserInfoInstance;
    }
    // End Singleton Related Code

    // Database (Firebase Firestore)
    FirebaseFirestore firestore;
    // End Database

    // Variable Initialization
    private String username;
    private Set<String> titleBlackSet = new HashSet<String>();
    private Set<String> titleWhiteSet = new HashSet<String>();
    private Set<String> titleStarSet = new HashSet<String>();
    private Set<String> contentBlackSet = new HashSet<String>();
    private Set<String> contentWhiteSet = new HashSet<String>();
    private Set<String> contentStarSet = new HashSet<String>();
    private Set<String> importantEmailAddressSet = new HashSet<String>();
    public void Initialize(String username) {
        this.username = username;
        titleBlackSet.clear();
        titleWhiteSet.clear();
        titleStarSet.clear();
        contentBlackSet.clear();
        contentWhiteSet.clear();
        contentStarSet.clear();
        importantEmailAddressSet.clear();
        // Test Firebase:
//        firestore.collection("Users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w(TAG, "Error getting documents.", task.getException());
//                        }
//                    }
//                });
        
    }
}
