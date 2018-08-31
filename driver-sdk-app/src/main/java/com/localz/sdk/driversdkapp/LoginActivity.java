package com.localz.sdk.driversdkapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.localz.sdk.driver.Callback;
import com.localz.sdk.driver.Error;
import com.localz.sdk.driver.LocalzDriverSDK;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (LocalzDriverSDK.getInstance().isLoggedIn(this)) {
            Intent intent = new Intent(LoginActivity.this, ActionsActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            LocalzDriverSDK.getInstance().login(LoginActivity.this, username, password, new Callback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    Log.d(TAG, "login onSuccess");
                    Intent intent = new Intent(LoginActivity.this, ActionsActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(Error error) {
                    Log.d(TAG, "login onError: " + error);
                }
            });
        });
    }
}
