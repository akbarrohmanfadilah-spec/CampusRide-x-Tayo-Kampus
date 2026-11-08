package com.mycompany.campusride;

import java.time.LocalDate;

public class Pengumuman {
    private String judul;
    private String isi;
    private String tanggal;

    public Pengumuman(String judul, String isi) {
        this.judul = judul;
        this.isi = isi;
        this.tanggal = LocalDate.now().toString();
    }

    public String getJudul() { return judul; }
    public String getIsi() { return isi; }
    public String getTanggal() { return tanggal; }

    public boolean setJudul(String judul) {
        if (judul == null || judul.trim().isEmpty()) {
            System.out.println("Judul tidak boleh kosong!");
            return false;
        }
        this.judul = judul;
        return true;
    }

    public boolean setIsi(String isi) {
        if (isi == null || isi.trim().isEmpty()) {
            System.out.println("Isi pengumuman tidak boleh kosong!");
            return false;
        }
        this.isi = isi;
        this.tanggal = LocalDate.now().toString(); 
        return true;
    }

    public void tampilkan() {
        System.out.println("(" + tanggal + ") " + judul);
        System.out.println("   " + isi);
    }
}