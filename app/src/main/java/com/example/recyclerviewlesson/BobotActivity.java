package com.example.recyclerviewlesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyclerviewlesson.model.ListItemModel;
import com.example.recyclerviewlesson.model.ListTopsis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BobotActivity extends AppCompatActivity {

    SeekBar seekbarHarga, seekbarJarak, seekbarFasilitas;
    TextView tvHarga, tvJarak, tvFasilitas, tvNmrPengguna;
    Button btnKonfirmasi;

    //Bobot Kriteria
    int bobotHarga, bobotJarak, bobotFasilitas;

    //Jumlah User
    int jumlahUser;

    //counter
    int counter = 0;

    double pembagiHarga, pembagiJarak, pembagiFasilitas;
    double aPositifHarga, aPositifJarak, aPositifFasilitas;
    double aNegatifHarga, aNegatifJarak, aNegatifFasilitas;

    double doubleJarak;

    double user_lat, user_lng;

    double bobotNormalisasiHarga, bobotNormalisasiJarak, bobotNormalisasiFasilitas;

    private ArrayList<ListItemModel> listModels;

    private ArrayList<ListTopsis> listTopsis;
    private ArrayList<ListTopsis> listSorted;

    //Array yang tidak di kosongkan saat perhitungan borda banyak user
    private ArrayList<ListTopsis> listBorda;

    private ArrayList<Double> normalisasiHarga;
    private ArrayList<Double> normalisasiJarak;
    private ArrayList<Double> normalisasiFasilitas;
    private ArrayList<Double> terbobotHarga;
    private ArrayList<Double> terbobotJarak;
    private ArrayList<Double> terbobotFasilitas;
    private ArrayList<Double> siPositif;
    private ArrayList<Double> siNegatif;
    private ArrayList<Double> jumlahSi;
    private ArrayList<Double> ciPositif;

    //Class berisi rumus Topsis
    HitungTopsis hitungTopsis;

    //Firebase database
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bobot);

        BuatDataUntukPerhitunganTopsis();

        if (!haveNetworkConnection()) {
            needInternetConnection();

        } else {

        }

        tvHarga = findViewById(R.id.tv_harga);
        tvJarak = findViewById(R.id.tv_jarak);
        tvFasilitas = findViewById(R.id.tv_fasilitas);
        tvNmrPengguna = findViewById(R.id.textview_nmrPengguna);
        btnKonfirmasi = findViewById(R.id.button_konfirmasiBobot);

        //Inisialisasi array
        listModels = new ArrayList<>();

        listTopsis = new ArrayList<>();
        listSorted = new ArrayList<>();

        listBorda = new ArrayList<ListTopsis>();

        normalisasiHarga = new ArrayList<>();
        normalisasiJarak = new ArrayList<>();
        normalisasiFasilitas = new ArrayList<>();

        terbobotHarga = new ArrayList<>();
        terbobotJarak = new ArrayList<>();
        terbobotFasilitas = new ArrayList<>();

        siPositif = new ArrayList<>();
        siNegatif = new ArrayList<>();

        jumlahSi = new ArrayList<>();

        ciPositif = new ArrayList<>();

        //seekbar
        seekbarHarga = findViewById(R.id.seekbar_harga);
        seekbarJarak = findViewById(R.id.seekbar_jarak);
        seekbarFasilitas = findViewById(R.id.seekbar_fasilitas);
        seekbarHarga.setMax(10);
        seekbarJarak.setMax(10);
        seekbarFasilitas.setMax(10);

        tvNmrPengguna.setText("Pengguna "+ String.valueOf(counter + 1));

        //get user latitude -- longitude
        Intent intent = getIntent();
        LatLng latLng = intent.getParcelableExtra("user_lat_lng");
        user_lat = latLng.latitude;
        user_lng = latLng.longitude;
        Log.i("USER_LAT", String.valueOf(user_lat));
        Log.i("USER_LNG", String.valueOf(user_lng));

        //get jumlah user
        jumlahUser = intent.getIntExtra("jumlah_pengguna", 1);

        //class HitungTopsis
        hitungTopsis = new HitungTopsis();

        seekbarHarga.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bobotHarga = seekBar.getProgress();
                Log.i("SB_NILAI", String.valueOf(bobotHarga));
                tvHarga.setText(String.valueOf(bobotHarga));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekbarJarak.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bobotJarak = seekBar.getProgress();
                tvJarak.setText(String.valueOf(bobotJarak));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekbarFasilitas.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bobotFasilitas = seekBar.getProgress();
                tvFasilitas.setText(String.valueOf(bobotFasilitas));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!haveNetworkConnection()) {
                    needInternetConnection();

                } else {

                if (bobotHarga == 0) {

                    Toast.makeText(getApplicationContext(), "Harga tidak boleh 0", Toast.LENGTH_SHORT).show();

                } else if (bobotJarak == 0) {

                    Toast.makeText(getApplicationContext(), "Jarak tidak boleh 0", Toast.LENGTH_SHORT).show();

                } else if (bobotFasilitas == 0) {

                    Toast.makeText(getApplicationContext(), "Fasilitas tidak boleh 0", Toast.LENGTH_SHORT).show();

                } else {

                    counter++;

                    //Normalisasi bobot kriteria
                    bobotNormalisasiHarga = ((double) bobotHarga) / ((double) bobotHarga + (double) bobotJarak + (double) bobotFasilitas);
                    Log.i("N_HARGA", String.valueOf(bobotNormalisasiHarga));
                    bobotNormalisasiJarak = ((double) bobotJarak) / ((double) bobotHarga + (double) bobotJarak + (double) bobotFasilitas);
                    Log.i("N_JARAK", String.valueOf(bobotNormalisasiJarak));
                    bobotNormalisasiFasilitas = ((double) bobotFasilitas) / ((double) bobotHarga + (double) bobotJarak + (double) bobotFasilitas);
                    Log.i("N_FASILITAS", String.valueOf(bobotNormalisasiFasilitas));


                    if (jumlahUser == 1) {

                        KalkulasiTopsis();
                        GabungTopsisBordaKeFirebaseForOneUserOnly(ciPositif);


                    } else if (counter == 1) {

                        KalkulasiTopsis();
                        GabungTopsisBordaKeFirebaseForFirstUser(ciPositif);

                    } else if (counter >= 1 && counter < jumlahUser) {

                        KalkulasiTopsis();
                        GabungTopsisBordaKeFirebaseForMiddleUser(ciPositif);

                    } else if (counter == jumlahUser) {

                        KalkulasiTopsis();
                        GabungTopsisBordaKeFirebaseForLastUser(ciPositif);

                    }

                }
            }
            }
        });

    }

    //Buat data model baru double untuk perhitungan Topsis
    private void BuatDataUntukPerhitunganTopsis() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("places");
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final ListItemModel model = dataSnapshot1.getValue(ListItemModel.class);
                    listModels.add(model);

                }

                for (int i = 0; i < listModels.size(); i++) {


                    Log.i("DATA_FIREBASE", String.valueOf(listModels.get(i).getHarga()));

                    double doubleHarga = listModels.get(i).getHarga();
                    doubleJarak = HitungJarak(listModels.get(i).getLat(), listModels.get(i).getLng(), user_lat, user_lng);
                    double doubleFasiltas = listModels.get(i).getFasilitas();

                    listTopsis.add(new ListTopsis(doubleHarga, doubleJarak, doubleFasiltas));
                }

                pembagiHarga = Math.sqrt(hitungTopsis.PembagiHarga(listTopsis, pembagiHarga));
                Log.i("PEMBAGI_HARGA", String.valueOf(pembagiHarga));
                pembagiJarak = Math.sqrt(hitungTopsis.PembagiJarak(listTopsis, pembagiJarak));
                Log.i("PEMBAGI_JARAK", String.valueOf(pembagiJarak));
                pembagiFasilitas = Math.sqrt(hitungTopsis.PembagiFasilitas(listTopsis, pembagiFasilitas));
                Log.i("PEMBAGI_FASILITAS", String.valueOf(pembagiFasilitas));
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

    //Sort berdasarkan id
    private class SortById implements Comparator<ListTopsis> {
        @Override
        public int compare(ListTopsis obj1, ListTopsis obj2) {

            return Integer.compare(obj1.getId(), obj2.getId());
        }
    }

    //Sort CI ascending (kecil ke besar) untuk dikali Ranking Borda
    private class SortBorda implements Comparator<ListTopsis> {
        @Override
        public int compare(ListTopsis obj1, ListTopsis obj2) {

            return Double.compare(obj1.getCiPositif(), obj2.getCiPositif());
        }
    }

    //Perhitungan Topsis
    private void KalkulasiTopsis() {

        //Normalisasi
        hitungTopsis.NormalisasiHarga(listTopsis, pembagiHarga, normalisasiHarga);
        hitungTopsis.NormalisasiJarak(listTopsis, pembagiJarak, normalisasiJarak);
        Log.i("NORMALISASI_JARAK", String.valueOf(normalisasiJarak));
        hitungTopsis.NormalisasiFasilitas(listTopsis, pembagiFasilitas, normalisasiFasilitas);

        //Terbobot
        hitungTopsis.TerbobotHarga(normalisasiHarga, bobotNormalisasiHarga, terbobotHarga);
        hitungTopsis.TerbobotJarak(normalisasiJarak, bobotNormalisasiJarak, terbobotJarak);
        Log.i("TERBOBOT_JARAK", String.valueOf(terbobotJarak));
        hitungTopsis.TerbobotFasilitas(normalisasiFasilitas, bobotNormalisasiFasilitas, terbobotFasilitas);

        //A+
        aPositifHarga = Collections.min(terbobotHarga);
        aPositifJarak = Collections.min(terbobotJarak);
        Log.i("APOS_JARAK", String.valueOf(aPositifJarak));
        aPositifFasilitas = Collections.max(terbobotFasilitas);

        //A-
        aNegatifHarga = Collections.max(terbobotHarga);
        aNegatifJarak = Collections.max(terbobotJarak);
        Log.i("ANEG_JARAK", String.valueOf(aNegatifJarak));
        aNegatifFasilitas = Collections.min(terbobotFasilitas);

        //Si+
        hitungTopsis.siPositif(terbobotHarga, terbobotJarak, terbobotFasilitas, aPositifHarga, aPositifJarak, aPositifFasilitas, siPositif);
        Log.i("SI_POSITIF", String.valueOf(siPositif));

        //Si-
        hitungTopsis.siNegatif(terbobotHarga, terbobotJarak, terbobotFasilitas, aNegatifHarga, aNegatifJarak, aNegatifFasilitas, siNegatif);
        Log.i("SI_NEGATIF", String.valueOf(siNegatif));

        //Jumlah Si
        hitungTopsis.jumlahSi(siPositif, siNegatif, jumlahSi);
        Log.i("JUMLAH_SI", String.valueOf(jumlahSi));

        //Ci+
        hitungTopsis.ciPositif(siNegatif, jumlahSi, ciPositif);
        Log.i("CI_POSITIF", String.valueOf(ciPositif));
    }

    //ambil data dari firebase
    private void GabungTopsisBordaKeFirebaseForOneUserOnly(final ArrayList<Double> Ci) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("places");
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final ListItemModel model = dataSnapshot1.getValue(ListItemModel.class);
                    listModels.add(model);
                }

                for (int i = 0; i < listTopsis.size(); i++) {

                    String nama = listModels.get(i).getNama();
                    String alamat = listModels.get(i).getAlamat();
                    String foto = listModels.get(i).getFoto();
                    String detHariBuka = listModels.get(i).getDetharibuka();
                    String detPenonton = listModels.get(i).getDetpenonton();
                    String detJumlap = listModels.get(i).getDetjumlap();
                    String detFasil = listModels.get(i).getDetfasil();

                    int id = listModels.get(i).getId();

                    double doubleHarga = listModels.get(i).getHarga();
                    doubleJarak = HitungJarak(listModels.get(i).getLat(), listModels.get(i).getLng(), user_lat, user_lng);
                    double doubleFasiltas = listModels.get(i).getFasilitas();
                    double doubleCi = Ci.get(i);

                    listSorted.add(new ListTopsis(doubleHarga, doubleJarak, doubleFasiltas, doubleCi, nama, alamat, foto, id, detHariBuka, detPenonton, detJumlap, detFasil));
                    Log.i("ARRAY_D_ONE", "\n " +
                            "\n HARGA: " + listSorted.get(i).getHarga() +
                            "\n JARAK: " + listSorted.get(i).getJarak() +
                            "\n FASILITAS: " + listSorted.get(i).getFasilitas() +
                            "\n NAMA: " + listSorted.get(i).getNama() +
                            "\n ID: " + listSorted.get(i).getId() +
                            "\n CIPOSITIF: " + listSorted.get(i).getCiPositif() +
                            "\n DETFASIL: " + listSorted.get(i).getDetfasil());
                }

                SortAndBordaForOneUserOnly(listSorted);
                tvNmrPengguna.setText("Pengguna 1");
                SendArrayHasil(listSorted);

                for(int i = 0; i<listSorted.size(); i++) {
                    Log.i("ARRAY_W_FASIL", "\n " +
                            "\n HARGA: " + listSorted.get(i).getHarga() +
                            "\n JARAK: " + listSorted.get(i).getJarak() +
                            "\n FASILITAS: " + listSorted.get(i).getFasilitas() +
                            "\n NAMA: " + listSorted.get(i).getNama() +
                            "\n ID: " + listSorted.get(i).getId() +
                            "\n CIPOSITIF: " + listSorted.get(i).getCiPositif() +
                            "\n DETFASIL: " + listSorted.get(i).getDetfasil());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //ambil data dari firebase
    private void GabungTopsisBordaKeFirebaseForFirstUser(final ArrayList<Double> Ci) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("places");
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final ListItemModel model = dataSnapshot1.getValue(ListItemModel.class);
                    listModels.add(model);
                }

                for (int i = 0; i < listTopsis.size(); i++) {

                    String nama = listModels.get(i).getNama();
                    String alamat = listModels.get(i).getAlamat();
                    String foto = listModels.get(i).getFoto();
                    String detHariBuka = listModels.get(i).getDetharibuka();
                    String detPenonton = listModels.get(i).getDetpenonton();
                    String detJumlap = listModels.get(i).getDetjumlap();
                    String detFasil = listModels.get(i).getDetfasil();

                    int id = listModels.get(i).getId();

                    double doubleHarga = listModels.get(i).getHarga();
                    doubleJarak = HitungJarak(listModels.get(i).getLat(), listModels.get(i).getLng(), user_lat, user_lng);
                    double doubleFasiltas = listModels.get(i).getFasilitas();
                    double doubleCi = Ci.get(i);

                    listSorted.add(new ListTopsis(doubleHarga, doubleJarak, doubleFasiltas, doubleCi, nama, alamat, foto, id, detHariBuka, detPenonton, detJumlap, detFasil));
                    Log.i("ARRAY_G_FIRST", "\n " +
                            "\n HARGA: " + listSorted.get(i).getHarga() +
                            "\n JARAK: " + listSorted.get(i).getJarak() +
                            "\n FASILITAS: " + listSorted.get(i).getFasilitas() +
                            "\n NAMA: " + listSorted.get(i).getNama() +
                            "\n ID: " + listSorted.get(i).getId() +
                            "\n CIPOSITIF: " + listSorted.get(i).getCiPositif());
                }

                SortAndBordaForFirstUser(listSorted);
                ClearArray();
                tvNmrPengguna.setText("Pengguna "+ String.valueOf(counter + 1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //ambil data dari firebase
    private void GabungTopsisBordaKeFirebaseForMiddleUser(final ArrayList<Double> Ci) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("places");
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final ListItemModel model = dataSnapshot1.getValue(ListItemModel.class);
                    listModels.add(model);
                }

                for (int i = 0; i < listTopsis.size(); i++) {

                    String nama = listModels.get(i).getNama();
                    String alamat = listModels.get(i).getAlamat();
                    String foto = listModels.get(i).getFoto();
                    String detHariBuka = listModels.get(i).getDetharibuka();
                    String detPenonton = listModels.get(i).getDetpenonton();
                    String detJumlap = listModels.get(i).getDetjumlap();
                    String detFasil = listModels.get(i).getDetfasil();

                    int id = listModels.get(i).getId();

                    double doubleHarga = listModels.get(i).getHarga();
                    doubleJarak = HitungJarak(listModels.get(i).getLat(), listModels.get(i).getLng(), user_lat, user_lng);
                    double doubleFasiltas = listModels.get(i).getFasilitas();
                    double doubleCi = Ci.get(i);

                    listSorted.add(new ListTopsis(doubleHarga, doubleJarak, doubleFasiltas, doubleCi, nama, alamat, foto, id, detHariBuka, detPenonton, detJumlap, detFasil));
                    Log.i("ARRAY_BARU", "\n " +
                            "\n HARGA: " + listSorted.get(i).getHarga() +
                            "\n JARAK: " + listSorted.get(i).getJarak() +
                            "\n FASILITAS: " + listSorted.get(i).getFasilitas() +
                            "\n NAMA: " + listSorted.get(i).getNama() +
                            "\n ID: " + listSorted.get(i).getId() +
                            "\n CIPOSITIF: " + listSorted.get(i).getCiPositif());
                }

                SortAndBordaForMiddleUser(listSorted);
                ClearArray();
                tvNmrPengguna.setText("Pengguna "+ String.valueOf(counter + 1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //ambil data dari firebase
    private void GabungTopsisBordaKeFirebaseForLastUser(final ArrayList<Double> Ci) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("places");
        mDatabase.keepSynced(true);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    final ListItemModel model = dataSnapshot1.getValue(ListItemModel.class);
                    listModels.add(model);
                }

                for (int i = 0; i < listTopsis.size(); i++) {

                    String nama = listModels.get(i).getNama();
                    String alamat = listModels.get(i).getAlamat();
                    String foto = listModels.get(i).getFoto();
                    String detHariBuka = listModels.get(i).getDetharibuka();
                    String detPenonton = listModels.get(i).getDetpenonton();
                    String detJumlap = listModels.get(i).getDetjumlap();
                    String detFasil = listModels.get(i).getDetfasil();

                    int id = listModels.get(i).getId();

                    double doubleHarga = listModels.get(i).getHarga();
                    doubleJarak = HitungJarak(listModels.get(i).getLat(), listModels.get(i).getLng(), user_lat, user_lng);
                    double doubleFasiltas = listModels.get(i).getFasilitas();
                    double doubleCi = Ci.get(i);

                    listSorted.add(new ListTopsis(doubleHarga, doubleJarak, doubleFasiltas, doubleCi, nama, alamat, foto, id, detHariBuka, detPenonton, detJumlap, detFasil));
                    Log.i("ARRAY_LAST", "\n " +
                            "\n HARGA: " + listSorted.get(i).getHarga() +
                            "\n JARAK: " + listSorted.get(i).getJarak() +
                            "\n FASILITAS: " + listSorted.get(i).getFasilitas() +
                            "\n NAMA: " + listSorted.get(i).getNama() +
                            "\n ID: " + listSorted.get(i).getId() +
                            "\n CIPOSITIF: " + listSorted.get(i).getCiPositif());
                }

                SortAndBordaForMiddleUser(listSorted);
                SendArrayHasil(listBorda);

                for (int i = 0; i < listBorda.size(); i++) {
                    Log.i("ARRAY_RAME-RAME", "\n " +
                            "\n HARGA: " + listBorda.get(i).getHarga() +
                            "\n JARAK: " + listBorda.get(i).getJarak() +
                            "\n FASILITAS: " + listBorda.get(i).getFasilitas() +
                            "\n NAMA: " + listBorda.get(i).getNama() +
                            "\n ID: " + listBorda.get(i).getId() +
                            "\n CIPOSITIF: " + listBorda.get(i).getCiPositif());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //sort borda 1 user only
    private void SortAndBordaForOneUserOnly(ArrayList<ListTopsis> arrayBaru) {

        //sort ascending berdasarkan ciPositif
        Collections.sort(arrayBaru, new SortBorda());

        for (int i = 0; i < arrayBaru.size(); i++) {

            Log.i("ARRAY_SORT_ONE", "\n " +
                    "\n HARGA: " + arrayBaru.get(i).getHarga() +
                    "\n JARAK: " + arrayBaru.get(i).getJarak() +
                    "\n FASILITAS: " + arrayBaru.get(i).getFasilitas() +
                    "\n NAMA: " + arrayBaru.get(i).getNama() +
                    "\n ID: " + arrayBaru.get(i).getId() +
                    "\n CIPOSITIF: " + arrayBaru.get(i).getCiPositif());
        }

        for (int i = 0; i < arrayBaru.size(); i++) {

            String nama = arrayBaru.get(i).getNama();
            String alamat = arrayBaru.get(i).getAlamat();
            String foto = arrayBaru.get(i).getFoto();
            String detHariBuka = arrayBaru.get(i).getDetharibuka();
            String detPenonton = arrayBaru.get(i).getDetpenonton();
            String detJumlap = arrayBaru.get(i).getDetjumlap();
            String detFasil = arrayBaru.get(i).getDetfasil();

            int id = arrayBaru.get(i).getId();

            double doubleHarga = arrayBaru.get(i).getHarga();
            double doubleJarak = arrayBaru.get(i).getJarak();
            double doubleFasiltas = arrayBaru.get(i).getFasilitas();

            //rumus Borda
            double doubleCi = arrayBaru.get(i).getCiPositif() * (i+1);

            //isi array listSorted kita ubah dengan fungsi SET. Isinya kita ubah ke array yg sudah di sort
            arrayBaru.set(i, new ListTopsis(doubleHarga, doubleJarak, doubleFasiltas, doubleCi, nama, alamat, foto, id, detHariBuka, detPenonton, detJumlap, detFasil));

        }

        Collections.sort(arrayBaru, new SortById());

        for (int i = 0; i < arrayBaru.size(); i++) {
            Log.i("ARRAY_ONE", "\n " +
                    "\n HARGA: " + listSorted.get(i).getHarga() +
                    "\n JARAK: " + listSorted.get(i).getJarak() +
                    "\n FASILITAS: " + listSorted.get(i).getFasilitas() +
                    "\n NAMA: " + listSorted.get(i).getNama() +
                    "\n ID: " + listSorted.get(i).getId() +
                    "\n CIPOSITIF: " + listSorted.get(i).getCiPositif());
        }
    }

    //sort borda user pertama
    private void SortAndBordaForFirstUser(ArrayList<ListTopsis> arrayBaru) {

        //sort ascending berdasarkan ciPositif
        Collections.sort(arrayBaru, new SortBorda());

        for (int i = 0; i < arrayBaru.size(); i++) {

            Log.i("ARRAY_SORT_FIRST", "\n " +
                    "\n HARGA: " + arrayBaru.get(i).getHarga() +
                    "\n JARAK: " + arrayBaru.get(i).getJarak() +
                    "\n FASILITAS: " + arrayBaru.get(i).getFasilitas() +
                    "\n NAMA: " + arrayBaru.get(i).getNama() +
                    "\n ID: " + arrayBaru.get(i).getId() +
                    "\n CIPOSITIF: " + arrayBaru.get(i).getCiPositif());
        }

        for (int i = 0; i < arrayBaru.size(); i++) {

            String nama = arrayBaru.get(i).getNama();
            String alamat = arrayBaru.get(i).getAlamat();
            String foto = arrayBaru.get(i).getFoto();
            String detHariBuka = arrayBaru.get(i).getDetharibuka();
            String detPenonton = arrayBaru.get(i).getDetpenonton();
            String detJumlap = arrayBaru.get(i).getDetjumlap();
            String detFasil = arrayBaru.get(i).getDetfasil();

            int id = arrayBaru.get(i).getId();

            double doubleHarga = arrayBaru.get(i).getHarga();
            double doubleJarak = arrayBaru.get(i).getJarak();
            double doubleFasiltas = arrayBaru.get(i).getFasilitas();

            //rumus Borda
            double doubleCi = arrayBaru.get(i).getCiPositif() * (i+1);

            /*
            masukan isi data ke ARRAY BARU yg bernama LISTBORDA,
            karena LISTTOPSIS kita kosongkan untuk user berikutnya...
            */
            listBorda.add(new ListTopsis(doubleHarga, doubleJarak, doubleFasiltas, doubleCi, nama, alamat, foto, id, detHariBuka, detPenonton, detJumlap, detFasil));

        }

        Collections.sort(listBorda, new SortById());

        for (int i = 0; i < arrayBaru.size(); i++) {
            Log.i("ARRAY_FIRST", "\n " +
                    "\n HARGA: " + listSorted.get(i).getHarga() +
                    "\n JARAK: " + listSorted.get(i).getJarak() +
                    "\n FASILITAS: " + listSorted.get(i).getFasilitas() +
                    "\n NAMA: " + listSorted.get(i).getNama() +
                    "\n ID: " + listSorted.get(i).getId() +
                    "\n CIPOSITIF: " + listSorted.get(i).getCiPositif());
        }
    }

    private void SortAndBordaForMiddleUser(ArrayList<ListTopsis> arrayBaru) {

        //sort ascending berdasarkan ciPositif
        Collections.sort(arrayBaru, new SortBorda());

        for (int i = 0; i < arrayBaru.size(); i++) {

            Log.i("ARRAY_SORT_NEXT", "\n " +
                    "\n HARGA: " + arrayBaru.get(i).getHarga() +
                    "\n JARAK: " + arrayBaru.get(i).getJarak() +
                    "\n FASILITAS: " + arrayBaru.get(i).getFasilitas() +
                    "\n NAMA: " + arrayBaru.get(i).getNama() +
                    "\n ID: " + arrayBaru.get(i).getId() +
                    "\n CIPOSITIF: " + arrayBaru.get(i).getCiPositif());
        }


        for (int i = 0; i < arrayBaru.size(); i++) {

            String nama = arrayBaru.get(i).getNama();
            String alamat = arrayBaru.get(i).getAlamat();
            String foto = arrayBaru.get(i).getFoto();
            String detHariBuka = arrayBaru.get(i).getDetharibuka();
            String detPenonton = arrayBaru.get(i).getDetpenonton();
            String detJumlap = arrayBaru.get(i).getDetjumlap();
            String detFasil = arrayBaru.get(i).getDetfasil();

            int id = arrayBaru.get(i).getId();

            double doubleHarga = arrayBaru.get(i).getHarga();
            double doubleJarak = arrayBaru.get(i).getJarak();
            double doubleFasiltas = arrayBaru.get(i).getFasilitas();

            //rumus Borda
            double doubleCi = arrayBaru.get(i).getCiPositif() * (i+1);

            //isi array listSorted kita ubah dengan fungsi SET. Isinya kita ubah ke array yg sudah di sort + borda
            arrayBaru.set(i, new ListTopsis(doubleHarga, doubleJarak, doubleFasiltas, doubleCi, nama, alamat, foto, id, detHariBuka, detPenonton, detJumlap, detFasil));

        }

        Collections.sort(arrayBaru, new SortById());

        for (int i = 0; i < arrayBaru.size(); i++) {

            String nama = arrayBaru.get(i).getNama();
            String alamat = arrayBaru.get(i).getAlamat();
            String foto = arrayBaru.get(i).getFoto();
            String detHariBuka = arrayBaru.get(i).getDetharibuka();
            String detPenonton = arrayBaru.get(i).getDetpenonton();
            String detJumlap = arrayBaru.get(i).getDetjumlap();
            String detFasil = arrayBaru.get(i).getDetfasil();

            int id = arrayBaru.get(i).getId();

            double doubleHarga = arrayBaru.get(i).getHarga();
            double doubleJarak = arrayBaru.get(i).getJarak();
            double doubleFasiltas = arrayBaru.get(i).getFasilitas();

            //rumus Borda -- Untuk banyak user
            double doubleCiGabungan = arrayBaru.get(i).getCiPositif() + listBorda.get(i).getCiPositif();

            //isi array listBorda kita ubah dengan fungsi SET. Isinya kita ubah ke array yg sudah di sort + borda gabungan
            listBorda.set(i, new ListTopsis(doubleHarga, doubleJarak, doubleFasiltas, doubleCiGabungan, nama, alamat, foto, id, detHariBuka, detPenonton, detJumlap, detFasil));

        }

        for (int i = 0; i < listBorda.size(); i++) {

            Log.i("ARRAY_NEXT", "\n " +
                    "\n HARGA: " + listBorda.get(i).getHarga() +
                    "\n JARAK: " + listBorda.get(i).getJarak() +
                    "\n FASILITAS: " + listBorda.get(i).getFasilitas() +
                    "\n NAMA: " + listBorda.get(i).getNama() +
                    "\n ID: " + listBorda.get(i).getId() +
                    "\n CIPOSITIF: " + listBorda.get(i).getCiPositif());
        }

    }

    private void ClearArray() {

        //Normalisasi clear
        normalisasiHarga.clear();
        normalisasiJarak.clear();
        normalisasiFasilitas.clear();

        //Terbobot clear
        terbobotHarga.clear();
        terbobotJarak.clear();
        terbobotFasilitas.clear();

        //Si+ clear
        siPositif.clear();

        //Si- clear
        siNegatif.clear();

        //Jumlah Si clear
        jumlahSi.clear();

        //Ci+
        ciPositif.clear();

        //Helper array clear
        listSorted.clear();

        seekbarHarga.setProgress(0);
        seekbarJarak.setProgress(0);
        seekbarFasilitas.setProgress(0);

    }

    private void SendArrayHasil(ArrayList<ListTopsis> arrayHasil) {

        Intent intent = new Intent(BobotActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("array_hasil", arrayHasil);
        intent.putExtras(bundle);
        Log.i("JUMLAH_BUNDLE", String.valueOf(arrayHasil));
        startActivity(intent);
        finish();

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





