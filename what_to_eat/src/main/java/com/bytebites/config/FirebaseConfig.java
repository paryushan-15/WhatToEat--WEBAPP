package com.bytebites.config;

import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class FirebaseConfig {
    static {
        getFireBaseConfig();
    }

     private static void getFireBaseConfig(){

          try{
                   
            InputStream serviceAccount =  FirebaseConfig.class
                                                  .getClassLoader()
                                                  .getResourceAsStream("WTE-API.json");

            FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase Initialized Successfully");


          } catch(Exception e ){
                e.printStackTrace();
          }
     }

     public static Firestore getFirestore(){
        return FirestoreClient.getFirestore();
     }
}

