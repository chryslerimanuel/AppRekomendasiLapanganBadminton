package com.example.recyclerviewlesson;

import android.util.Log;

import com.example.recyclerviewlesson.model.ListItemModel;
import com.example.recyclerviewlesson.model.ListTopsis;

import java.util.ArrayList;
import java.util.Collections;

public class HitungTopsis {

    public HitungTopsis() {

    }


    public Double PembagiHarga(ArrayList<ListTopsis> list, double pembagiHarga) {

        for (int i = 0; i < list.size(); i++) {
            pembagiHarga = pembagiHarga + (Math.pow(list.get(i).getHarga(), 2));
            Log.i("TOT_PEMBAGI_H", String.valueOf(list.get(i).getHarga()));
        }

        return pembagiHarga;
    }

    public Double PembagiJarak(ArrayList<ListTopsis> list, double pembagiJarak) {

        for (int i = 0; i < list.size(); i++) {
            pembagiJarak = pembagiJarak + (Math.pow(list.get(i).getJarak(), 2));
        }

        return pembagiJarak;
    }


    public Double PembagiFasilitas(ArrayList<ListTopsis> list, double pembagiFasilitas) {


        for (int i = 0; i < list.size(); i++) {
            pembagiFasilitas = pembagiFasilitas + (Math.pow(list.get(i).getFasilitas(), 2));
            Log.i("TOT_PEMBAGI_F", String.valueOf(list.get(i).getFasilitas()));
        }

        return pembagiFasilitas;
    }

    public ArrayList<Double> NormalisasiHarga(ArrayList<ListTopsis> list,
                                              double pembagiHarga, ArrayList<Double> normalisasiHarga) {

        for (int i = 0; i < list.size(); i++) {
            normalisasiHarga.add(list.get(i).getHarga() / pembagiHarga);
            Log.i("TOPSIS HARGA",String.valueOf(list.get(i).getHarga()));
        }

        return normalisasiHarga;
    }

    public ArrayList<Double> NormalisasiJarak(ArrayList<ListTopsis> list,
                                              double pembagiJarak, ArrayList<Double> normalisasiJarak) {

        for (int i = 0; i < list.size(); i++) {
           normalisasiJarak.add(list.get(i).getJarak() / pembagiJarak);
        }

        return normalisasiJarak;
    }

    public ArrayList<Double> NormalisasiFasilitas(ArrayList<ListTopsis> list,
                                               double pembagiFasilitas, ArrayList<Double> normalisasiFasilitas) {

        for (int i = 0; i < list.size(); i++) {
            normalisasiFasilitas.add(list.get(i).getFasilitas() / pembagiFasilitas);
        }

        return normalisasiFasilitas;
    }

    public ArrayList<Double> TerbobotHarga(ArrayList<Double> normalisasiHarga,
                                           double bobotHarga,
                                           ArrayList<Double> terbobotHarga) {

        for (int i = 0; i < normalisasiHarga.size(); i++) {
            terbobotHarga.add(normalisasiHarga.get(i) * bobotHarga);
        }

        return terbobotHarga;
    }

    public ArrayList<Double> TerbobotJarak(ArrayList<Double> normalisasiJarak,
                                           double bobotJarak,
                                           ArrayList<Double> terbobotJarak) {

        for (int i = 0; i < normalisasiJarak.size(); i++) {
            terbobotJarak.add(normalisasiJarak.get(i) * bobotJarak);
        }

        return terbobotJarak;
    }

    public ArrayList<Double> TerbobotFasilitas(ArrayList<Double> normalisasiFasilitas,
                                            double bobotFasilitas,
                                            ArrayList<Double> terbobotFasilitas) {

        for (int i = 0; i < normalisasiFasilitas.size(); i++) {
            terbobotFasilitas.add(normalisasiFasilitas.get(i) * bobotFasilitas);
        }

        return terbobotFasilitas;
    }


    public ArrayList<Double> siPositif
            (ArrayList<Double> listHarga, ArrayList<Double> listJarak, ArrayList<Double> listFasilitas,
             double aPositifHarga, double aPositifJarak, double aPositifFasilitas, ArrayList<Double> siPositif) {

        for (int i = 0; i < listHarga.size(); i++) {
            siPositif.add(Math.sqrt((Math.pow((listHarga.get(i) - aPositifHarga), 2))
                    + (Math.pow((listJarak.get(i) - aPositifJarak), 2))
                    + (Math.pow((listFasilitas.get(i) - aPositifFasilitas), 2))));
        }

        return siPositif;
    }

    public ArrayList<Double> siNegatif
            (ArrayList<Double> listHarga, ArrayList<Double> listJarak, ArrayList<Double> listFasilitas,
             double aNegatifHarga, double aNegatifJarak, double aNegatifFasilitas, ArrayList<Double> siNegatif) {

        for (int i = 0; i < listHarga.size(); i++) {
            siNegatif.add(Math.sqrt((Math.pow((listHarga.get(i) - aNegatifHarga), 2))
                    + (Math.pow((listJarak.get(i) - aNegatifJarak), 2))
                    + (Math.pow((listFasilitas.get(i) - aNegatifFasilitas), 2))));
        }

        return siNegatif;
    }

    public ArrayList<Double> jumlahSi
            (ArrayList<Double> listSiPositif, ArrayList<Double> listSiNegatif, ArrayList<Double> jumlahSi) {

        for (int i = 0; i < listSiPositif.size(); i++) {
            jumlahSi.add(listSiPositif.get(i) + listSiNegatif.get(i));
        }
        return jumlahSi;
    }

    public ArrayList<Double> ciPositif
            (ArrayList<Double> listSiNegatif, ArrayList<Double> jumlahSi, ArrayList<Double> ciPositif) {

        for (int i = 0; i < listSiNegatif.size(); i++) {
            ciPositif.add(listSiNegatif.get(i) / jumlahSi.get(i));
        }
        return ciPositif;
    }

}
