package com.bsn.nurelfal.Models;


import java.util.HashMap;
import java.util.Map;

import static com.bsn.nurelfal.Variable.URL_PHOTO;

public class Nasabah {

    String id_nasabah, norek, noatm,  nama, alamat, saldo, foto ;

    public Nasabah(){}

    public Nasabah(String id_nasabah, String norek, String noatm, String nama, String saldo, String foto){
        this.id_nasabah = id_nasabah;
        this.norek = norek;
        this.noatm = noatm;
        this.nama = nama;
        this.saldo = saldo;
        this.foto = foto;
    }

    public String getId_nasabah() {
        return id_nasabah;
    }

    public void setIdNasabah(String id_nasabah) {
        this.id_nasabah = id_nasabah;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    public String getNoatm() {
        return noatm;
    }

    public void setNoatm(String noatm) {
        this.noatm = noatm;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }


    public String getFoto() {
        return URL_PHOTO+foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


}
