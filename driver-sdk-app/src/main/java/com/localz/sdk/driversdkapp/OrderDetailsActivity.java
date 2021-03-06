package com.localz.sdk.driversdkapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.localz.sdk.driver.Callback;
import com.localz.sdk.driver.Error;
import com.localz.sdk.driver.model.Order;
import com.localz.sdk.driver.model.OrderEta;
import com.localz.sdk.driver.play.LocalzDriverSDK;

import org.jetbrains.annotations.NotNull;

public class OrderDetailsActivity extends AppCompatActivity {

    private static final String TAG = OrderDetailsActivity.class.getSimpleName();

    public static Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        if (order == null) {
            return;
        }
        updateView(order);

        findViewById(R.id.sendEtaNotification).setOnClickListener(v -> LocalzDriverSDK.INSTANCE.sendEtaNotification(OrderDetailsActivity.this, order.orderNumber, false, 0, null, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG, "sendEtaNotification onSuccess");
                refreshOrderDetails();
            }

            @Override
            public void onError(@NotNull Error error) {
                Log.d(TAG, "sendEtaNotification onError: " + error);
            }
        }));

        findViewById(R.id.complete).setOnClickListener(v -> LocalzDriverSDK.INSTANCE.completeOrder(OrderDetailsActivity.this, order.orderNumber, "signature", "notes", new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG, "completeOrder onSuccess");
                refreshOrderDetails();
            }

            @Override
            public void onError(@NotNull Error error) {
                Log.d(TAG, "completeOrder onError: " + error);
            }
        }));

        findViewById(R.id.acknowledge).setOnClickListener(v -> LocalzDriverSDK.INSTANCE.acknowledgeOrder(OrderDetailsActivity.this, order.orderNumber, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG, "acknowledgeOrder onSuccess");
                refreshOrderDetails();
            }

            @Override
            public void onError(@NotNull Error error) {
                Log.d(TAG, "acknowledgeOrder onError: " + error);
            }
        }));

        findViewById(R.id.getEtaForOrderNumber).setOnClickListener(v -> LocalzDriverSDK.INSTANCE.getEtaForOrderNumber(OrderDetailsActivity.this, order.orderNumber, new Callback<OrderEta>() {
            @Override
            public void onSuccess(OrderEta result) {
                Log.d(TAG, "getEtaForOrderNumber onSuccess");
            }

            @Override
            public void onError(@NotNull Error error) {
                Log.d(TAG, "getEtaForOrderNumber onError: " + error);
            }
        }));
    }

    private void refreshOrderDetails() {
        LocalzDriverSDK.INSTANCE.getOrderDetails(OrderDetailsActivity.this, order.orderNumber, new Callback<Order>() {
            @Override
            public void onSuccess(Order result) {
                Log.d(TAG, "getOrderDetails onSuccess");
                OrderDetailsActivity.order = result;
                runOnUiThread(() -> updateView(OrderDetailsActivity.order));
            }

            @Override
            public void onError(@NotNull Error error) {
                Log.d(TAG, "getOrderDetails onError: " + error);
            }
        });
    }

    private void updateView(Order order) {
        ((TextView) findViewById(R.id.orderNumber)).setText(order.orderNumber);
        ((TextView) findViewById(R.id.orderStatus)).setText("" + order.orderStatus);
    }
}
