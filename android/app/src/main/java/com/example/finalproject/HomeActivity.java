package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.finalproject.api.UserAPI;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.models.Account;
import com.example.finalproject.models.Course;
import com.example.finalproject.models.CourseAdapter;
import com.example.finalproject.models.ItemClickListener;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.models.User;
import com.example.finalproject.models.Word;
import com.example.finalproject.models.WordAdapter;
import com.example.finalproject.view.LoadingDialog;
import com.example.finalproject.viewmodel.HomeViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener, NavigationView.OnNavigationItemSelectedListener {
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

        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        Account account = MyDatabase.getInstance(this).userDAO().getFirstAccount();
        if (account != null)
            homeViewModel.getAccount().postValue(account);

        binding.cvAvatar.setOnClickListener(this);
        binding.imageView.setOnClickListener(this);
        binding.navView.setCheckedItem(R.id.mnHome);
        openFragment(HomeFragment.class);
    }

    private void initDrawerLayout() {
        binding.navView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                binding.drawerLayout,
                null,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.drawerLayout.open();
        binding.navView.setNavigationItemSelectedListener(this);
    }

    private void initViewModel() {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getAccount().observe(this, account -> {
            if (account != null)
                checkLogin(account);
        });
        homeViewModel.getUser().observe(this, user -> {
            if (user != null)
                showUserInfo(user);
        });

        homeViewModel.getCourses().postValue(MyDatabase.getInstance(this).courseDAO().getAllCourse());
        homeViewModel.getWords().postValue(MyDatabase.getInstance(this).wordDAO().getTop30());
    }

    public HomeViewModel getHomeViewModel() {
        return homeViewModel;
    }

    private void checkLogin(Account account) {
        if (!homeViewModel.getNetworkState().getValue()) {
            binding.imgAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.com_facebook_profile_picture_blank_square));
            isLogin = false;
            return;
        }
        if (account != null && account.isLogin()) {
            isLogin = true;
            binding.imgAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_launcher_foreground));
        } else {
            LoadingDialog.showLoadingDialog(this);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                LoadingDialog.dismissDialog();
                isLogin = false;
                binding.imgAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.com_facebook_profile_picture_blank_square));
            }, 1500);
        }
    }

    private void showPopUp(Account acc, boolean isLogin) {
        PopupMenu popupMenu = new PopupMenu(this, binding.cvAvatar);

        if (isLogin) {
            popupMenu.getMenuInflater().inflate(R.menu.account_menu_logout, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.mnInfo:
//                        getInfo(acc);
                        startActivity(new Intent(HomeActivity.this, UserActivity.class));
                        break;
                    case R.id.mnLogout:
                        showLogoutDialog();
                        break;
                }
                return false;
            });
        } else {
            popupMenu.getMenuInflater().inflate(R.menu.account_menu_login, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.mnLogin:
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        break;
                }
                return false;
            });
        }
        popupMenu.show();
    }

    private void logout() {
        Account account = MyDatabase.getInstance(this).userDAO().getFirstAccount();
        account.setLogin(false);
        MyDatabase.getInstance(this).userDAO().updateAccount(account);
        homeViewModel.getAccount().postValue(account);
    }

    private void getInfo(Account acc) {
        if (homeViewModel.getNetworkState().getValue()) {
            if (homeViewModel.getServerState().getValue()) {
                User user = homeViewModel.getUser().getValue();
                if (user == null)
                    UserAPI.loadUserInfo(this, acc);
                else
                    showUserInfo(user);
            } else {
                Toast.makeText(this, getString(R.string.server), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show();
        }
    }

    public void showUserInfo(User user) {
        Log.e("TAG", user.getName() + " " + user.getAccountID());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.account_title))
                .setMessage(getString(R.string.account_message_logout))
                .setIcon(R.drawable.ic_baseline_info_24)
                .setPositiveButton("Yes", ((dialog, which) -> {
                    logout();
                }))
                .setNegativeButton("No", ((dialog, which) -> dialog.dismiss()))
                .setCancelable(false)
                .create().show();
    }

    private void openFragment(Class fragmentClass) {
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.framelayout, fragment).commit();
    }

    @Override
    public void onItemClick(Object object, int position) {
        if (object instanceof CourseAdapter) {
            Log.e("TAG", "Open course detail activity");
        } else if (object instanceof WordAdapter) {
            Log.e("TAG", "Open word detail activity");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.mnRank:
                fragmentClass = RankFragment.class;
                break;
            case R.id.mnCourse:
                fragmentClass = RankFragment.class;
                break;
            case R.id.mnVocab:
                fragmentClass = RankFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        openFragment(fragmentClass);

        binding.navView.setCheckedItem(item);
        binding.bottomNav.setSelectedItemId(item.getItemId());
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cvAvatar:
                Account acc = homeViewModel.getAccount().getValue();
                if (acc != null && acc.isLogin()) {
                    showPopUp(acc, true);
                } else {
                    showPopUp(acc, false);
                }
                break;
            case R.id.txtSeall:
                Log.e("TAG", "Open vocabulary details activity");
                break;
            case R.id.imageView:
                initDrawerLayout();
                break;
        }
    }
}