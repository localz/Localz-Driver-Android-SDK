package com.localz.sdk.driversdkapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.localz.sdk.driver.Callback;
import com.localz.sdk.driver.Error;
import com.localz.sdk.driver.model.Address;
import com.localz.sdk.driver.model.Order;
import com.localz.sdk.driver.model.OrderStatus;
import com.localz.sdk.driver.play.LocalzDriverSDK;

import java.math.BigDecimal;
import java.util.Date;

public class CreateOrderActivity extends AppCompatActivity {

    private static final String TAG = "CreateOrderActivity";

    private EditText orderNumber;
    private EditText orderAmount;
    private EditText deliveryName;
    private EditText deliveryEmail;
    private EditText deliveryPhone;
    private EditText subStreet;
    private EditText street;
    private EditText locality;
    private EditText postcode;
    private EditText region;
    private EditText country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        orderNumber = findViewById(R.id.orderNumber);
        orderAmount = findViewById(R.id.orderAmount);
        deliveryName = findViewById(R.id.deliveryName);
        deliveryEmail = findViewById(R.id.deliveryEmail);
        deliveryPhone = findViewById(R.id.deliveryPhone);
        subStreet = findViewById(R.id.sub_street);
        street = findViewById(R.id.street);
        locality = findViewById(R.id.locality);
        postcode = findViewById(R.id.postcode);
        region = findViewById(R.id.region);
        country = findViewById(R.id.country);

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
            Address address = new Address();
            address.subStreetAddress = subStreet.getText().toString();
            address.streetAddress = street.getText().toString();
            address.locality = locality.getText().toString();
            address.postcode = postcode.getText().toString();
            address.region = region.getText().toString();
            address.country = country.getText().toString();
            order.address = address;

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
