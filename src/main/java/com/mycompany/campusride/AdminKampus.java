package com.mycompany.campusride;

public class AdminKampus extends User {

    public AdminKampus(String username, String password, String nama) {
        super(username, password, nama);
    }

    @Override
    public String getRole() { return User.ADMIN; }

    @Override
    public String getHakAkses() {
        return "Mengelola rute, halte, jadwal, pengumuman, akun pengguna, dan memantau data.";
    }

    @Override
    public void menu() {
        String pilih;
        do {
            System.out.println("\n=== MENU ADMIN: " + getNama() + " ===");
            System.out.println("-- Data Transportasi --");
            System.out.println(" 1. Lihat Rute");
            System.out.println(" 2. Tambah Rute");
            System.out.println(" 3. Ubah Nama Rute");
            System.out.println(" 4. Hapus Rute");
            System.out.println(" 5. Tambah Halte ke Rute");
            System.out.println(" 6. Ubah Jadwal");
            System.out.println("-- Pengumuman --");
            System.out.println(" 7. Buat Pengumuman");
            System.out.println(" 8. Ubah Pengumuman");
            System.out.println("-- Akun & Monitoring --");
            System.out.println(" 9. Lihat Pengguna");
            System.out.println("10. Tambah Akun");
            System.out.println("11. Hapus Akun");
            System.out.println("12. Monitoring Data Transportasi");
            System.out.println(" 0. Logout");
            pilih = Main.baca("Pilih: ");

            switch (pilih) {
                case "1":
                    for (int i = 0; i < Main.jumlahRute; i++) {
                        Main.daftarRute[i].tampilkanInfo();
                    }
                    break;
                case "2": inputRuteBaru(); break;
                case "3": ubahNamaRute(); break;
                case "4": hapusRute(); break;
                case "5": tambahHalte(); break;
                case "6": ubahJadwal(); break;
                case "7": buatPengumuman(); break;
                case "8": ubahPengumuman(); break;
                case "9":
                    for (int i = 0; i < Main.jumlahUser; i++) {
                        Main.daftarUser[i].tampilkan();
                    }
                    break;
                case "10": tambahAkun(); break;
                case "11": hapusAkun(); break;
                case "12": monitoring(); break;
                case "0": System.out.println("Logout..."); break;
                default: System.out.println("Pilihan tidak tersedia.");
            }
        } while (!pilih.equals("0"));
    }

    private void inputRuteBaru() {
        if (Main.jumlahRute >= Main.MAX) {
            System.out.println("Kapasitas rute penuh.");
            return;
        }
        String kode = Main.baca("Kode rute: ").toUpperCase();
        if (kode.isEmpty()) { System.out.println("Kode tidak boleh kosong."); return; }
        if (Main.cariRuteByKode(kode) != -1) {
            System.out.println("Kode rute sudah dipakai.");
            return;
        }
        String nama = Main.baca("Nama rute: ");
        String berangkat = Main.baca("Jam berangkat (07.00): ");
        String mulai = Main.baca("Operasional mulai (07.00): ");
        String selesai = Main.baca("Operasional selesai (17.00): ");

        if (nama.isEmpty()) {
            System.out.println("Nama rute tidak boleh kosong.");
        } else if (!Rute.jamValid(berangkat) || !Rute.jamValid(mulai) || !Rute.jamValid(selesai)) {
            System.out.println("Format jam salah (contoh: 07.30).");
        } else {
            Main.daftarRute[Main.jumlahRute++] =
                    new Rute(kode, nama, berangkat, mulai, selesai);
            System.out.println("Rute ditambahkan. Tambahkan halte lewat menu 5.");
        }
    }

    private void ubahNamaRute() {
        int i = Main.cariRuteByKode(Main.baca("Kode rute: "));
        if (i == -1) { System.out.println("Rute tidak ditemukan."); return; }
        if (Main.daftarRute[i].setNama(Main.baca("Nama baru: "))) {
            System.out.println("Nama rute diperbarui.");
        }
    }

    private void hapusRute() {
        int i = Main.cariRuteByKode(Main.baca("Kode rute: "));
        if (i == -1) { System.out.println("Rute tidak ditemukan."); return; }
        for (int j = i; j < Main.jumlahRute - 1; j++) {
            Main.daftarRute[j] = Main.daftarRute[j + 1];
        }
        Main.daftarRute[Main.jumlahRute - 1] = null;
        Main.jumlahRute--;
        System.out.println("Rute dihapus.");
    }

