package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.finalproject.api.UserAPI;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.models.User;
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

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CallbackManager fbCallback;
    private ActivityLoginBinding binding;
    private Dialog loadingDialog;
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
        checkLogin();
        handleLogoutFacebook();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        binding.btnLogin.setOnClickListener((view) -> {
            String username = binding.layoutUsername.getEditText().getText().toString();
            String password = binding.layoutPassword.getEditText().getText().toString();

            if (!toeicViewModel.getNetworkState().getValue()) {
                Toast.makeText(this, "Không có kết nối internet. Vui lòng kiểm tra lại",
                        Toast.LENGTH_SHORT).show();
            } else if (!toeicViewModel.getServerState().getValue()) {
                Toast.makeText(this, "Server không phản hồi ngay bây giờ. Vui lòng thử lại sau!",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (username.length() < 8){
                    binding.layoutUsername.setError("Username must has at least 8 digits");
                    binding.layoutUsername.getEditText().requestFocus();
                    binding.layoutPassword.setErrorEnabled(false);
                    return;
                }
                if (password.length() < 8){
                    binding.layoutPassword.setError("Password must has at least 8 digits");
                    binding.layoutPassword.getEditText().requestFocus();
                    binding.layoutUsername.setErrorEnabled(false);
                    return;
                }
                binding.layoutUsername.setErrorEnabled(false);
                binding.layoutPassword.setErrorEnabled(false);
                UserAPI.handleLogin(this, username, password);
            }
        });
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

    public void showLoadingDialog() {
        loadingDialog = new Dialog(this, R.style.LoadingDialog);

        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.show();
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setCancelable(false);
        loadingDialog.findViewById(R.id.loading_icon).startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate360));
    }

    public void dismissLoadingDialog() {
        loadingDialog.dismiss();
    }

    private void handleLogoutFacebook() {
        User user = MyDatabase.getInstance(this).getUserDAO().getUser().get(0);
        if (user.getType().equals("facebook") && !user.isLogin())
            LoginManager.getInstance().logOut();
    }

    private void handleLoginFacebook(AccessToken accessToken) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        User.saveUserInfo(user, this);
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

    private void checkLogin() {
        User user = MyDatabase.getInstance(this).getUserDAO().getUser().get(0);
        if (user != null && user.isLogin()) {
            showLoadingDialog();
            new Handler().postDelayed(() -> {
                navigateHome();
            }, 2000);
        }
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
}