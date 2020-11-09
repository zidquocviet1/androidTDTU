package com.example.finalproject.api;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.LoginActivity;
import com.example.finalproject.models.User;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class UserAPI {
    private static final String BASE_URL = "http://192.168.42.65:5000/api/toeic/";

    public static void handleLogin(@NotNull LoginActivity context, String username, String password) {
        context.showLoadingDialog();
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
                                context.dismissLoadingDialog();
                                context.navigateHome();
                            }, 2000);
                        } else {
                            new Handler().postDelayed(() -> {
                                context.dismissLoadingDialog();
                                Toast.makeText(context, "Tài khoản hoặc mật khẩu không hợp lệ.", Toast.LENGTH_SHORT).show();
                                context.getBinding().layoutUsername.getEditText().requestFocus();
                            }, 2000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG", e.getMessage());

                        new Handler().postDelayed(() -> context.dismissLoadingDialog(), 2000);
                    }
                },
                error -> {
                    Log.e("TAG", "Login failed. Error: " + error.getMessage());
                    context.dismissLoadingDialog();
                    Toast.makeText(context, "Không thể kết nối đến server!", Toast.LENGTH_SHORT).show();
                    context.getBinding().layoutUsername.getEditText().requestFocus();
                    context.getVM().getServerState().postValue(false);
                });

        queue.add(stringRequest);
    }
}
