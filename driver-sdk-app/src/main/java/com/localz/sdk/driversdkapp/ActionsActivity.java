package com.localz.sdk.driversdkapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.localz.sdk.driver.Callback;
import com.localz.sdk.driver.Error;
import com.localz.sdk.driver.play.LocalzDriverSDK;

import org.jetbrains.annotations.NotNull;

public class ActionsActivity extends AppCompatActivity {

    private static final String TAG = "ActionsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);

        findViewById(R.id.createOrder).setOnClickListener(v -> {
            Intent intent = new Intent(ActionsActivity.this, CreateOrderActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.listOrders).setOnClickListener(v -> {
            Intent intent = new Intent(ActionsActivity.this, GetOrdersActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.onDuty).setOnClickListener(v -> LocalzDriverSDK.INSTANCE.onDuty(ActionsActivity.this, true, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG, "onDuty onSuccess");
                refresh();
            }

            @Override
            public void onError(@NotNull Error error) {
                Log.d(TAG, "onDuty onError: " + error);
            }
        }));

        findViewById(R.id.offDuty).setOnClickListener(v -> LocalzDriverSDK.INSTANCE.onDuty(ActionsActivity.this, false, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG, "setOffDuty onSuccess");
                refresh();
            }

            @Override
            public void onError(@NotNull Error error) {
                Log.d(TAG, "setOffDuty onError: " + error);
            }
        }));

        findViewById(R.id.logout).setOnClickListener(v -> LocalzDriverSDK.INSTANCE.logout(ActionsActivity.this, true, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG, "logout onSuccess");
                Intent intent = new Intent(ActionsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(@NotNull Error error) {
                Log.d(TAG, "logout onError: " + error);
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        runOnUiThread(() -> {
            final LocalzDriverSDK localzDriverSDK = LocalzDriverSDK.INSTANCE;

            ((TextView) findViewById(R.id.is_initialised)).setText(
                    "Initialised: " + localzDriverSDK.isInitialised()
            );

            ((TextView) findViewById(R.id.is_logged_in)).setText(
                    "Logged in: " + localzDriverSDK.isLoggedIn()
            );

            ((TextView) findViewById(R.id.is_on_duty)).setText(
                    "On duty: " + localzDriverSDK.isOnDuty()
            );

            ((TextView) findViewById(R.id.has_local_changes)).setText(
                    "Has local changes: " + localzDriverSDK.isCachedData(ActionsActivity.this)
            );
        });
    }
}
