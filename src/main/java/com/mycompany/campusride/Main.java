package com.mycompany.campusride;

import java.util.Scanner;

/**
 * MAIN CLASS: titik awal program CampusRide x Tayo Kampus.
 * Berisi data (array objek), method bantu, dan method main.
 */
public class Main {

    static final int MAX = 20;
    static Scanner sc = new Scanner(System.in);

    static Rute[] daftarRute = new Rute[MAX];
    static int jumlahRute = 0;

    static Pengumuman[] daftarPengumuman = new Pengumuman[MAX];
    static int jumlahPengumuman = 0;

    // Array bertipe superclass User, isinya objek subclass
    static User[] daftarUser = new User[MAX];
    static int jumlahUser = 0;

    // ---------------------------------------------------------------
    public static void main(String[] args) {
        isiDataAwal();
        boolean jalan = true;

        System.out.println("==============================");
        System.out.println("  CampusRide x Tayo Kampus");
        System.out.println("==============================");

        while (jalan) {
            System.out.println("\n1. Login");
            System.out.println("2. Daftar (Mahasiswa)");
            System.out.println("0. Keluar");
            String pilih = baca("Pilih: ");

            switch (pilih) {
                case "1": login(); break;
                case "2": daftar(); break;
                case "0": jalan = false; break;
                default: System.out.println("Pilihan tidak tersedia.");
            }
        }
        System.out.println("Terima kasih telah menggunakan CampusRide!");
    }

    // Data contoh -> ganti dengan data asli Tayo Kampus
    static void isiDataAwal() {
        // Object instantiation dengan keyword 'new'
        tambahUser(new AdminKampus("admin", "admin123", "Admin Kampus"));

        Rute a = new Rute("A", "Gerbang Utama - Perpustakaan", "07.00", "07.00", "17.00");
        a.tambahHalte("Gerbang Utama", "Pintu masuk kampus");
        a.tambahHalte("Rektorat", "Dekat gedung rektorat");
        a.tambahHalte("Fakultas Teknik", "Depan gedung Fakultas Teknik");
        a.tambahHalte("Perpustakaan", "Depan perpustakaan pusat");

        Rute b = new Rute("B", "Asrama - Fakultas Ekonomi", "07.30", "07.30", "16.30");
        b.tambahHalte("Asrama", "Area asrama mahasiswa");
        b.tambahHalte("Fakultas MIPA", "Samping gedung MIPA");
        b.tambahHalte("Fakultas Ekonomi");

        tambahRute(a);
        tambahRute(b);

        tambahPengumuman(new Pengumuman("Selamat datang",
                "Cek jadwal Tayo Kampus sebelum berangkat."));
    }

    // ===== Login & Daftar =====
    static void login() {
        String u = baca("Username: ");
        String p = baca("Password: ");
        int idx = cariUser(u);

        if (idx == -1) {
            System.out.println("Username tidak ditemukan.");
        } else if (!daftarUser[idx].cekPassword(p)) {
            System.out.println("Password salah.");
        } else {
            User user = daftarUser[idx];
            System.out.println("Login berhasil sebagai " + user.getRole());
            System.out.println("Hak akses: " + user.getHakAkses());
            user.menu(); // menjalankan menu() milik subclass yang sesuai
        }
    }

    static void daftar() {
        if (jumlahUser >= MAX) {
            System.out.println("Kapasitas user penuh.");
            return;
        }
        String nama = baca("Nama lengkap: ");
        String npm = baca("NPM (boleh kosong): ");
        String u = baca("Username: ");
        String p = baca("Password: ");
        if (nama.isEmpty() || u.isEmpty() || p.isEmpty()) {
            System.out.println("Nama, username, dan password wajib diisi.");
        } else if (cariUser(u) != -1) {
            System.out.println("Username sudah dipakai.");
        } else {
            tambahUser(new Mahasiswa(u, p, nama, npm));
            System.out.println("Pendaftaran berhasil, silakan login.");
        }
    }

    // ===== Helper (dipakai juga oleh Mahasiswa dan AdminKampus) =====
    static String baca(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static void tambahUser(User u) { daftarUser[jumlahUser++] = u; }
    static void tambahRute(Rute r) { daftarRute[jumlahRute++] = r; }
    static void tambahPengumuman(Pengumuman p) { daftarPengumuman[jumlahPengumuman++] = p; }

    static int cariRuteByKode(String kode) {
        for (int i = 0; i < jumlahRute; i++) {
            if (daftarRute[i].getKode().equalsIgnoreCase(kode)) return i;
        }
        return -1;
    }

    static int cariUser(String uname) {
        for (int i = 0; i < jumlahUser; i++) {
            if (daftarUser[i].getUsername().equals(uname)) return i;
        }
        return -1;
    }

    // ===== Overloading cariRute =====
    static void cariRute(String dari) {
        boolean ketemu = false;
        for (int i = 0; i < jumlahRute; i++) {
            if (daftarRute[i].punyaHalte(dari)) {
                ketemu = true;
                tampilHasilRute(daftarRute[i], dari);
            }
        }
        if (!ketemu) System.out.println("Rute tidak ditemukan.");
    }

    static void cariRute(String dari, String ke) {
        boolean ketemu = false;
        for (int i = 0; i < jumlahRute; i++) {
            if (daftarRute[i].punyaHalte(dari) && daftarRute[i].punyaHalte(ke)) {
                ketemu = true;
                tampilHasilRute(daftarRute[i], dari);
            }
        }
        if (!ketemu) System.out.println("Rute tidak ditemukan.");
    }

    static void tampilHasilRute(Rute r, String dari) {
        r.tampilkanInfo();
        System.out.println("   Halte terdekat dari asal: " + r.getNamaHalte(r.cariHalte(dari)));
    }

    static void tampilJadwal() {
        System.out.printf("%-6s %-30s %-11s %-15s%n", "Rute", "Nama", "Berangkat", "Operasional");
        for (int i = 0; i < jumlahRute; i++) {
            Rute r = daftarRute[i];
            System.out.printf("%-6s %-30s %-11s %-15s%n",
                    r.getKode(), r.getNama(), r.getJamBerangkat(), r.jamOperasional());
        }
    }

    static void tampilJalur() {
        for (int i = 0; i < jumlahRute; i++) {
            System.out.println("Rute " + daftarRute[i].getKode() + ": " + daftarRute[i].jalur());
        }
    }

    static void tampilPengumuman() {
        if (jumlahPengumuman == 0) System.out.println("Belum ada pengumuman.");
        for (int i = 0; i < jumlahPengumuman; i++) {
            System.out.print((i + 1) + ". ");
            daftarPengumuman[i].tampilkan();
        }
    }
}