package com.localz.sdk.driversdkapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.localz.sdk.driver.Callback;
import com.localz.sdk.driver.Error;
import com.localz.sdk.driver.play.LocalzDriverSDK;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    
    // Provide your own values
    private static final String PROJECT_ID = "YOUR_PROJECT_ID";
    private static final String SPOTZ_PROJECT_KEY = "YOUR_SPOTZ_PROJECT_KEY";
    private static final String ATTENDANT_KEY = "YOUR_ATTENDANT_KEY";
    private static final String ENVIRONMENT = "prd"; // prd, dev, stg, tst

    private static final int REQUEST_ACCESS_LOCATION = 14;
    private Button initializeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (hasPermissions() && LocalzDriverSDK.INSTANCE.isInitialised()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if (!hasPermissions()) {
            requestPermissions();
        }

        initializeButton = findViewById(R.id.btn_do_it);
        initializeButton.setOnClickListener(v -> initSdk());
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLocationPermission();
    }

    private void checkBatteryOptimisation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // allow app to always run in the background
            PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!powerManager.isIgnoringBatteryOptimizations(getPackageName())) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(R.string.location_permission_required_title);
                alertBuilder.setMessage(R.string.location_permission_required_text);
                alertBuilder.setPositiveButton(android.R.string.yes, (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0));

                android.support.v7.app.AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        } else {
            checkBatteryOptimisation();
        }
    }

    private void initSdk() {
        LocalzDriverSDK.Configuration configuration = new LocalzDriverSDK.Configuration()
                .setProjectId(PROJECT_ID)
                .setSpotzProjectKey(SPOTZ_PROJECT_KEY)
                .setCncProjectKey(ATTENDANT_KEY)
                .setEnvironment(ENVIRONMENT)
                .setPinningEnabled(false);

        LocalzDriverSDK.INSTANCE.init(this, configuration, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG, "init onSuccess");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(@NotNull Error error) {
                Log.d(TAG, "init onError: " + error);
            }
        });
    }

    private boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == REQUEST_ACCESS_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay!
            } else {
                // permission denied, boo!
            }
        }
    }
}
