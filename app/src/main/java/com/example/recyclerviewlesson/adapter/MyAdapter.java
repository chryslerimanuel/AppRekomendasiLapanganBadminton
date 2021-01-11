package com.example.recyclerviewlesson.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recyclerviewlesson.R;
import com.example.recyclerviewlesson.model.ListItemModel;
import com.example.recyclerviewlesson.model.ListTopsis;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListTopsis> listItems;

    ItemClickListener mItemClickListener;

    public MyAdapter(Context context, ArrayList<ListTopsis> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tv_namaLap.setText(listItems.get(position).getNama());
        holder.tv_alamatLap.setText(listItems.get(position).getAlamat());
        Picasso.get().load(listItems.get(position).getFoto()).into(holder.iv_fotoLap);
        double aa = listItems.get(position).getHarga();
        String bb = Double.toString(aa);
        holder.tv_hargaLap.setText("Rp "+ bb);
        holder.tv_jarakLap.setText(String.valueOf(listItems.get(position).getJarak()) + " km");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(position);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return 3;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_namaLap, tv_alamatLap, tv_hargaLap, tv_jarakLap;
        ImageView iv_fotoLap;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_namaLap = itemView.findViewById(R.id.tv_namaLap);
            tv_alamatLap = itemView.findViewById(R.id.tv_alamatLap);
            iv_fotoLap = itemView.findViewById(R.id.iv_fotoLap);
            tv_hargaLap = itemView.findViewById(R.id.tv_hargaLap);
            tv_jarakLap = itemView.findViewById(R.id.tv_jarakLap);

        }
    }

    public void addItemClickListener(ItemClickListener listener) {
        mItemClickListener = listener;
    }

    //Define your Interface method here
    public interface ItemClickListener {
        void onItemClick(int position);
    }
}