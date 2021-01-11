package com.example.recyclerviewlesson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.recyclerviewlesson.adapter.MyAdapter;
import com.example.recyclerviewlesson.model.ListItemModel;
import com.example.recyclerviewlesson.model.ListTopsis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClickListener {

    private RecyclerView recyclerView;

    private ArrayList<ListTopsis> listTopsisBorda;

    private DatabaseReference mDatabase;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listTopsisBorda = new ArrayList<>();

        //get Parceable array data dari BobotActivity
        Intent intent = getIntent();
        listTopsisBorda = this.getIntent().getParcelableArrayListExtra("array_hasil");
        Log.i("LIST_FIREBASE", String.valueOf(listTopsisBorda));

        Collections.sort(listTopsisBorda, new SortBorda());

        adapter = new MyAdapter(MainActivity.this, listTopsisBorda);
        adapter.addItemClickListener(MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private class SortBorda implements Comparator<ListTopsis> {
        @Override
        public int compare(ListTopsis obj1, ListTopsis obj2) {

            return Double.compare(obj2.getCiPositif(), obj1.getCiPositif());
        }
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("foto", listTopsisBorda.get(position).getFoto());
        intent.putExtra("nama", listTopsisBorda.get(position).getNama());
        intent.putExtra("jarak", String.valueOf(listTopsisBorda.get(position).getJarak()));
        intent.putExtra("alamat", listTopsisBorda.get(position).getAlamat());
        intent.putExtra("harga", String.valueOf(listTopsisBorda.get(position).getHarga()));
        intent.putExtra("hari", listTopsisBorda.get(position).getDetharibuka());
        intent.putExtra("jumlah", listTopsisBorda.get(position).getDetjumlap());
        intent.putExtra("penonton", listTopsisBorda.get(position).getDetpenonton());
        intent.putExtra("det_fasilitas", listTopsisBorda.get(position).getDetfasil());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Kembali ke halaman awal?")
                .setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setCancelable(true)
                .create().show();
    }
}