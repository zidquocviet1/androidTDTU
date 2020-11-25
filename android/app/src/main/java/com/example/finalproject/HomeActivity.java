package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.finalproject.api.ToeicAPI;
import com.example.finalproject.databinding.ActivityHomeBinding;
import com.example.finalproject.fragment.CourseFragment;
import com.example.finalproject.fragment.HomeFragment;
import com.example.finalproject.fragment.RankFragment;
import com.example.finalproject.fragment.VocabularyFragment;
import com.example.finalproject.models.Account;
import com.example.finalproject.models.adapter.CourseAdapter;
import com.example.finalproject.models.ItemClickListener;
import com.example.finalproject.models.MyDatabase;
import com.example.finalproject.models.User;
import com.example.finalproject.models.adapter.UserAdapter;
import com.example.finalproject.models.adapter.WordAdapter;
import com.example.finalproject.view.LoadingDialog;
import com.example.finalproject.viewmodel.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener, ItemClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityHomeBinding binding;
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

        binding.cvAvatar.setOnClickListener(this);
        binding.imageView.setOnClickListener(this);
        binding.navView.setCheckedItem(R.id.mnHome);
        binding.bottomNav.setOnNavigationItemSelectedListener(this);
        openFragment(HomeFragment.class, "Home");

        if (homeViewModel.getWords().getValue() == null)
            homeViewModel.getWords().postValue(MyDatabase.getInstance(this).wordDAO().getAll());

        Account acc = MyDatabase.getInstance(this).accDAO().getFirstAccount();
        if (acc != null && acc.isLogin()) {
           ToeicAPI.getUserInfoBackground(this, acc);
        }

        ToeicAPI.getComment(this);
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
        homeViewModel.get30Words().postValue(MyDatabase.getInstance(this).wordDAO().getTop30());
    }

    public HomeViewModel getHomeViewModel() {
        return homeViewModel;
    }

    private void checkLogin(Account account) {
        if (!homeViewModel.getNetworkState().getValue()) {
            binding.imgAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.com_facebook_profile_picture_blank_square));
            return;
        }
        if (account != null && !account.isLogin()) {
            LoadingDialog.showLoadingDialog(this);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                LoadingDialog.dismissDialog();
                binding.imgAvatar.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.com_facebook_profile_picture_blank_square));
                homeViewModel.getUser().postValue(null);
            }, 1500);
        }
    }

    private void showPopUp(User user, boolean isLogin) {
        PopupMenu popupMenu = new PopupMenu(this, binding.cvAvatar);

        if (isLogin) {
            popupMenu.getMenuInflater().inflate(R.menu.account_menu_logout, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.mnInfo:
                        Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
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
                        startActivityForResult(new Intent(HomeActivity.this, LoginActivity.class), 111);
                        break;
                }
                return false;
            });
        }
        popupMenu.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111){
            Account acc = data.getParcelableExtra("account");
            if (resultCode == RESULT_OK && acc != null){
                ToeicAPI.getUserInfoBackground(this, acc);
            }
        }
    }

    private void logout() {
        Account account = MyDatabase.getInstance(this).accDAO().getFirstAccount();
        account.setLogin(false);
        MyDatabase.getInstance(this).accDAO().updateAccount(account);
        homeViewModel.getAccount().postValue(account);
    }

    public void navigateToUserInfo() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(HomeActivity.this, UserActivity.class));
            LoadingDialog.dismissDialog();
        }, 1000);
    }

    public void showUserInfo(User user) {
        int i = UserAdapter.images[user.getAvatar()];
        binding.imgAvatar.setImageDrawable(ContextCompat.getDrawable(this, i));

        MyDatabase.getInstance(this).userDAO().addUser(user);
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

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.about))
                .setMessage(getString(R.string.developer))
                .setIcon(R.drawable.ic_baseline_info_24)
                .setPositiveButton("OK", ((dialog, which) -> {
                    dialog.dismiss();
                }))
                .setCancelable(false)
                .create().show();
    }

    private void showRateDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.rateus);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, getString(R.string.share_message), Toast.LENGTH_SHORT).show();
            binding.navView.setCheckedItem(binding.bottomNav.getSelectedItemId());
        });

        dialog.show();
    }

    public void openFragment(Class fragmentClass, CharSequence title) {
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.framelayout, fragment).commit();
        binding.txtTitle.setText(title);
    }

    public boolean isServerAndNetworkAvailable() {
        if (homeViewModel.getNetworkState().getValue()) {
            if (homeViewModel.getServerState().getValue()) {
                return true;
            } else {
                Toast.makeText(this, getString(R.string.server), Toast.LENGTH_SHORT).show();
                homeViewModel.getServerState().postValue(false);
                return false;
            }
        } else {
            Toast.makeText(this, getString(R.string.connection), Toast.LENGTH_SHORT).show();
            homeViewModel.getNetworkState().postValue(false);
            return false;
        }
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
        Class fragmentClass = null;
        switch (item.getItemId()) {
            case R.id.mnRank:
                if (binding.bottomNav.getSelectedItemId() != R.id.mnRank)
                    fragmentClass = RankFragment.class;
                break;
            case R.id.mnCourse:
                if (binding.bottomNav.getSelectedItemId() != R.id.mnCourse)
                    fragmentClass = CourseFragment.class;
                break;
            case R.id.mnVocab:
                if (binding.bottomNav.getSelectedItemId() != R.id.mnVocab)
                    fragmentClass = VocabularyFragment.class;
                break;
            case R.id.mnAbout:
                if (binding.bottomNav.getSelectedItemId() != R.id.mnAbout)
                    showAboutDialog();
                break;
            case R.id.mnShare:
                if (binding.bottomNav.getSelectedItemId() != R.id.mnShare)
                    Toast.makeText(this, getString(R.string.share_message), Toast.LENGTH_SHORT).show();
                break;
            case R.id.mnRate:
                if (binding.bottomNav.getSelectedItemId() != R.id.mnRate) {
                    showRateDialog();
                }
                break;
            default:
                if (binding.bottomNav.getSelectedItemId() != R.id.mnHome)
                    fragmentClass = HomeFragment.class;
        }

        if (fragmentClass != null)
            openFragment(fragmentClass, item.getTitle());

        binding.navView.setCheckedItem(item);
        binding.bottomNav.setOnNavigationItemSelectedListener(null);
        binding.bottomNav.setSelectedItemId(item.getItemId());
        binding.bottomNav.setOnNavigationItemSelectedListener(this);
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
                User user = homeViewModel.getUser().getValue();
                if (user != null) {
                    showPopUp(user, true);
                } else {
                    showPopUp(user, false);
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