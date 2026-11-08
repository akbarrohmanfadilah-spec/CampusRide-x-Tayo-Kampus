package com.mycompany.campusride;

public class Mahasiswa extends User {
    private String npm;

    public Mahasiswa(String username, String password, String nama, String npm) {
        super(username, password, nama);
        this.npm = npm;
    }

    public String getNpm() { return npm; }

    @Override
    public String getRole() { return User.MAHASISWA; }

    @Override
    public String getHakAkses() {
        return "Melihat rute, jadwal, halte, dan pengumuman.";
    }

    @Override
    public void tampilkan() {
        super.tampilkan();
        System.out.println("   NPM: " + (npm.isEmpty() ? "-" : npm));
    }

    @Override
    public void menu() {
        String pilih;
        do {
            System.out.println("\n=== MENU MAHASISWA: " + getNama() + " ===");
            System.out.println("1. Pencarian Rute");
            System.out.println("2. Informasi Jadwal");
            System.out.println("3. Informasi Halte");
            System.out.println("4. Informasi Rute Transportasi");
            System.out.println("5. Pengumuman");
            System.out.println("0. Logout");
            pilih = Main.baca("Pilih: ");

            switch (pilih) {
                case "1":
                    String dari = Main.baca("Lokasi asal: ");
                    String ke = Main.baca("Lokasi tujuan (kosongkan jika bebas): ");
                    if (ke.isEmpty()) Main.cariRute(dari);
                    else Main.cariRute(dari, ke);
                    break;
                case "2": Main.tampilJadwal(); break;
                case "3":
                    for (int i = 0; i < Main.jumlahRute; i++) {
                        Main.daftarRute[i].tampilkanHalte();
                    }
                    break;
                case "4": Main.tampilJalur(); break;
                case "5": Main.tampilPengumuman(); break;
                case "0": System.out.println("Logout..."); break;
                default: System.out.println("Pilihan tidak tersedia.");
            }
        } while (!pilih.equals("0"));
    }
}