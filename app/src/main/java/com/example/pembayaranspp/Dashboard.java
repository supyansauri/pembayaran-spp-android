package com.example.pembayaranspp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Dashboard extends AppCompatActivity {
    Button datasiswa;
    Button pembayaransis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        datasiswa = (Button)findViewById(R.id.btndatasiswa);
        pembayaransis = (Button)findViewById(R.id.btnpembayaransiswa);


        datasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent datasiswaIntent = new Intent(Dashboard.this, InputSiswa.class);
                startActivity(datasiswaIntent);
            }
        });

        pembayaransis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pembayaransiswa = new Intent(Dashboard.this, Pembayaran.class);
                startActivity(pembayaransiswa);
            }
        });
    }

}

