package com.panashematsaudza.travelmantics;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class FirebaseUtil {

    public  static FirebaseDatabase mFirebaseDatabase;
    public  static DatabaseReference mDatabaseReference;
    public  static FirebaseAuth  mFirebaseAuth;
    public  static FirebaseStorage mStorage;
     public static StorageReference mStorageRef;
    public  static FirebaseAuth.AuthStateListener mAuthListner;
    private static  UserActivity caller;
    public static  FirebaseUtil firebaseUtil;
    public static ArrayList<TravelDeal> mdeals;
    private static int RC_SIGN_IN = 123;

    public  static boolean isAdmin;


   private FirebaseUtil(){}

    public  static  void  openFbReference(String ref , final UserActivity callerActivity){

       if (firebaseUtil == null){
           firebaseUtil = new FirebaseUtil();
           mFirebaseDatabase = FirebaseDatabase.getInstance();
           mFirebaseAuth = FirebaseAuth.getInstance();
           caller =callerActivity;
           mAuthListner = new FirebaseAuth.AuthStateListener() {
               @Override
               public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                   if(firebaseAuth.getCurrentUser() == null){
                       FirebaseUtil.signIn();
                   }else {

                       String userId = firebaseAuth.getUid();
                       checkAdmin(userId);
                       
                   }

                   Toast.makeText(callerActivity.getBaseContext(), "Welcome Back", Toast.LENGTH_LONG).show();


               }


           };
           connectStorage();


       }
        mdeals  = new ArrayList<TravelDeal>();
       mDatabaseReference = mFirebaseDatabase.getReference().child(ref);
    }

    private static void checkAdmin(String uid) {
       FirebaseUtil.isAdmin = false;

       DatabaseReference ref = mFirebaseDatabase.getReference()
               .child("admin")
               .child(uid);
        ChildEventListener listener  = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin = true;
                caller.showMenu();
                Log.d("Admin", "User is Administrator ");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(listener);
    }

    public static void attachListener() {
       mFirebaseAuth.addAuthStateListener(mAuthListner);

    }

    public static void detachListener() {
       mFirebaseAuth.removeAuthStateListener(mAuthListner);

    }


    private static  void signIn(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN);

    }

    public static void connectStorage(){
       mStorage = FirebaseStorage.getInstance();
       mStorageRef = mStorage.getReference().child("deals_pictures");

    }
}
