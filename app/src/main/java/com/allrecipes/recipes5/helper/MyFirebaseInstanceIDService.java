package com.allrecipes.recipes5.helper;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFireBaseIDService";


    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // Saving reg id to shared preferences
        StoreToken(refreshedToken);
    }

    private void StoreToken(String token) {

        System.out.println("token***"+token);
        //we will save the token in sharedPreferences
       // AppController.getInstance().setFcm_id(token);
        //System.out.println("registration token  "+token);
      /*  PrefManager.setDeviceToken(token,getApplicationContext());
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);*/

        AppController.getInstance().saveToken(token);
    }


}
