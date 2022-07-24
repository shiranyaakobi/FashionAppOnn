package com.idc.fashion;

import static com.idc.fashion.Firebase.Database.USERS_PATH;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.idc.fashion.Firebase.Database;
import com.idc.fashion.Model.User;


import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                /*
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),*/
                new AuthUI.IdpConfig.GoogleBuilder().build()/*
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()*/);


        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);

    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            if(FirebaseAuth.getInstance().getUid()!=null)
            FirebaseFirestore.getInstance()
                    .collection(USERS_PATH)
                    .document(FirebaseAuth.getInstance().getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (!documentSnapshot.exists()) {
                            Database.UserManager.saveUser(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                        }
                    });
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            if (response != null && response.getError() != null)
                Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
