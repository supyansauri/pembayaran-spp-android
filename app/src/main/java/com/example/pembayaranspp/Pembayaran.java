package com.example.pembayaranspp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Pembayaran extends AppCompatActivity {
    EditText nis, kelas, jumlah_pembayaran, semester, tahun_ajaran;
    Button submitpay;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);
        nis = (EditText)findViewById(R.id.nispembayaran);
        kelas = (EditText)findViewById(R.id.kelaspay);
        jumlah_pembayaran = (EditText)findViewById(R.id.jumlah_pembayaranpay);
        semester = (EditText)findViewById(R.id.semesterpay);
        tahun_ajaran = (EditText)findViewById(R.id.tahunajaranpay);
        submitpay = (Button) findViewById(R.id.btnsubmitpembayaran);
        progressDialog = new ProgressDialog(Pembayaran.this);

        submitpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sNis = nis.getText().toString();
                String sKelas = kelas.getText().toString();
                String sJumlah_pembayaran = jumlah_pembayaran.getText().toString();
                String sSemester = semester.getText().toString();
                String sTahun_ajaran = tahun_ajaran.getText().toString();

                CheckPembayaran(sNis, sKelas, sJumlah_pembayaran, sSemester, sTahun_ajaran);
            }
        });
    }
    public void CheckPembayaran(final String nis, final String kelas, final String jumlah_pembayaran, final String semester, final String tahun_ajaran) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_PEMBAYARAN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"ok\"}]")) {
                                    Toast.makeText(getApplicationContext(), "Input Data Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent dashboardIntent = new Intent(Pembayaran.this, Dashboard.class);
                                    startActivity(dashboardIntent);
                                } else {
                                    Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("nis", nis);
                    params.put("kelas", kelas);
                    params.put("jumlah_pembayaran", jumlah_pembayaran);
                    params.put("semester", semester);
                    params.put("tahun_ajaran", tahun_ajaran);
                    return params;
                }
            };

            VolleyConnection.getInstance(Pembayaran.this).addToRequestQue(stringRequest);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.cancel();
                }
            }, 2000);
        } else {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
        }

    }
    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}