package com.example.pembayaranspp;

import androidx.appcompat.app.AppCompatActivity;

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

public class InputSiswa extends AppCompatActivity {
    EditText nis, nama, tmp_lahir, tgl_lahir, jk, no_hp, nama_ortu, nohp_ortu, alamat, agama;
    Button submit;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_siswa);

        nis = (EditText)findViewById(R.id.nisinput);
        nama = (EditText)findViewById(R.id.namainput);
        tmp_lahir = (EditText)findViewById(R.id.tmp_lahirinput);
        tgl_lahir = (EditText)findViewById(R.id.tgl_lahir);
        jk = (EditText)findViewById(R.id.jkinput);
        no_hp = (EditText)findViewById(R.id.nohpinput);
        nama_ortu = (EditText)findViewById(R.id.namaortuinput);
        nohp_ortu = (EditText)findViewById(R.id.nohportuipt);
        alamat = (EditText)findViewById(R.id.alamatinput);
        agama = (EditText)findViewById(R.id.agamainput);
        submit = (Button)findViewById(R.id.btnsubmitsiswa);
        progressDialog = new ProgressDialog(InputSiswa.this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sNis = nis.getText().toString();
                String sNama = nama.getText().toString();
                String sTmp_lahir = tmp_lahir.getText().toString();
                String sTgl_lahir = tgl_lahir.getText().toString();
                String sJk = jk.getText().toString();
                String sNo_hp = no_hp.getText().toString();
                String sNama_ortu = nama_ortu.getText().toString();
                String sNohp_ortu = nohp_ortu.getText().toString();
                String sAlamat = alamat.getText().toString();
                String sAgama = agama.getText().toString();

                CheckSiswa(sNis, sNama, sTmp_lahir, sTgl_lahir, sJk, sNo_hp, sNama_ortu, sNohp_ortu, sAlamat, sAgama);

            }
        });
    }
    public void CheckSiswa(final String nis, final String nama, final String tmp_lahir, final String tgl_lahir, final String jk, final String no_hp, final String nama_ortu, final String nohp_ortu, final String alamat, final String agama) {
        if (checkNetworkConnection()) {
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContract.SERVER_SISWA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String resp = jsonObject.getString("server_response");
                                if (resp.equals("[{\"status\":\"ok\"}]")) {
                                    Toast.makeText(getApplicationContext(), "Input Data Siswa Berhasil", Toast.LENGTH_SHORT).show();
                                    Intent dashboardIntent = new Intent(InputSiswa.this, Dashboard.class);
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
                    params.put("nama", nama);
                    params.put("tmp_lahir", tmp_lahir);
                    params.put("tgl_lahir", tgl_lahir);
                    params.put("jk", jk);
                    params.put("no_hp", no_hp);
                    params.put("nama_ortu", nama_ortu);
                    params.put("nohp_ortu", nohp_ortu);
                    params.put("alamat", alamat);
                    params.put("agama", agama);
                    return params;
                }
            };

            VolleyConnection.getInstance(InputSiswa.this).addToRequestQue(stringRequest);

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