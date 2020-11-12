package com.example.finalproject.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.HomeActivity;
import com.example.finalproject.LoginActivity;
import com.example.finalproject.R;
import com.example.finalproject.RegisterActivity;
import com.example.finalproject.models.Account;
import com.example.finalproject.models.User;
import com.example.finalproject.view.LoadingDialog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserAPI {
    private static final String BASE_URL = "http://192.168.42.65:5000/api/toeic/";

    public static void handleLogin(@NotNull LoginActivity context, Account loginAccount) {
        LoadingDialog.showLoadingDialog(context);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                BASE_URL + "login?username=" + loginAccount.getUsername() + "&password=" + loginAccount.getPassword(),
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.getBoolean("status")) {
                            String data = object.getString("data");
                            JSONObject user = new JSONObject(data);

                            String id = user.getString("id");
                            String type = user.getString("type");

                            Account.saveAccountInfo(new Account(id, "", "", type, true), context);
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                context.navigateHome();
                            }, 2000);
                        } else {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                Toast.makeText(context, context.getText(R.string.login_error), Toast.LENGTH_SHORT).show();
                                context.getBinding().layoutUsername.getEditText().requestFocus();
                            }, 2000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TAG", e.getMessage());

                        new Handler(Looper.getMainLooper()).postDelayed(() -> LoadingDialog.dismissDialog(), 2000);
                    }
                },
                error -> {
                    LoadingDialog.dismissDialog();
                    Toast.makeText(context, context.getText(R.string.server_error), Toast.LENGTH_SHORT).show();

                    context.getBinding().layoutUsername.getEditText().requestFocus();
                    context.getVM().getServerState().postValue(false);
                });

        queue.add(stringRequest);
    }

    public static void register(@NotNull Context context, Account account) {
        LoadingDialog.showLoadingDialog(context);
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

        Map<String, String> params = new HashMap<>();
        params.put("id", account.getId());
        params.put("username", account.getUsername());
        params.put("password", account.getPassword());
        params.put("type", account.getType());

        JSONObject postParams = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                BASE_URL + "register", postParams,
                response -> {
                    try {
                        RegisterActivity activity = (RegisterActivity) context;

                        if (response.getBoolean("status")) {
                            activity.navigate();
                        } else {
                            activity.onRegisterFail();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(context, context.getText(R.string.server_error), Toast.LENGTH_SHORT).show();
                });

        queue.add(request);
    }

    public static void loadUserInfo(Context context, Account acc) {
        LoadingDialog.showLoadingDialog(context);
        HomeActivity home = (HomeActivity) context;
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET,
                BASE_URL + "user?id=" + acc.getId(),
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject data = object.getJSONObject("data");

                            int id = data.getInt("id");
                            String name = data.getString("name");
                            boolean gender = data.getBoolean("gender");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            Date date = null;
                            try {
                                date = format.parse(data.getString("birthday"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            String address = data.getString("address");
                            String email = data.getString("email");
                            int score = data.getInt("score");
                            String accountID = data.getString("accountId");

                            Date finalDate = date;
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                User user = new User(id, name, gender, finalDate, address, email, score, accountID);
                                home.getHomeViewModel().getUser().postValue(user);
                                LoadingDialog.dismissDialog();
                            }, 1000);
                        }else{
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                try {
                                    Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                LoadingDialog.dismissDialog();
                            }, 1000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        home.getHomeViewModel().getServerState().postValue(false);
                        Toast.makeText(context, context.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        LoadingDialog.dismissDialog();
                    }, 1000);
                });
        queue.add(request);
    }
}
