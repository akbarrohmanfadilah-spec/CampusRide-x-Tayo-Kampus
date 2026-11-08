package com.mycompany.campusride;

public class Rute {
    // Static: dimiliki class, dipakai bersama oleh semua objek Rute
    private static int totalRuteDibuat = 0;

    // Data rute dan jadwal
    private String kode;
    private String nama;
    private String jamBerangkat;
    private String jamMulai;
    private String jamSelesai;

    // Data halte disimpan langsung di Rute (array paralel: nama dan keterangan)
    private String[] namaHalte = new String[10];
    private String[] keteranganHalte = new String[10];
    private int jumlahHalte = 0;

    public Rute(String kode, String nama, String jamBerangkat, String jamMulai, String jamSelesai) {
        this.kode = kode;
        this.nama = nama;
        this.jamBerangkat = jamBerangkat;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        totalRuteDibuat++;
    }

    // ===== Static method =====
    public static int getTotalRuteDibuat() { return totalRuteDibuat; }

    public static boolean jamValid(String jam) {
        return jam != null && jam.matches("([01]\\d|2[0-3])\\.[0-5]\\d");
    }

    // ===== Getter =====
    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public String getJamBerangkat() { return jamBerangkat; }
    public String getJamMulai() { return jamMulai; }
    public String getJamSelesai() { return jamSelesai; }
    public int getJumlahHalte() { return jumlahHalte; }
    public String getNamaHalte(int posisi) { return namaHalte[posisi]; }
    public String getKeteranganHalte(int posisi) { return keteranganHalte[posisi]; }

    // ===== Setter + validasi =====
    public boolean setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            System.out.println("Nama rute tidak boleh kosong!");
            return false;
        }
        this.nama = nama;
        return true;
    }

    public boolean setJamBerangkat(String jam) {
        if (!jamValid(jam)) {
            System.out.println("Format jam berangkat salah (contoh: 07.30)!");
            return false;
        }
        this.jamBerangkat = jam;
        return true;
    }

    public boolean setJamMulai(String jam) {
        if (!jamValid(jam)) {
            System.out.println("Format jam mulai salah (contoh: 07.30)!");
            return false;
        }
        this.jamMulai = jam;
        return true;
    }

    public boolean setJamSelesai(String jam) {
        if (!jamValid(jam)) {
            System.out.println("Format jam selesai salah (contoh: 17.00)!");
            return false;
        }
        this.jamSelesai = jam;
        return true;
    }

    // ===== Method Overloading: tambahHalte =====
    public boolean tambahHalte(String nama, String keterangan) {
        if (jumlahHalte >= namaHalte.length) {
            return false; // kapasitas penuh
        }
        namaHalte[jumlahHalte] = nama;
        keteranganHalte[jumlahHalte] = keterangan;
        jumlahHalte++;
        return true;
    }

    // Versi tanpa keterangan
    public boolean tambahHalte(String nama) {
        return tambahHalte(nama, "-");
    }

    // ===== Method lain =====
    // Posisi halte yang cocok dengan kata kunci, -1 jika tidak ada
    public int cariHalte(String kataKunci) {
        String k = kataKunci.toLowerCase();
        for (int i = 0; i < jumlahHalte; i++) {
            if (namaHalte[i].toLowerCase().contains(k) || keteranganHalte[i].toLowerCase().contains(k)) {
                return i;
            }
        }
        return -1;
    }

    public boolean punyaHalte(String kataKunci) {
        return cariHalte(kataKunci) != -1;
    }

    public String jamOperasional() {
        return jamMulai + "-" + jamSelesai;
    }

    public String jalur() {
        if (jumlahHalte == 0) return "(belum ada halte)";
        String hasil = "";
        for (int i = 0; i < jumlahHalte; i++) {
            hasil += namaHalte[i];
            if (i < jumlahHalte - 1) hasil += " -> ";
        }
        return hasil;
    }

    public void tampilkanInfo() {
        System.out.println("[" + kode + "] " + nama);
        System.out.println("   Berangkat : " + jamBerangkat + " | Operasional: " + jamOperasional());
        System.out.println("   Jalur     : " + jalur());
    }

    public void tampilkanHalte() {
        System.out.println("Halte rute " + kode + " (" + nama + "):");
        if (jumlahHalte == 0) System.out.println("   (belum ada halte)");
        for (int i = 0; i < jumlahHalte; i++) {
            System.out.println("   " + (i + 1) + ". " + namaHalte[i] + " - " + keteranganHalte[i]);
        }
    }
}