package com.example.recyclerviewlesson.adapter;

import android.content.ClipData;
import android.content.Context;
import android.preference.TwoStatePreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recyclerviewlesson.R;
import com.example.recyclerviewlesson.model.ListTopsis;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LapAdapter extends RecyclerView.Adapter<LapAdapter.LapViewHolder> {

    Context context;
    ArrayList<ListTopsis> listLapangan;

    ItemClickListener aItemClickListener;

    public LapAdapter(Context context, ArrayList<ListTopsis> listLapangan) {
        this.context = context;
        this.listLapangan = listLapangan;
    }

    @NonNull
    @Override
    public LapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LapViewHolder(LayoutInflater.from(context).inflate(R.layout.list_lapangan, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LapViewHolder holder, final int position) {
        holder.tv_namaLap_lap.setText(listLapangan.get(position).getNama());
        holder.tv_alamatLap_lap.setText(listLapangan.get(position).getAlamat());
        Picasso.get().load(listLapangan.get(position).getFoto()).into(holder.iv_fotoLap_lap);
        double lap = listLapangan.get(position).getHarga();
        String aLap = Double.toString(lap);
        holder.tv_hargaLap_lap.setText("Rp "+ aLap);
        holder.tv_jarakLap_lap.setText(String.valueOf(listLapangan.get(position).getJarak()) + " km");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aItemClickListener != null) {
                    aItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listLapangan.size();
    }


    class LapViewHolder extends RecyclerView.ViewHolder{

        TextView tv_namaLap_lap, tv_alamatLap_lap, tv_hargaLap_lap, tv_jarakLap_lap;
        ImageView iv_fotoLap_lap;

        public LapViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_namaLap_lap = itemView.findViewById(R.id.tv_namaLap_lap);
            tv_alamatLap_lap = itemView.findViewById(R.id.tv_alamatLap_lap);
            tv_hargaLap_lap = itemView.findViewById(R.id.tv_hargaLap_lap);
            tv_jarakLap_lap = itemView.findViewById(R.id.tv_jarakLap_lap);
            iv_fotoLap_lap = itemView.findViewById(R.id.iv_fotoLap_lap);

        }
    }

    public void addItemClickListener(ItemClickListener listener) {
        aItemClickListener = listener;
    }

    //Define your Interface method here
    public interface ItemClickListener {
        void onItemClick(int position);
    }

}
