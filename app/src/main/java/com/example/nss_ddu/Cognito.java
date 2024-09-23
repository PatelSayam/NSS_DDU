package com.example.nss_ddu;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;

public class Cognito {
    private static final String TAG = "Cognito";
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

    public void addAttribute(String key, String value) {
        userAttributes.addAttribute(key, value);
    }

    public void signUpInBackground(String userId, String password) {
        userPool.signUpInBackground(userId, password, this.userAttributes, null, signUpCallback);
    }

    private SignUpHandler signUpCallback = new SignUpHandler() {
        public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            Log.d(TAG, "Sign-up success");
            ((Activity) appContext).runOnUiThread(() ->
                    Toast.makeText(appContext, "Sign-up success", Toast.LENGTH_LONG).show()
            );

            if (!userConfirmed) {
                Log.d(TAG, "User needs to confirm their account. Code sent to: "
                        + cognitoUserCodeDeliveryDetails.getDestination());
            } else {
                Toast.makeText(appContext, "User already confirmed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
            Log.d(TAG, "Sign-up success with result: " + signUpResult);
            ((Activity) appContext).runOnUiThread(() -> Toast.makeText(appContext, "Sign-up success", Toast.LENGTH_LONG).show());
        }

        @Override
        public void onFailure(Exception exception) {
            ((Activity) appContext).runOnUiThread(() ->
                    Toast.makeText(appContext, "Sign-up failed: " + exception.getMessage(), Toast.LENGTH_LONG).show()
            );
            Log.d(TAG, "Sign-up failed: " + exception.getMessage());
        }
    };



    public void confirmUser(String userId, String code) {
        CognitoUser cognitoUser = userPool.getUser(userId);
        cognitoUser.confirmSignUpInBackground(code, false, confirmationCallback);
    }

    private GenericHandler confirmationCallback = new GenericHandler() {
        @Override
        public void onSuccess() {
            Toast.makeText(appContext, "User Confirmed", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(appContext, "User confirmation failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "User confirmation failed: " + exception);
        }
    };

    public void userLogin(String userId, String password, LoginCallback callback) {
        CognitoUser cognitoUser = userPool.getUser(userId);
        this.userPassword = password;
        cognitoUser.getSessionInBackground(new AuthenticationHandler() {
            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {
                Log.d(TAG, "Authentication challenge received.");
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
            public void getAuthenticationDetails(AuthenticationContinuation continuation, String userId) {
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
                continuation.setAuthenticationDetails(authenticationDetails);
                continuation.continueTask();
                Log.d(TAG, "Authentication details sent.");
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                Log.d(TAG, "MFA code required.");
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

    public static class PasswordResetViewModel extends ViewModel {
        private MutableLiveData<ForgotPasswordContinuation> continuation = new MutableLiveData<>();

        public void setForgotPasswordContinuation(ForgotPasswordContinuation continuation) {
            this.continuation.setValue(continuation);
        }

        public LiveData<ForgotPasswordContinuation> getForgotPasswordContinuation() {
            return continuation;
        }
    }

    public void forgotPassword(String email, ForgotPasswordCallback callback, Fragment fragment) {
        CognitoUser cognitoUser = userPool.getUser(email);
        cognitoUser.forgotPasswordInBackground(new ForgotPasswordHandler() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Forgot password request successful.");
            }

            @Override
            public void getResetCode(ForgotPasswordContinuation continuation) {
                PasswordResetViewModel viewModel = new ViewModelProvider(fragment.requireActivity()).get(PasswordResetViewModel.class);
                viewModel.setForgotPasswordContinuation(continuation);
                Log.d(TAG, "Reset code requested. Continuation set: " + (continuation != null));
                callback.onSuccess();
            }

            @Override
            public void onFailure(Exception exception) {
                Log.d(TAG, "Forgot password failed: " + exception.getMessage());
                callback.onFailure(exception);
            }
        });
    }

    public interface ForgotPasswordCallback {
        void onSuccess();

        void onFailure(Exception exception);
    }

    public void confirmForgotPassword(String email, String resetCode, String newPassword, ConfirmPasswordCallback callback, Fragment fragment) {
        PasswordResetViewModel viewModel = new ViewModelProvider(fragment.requireActivity()).get(PasswordResetViewModel.class);

        ForgotPasswordContinuation continuation = viewModel.getForgotPasswordContinuation().getValue();
        if (continuation == null) {
            callback.onFailure1(new Exception("Reset code not received or expired"));
            return;
        }

        Log.d(TAG, "Attempting to confirm forgot password with email: " + email + " and resetCode: " + resetCode);

        try {
            continuation.setPassword(newPassword);
            continuation.setVerificationCode(resetCode);
            continuation.continueTask();
            callback.onSuccess1();
        } catch (Exception e) {
            Log.e(TAG, "Error during password reset", e);
            callback.onFailure1(e);
        }
    }

    public interface ConfirmPasswordCallback {
        void onSuccess1();
        void onFailure1(Exception exception);
    }
}
