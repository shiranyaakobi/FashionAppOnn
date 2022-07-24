package com;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.idc.fashion.Firebase.Database;
import com.idc.fashion.Model.CategoryItems;
import com.idc.fashion.Model.Item;
import com.idc.fashion.SecurityHelper;

public class FashionApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);


        // Analytics and Crashlytics
        FirebaseCrashlytics.getInstance().checkForUnsentReports();
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);

    }
}
