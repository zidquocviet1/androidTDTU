package com.example.finalproject.api;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.LoginActivity;
import com.example.finalproject.RegisterActivity;
import com.example.finalproject.models.User;
import com.example.finalproject.view.LoadingDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserAPI {
    private static final String BASE_URL = "http://192.168.42.65:5000/api/toeic/";

    public static void handleLogin(@NotNull LoginActivity context, String username, String password) {
        LoadingDialog.showLoadingDialog(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                BASE_URL+"login?username=" + username + "&password=" + password,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.getBoolean("status")) {
                            String data = object.getString("data");
                            JSONObject user = new JSONObject(data);

                            String id = user.getString("id");
                            String type = user.getString("type");

                            User.saveUserInfo(new User(id, "", "", type, true), context);
                            new Handler().postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                context.navigateHome();
                            }, 2000);
                        } else {
                            String message = object.getString("message");

                            new Handler().postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                context.getBinding().layoutUsername.getEditText().requestFocus();
                            }, 2000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG", e.getMessage());

                        new Handler().postDelayed(() -> LoadingDialog.dismissDialog(), 2000);
                    }
                },
                error -> {
                    Log.e("TAG", "Login failed. Error: " + error.getMessage());
                    LoadingDialog.dismissDialog();
                    Toast.makeText(context, "Không thể kết nối đến server!", Toast.LENGTH_SHORT).show();
                    context.getBinding().layoutUsername.getEditText().requestFocus();
                    context.getVM().getServerState().postValue(false);
                });

        queue.add(stringRequest);
    }

    public static void register(@NotNull Context context, User user){
        LoadingDialog.showLoadingDialog(context);
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

        Map<String, String> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("username", user.getUsername());
        params.put("password", user.getPassword());
        params.put("type", user.getType());

        JSONObject postParams = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                BASE_URL + "register", postParams,
                response -> {
                    try {
                        RegisterActivity activity = (RegisterActivity) context;

                        if (response.getBoolean("status")){
                            activity.navigate(response.getString("message"));
                        }else{
                            activity.onRegisterFail(response.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(context, "Không thể kết nối với Server!", Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }
}
