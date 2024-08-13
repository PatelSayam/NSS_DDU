package com.example.nss_ddu;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;

import static android.content.ContentValues.TAG;

public class Cognito {
    private String poolID;
    private String clientID;
    private String clientSecret = null;
    private Regions awsRegion = Regions.US_EAST_1;
    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;
    private Context appContext;
    private String userPassword;

    public Cognito(Context context) {
        appContext = context;
        this.poolID = context.getString(R.string.cognito_pool_id);
        this.clientID = context.getString(R.string.cognito_client_id);
        userPool = new CognitoUserPool(context, this.poolID, this.clientID, this.clientSecret, this.awsRegion);
        userAttributes = new CognitoUserAttributes();
    }

    // Add user attributes
    public void addAttribute(String key, String value) {
        userAttributes.addAttribute(key, value);
    }

    public void signUpInBackground(String userId, String password) {
        userPool.signUpInBackground(userId, password, this.userAttributes, null, signUpCallback);
    }

    SignUpHandler signUpCallback = new SignUpHandler() {
        public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            Log.d(TAG, "Sign-up success");
            ((Activity) appContext).runOnUiThread(() -> {
                Toast.makeText(appContext, "Sign-up success", Toast.LENGTH_LONG).show();
            });

            if (!userConfirmed) {
                // Handle user confirmation here
            } else {
                Toast.makeText(appContext, "User already confirmed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onSuccess(CognitoUser user, SignUpResult signUpResult) {

        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(appContext, "Sign-up failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Sign-up failed: " + exception);
        }
    };

    public void confirmUser(String userId, String code) {
        CognitoUser cognitoUser = userPool.getUser(userId);
        cognitoUser.confirmSignUpInBackground(code, false, confirmationCallback);
    }

    GenericHandler confirmationCallback = new GenericHandler() {
        @Override
        public void onSuccess() {
            Toast.makeText(appContext, "User Confirmed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(appContext, "User confirmation failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public void userLogin(String userId, String password, LoginCallback callback) {
        CognitoUser cognitoUser = userPool.getUser(userId);
        this.userPassword = password;
        cognitoUser.getSessionInBackground(new AuthenticationHandler() {
            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                // Handle authentication challenge here
            }

            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                if (userSession.isValid()) {
                    Log.d(TAG, "Login successful: Session is valid.");
                    callback.onSuccess(userSession);
                } else {
                    Log.d(TAG, "Login failed: Session is invalid.");
                    callback.onFailure(new Exception("Invalid session"));
                }
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
                authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                // Handle MFA if required
            }

            @Override
            public void onFailure(Exception exception) {
                Log.d(TAG, "Login failed: " + exception.getMessage());
                callback.onFailure(exception);
            }
        });
    }

    public interface LoginCallback {
        void onSuccess(CognitoUserSession session);
        void onFailure(Exception exception);
    }
}
