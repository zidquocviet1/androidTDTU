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
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.finalproject.CommentActivity;
import com.example.finalproject.HomeActivity;
import com.example.finalproject.LoginActivity;
import com.example.finalproject.R;
import com.example.finalproject.RegisterActivity;
import com.example.finalproject.models.Account;
import com.example.finalproject.models.Comment;
import com.example.finalproject.models.Course;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.models.Record;
import com.example.finalproject.models.User;
import com.example.finalproject.models.Word;
import com.example.finalproject.view.LoadingDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ToeicAPI {
    private static final String IP = "192.168.42.65";
    private static final String BASE_URL = "http://" + IP + ":5000/api/toeic/";

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
                            String fullName = user.getString("fullName");

                            Account acc = new Account(id, "", "", type, true, fullName);
                            Account.saveAccountInfo(acc, context);
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                context.navigateHome(acc);
                            }, 1000);
                        } else {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                Toast.makeText(context, context.getText(R.string.login_error), Toast.LENGTH_SHORT).show();
                                context.getBinding().layoutUsername.getEditText().requestFocus();
                            }, 1000);
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
        params.put("fullName", account.getFullName());

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
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        LoadingDialog.dismissDialog();
                        Toast.makeText(context, context.getText(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }, 1500);
                });

        queue.add(request);
    }

    public static void updateUser(@NotNull Context context, User user) {
        LoadingDialog.showLoadingDialog(context);
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

        Map<String, String> params = new HashMap<>();
        params.put("id", user.getId()+"");
        params.put("name", user.getName());
        params.put("gender", user.isGender()+"");
        params.put("birthday", user.getBirthday()+"");
        params.put("address", user.getAddress());
        params.put("email", user.getEmail());
        params.put("score", user.getScore()+"");
        params.put("avatar", user.getAvatar()+"");

        JSONObject postParams = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                BASE_URL + "user", postParams,
                response -> {
                    try {
                        RegisterActivity activity = (RegisterActivity) context;

                        if (response.getBoolean("status")) {
                            MyDatabase.getInstance(context).userDAO().addUser(user);
                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        LoadingDialog.dismissDialog();
                        Toast.makeText(context, context.getText(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }, 1500);
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
                        if (object.getBoolean("status")) {
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
                            int avatar = data.getInt("avatar");

                            Date finalDate = date;

                            User user = new User(id, name, gender, finalDate, address, email, score, accountID, avatar);
                            home.getHomeViewModel().getUser().postValue(user);
                            home.navigateToUserInfo();
                        } else {
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

    public static void getUserInfoBackground(Context context, Account acc) {
        HomeActivity home = (HomeActivity) context;
        LoadingDialog.showLoadingDialog(context);

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
//        RequestFuture<String> future = RequestFuture.newFuture();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL + "user?id=" + acc.getId(),
//                future, future);
//        queue.add(stringRequest);
//
//        try {
//            String response = future.get(5, TimeUnit.SECONDS);
//            try {
//                JSONObject object = new JSONObject(response);
//                if (object.getBoolean("status")) {
//                    JSONObject data = object.getJSONObject("data");
//
//                    int id = data.getInt("id");
//                    String name = data.getString("name");
//                    boolean gender = data.getBoolean("gender");
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//                    Date date = null;
//                    try {
//                        date = format.parse(data.getString("birthday"));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    String address = data.getString("address");
//                    String email = data.getString("email");
//                    int score = data.getInt("score");
//                    String accountID = data.getString("accountId");
//                    int avatar = data.getInt("avatar");
//
//                    Date finalDate = date;
//
//                    User user = new User(id, name, gender, finalDate, address, email, score, accountID, avatar);
//                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                        home.getHomeViewModel().getUser().postValue(user);
//                        home.getHomeViewModel().getServerState().postValue(true);
//                        LoadingDialog.dismissDialog();
//                    }, 1000);
//                } else {
//                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                        home.getHomeViewModel().getUser().postValue(null);
//                        home.getHomeViewModel().getServerState().postValue(true);
//                        try {
//                            Toast.makeText(home, object.getString("message"), Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        LoadingDialog.dismissDialog();
//                    }, 1000);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } catch (InterruptedException e) {
//            // exception handling
//        } catch (ExecutionException e) {
//            new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                        home.getHomeViewModel().getUser().postValue(null);
//                        home.getHomeViewModel().getServerState().postValue(false);
//                        Toast.makeText(home, home.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
//                        LoadingDialog.dismissDialog();
//                    }, 500);
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
        StringRequest request = new StringRequest(Request.Method.GET,
                BASE_URL + "user?id=" + acc.getId(),
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
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
                            int avatar = data.getInt("avatar");

                            Date finalDate = date;

                            User user = new User(id, name, gender, finalDate, address, email, score, accountID, avatar);
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                home.getHomeViewModel().getUser().postValue(user);
                                home.getHomeViewModel().getServerState().postValue(true);
                                LoadingDialog.dismissDialog();
                            }, 1000);
                        } else {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                home.getHomeViewModel().getUser().postValue(null);
                                home.getHomeViewModel().getServerState().postValue(true);
                                try {
                                    Toast.makeText(home, object.getString("message"), Toast.LENGTH_SHORT).show();
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
                        home.getHomeViewModel().getUser().postValue(null);
                        home.getHomeViewModel().getServerState().postValue(false);
                        Toast.makeText(home, home.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        LoadingDialog.dismissDialog();
                    }, 500);
                });
        queue.add(request);
    }

    public static void getUserByScore(Context context) {
        LoadingDialog.showLoadingDialog(context);
        HomeActivity home = (HomeActivity) context;

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                BASE_URL + "rank", null,
                response -> {
                    try {
                        if (response.getBoolean("status")) {

                            JSONArray array = response.getJSONArray("data");

                            GsonBuilder builder = new GsonBuilder();
                            builder.setDateFormat("yyyy-MM-dd");

                            Gson gson = builder.create();
                            Type listWordType = new TypeToken<List<User>>() {
                            }.getType();
                            List<User> users = gson.fromJson(array.toString(), listWordType);

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                home.getHomeViewModel().getUsers().postValue(users);
                            }, 1000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        LoadingDialog.dismissDialog();
                        Toast.makeText(context, context.getText(R.string.server_error), Toast.LENGTH_SHORT).show();
                        home.getHomeViewModel().getServerState().postValue(false);
                    }, 1000);
                });

        queue.add(request);
    }

    public static void getComment(Context context) {
        HomeActivity home = (HomeActivity) context;

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                BASE_URL + "courses", null,
                response -> {
                    try {
                        if (response.getBoolean("status")) {
                            JSONArray array = response.getJSONArray("data");
                            List<Course> comments = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                int id = object.getInt("id");
                                String name = object.getString("name");
                                String des = object.getString("description");
                                int rating = object.getInt("rating");

                                ArrayList<Comment> listComment = new ArrayList<>();
                                JSONArray commentArray = object.getJSONArray("comment");
                                for (int j = 0; j < commentArray.length(); j++) {
                                    JSONObject cmtObject = commentArray.getJSONObject(j);

                                    int commentID = cmtObject.getInt("id");
                                    String description = cmtObject.getString("description");
                                    int cmtRate = cmtObject.getInt("rating");
                                    int cmtCourseID = cmtObject.getInt("courseId");
                                    String userId = cmtObject.getString("userId");

                                    listComment.add(new Comment(commentID, description, cmtRate,
                                            cmtCourseID, userId, null, null));
                                }

                                comments.add(new Course(id, name, des, rating, listComment));
                            }
                            home.getHomeViewModel().getComments().postValue(comments);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        home.getHomeViewModel().getComments().postValue(null);
                    }
                },
                error -> {
                    home.getHomeViewModel().getComments().postValue(null);
                });
        queue.add(request);
    }

    public static void checkConnection(Context context) {
        HomeActivity home = (HomeActivity) context;

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                BASE_URL + "account", null,
                response -> {
                    home.getHomeViewModel().getServerState().postValue(true);
                },
                error -> {
                    home.getHomeViewModel().getServerState().postValue(false);
                });
        queue.add(request);
    }

    public static void getCommentByCourseID(Context context, long courseId) {
        CommentActivity home = (CommentActivity) context;
        LoadingDialog.showLoadingDialog(context);

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                BASE_URL + "comments?id="+courseId, null,
                response -> {
                    try {
                        if (response.getBoolean("status")) {
                            JSONArray array = response.getJSONArray("data");
                            List<Comment> comments = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                int id = object.getInt("id");
                                String des = object.getString("description");
                                int rating = object.getInt("rating");
                                String userId = object.getString("userId");

                                JSONObject userObject = object.getJSONObject("user");

                                String fullName = userObject.getString("fullName");

                                comments.add(new Comment(id, des, rating,
                                        courseId, userId, null, new User(0, fullName,
                                        false, null, null, null, -1, null, -1)));
                            }
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                home.getVM().getComments().postValue(comments);
                                home.checkNetworkAndServer(true);
                                home.setUpUI();
                            },1500);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        LoadingDialog.dismissDialog();
                        home.checkNetworkAndServer(false);
                        home.setUpUI();
                    },1000);
                });
        queue.add(request);
    }

    public static void postComment(Context context, Comment comment, String accountId, long courseId){
        LoadingDialog.showLoadingDialog(context);
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        CommentActivity home = (CommentActivity) context;

        Map<String, String> params = new HashMap<>();
        params.put("description", comment.getDescription());
        params.put("rating", comment.getRating()+"");
        params.put("courseId", courseId+"");
        params.put("userId", accountId);

        JSONObject postParams = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                BASE_URL + "addComment", postParams,
                response -> {
                    try {
                        if (response.getBoolean("status")) {
                            JSONObject object = response.getJSONObject("data");

                            int id = object.getInt("id");
                            String des = object.getString("description");
                            int rating = object.getInt("rating");
                            String userId = object.getString("userId");

                            JSONObject userObject = object.getJSONObject("user");

                            String fullName = userObject.getString("fullName");

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                home.getCommentAdapter().addItem(new Comment(id, des, rating,
                                        courseId, userId, null, new User(0, fullName,
                                        false, null, null, null, -1, accountId, -1)));
                            }, 1000);
                        } else {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                try {
                                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, 1500);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        LoadingDialog.dismissDialog();
                        Toast.makeText(context, context.getText(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }, 1500);
                });

        queue.add(request);
    }

    public static void updateRecord(Context context, Record record){
        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());

        Map<String, String> params = new HashMap<>();
        params.put("courseId", record.getCourseId()+"");
        params.put("accountId", record.getAccountId());
        params.put("score", record.getScore()+"");

        JSONObject postParams = new JSONObject(params);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT,
                BASE_URL + "updateRecord", postParams,
                response -> {
                    try {
                        if (response.getBoolean("status")) {
                        } else {
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                });

        queue.add(request);
    }

    public static void getRankByCourse(Context context, long courseId) {
        LoadingDialog.showLoadingDialog(context);
        HomeActivity home = (HomeActivity) context;

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET,
                BASE_URL + "getRankByCourse?courseId=" + courseId,
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            JSONArray array = object.getJSONArray("data");
                            List<User> users = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++){
                                JSONObject data = array.getJSONObject(i);

                                int id = data.getInt("id");
                                String accountId = data.getString("accountId");
                                int score = data.getInt("score");

                                JSONObject accObject = data.getJSONObject("account");
                                String fullName = accObject.getString("fullName");

                                users.add(new User(id, fullName, false, null,
                                        "", "", score, accountId, new Random().nextInt(5)));
                            }

                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                LoadingDialog.dismissDialog();
                                home.getHomeViewModel().getUsers().postValue(users);
                            }, 1000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        LoadingDialog.dismissDialog();
                        home.getHomeViewModel().getUsers().postValue(null);
                    }, 1000);
                });
        queue.add(request);
    }
}
