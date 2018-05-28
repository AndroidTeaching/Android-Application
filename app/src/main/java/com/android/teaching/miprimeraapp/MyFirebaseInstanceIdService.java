package com.android.teaching.miprimeraapp;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Log.d("InstanceIdService", "EP! Token refreshed!");

        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseDatabase myDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = myDatabase
                .getReference("device_push_token");
        databaseReference.setValue(token);
    }
}
