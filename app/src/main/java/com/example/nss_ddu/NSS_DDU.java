package com.example.nss_ddu;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;

public class NSS_DDU extends Application {

    private static NSS_DDU instance;
    private CognitoUserPool userPool;
    private CognitoCachingCredentialsProvider credentialsProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Initialize AWS Cognito User Pool
        initCognitoUserPool();
        initCognitoCredentialsProvider();
    }

    public static NSS_DDU getInstance() {
        return instance;
    }

    // Initialize the Cognito User Pool
    private void initCognitoUserPool() {
        userPool = new CognitoUserPool(
                getApplicationContext(),
                getString(R.string.cognito_pool_id),
                getString(R.string.cognito_client_id),
                null,
                Regions.US_EAST_1
        );
    }

    // Initialize the Cognito Credentials Provider
    private void initCognitoCredentialsProvider() {
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getAppContext(),
                getString(R.string.identity_pool_id), // Identity Pool ID from strings.xml
                Regions.US_EAST_1 // Replace with your region
        );
    }

    // Get the Cognito User Pool
    public CognitoUserPool getUserPool() {
        return userPool;
    }

    // Get the Cognito Credentials Provider
    public CognitoCachingCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }

    // Use this method to get application context from anywhere in the app
    public static Context getAppContext() {
        return instance.getApplicationContext();
    }
}
