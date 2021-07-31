package com.example.demo.controller;

import com.example.demo.database.FirebaseService;
import com.example.demo.model.UserProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1.DocumentRemove;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class UserProfileController {
    private Map<String, UserProfile> userMap;
    public static final String COLLECTION_NAME = "userdb";

    @PostConstruct
    public void init(){
        userMap = new HashMap<String, UserProfile>();
        userMap.put("1",new UserProfile("1","hong","111-1111","seoul"));
        userMap.put("2",new UserProfile("2","kim","122-1221","seoul"));
        userMap.put("3",new UserProfile("3","lee","141-1141","seoul"));
    }

    @GetMapping("/user/{id}")
    public UserProfile getUserProfile(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        System.out.println("db" + db);
        UserProfile userProfile = null;
        ApiFuture<DocumentSnapshot> apiFuture = db.collection(COLLECTION_NAME).document("userProfileDB").get();
        DocumentSnapshot documentSnapshot = apiFuture.get();

        if(documentSnapshot.exists()){
            userProfile = documentSnapshot.toObject(UserProfile.class);
        }

        return userMap.get(id);
    }

    @GetMapping("user/all")
    public List<UserProfile> getUserProfileList(){
        return new ArrayList<UserProfile>(userMap.values());
    }

    @PutMapping("user/{id}")
    public void putUserProfile(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address){
        Firestore db = FirestoreClient.getFirestore();
        UserProfile userProfile = new UserProfile(id, name, phone, address);
        ApiFuture<WriteResult> apiFuture = db.collection(COLLECTION_NAME).document("userProfileDB").set(userProfile);

        userMap.put(id, userProfile);
    }

    @PostMapping("user/{id}")
    public void postUserProfile(@PathVariable("id") String id, @RequestParam("name") String name, @RequestParam("phone") String phone, @RequestParam("address") String address){
        UserProfile userProfile = userMap.get(id);
        userProfile.setName(name);
        userProfile.setPhone(phone);
        userProfile.setAddress(address);
    }

    @DeleteMapping("user/{id}")
    public void deleteUserProfile(@PathVariable("id") String id){
        Firestore db = FirestoreClient.getFirestore();
        UserProfile userProfile = userMap.get(id);
        userMap.remove(id);
    }
}
