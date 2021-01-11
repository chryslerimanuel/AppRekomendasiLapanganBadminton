package com.example.recyclerviewlesson;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    Button btnGmaps1;
    ImageView fotoLap1;
    TextView namaLap1, jarakLap1, alamatLap1, hargaLap1, hariLap1, fasilitasLap1, jumlahLap1, penontonLap1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btnGmaps1 = findViewById(R.id.btn_gmaps);

        fotoLap1 = findViewById(R.id.iv_detFotoLap);

        namaLap1 = findViewById(R.id.tv_detNamaLap);
        jarakLap1 = findViewById(R.id.tv_detJarakLap);
        alamatLap1 = findViewById(R.id.tv_detAlamatLap);
        hargaLap1 = findViewById(R.id.tv_detHargaLap);
        hariLap1 = findViewById(R.id.tv_detHariLap);
        fasilitasLap1 = findViewById(R.id.tv_detFasilitasLap);
        jumlahLap1 = findViewById(R.id.tv_detJumlahLap);
        penontonLap1 = findViewById(R.id.tv_detPenontonLap);

        String namaLap = getIntent().getStringExtra("nama");
        Log.i("VALUE NAMA", namaLap);
        Toast.makeText(this, " " + namaLap, Toast.LENGTH_SHORT).show();

        String foto_array = getIntent().getStringExtra("foto");
        Picasso.get().load(foto_array).into(fotoLap1);

        String nama_array = getIntent().getStringExtra("nama");
        namaLap1.setText(nama_array);

        String jarak_array = getIntent().getStringExtra("jarak");
        jarakLap1.setText(jarak_array);

        final String alamat_array = getIntent().getStringExtra("alamat");
        alamatLap1.setText(alamat_array);

        String harga_array = getIntent().getStringExtra("harga");
        hargaLap1.setText(harga_array);

        String hari_array = getIntent().getStringExtra("hari");
        hariLap1.setText(hari_array);

        String fasilitas_array = getIntent().getStringExtra("det_fasilitas");
        fasilitasLap1.setText(fasilitas_array);

        String jumlah_array = getIntent().getStringExtra("jumlah");
        jumlahLap1.setText(jumlah_array);

        String penonton_array = getIntent().getStringExtra("penonton");
        penontonLap1.setText(penonton_array);

        btnGmaps1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!haveNetworkConnection()) {
                    needInternetConnection();

                } else {
                    String map = "http://maps.google.co.in/maps?q=" + alamat_array;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                    startActivity(intent);
                }
            }
        });
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
                .setMessage("Tidak dapat tersambung ke internet.")
                .setPositiveButton("Coba Lagi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!haveNetworkConnection()) {
                            needInternetConnection();
                        }
                    }
                })
                .create().show();

    }

}
