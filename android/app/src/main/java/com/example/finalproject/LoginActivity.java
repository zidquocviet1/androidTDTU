package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.api.UserAPI;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.models.Account;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.viewmodel.ToeicViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CallbackManager fbCallback;
    private ActivityLoginBinding binding;
    private ToeicViewModel toeicViewModel;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkController.isOnline(context)) {
                toeicViewModel.getNetworkState().postValue(true);
            } else {
                toeicViewModel.getNetworkState().postValue(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(binding.getRoot());

        initViewModel();
        setupLoginFacebook();
        handleLogoutFacebook();

        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        binding.btnLogin.setOnClickListener(this);
        binding.txtSignup.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fbCallback.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViewModel() {
        toeicViewModel = new ViewModelProvider(this).get(ToeicViewModel.class);
    }

    private void handleLogoutFacebook() {
        Account account = MyDatabase.getInstance(this).userDAO().getFirstAccount();
        if (account != null && account.getType().equals("facebook") && !account.isLogin())
            LoginManager.getInstance().logOut();
    }

    private void handleLoginFacebook(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Account.saveAccountInfo(user, this);
                        navigateHome();
                        Log.e("TAG", "Sign in with credential successfully! " + user.getUid());
                    } else {
                        Log.e("TAG", "Sign in with credential failure!" + task.getException());
                    }
                });
    }

    public ToeicViewModel getVM() {
        return toeicViewModel;
    }

    public ActivityLoginBinding getBinding() {
        return binding;
    }

    public void navigateHome() {
        this.finish();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void setupLoginFacebook() {
        fbCallback = CallbackManager.Factory.create();
        binding.btnLoginFB.setPermissions(Arrays.asList("email", "public_profile"));
        binding.btnLoginFB.registerCallback(fbCallback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("TAG", "Login success. Result: " + loginResult.getAccessToken().getExpires());
                handleLoginFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e("TAG", "Login cancel.");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("TAG", "Login fails. Error: " + error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                String username = binding.layoutUsername.getEditText().getText().toString();
                String password = binding.layoutPassword.getEditText().getText().toString();

                if (!toeicViewModel.getNetworkState().getValue()) {
                    Toast.makeText(this, getText(R.string.connection),
                            Toast.LENGTH_SHORT).show();
                } else if (!toeicViewModel.getServerState().getValue()) {
                    Toast.makeText(this, getText(R.string.server),
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (username.length() < 8){
                        binding.layoutUsername.setError(getText(R.string.username_error));
                        binding.layoutUsername.getEditText().requestFocus();
                        binding.layoutPassword.setErrorEnabled(false);
                        return;
                    }
                    if (password.length() < 8){
                        binding.layoutPassword.setError(getText(R.string.password_error));
                        binding.layoutPassword.getEditText().requestFocus();
                        binding.layoutUsername.setErrorEnabled(false);
                        return;
                    }
                    binding.layoutUsername.setErrorEnabled(false);
                    binding.layoutPassword.setErrorEnabled(false);
                    Account account = new Account("abc", username, password, "default", false);
                    account.encryptPassword();

                    UserAPI.handleLogin(this, account);
                }
                break;
            case R.id.txtSignup:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }
}