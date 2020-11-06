package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.databinding.ActivityLoginBinding;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.models.User;
import com.example.finalproject.models.UserDAO;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    private CallbackManager fbCallback;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(binding.getRoot());

        setupLoginFacebook();
        checkLogin();
        binding.btnLogin.setOnClickListener((view) -> {
            String username = binding.layoutUsername.getEditText().getText().toString();
            String password = binding.layoutPassword.getEditText().getText().toString();

            handleLogin(username, password);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        fbCallback.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleLoginFacebook(AccessToken accessToken){
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        saveUserInfo(user);
                        navigateHome();
                        Log.e("TAG", "Sign in with credential successfully! " + user.getUid());
                    }else{
                        Log.e("TAG", "Sign in with credential failure!" + task.getException());
                    }
                });
    }
    private void handleLogin(String username, String password){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://192.168.42.65:8080/user/login.php",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getJSONArray("data").length() == 0){
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (object.getBoolean("status")){
                            JSONArray jsonArray = object.getJSONArray("data");
                            JSONObject user = jsonArray.getJSONObject(0);

                            String id = user.getString("id");
                            String newUsername = user.getString("username");
                            String newPassword = user.getString("password");
                            String type = user.getString("type");

                            saveUserInfo(new User(id, newUsername, newPassword, type, true));
                            navigateHome();
                        }else{
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG", e.getMessage());
                    }
                },
                error -> {
                    Log.e("TAG", "Login failed. Error: " + error.getMessage());
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        queue.add(stringRequest);
    }
    private void saveUserInfo(FirebaseUser user){
        User newUser = new User(user.getUid(), user.getEmail(), "", "facebook", true);
        UserDAO dao = MyDatabase.getInstance(this).getUserDAO();

        if (dao.getUserById(user.getUid()) == null){
            dao.addUser(newUser);
        }else{
            dao.updateUser(newUser);
        }
    }
    private void saveUserInfo(User user){
        User newUser = new User(user.getId(), user.getUsername(), user.getPassword(), user.getType(), true);
        UserDAO dao = MyDatabase.getInstance(this).getUserDAO();

        if (dao.getUserById(user.getId()) == null){
            dao.addUser(newUser);
        }else{
            dao.updateUser(newUser);
        }
    }
    private void checkLogin(){
        User user = MyDatabase.getInstance(this).getUserDAO().getUser();
        if (user != null && user.isLogin()){
            navigateHome();
        }
    }
    private void navigateHome(){
        startActivity(new Intent(this, HomeActivity.class));
    }
    private void setupLoginFacebook(){
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