package com.example.recyclerviewlesson.model;

public class ListItemModel {

    //Nama variabel harus sama dengan yang di firebase
    private String nama;
    private String alamat;
    private String foto;

    private String detharibuka;
    private String detpenonton;
    private String detjumlap;
    private String detfasil;

    private int id;
    private int harga;
    private int fasilitas;

    private double lat;
    private double lng;

    public ListItemModel() {

    }

    public ListItemModel(String nama, String alamat, String foto, String detharibuka, String detpenonton, String detjumlap, String detfasil, int id, int harga, int fasilitas, double lat, double lng) {
        this.nama = nama;
        this.alamat = alamat;
        this.foto = foto;
        this.detharibuka = detharibuka;
        this.detpenonton = detpenonton;
        this.detjumlap = detjumlap;
        this.detfasil = detfasil;
        this.id = id;
        this.harga = harga;
        this.fasilitas = fasilitas;
        this.lat = lat;
        this.lng = lng;
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

    public int getId() {
        return id;
    }

    public int getHarga() {
        return harga;
    }

    public int getFasilitas() {
        return fasilitas;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}