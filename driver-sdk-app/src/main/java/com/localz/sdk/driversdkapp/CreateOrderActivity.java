package com.localz.sdk.driversdkapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.localz.sdk.driver.Callback;
import com.localz.sdk.driver.Error;
import com.localz.sdk.driver.LocalzDriverSDK;
import com.localz.sdk.driver.model.Order;
import com.localz.sdk.driver.model.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

public class CreateOrderActivity extends AppCompatActivity {

    private static final String TAG = "CreateOrderActivity";

    private EditText orderNumber;
    private EditText orderAmount;
    private EditText deliveryName;
    private EditText deliveryEmail;
    private EditText deliveryPhone;
    private EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        orderNumber = findViewById(R.id.orderNumber);
        orderAmount = findViewById(R.id.orderAmount);
        deliveryName = findViewById(R.id.deliveryName);
        deliveryEmail = findViewById(R.id.deliveryEmail);
        deliveryPhone = findViewById(R.id.deliveryPhone);
        address = findViewById(R.id.address);

        findViewById(R.id.submit).setOnClickListener(v -> {
            Order order = new Order();
            order.orderStatus = OrderStatus.PENDING;
            order.orderDate = new Date();
            order.pickupStart = new Date();
            order.pickupEnd = new Date(System.currentTimeMillis() + 1000000);
            order.totalItems = 0;
            order.pickupLocation = "Entrance";
            order.currency = "AUD";
            order.orderNumber = orderNumber.getText().toString();
            order.orderAmount = new BigDecimal(orderAmount.getText().toString());
            order.deliveryName = deliveryName.getText().toString();
            order.deliveryEmail = deliveryEmail.getText().toString();
            order.deliveryPhone = deliveryPhone.getText().toString();
            order.specific = new HashMap<>();
            order.specific.put("address", address.getText().toString());

            LocalzDriverSDK.getInstance().createOrder(CreateOrderActivity.this, order, new Callback<Order>() {

                @Override
                public void onSuccess(Order result) {
                    Log.d(TAG, "createOrder onSuccess");
                }

                @Override
                public void onError(Error error) {
                    Log.d(TAG, "createOrder onError: " + error);
                }
            });
        });
    }
}
