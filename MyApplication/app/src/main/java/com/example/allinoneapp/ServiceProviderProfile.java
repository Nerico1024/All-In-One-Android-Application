package com.example.allinoneapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.allinoneapp.ui.login.LoginActivity;

public class ServiceProviderProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_profile);

        Intent intent=getIntent();
        String address = intent.getStringExtra("StreetAddress");
        String service = intent.getStringExtra("Service");
        String fullName = intent.getStringExtra("FullName");
        String mobile = intent.getStringExtra("MobileNumber");

        final TextView tv_name = findViewById(R.id.tv_name);
        final TextView tv_address = findViewById(R.id.tv_address);
        final TextView tv_service = findViewById(R.id.tv_service);
        final TextView tv_mobile = findViewById(R.id.tv_mobile);

        tv_name.setText(fullName);
        tv_address.setText(address);
        tv_service.setText(service);
        tv_mobile.setText(mobile);

        Button btnHire = (Button) findViewById(R.id.btn_hire);

        btnHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceProviderProfile.this, ServiceDetailsForm.class);
                startActivity(intent);
            }
        });

    }
}