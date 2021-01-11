package com.example.recyclerviewlesson.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListTopsis implements Parcelable {

    //listTopsis
    double harga;
    double jarak;
    double fasilitas;
    double ciPositif;

    //listSorted
    private String nama;
    private String alamat;
    private String foto;

    private int id;

    private String detharibuka;
    private String detpenonton;
    private String detjumlap;
    private String detfasil;

    public ListTopsis(double harga, double jarak, double fasilitas, double ciPositif, String nama, String alamat, String foto, int id, String detharibuka, String detpenonton, String detjumlap, String detfasil) {
        this.harga = harga;
        this.jarak = jarak;
        this.fasilitas = fasilitas;
        this.ciPositif = ciPositif;
        this.nama = nama;
        this.alamat = alamat;
        this.foto = foto;
        this.id = id;
        this.detharibuka = detharibuka;
        this.detpenonton = detpenonton;
        this.detjumlap = detjumlap;
        this.detfasil = detfasil;
    }

    public ListTopsis(double harga, double jarak, double fasilitas,  String nama, String alamat, String foto, int id, String detharibuka, String detpenonton, String detjumlap, String detfasil) {
        this.harga = harga;
        this.jarak = jarak;
        this.fasilitas = fasilitas;
        this.nama = nama;
        this.alamat = alamat;
        this.foto = foto;
        this.id = id;
        this.detharibuka = detharibuka;
        this.detpenonton = detpenonton;
        this.detjumlap = detjumlap;
        this.detfasil = detfasil;
    }

    public ListTopsis(double harga, double jarak, double fasilitas) {
        this.harga = harga;
        this.jarak = jarak;
        this.fasilitas = fasilitas;
    }

    protected ListTopsis(Parcel in) {
        harga = in.readDouble();
        jarak = in.readDouble();
        fasilitas = in.readDouble();
        ciPositif = in.readDouble();
        nama = in.readString();
        alamat = in.readString();
        foto = in.readString();
        id = in.readInt();
        detharibuka = in.readString();
        detpenonton = in.readString();
        detjumlap = in.readString();
        detfasil = in.readString();
    }

    public static final Creator<ListTopsis> CREATOR = new Creator<ListTopsis>() {
        @Override
        public ListTopsis createFromParcel(Parcel in) {
            return new ListTopsis(in);
        }

        @Override
        public ListTopsis[] newArray(int size) {
            return new ListTopsis[size];
        }
    };

    public double getHarga() {
        return harga;
    }

    public double getJarak() {
        return jarak;
    }

    public double getFasilitas() {
        return fasilitas;
    }

    public double getCiPositif() {
        return ciPositif;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getFoto() {
        return foto;
    }

    public int getId() {
        return id;
    }

    public String getDetharibuka() {
        return detharibuka;
    }

    public String getDetpenonton() {
        return detpenonton;
    }

    public String getDetjumlap() {
        return detjumlap;
    }

    public String getDetfasil() {
        return detfasil;
    }

    public void setHarga(double harga) {
        harga = harga;
    }

    public void setJarak(double jarak) {
        jarak = jarak;
    }

    public void setFasilitas(double fasilitas) {
        fasilitas = fasilitas;
    }

    public void setCiPositif(double ciPositif) {
        ciPositif = ciPositif;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDetharibuka(String detharibuka) {
        this.detharibuka = detharibuka;
    }

    public void setDetpenonton(String detpenonton) {
        this.detpenonton = detpenonton;
    }

    public void setDetjumlap(String detjumlap) {
        this.detjumlap = detjumlap;
    }

    public void setDetfasil(String detfasil) {
        this.detfasil = detfasil;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(harga);
        parcel.writeDouble(jarak);
        parcel.writeDouble(fasilitas);
        parcel.writeDouble(ciPositif);
        parcel.writeString(nama);
        parcel.writeString(alamat);
        parcel.writeString(foto);
        parcel.writeInt(id);
        parcel.writeString(detharibuka);
        parcel.writeString(detpenonton);
        parcel.writeString(detjumlap);
        parcel.writeString(detfasil);
    }
}