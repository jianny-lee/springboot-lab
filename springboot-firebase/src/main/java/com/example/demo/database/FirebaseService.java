package com.example.demo.database;

import com.example.demo.model.UserProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService{
    public static final String COLLECTION_NAME = "userdb";

    public void insertUser(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address) {
        Firestore db = FirestoreClient.getFirestore();
        System.out.println("db" + db);
        UserProfile userProfile = new UserProfile(id, name, phone, address);
        ApiFuture<WriteResult> apiFuture = db.collection(COLLECTION_NAME).document("userProfileDB").set(userProfile);
    }

    public void selectUser(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        System.out.println("db" + db);
        UserProfile userProfile = null;
        ApiFuture<DocumentSnapshot> apiFuture = db.collection(COLLECTION_NAME).document("userProfileDB").get();
        DocumentSnapshot documentSnapshot = apiFuture.get();

        if(documentSnapshot.exists()){
            userProfile = documentSnapshot.toObject(UserProfile.class);
        }
    }
}
