package com.example.recyclerviewlesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.recyclerviewlesson.adapter.LapAdapter;
import com.example.recyclerviewlesson.adapter.MyAdapter;
import com.example.recyclerviewlesson.model.ListItemModel;
import com.example.recyclerviewlesson.model.ListTopsis;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LapanganActivity extends AppCompatActivity implements LapAdapter.ItemClickListener {

    RecyclerView recyclerView1;

    double doubleJarak;
    double user_lat, user_lng;

    private ArrayList<ListItemModel> listModels1;
    private ArrayList<ListTopsis> listLap;

    DatabaseReference lapDatabase;

    private LapAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapangan);

        //RecyclerView
        recyclerView1 = findViewById(R.id.recyclerView_lap);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(LapanganActivity.this));

        listModels1 = new ArrayList<>();
        listLap = new ArrayList<>();

        //get user latitude -- longitude
        Intent intent = getIntent();
        LatLng latLng = intent.getParcelableExtra("user_lat_lng_lap");
        user_lat = latLng.latitude;
        user_lng = latLng.longitude;
        Log.i("USER_LAT_LAP", String.valueOf(user_lat));
        Log.i("USER_LNG_LAP", String.valueOf(user_lng));

        lapDatabase = FirebaseDatabase.getInstance().getReference().child("places");
        lapDatabase.keepSynced(true);
        lapDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final ListItemModel model = dataSnapshot1.getValue(ListItemModel.class);
                    listModels1.add(model);

                }

                for (int i = 0; i<listModels1.size(); i++ ){

                    Log.i("DATA_FIREBASE_LAP", String.valueOf(listModels1.get(i).getHarga()));

                    String nama = listModels1.get(i).getNama();
                    String alamat = listModels1.get(i).getAlamat();
                    String foto = listModels1.get(i).getFoto();
                    String detHariBuka = listModels1.get(i).getDetharibuka();
                    String detPenonton = listModels1.get(i).getDetpenonton();
                    String detJumlap = listModels1.get(i).getDetjumlap();
                    String detFasil = listModels1.get(i).getDetfasil();

                    int id = listModels1.get(i).getId();

                    Log.i("JUMLAH_PENONTON", detPenonton);

                    double doubleHarga = listModels1.get(i).getHarga();
                    doubleJarak = HitungJarak(listModels1.get(i).getLat(), listModels1.get(i).getLng(), user_lat, user_lng);

                    Log.i("JARAK_LAP_LAP", detPenonton);
                    double doubleFasiltas = listModels1.get(i).getFasilitas();


                    listLap.add(new ListTopsis(doubleHarga, doubleJarak, doubleFasiltas, nama, alamat, foto, id, detHariBuka, detPenonton, detJumlap, detFasil));
                    Log.i("DATA_ARRAY_LAP", String.valueOf(listLap.get(i).getJarak()));

                    adapter1 = new LapAdapter(LapanganActivity.this, listLap);
                    adapter1.addItemClickListener(LapanganActivity.this);
                    recyclerView1.setAdapter(adapter1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Hitung jarak
    private Double HitungJarak(double lapangan_latitude, double lapangan_longitude, double user_latitude, double user_longitude) {

        double hasil = 0.0;

        // jarak lapangan - user latitude&longitude
        double lapBad_Lat = Math.toRadians(lapangan_latitude - user_latitude);
        double lapBad_Lng = Math.toRadians(lapangan_longitude - user_longitude);

        // ubah ke bentuk radians
        double lat_lap_rad = Math.toRadians(lapangan_latitude);
        double lat_user_rad = Math.toRadians(user_latitude);

        // perhitungan Cousine
        double cosines = Math.pow(Math.sin(lapBad_Lat / 2), 2) +
                Math.pow(Math.sin(lapBad_Lng / 2), 2) *
                        Math.cos(lat_lap_rad) *
                        Math.cos(lat_user_rad);
        double rad = 6371;

        hasil = 2 * rad * Math.asin(Math.sqrt(cosines));
        Log.i("HASIL_JARAK", String.valueOf(hasil));

        return hasil;
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(LapanganActivity.this, DetailActivity.class);
        intent.putExtra("foto", listLap.get(position).getFoto());
        intent.putExtra("nama", listLap.get(position).getNama());
        intent.putExtra("jarak", String.valueOf(listLap.get(position).getJarak()));
        intent.putExtra("alamat", listLap.get(position).getAlamat());
        intent.putExtra("harga", String.valueOf(listLap.get(position).getHarga()));
        intent.putExtra("hari", listLap.get(position).getDetharibuka());
        intent.putExtra("jumlah", listLap.get(position).getDetjumlap());
        intent.putExtra("penonton", listLap.get(position).getDetpenonton());
        intent.putExtra("det_fasilitas", listLap.get(position).getDetfasil());
        startActivity(intent);
    }



    /*
    @Override
    public void on(int position) {
        Intent intent = new Intent(LapanganActivity.this, DetailActivity.class);
        intent.putExtra("foto", listLap.get(position).getFoto());
        intent.putExtra("nama", listLap.get(position).getNama());
        intent.putExtra("jarak", String.valueOf(listLap.get(position).getJarak()));
        intent.putExtra("alamat", listLap.get(position).getAlamat());
        intent.putExtra("harga", String.valueOf(listLap.get(position).getHarga()));
        intent.putExtra("hari", listLap.get(position).getDetharibuka());
        intent.putExtra("jumlah", listLap.get(position).getDetjumlap());
        intent.putExtra("penonton", listLap.get(position).getDetpenonton());
        intent.putExtra("det_fasilitas", listLap.get(position).getDetfasil());
        startActivity(intent);
    }

     */
}