    private void tambahHalte() {
        int i = Main.cariRuteByKode(Main.baca("Kode rute: "));
        if (i == -1) { System.out.println("Rute tidak ditemukan."); return; }
        String nama = Main.baca("Nama halte: ");
        String ket = Main.baca("Keterangan lokasi (boleh kosong): ");
        if (nama.isEmpty()) { System.out.println("Nama halte tidak boleh kosong."); return; }

        Rute r = Main.daftarRute[i];
        boolean berhasil = ket.isEmpty() ? r.tambahHalte(nama) : r.tambahHalte(nama, ket);
        System.out.println(berhasil ? "Halte ditambahkan." : "Kapasitas halte rute ini penuh.");
    }

    private void ubahJadwal() {
        int i = Main.cariRuteByKode(Main.baca("Kode rute: "));
        if (i == -1) { System.out.println("Rute tidak ditemukan."); return; }
        String berangkat = Main.baca("Jam berangkat baru: ");
        String mulai = Main.baca("Operasional mulai baru: ");
        String selesai = Main.baca("Operasional selesai baru: ");

        if (!Rute.jamValid(berangkat) || !Rute.jamValid(mulai) || !Rute.jamValid(selesai)) {
            System.out.println("Format jam salah (contoh: 07.30).");
            return;
        }
        Rute r = Main.daftarRute[i];
        r.setJamBerangkat(berangkat);
        r.setJamMulai(mulai);
        r.setJamSelesai(selesai);
        System.out.println("Jadwal diperbarui.");
    }

    private void buatPengumuman() {
        if (Main.jumlahPengumuman >= Main.MAX) {
            System.out.println("Kapasitas pengumuman penuh.");
            return;
        }
        String judul = Main.baca("Judul: ");
        String isi = Main.baca("Isi: ");
        if (judul.isEmpty() || isi.isEmpty()) {
            System.out.println("Judul dan isi wajib diisi.");
        } else {
            Main.daftarPengumuman[Main.jumlahPengumuman++] =
                    new Pengumuman(judul, isi);
            System.out.println("Pengumuman dibuat.");
        }
    }

    private void ubahPengumuman() {
        Main.tampilPengumuman();
        if (Main.jumlahPengumuman == 0) return;
        String no = Main.baca("Nomor pengumuman: ");
        int n;
        try {
            n = Integer.parseInt(no);
        } catch (NumberFormatException e) {
            System.out.println("Nomor harus angka.");
            return;
        }
        if (n < 1 || n > Main.jumlahPengumuman) {
            System.out.println("Nomor tidak valid.");
            return;
        }
        if (Main.daftarPengumuman[n - 1].setIsi(Main.baca("Isi baru: "))) {
            System.out.println("Pengumuman diperbarui.");
        }
    }

    private void tambahAkun() {
        if (Main.jumlahUser >= Main.MAX) {
            System.out.println("Kapasitas user penuh.");
            return;
        }
        System.out.println("Hak akses: 1. Mahasiswa  2. Admin");
        String jenis = Main.baca("Pilih: ");
        String nama = Main.baca("Nama: ");
        String u = Main.baca("Username: ");
        String p = Main.baca("Password: ");
        if (nama.isEmpty() || u.isEmpty() || p.isEmpty()) {
            System.out.println("Semua data wajib diisi.");
        } else if (Main.cariUser(u) != -1) {
            System.out.println("Username sudah dipakai.");
        } else if (jenis.equals("2")) {
            Main.daftarUser[Main.jumlahUser++] = new AdminKampus(u, p, nama);
            System.out.println("Akun admin dibuat.");
        } else {
            String npm = Main.baca("NPM (boleh kosong): ");
            Main.daftarUser[Main.jumlahUser++] = new Mahasiswa(u, p, nama, npm);
            System.out.println("Akun mahasiswa dibuat.");
        }
    }

    private void hapusAkun() {
        String uname = Main.baca("Username yang dihapus: ");
        int i = Main.cariUser(uname);
        if (i == -1) { System.out.println("User tidak ditemukan."); return; }
        if (uname.equals(getUsername())) { // akun sendiri tidak boleh dihapus
            System.out.println("Anda tidak bisa menghapus akun yang sedang dipakai.");
            return;
        }
        for (int j = i; j < Main.jumlahUser - 1; j++) {
            Main.daftarUser[j] = Main.daftarUser[j + 1];
        }
        Main.daftarUser[Main.jumlahUser - 1] = null;
        Main.jumlahUser--;
        System.out.println("Akun dihapus.");
    }

    private void monitoring() {
        System.out.println("===== RUTE & JADWAL (" + Main.jumlahRute + " rute aktif, "
                + Rute.getTotalRuteDibuat() + " rute pernah dibuat) =====");
        for (int i = 0; i < Main.jumlahRute; i++) {
            Main.daftarRute[i].tampilkanInfo();
        }
        System.out.println("===== PENGUMUMAN (" + Main.jumlahPengumuman + ") =====");
        Main.tampilPengumuman();
    }
}