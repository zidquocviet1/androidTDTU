package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.finalproject.api.ToeicAPI;
import com.example.finalproject.databinding.ActivityRegisterBinding;
import com.example.finalproject.models.Account;
import com.example.finalproject.view.LoadingDialog;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity implements TextWatcher {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSignup.setOnClickListener((view) -> {
            String username = binding.txtLayoutUsername.getEditText().getText().toString();
            String password = binding.txtLayoutPassword.getEditText().getText().toString();
            String confirmPassword = binding.txtLayoutConfirm.getEditText().getText().toString();
            String fullName = binding.txtLayoutName.getEditText().getText().toString();

            if (!isValid(username, password, confirmPassword)) return;
            if (confirmPassword.equals(password)){
                String id = UUID.randomUUID().toString();
                Account account = new Account(id, username, password, "default", false, fullName);
                account.encryptPassword();

                ToeicAPI.register(this, account);
            }else{
                Toast.makeText(this, getText(R.string.re_password), Toast.LENGTH_SHORT).show();
                binding.txtLayoutConfirm.getEditText().requestFocus();
                binding.txtLayoutConfirm.setError(getText(R.string.re_password));
            }
        });
        binding.txtLayoutUsername.getEditText().addTextChangedListener(this);
        binding.txtLayoutPassword.getEditText().addTextChangedListener(this);
        binding.txtLayoutConfirm.getEditText().addTextChangedListener(this);
    }

    public void navigate(){
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Toast.makeText(this, getText(R.string.signup_success), Toast.LENGTH_SHORT).show();
            this.finish();
        }, 1500);
    }

    private boolean isValid(String username, String password, String confirm){
        if (username.length() < 8){
            binding.txtLayoutUsername.setError(getText(R.string.username_error));
            return false;
        }
        if (password.length() < 8){
            binding.txtLayoutPassword.setError(getText(R.string.password_error));
            binding.txtLayoutUsername.setErrorEnabled(false);
            return false;
        }
        if (confirm.length() < 8){
            binding.txtLayoutUsername.setError(getText(R.string.username_error));
            binding.txtLayoutPassword.setErrorEnabled(false);
            return false;
        }
        binding.txtLayoutUsername.setErrorEnabled(false);
        binding.txtLayoutPassword.setErrorEnabled(false);
        binding.txtLayoutConfirm.setErrorEnabled(false);
        return true;
    }
    public void onRegisterFail(){
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            LoadingDialog.dismissDialog();
            binding.txtLayoutUsername.setError(getText(R.string.signup_error));
        }, 1500);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (binding.txtLayoutUsername.getEditText().getText().equals(s)){
            if (s.length() == 0)
                binding.txtLayoutUsername.setErrorEnabled(false);
            else if (s.length() < 8)
                binding.txtLayoutUsername.setError(getText(R.string.username_error));
            else
                binding.txtLayoutUsername.setErrorEnabled(false);
        }else if (binding.txtLayoutPassword.getEditText().getText().equals(s)){
            if (s.length() == 0)
                binding.txtLayoutPassword.setErrorEnabled(false);
            else if (s.length() < 8)
                binding.txtLayoutPassword.setError(getText(R.string.password_error));
            else
                binding.txtLayoutPassword.setErrorEnabled(false);

            if (!binding.txtLayoutConfirm.getEditText().getText().toString().equals(s.toString()))
                binding.txtLayoutConfirm.setError(getText(R.string.re_password));
            else
                binding.txtLayoutConfirm.setErrorEnabled(false);
        }else if (binding.txtLayoutConfirm.getEditText().getText().equals(s)){
            if (s.length() == 0)
                binding.txtLayoutConfirm.setErrorEnabled(false);
            else if (s.length() < 8)
                binding.txtLayoutConfirm.setError(getText(R.string.password_error));
            else if (!binding.txtLayoutPassword.getEditText().getText().toString().equals(s.toString()))
                binding.txtLayoutConfirm.setError(getText(R.string.re_password));
            else
                binding.txtLayoutConfirm.setErrorEnabled(false);
        }
    }
}