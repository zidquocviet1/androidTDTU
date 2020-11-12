package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.finalproject.api.UserAPI;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.models.Account;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.models.User;
import com.example.finalproject.view.LoadingDialog;
import com.example.finalproject.viewmodel.HomeViewModel;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityHomeBinding binding;
    private boolean isLogin = false;
    private HomeViewModel homeViewModel;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkController.isOnline(context)) {
                homeViewModel.getNetworkState().postValue(true);
            } else {
                homeViewModel.getNetworkState().postValue(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViewModel();

        Account account = MyDatabase.getInstance(this).userDAO().getFirstAccount();
        if (account != null)
            homeViewModel.getAccount().postValue(account);

        binding.btnLogout.setOnClickListener(this);
        binding.cvAvatar.setOnClickListener(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

//        MyDatabase.getInstance(this).courseDAO().getAllCourse().forEach(c -> Log.e("TAG", c.getName()));
//        MyDatabase.getInstance(this).courseDAO().getCourseAndQuestion(1).forEach(c -> {
//            c.questions.forEach(q -> Log.e("TAG", q.getAnswer()));
//        });
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
    private void initViewModel() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAccount().observe(this, account -> {
            if (account != null)
                checkLogin(account);
            binding.txtHello.setVisibility(View.VISIBLE);
        });
        homeViewModel.getUser().observe(this, user -> {
            if (user != null)
                showUserInfo(user);
        });
    }

    public HomeViewModel getHomeViewModel(){
        return homeViewModel;
    }

    private void checkLogin(Account account) {
        if (!homeViewModel.getNetworkState().getValue()) {
            binding.btnLogout.setText(getString(R.string.login));
            binding.txtHello.setText(getString(R.string.hello, "Guy"));
            binding.txtHello.setVisibility(View.VISIBLE);
            binding.imgAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.com_facebook_profile_picture_blank_square));
            isLogin = false;
            return;
        }

        if (account != null && account.isLogin()) {
            binding.btnLogout.setText(getString(R.string.logout));
            isLogin = true;
            binding.txtHello.setText(getString(R.string.hello, account.getUsername()));
            binding.txtHello.setVisibility(View.VISIBLE);
            binding.imgAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground));
        } else {
            LoadingDialog.showLoadingDialog(this);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                LoadingDialog.dismissDialog();
                binding.btnLogout.setText(getString(R.string.login));
                isLogin = false;
                binding.txtHello.setText(getString(R.string.hello, "Guy"));
                binding.txtHello.setVisibility(View.VISIBLE);
                binding.imgAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.com_facebook_profile_picture_blank_square));
            }, 1500);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogout:
                if (!isLogin) {
                    startActivity(new Intent(this, LoginActivity.class));
                }else{
                    Account account = MyDatabase.getInstance(this).userDAO().getFirstAccount();
                    account.setLogin(false);
                    MyDatabase.getInstance(this).userDAO().updateAccount(account);
                    homeViewModel.getAccount().postValue(account);
                }
                break;
            case R.id.cvAvatar:
                Account acc = homeViewModel.getAccount().getValue();
                if (acc != null && acc.isLogin()){
                    if (homeViewModel.getNetworkState().getValue()){
                        if (homeViewModel.getServerState().getValue()){
                            User user = homeViewModel.getUser().getValue();
                            if (user == null)
                                UserAPI.loadUserInfo(this, acc);
                            else
                                showUserInfo(user);
                        }else{
                            Toast.makeText(this, getString(R.string.server), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.e("TAG", "Chua dang nhap tai khoan!");
                }
                break;
        }
    }
    public void showUserInfo(User user){
        Log.e("TAG", user.getName() + " " + user.getAccountID());
    }
}