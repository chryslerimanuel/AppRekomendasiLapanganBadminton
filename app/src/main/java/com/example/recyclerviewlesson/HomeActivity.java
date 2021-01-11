package com.example.recyclerviewlesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclerviewlesson.adapter.MyAdapter;
import com.example.recyclerviewlesson.model.ListTopsis;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends AppCompatActivity {

    Button btnRekomendasi, btnSemuaLap;
    //TextView tvLokasi;

    //ArrayList<ListTopsis> listHome;
    //private MyAdapter adapter;
    //private RecyclerView recyclerView;

    //Jumlah user awal = 1
    int mCounter = 1;

    FusedLocationProviderClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        requestPermission();

        if (!haveNetworkConnection()) {
            needInternetConnection();

        } else {

        }

        btnRekomendasi = findViewById(R.id.btn_rekomendasi);
        btnSemuaLap = findViewById(R.id.btn_lihatLap);

        mClient = LocationServices.getFusedLocationProviderClient(this);

        btnRekomendasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomeActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.jumlah_pengguna_dialog, null);

                Button btnTambah = mView.findViewById(R.id.button_tambah);
                Button btnKurang = mView.findViewById(R.id.button_kurang);
                Button btnKonfirmasi = mView.findViewById(R.id.button_konfirmasiJumlah);
                final TextView tvJumlahPengguna = mView.findViewById(R.id.tv_jumlahPengguna);

                tvJumlahPengguna.setText(Integer.toString(mCounter));

                btnTambah.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCounter++;
                        tvJumlahPengguna.setText(Integer.toString(mCounter));
                        Log.i("COUNTER_PLUS", String.valueOf(mCounter));
                    }
                });


                btnKurang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mCounter >= 2) {
                            mCounter--;
                        }
                        tvJumlahPengguna.setText(Integer.toString(mCounter));
                    }
                });


                btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!haveNetworkConnection()) {
                            needInternetConnection();

                        } else {

                            if (ActivityCompat.checkSelfPermission(HomeActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                return;
                            }

                            mClient.getLastLocation().addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {

                                    if (location != null) {

                                        double latitude = location.getLatitude();
                                        double longitude = location.getLongitude();
                                        Log.i("lat_penggunga", String.valueOf(latitude));
                                        Log.i("lng_pengguna", String.valueOf(longitude));

                                        LatLng latLng = new LatLng(latitude, longitude);
                                        Log.i("lat_lang_pengguna", String.valueOf(latLng));

                                        Intent intent = new Intent(HomeActivity.this, BobotActivity.class);
                                        Bundle args = new Bundle();
                                        args.putParcelable("user_lat_lng", latLng);
                                        intent.putExtras(args);
                                        intent.putExtra("jumlah_pengguna", mCounter);
                                        Log.i("JUMLAH_USER", String.valueOf(mCounter));
                                        startActivity(intent);

                                    }
                                }
                            });
                        }

                    }
                });

                //call dialog layout
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });

        btnSemuaLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!haveNetworkConnection()) {
                    needInternetConnection();

                } else {

                    if (ActivityCompat.checkSelfPermission(HomeActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }

                    mClient.getLastLocation().addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {

                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();

                                LatLng latLng = new LatLng(latitude, longitude);

                                Intent intent = new Intent(HomeActivity.this,LapanganActivity.class);
                                Bundle args = new Bundle();
                                args.putParcelable("user_lat_lng_lap", latLng);
                                intent.putExtras(args);
                                startActivity(intent);

                            }
                        }
                    });
                }

            }
        });

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private boolean haveNetworkConnection() {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo info : netInfo) {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    haveConnectedWifi = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    private void needInternetConnection() {

        new AlertDialog.Builder(this)
                .setTitle("Connection Error")
                .setMessage("Mohon aktifkan koneksi internet dan GPS.")
                .setPositiveButton("Coba Lagi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!haveNetworkConnection()) {
                            needInternetConnection();
                        }
                    }
                })
                .setCancelable(false)
                .create().show();

    }


}
