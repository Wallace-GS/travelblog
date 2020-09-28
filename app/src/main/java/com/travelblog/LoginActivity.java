package com.travelblog;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textUsernameLayout;
    private TextInputLayout textPasswordLayout;
    private Button loginButton;
    private ProgressBar progressBar;
    private BlogPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = new BlogPreferences(this);
        if (prefs.isLoggedIn()) {
            startMainActivity();
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        textUsernameLayout = findViewById(R.id.textUsernameLayout);
        textPasswordLayout = findViewById(R.id.textPasswordLayout);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // adding .this makes it so that onLoginClicked belongs to an instance of this class
                // otherwise onLoginClicked would need to be a static method
                LoginActivity.this.onLoginClicked();
            }
        });
        // the click listener above can be simplified to lambda when using Java 8:
        // loginButton.setOnClickListener(view -> onLoginClicked());

        textUsernameLayout.getEditText().addTextChangedListener(createTextWatcher(textUsernameLayout));
        textPasswordLayout.getEditText().addTextChangedListener(createTextWatcher(textPasswordLayout));
    }

    private void onLoginClicked() {
        String username = textUsernameLayout.getEditText().getText().toString();
        String password = textPasswordLayout.getEditText().getText().toString();

        // input validation
        if (username.isEmpty()) textUsernameLayout.setError("Username must not be empty");
        else if (password.isEmpty()) textPasswordLayout.setError("Password must not be empty");
        else if (!username.equals("admin") || !password.equals("admin")) showErrorDialog();
        else performLogin();
    }

    private void performLogin() {
        prefs.setLoggedIn(true);

        textUsernameLayout.setEnabled(false);
        textPasswordLayout.setEnabled(false);
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        // simulate long-running activity (in this case an http call)
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startMainActivity();
            finish();
        }, 2000);
    }

    private void startMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage("Username or password is not correct. Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // reset error dialog once user enters text
    private TextWatcher createTextWatcher(final TextInputLayout textLayout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                textLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
               // not needed
            }
        };
    }
}
