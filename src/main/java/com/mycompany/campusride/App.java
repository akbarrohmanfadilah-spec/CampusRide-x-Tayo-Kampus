package com.mycompany.campusride;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private Stage stage;
    private User userAktif;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        Main.isiDataAwal();
        tampilLogin();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void alert(Alert.AlertType tipe, String judul, String pesan) {
        Alert a = new Alert(tipe);
        a.setTitle(judul);
        a.setHeaderText(null);
        a.setContentText(pesan);
        a.showAndWait();
    }

    private Label judulLabel(String teks) {
        Label l = new Label(teks);
        l.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        return l;
    }

    private TextArea buatTextArea(String isi, int baris) {
        TextArea ta = new TextArea(isi);
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.setPrefRowCount(baris);
        return ta;
    }

    private void tampilLogin() {
        Label judul = judulLabel("CampusRide x Tayo Kampus");

        TextField tfUser = new TextField();
        tfUser.setPromptText("Username");
        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("Password");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);
        form.add(new Label("Username"), 0, 0);
        form.add(tfUser, 1, 0);
        form.add(new Label("Password"), 0, 1);
        form.add(pfPass, 1, 1);

        Button btnLogin = new Button("Login");
        Button btnDaftar = new Button("Daftar");
        HBox tombol = new HBox(10, btnLogin, btnDaftar);
        tombol.setAlignment(Pos.CENTER);

        btnLogin.setOnAction(e -> prosesLogin(tfUser.getText().trim(), pfPass.getText()));
        btnDaftar.setOnAction(e -> tampilDaftar());

        VBox root = new VBox(15, judul, form, tombol);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));

        stage.setTitle("CampusRide - Login");
        stage.setScene(new Scene(root, 450, 320));
    }

    private void prosesLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Data belum lengkap", "Username dan password wajib diisi.");
            return;
        }
        int idx = Main.cariUser(username);
        if (idx == -1) {
            alert(Alert.AlertType.ERROR, "Login gagal", "Username tidak ditemukan.");
        } else if (!Main.daftarUser[idx].cekPassword(password)) {
            alert(Alert.AlertType.ERROR, "Login gagal", "Password salah.");
        } else {
            userAktif = Main.daftarUser[idx];
            if (userAktif.getRole().equals(User.ADMIN)) tampilAdmin();
            else tampilMahasiswa();
        }
    }

    private void tampilDaftar() {
        Label judul = judulLabel("Daftar Mahasiswa");

        TextField tfNama = new TextField();
        TextField tfNpm = new TextField();
        TextField tfUser = new TextField();
        PasswordField pfPass = new PasswordField();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);
        form.add(new Label("Nama lengkap"), 0, 0);
        form.add(tfNama, 1, 0);
        form.add(new Label("NPM (boleh kosong)"), 0, 1);
        form.add(tfNpm, 1, 1);
        form.add(new Label("Username"), 0, 2);
        form.add(tfUser, 1, 2);
        form.add(new Label("Password"), 0, 3);
        form.add(pfPass, 1, 3);

        Button btnDaftar = new Button("Daftar");
        Button btnKembali = new Button("Kembali");
        HBox tombol = new HBox(10, btnDaftar, btnKembali);
        tombol.setAlignment(Pos.CENTER);

        btnDaftar.setOnAction(e -> prosesDaftar(tfNama.getText().trim(), tfNpm.getText().trim(),
                tfUser.getText().trim(), pfPass.getText()));
        btnKembali.setOnAction(e -> tampilLogin());

        VBox root = new VBox(15, judul, form, tombol);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(25));

        stage.setTitle("CampusRide - Daftar");
        stage.setScene(new Scene(root, 450, 400));
    }

    private void prosesDaftar(String nama, String npm, String username, String password) {
        if (nama.isEmpty() || username.isEmpty() || password.isEmpty()) {
            alert(Alert.AlertType.WARNING, "Data belum lengkap", "Nama, username, dan password wajib diisi.");
            return;
        }
        if (Main.jumlahUser >= Main.MAX) {
            alert(Alert.AlertType.ERROR, "Gagal", "Kapasitas user penuh.");
            return;
        }
        if (Main.cariUser(username) != -1) {
            alert(Alert.AlertType.ERROR, "Gagal", "Username sudah dipakai.");
            return;
        }
        Main.tambahUser(new Mahasiswa(username, password, nama, npm));
        alert(Alert.AlertType.INFORMATION, "Berhasil", "Pendaftaran berhasil, silakan login.");
        tampilLogin();
    }

    private void tampilMahasiswa() {
        Label judul = judulLabel("Halo, " + userAktif.getNama());

        TextField tfAsal = new TextField();
        tfAsal.setPromptText("Contoh: Gerbang");
        TextField tfTujuan = new TextField();
        tfTujuan.setPromptText("Boleh kosong");
        Button btnCari = new Button("Cari Rute");

        GridPane formCari = new GridPane();
        formCari.setHgap(10);
        formCari.setVgap(8);
        formCari.add(new Label("Lokasi asal"), 0, 0);
        formCari.add(tfAsal, 1, 0);
        formCari.add(new Label("Lokasi tujuan"), 0, 1);
        formCari.add(tfTujuan, 1, 1);
        formCari.add(btnCari, 2, 1);

        Button btnJadwal = new Button("Jadwal");
        Button btnHalte = new Button("Halte");
        Button btnJalur = new Button("Jalur Rute");
        Button btnInfo = new Button("Pengumuman");
        HBox menu = new HBox(10, btnJadwal, btnHalte, btnJalur, btnInfo);

        TextArea hasil = buatTextArea("Hai Bro Kami Dari KampusRide x Tayo Kampus, Lagi Mau Kemana nii", 16);
        Button btnLogout = new Button("Logout");

        btnCari.setOnAction(e -> {
            String asal = tfAsal.getText().trim();
            String tujuan = tfTujuan.getText().trim();
            if (asal.isEmpty()) {
                alert(Alert.AlertType.WARNING, "Data belum lengkap", "Lokasi asal wajib diisi.");
                return;
            }
            hasil.setText(teksCari(asal, tujuan));
        });
        btnJadwal.setOnAction(e -> hasil.setText(teksJadwal()));
        btnHalte.setOnAction(e -> hasil.setText(teksHalte()));
        btnJalur.setOnAction(e -> hasil.setText(teksJalur()));
        btnInfo.setOnAction(e -> hasil.setText(teksPengumuman()));
        btnLogout.setOnAction(e -> { userAktif = null; tampilLogin(); });

        VBox root = new VBox(12, judul, formCari, menu, hasil, btnLogout);
        root.setPadding(new Insets(20));

        stage.setTitle("CampusRide - Mahasiswa");
        stage.setScene(new Scene(root, 680, 560));
    }

    private void tampilAdmin() {
        Label judul = judulLabel("Admin");

        Tab tabRute = new Tab("Rute", buatTabRute());
        Tab tabInfo = new Tab("Pengumuman", buatTabPengumuman());
        Tab tabAkun = new Tab("Akun", buatTabAkun());
        tabRute.setClosable(false);
        tabInfo.setClosable(false);
        tabAkun.setClosable(false);

        TabPane tabs = new TabPane(tabRute, tabInfo, tabAkun);
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> { userAktif = null; tampilLogin(); });

        VBox root = new VBox(12, judul, tabs, btnLogout);
        root.setPadding(new Insets(20));

        stage.setTitle("CampusRide - Admin");
        stage.setScene(new Scene(root, 760, 700));
    }

    private VBox buatTabRute() {
        TextArea daftar = buatTextArea(teksSemuaRute(), 8);

        TextField tfKode = new TextField();
        tfKode.setPromptText("Contoh: A");
        TextField tfNama = new TextField();
        TextField tfBerangkat = new TextField();
        tfBerangkat.setPromptText("07.00");
        TextField tfMulai = new TextField();
        tfMulai.setPromptText("07.00");
        TextField tfSelesai = new TextField();
        tfSelesai.setPromptText("17.00");
        TextField tfHalte = new TextField();
        TextField tfKet = new TextField();

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.add(new Label("Kode rute"), 0, 0);
        form.add(tfKode, 1, 0);
        form.add(new Label("Nama rute"), 0, 1);
        form.add(tfNama, 1, 1);
        form.add(new Label("Jam berangkat"), 0, 2);
        form.add(tfBerangkat, 1, 2);
        form.add(new Label("Operasional mulai"), 0, 3);
        form.add(tfMulai, 1, 3);
        form.add(new Label("Operasional selesai"), 0, 4);
        form.add(tfSelesai, 1, 4);
        form.add(new Label("Nama halte"), 0, 5);
        form.add(tfHalte, 1, 5);
        form.add(new Label("Keterangan halte"), 0, 6);
        form.add(tfKet, 1, 6);

        Button btnTambah = new Button("Tambah Rute");
        Button btnJadwal = new Button("Ubah Jadwal");
        Button btnHapus = new Button("Hapus Rute");
        Button btnHalte = new Button("Tambah Halte");
        HBox tombol = new HBox(10, btnTambah, btnJadwal, btnHapus, btnHalte);

        Label petunjuk = new Label("Tambah Rute: isi kode, nama, 3 jam | Ubah Jadwal: kode + 3 jam | "
                + "Hapus Rute: kode | Tambah Halte: kode + nama halte");
        petunjuk.setWrapText(true);

        btnTambah.setOnAction(e -> {
            String kode = tfKode.getText().trim().toUpperCase();
            String nama = tfNama.getText().trim();
            String jb = tfBerangkat.getText().trim();
            String jm = tfMulai.getText().trim();
            String js = tfSelesai.getText().trim();

            if (kode.isEmpty() || nama.isEmpty() || jb.isEmpty() || jm.isEmpty() || js.isEmpty()) {
                alert(Alert.AlertType.WARNING, "Data belum lengkap", "Kode, nama, dan ketiga jam wajib diisi.");
                return;
            }
            if (!Rute.jamValid(jb) || !Rute.jamValid(jm) || !Rute.jamValid(js)) {
                alert(Alert.AlertType.ERROR, "Format jam salah", "Gunakan format JJ.MM, contoh 07.30");
                return;
            }
            if (Main.cariRuteByKode(kode) != -1) {
                alert(Alert.AlertType.ERROR, "Gagal", "Kode rute sudah dipakai.");
                return;
            }
            if (Main.jumlahRute >= Main.MAX) {
                alert(Alert.AlertType.ERROR, "Gagal", "Kapasitas rute penuh.");
                return;
            }
            Main.tambahRute(new Rute(kode, nama, jb, jm, js));
            daftar.setText(teksSemuaRute());
            alert(Alert.AlertType.INFORMATION, "Berhasil", "Rute " + kode + " ditambahkan.");
        });

        btnJadwal.setOnAction(e -> {
            String kode = tfKode.getText().trim();
            String jb = tfBerangkat.getText().trim();
            String jm = tfMulai.getText().trim();
            String js = tfSelesai.getText().trim();

            if (kode.isEmpty() || jb.isEmpty() || jm.isEmpty() || js.isEmpty()) {
                alert(Alert.AlertType.WARNING, "Data belum lengkap", "Kode rute dan ketiga jam wajib diisi.");
                return;
            }
            int idx = Main.cariRuteByKode(kode);
            if (idx == -1) {
                alert(Alert.AlertType.ERROR, "Gagal", "Rute tidak ditemukan.");
                return;
            }
            if (!Rute.jamValid(jb) || !Rute.jamValid(jm) || !Rute.jamValid(js)) {
                alert(Alert.AlertType.ERROR, "Format jam salah", "Gunakan format JJ.MM, contoh 07.30");
                return;
            }
            Rute r = Main.daftarRute[idx];
            r.setJamBerangkat(jb);
            r.setJamMulai(jm);
            r.setJamSelesai(js);
            daftar.setText(teksSemuaRute());
            alert(Alert.AlertType.INFORMATION, "Berhasil", "Jadwal rute diperbarui.");
        });

        btnHapus.setOnAction(e -> {
            String kode = tfKode.getText().trim();
            if (kode.isEmpty()) {
                alert(Alert.AlertType.WARNING, "Data belum lengkap", "Kode rute wajib diisi.");
                return;
            }
            int idx = Main.cariRuteByKode(kode);
            if (idx == -1) {
                alert(Alert.AlertType.ERROR, "Gagal", "Rute tidak ditemukan.");
                return;
            }
            Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION,
                    "Hapus rute " + kode.toUpperCase() + "?", ButtonType.YES, ButtonType.NO);
            konfirmasi.showAndWait();
            if (konfirmasi.getResult() != ButtonType.YES) return;

            hapusRuteIndeks(idx);
            daftar.setText(teksSemuaRute());
        });

        btnHalte.setOnAction(e -> {
            String kode = tfKode.getText().trim();
            String namaHalte = tfHalte.getText().trim();
            String ket = tfKet.getText().trim();

            if (kode.isEmpty() || namaHalte.isEmpty()) {
                alert(Alert.AlertType.WARNING, "Data belum lengkap", "Kode rute dan nama halte wajib diisi.");
                return;
            }
            int idx = Main.cariRuteByKode(kode);
            if (idx == -1) {
                alert(Alert.AlertType.ERROR, "Gagal", "Rute tidak ditemukan.");
                return;
            }
            Rute r = Main.daftarRute[idx];
            boolean berhasil = ket.isEmpty() ? r.tambahHalte(namaHalte) : r.tambahHalte(namaHalte, ket);
            if (berhasil) {
                daftar.setText(teksSemuaRute());
                alert(Alert.AlertType.INFORMATION, "Berhasil", "Halte ditambahkan.");
            } else {
                alert(Alert.AlertType.ERROR, "Gagal", "Kapasitas halte rute ini penuh.");
            }
        });

        VBox box = new VBox(10, daftar, form, tombol, petunjuk);
        box.setPadding(new Insets(10));
        return box;
    }

    private VBox buatTabPengumuman() {
        TextArea daftar = buatTextArea(teksPengumuman(), 14);
        TextField tfJudul = new TextField();
        TextField tfIsi = new TextField();
        Button btnBuat = new Button("Buat Pengumuman");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.add(new Label("Judul"), 0, 0);
        form.add(tfJudul, 1, 0);
        form.add(new Label("Isi"), 0, 1);
        form.add(tfIsi, 1, 1);

        btnBuat.setOnAction(e -> {
            String judul = tfJudul.getText().trim();
            String isi = tfIsi.getText().trim();
            if (judul.isEmpty() || isi.isEmpty()) {
                alert(Alert.AlertType.WARNING, "Data belum lengkap", "Judul dan isi wajib diisi.");
                return;
            }
            if (Main.jumlahPengumuman >= Main.MAX) {
                alert(Alert.AlertType.ERROR, "Gagal", "Kapasitas pengumuman penuh.");
                return;
            }
            Main.tambahPengumuman(new Pengumuman(judul, isi));
            daftar.setText(teksPengumuman());
            tfJudul.clear();
            tfIsi.clear();
            alert(Alert.AlertType.INFORMATION, "Berhasil", "Pengumuman dibuat.");
        });

        VBox box = new VBox(10, daftar, form, btnBuat);
        box.setPadding(new Insets(10));
        return box;
    }

    private VBox buatTabAkun() {
        TextArea daftar = buatTextArea(teksAkun(), 14);
        TextField tfUsername = new TextField();
        tfUsername.setPromptText("Username yang akan dihapus");
        Button btnHapus = new Button("Hapus Akun");

        btnHapus.setOnAction(e -> {
            String username = tfUsername.getText().trim();
            if (username.isEmpty()) {
                alert(Alert.AlertType.WARNING, "Data belum lengkap", "Username wajib diisi.");
                return;
            }
            int idx = Main.cariUser(username);
            if (idx == -1) {
                alert(Alert.AlertType.ERROR, "Gagal", "User tidak ditemukan.");
                return;
            }
            if (username.equals(userAktif.getUsername())) {
                alert(Alert.AlertType.WARNING, "Tidak bisa", "Akun yang sedang dipakai tidak bisa dihapus.");
                return;
            }
            Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION,
                    "Hapus akun " + username + "?", ButtonType.YES, ButtonType.NO);
            konfirmasi.showAndWait();
            if (konfirmasi.getResult() != ButtonType.YES) return;

            hapusUserIndeks(idx);
            daftar.setText(teksAkun());
            tfUsername.clear();
        });

        HBox baris = new HBox(10, tfUsername, btnHapus);
        VBox box = new VBox(10, daftar, baris);
        box.setPadding(new Insets(10));
        return box;
    }

    private void hapusRuteIndeks(int idx) {
        for (int j = idx; j < Main.jumlahRute - 1; j++) {
            Main.daftarRute[j] = Main.daftarRute[j + 1];
        }
        Main.daftarRute[Main.jumlahRute - 1] = null;
        Main.jumlahRute--;
    }

    private void hapusUserIndeks(int idx) {
        for (int j = idx; j < Main.jumlahUser - 1; j++) {
            Main.daftarUser[j] = Main.daftarUser[j + 1];
        }
        Main.daftarUser[Main.jumlahUser - 1] = null;
        Main.jumlahUser--;
    }

    private String teksRute(Rute r) {
        return "[" + r.getKode() + "] " + r.getNama() + "\n"
                + "   Berangkat : " + r.getJamBerangkat() + " | Operasional: " + r.jamOperasional() + "\n"
                + "   Jalur     : " + r.jalur() + "\n";
    }

    private String teksSemuaRute() {
        if (Main.jumlahRute == 0) return "Belum ada rute.";
        String hasil = "";
        for (int i = 0; i < Main.jumlahRute; i++) {
            hasil += teksRute(Main.daftarRute[i]) + "\n";
        }
        return hasil;
    }

    private String teksCari(String dari, String ke) {
        String hasil = "";
        for (int i = 0; i < Main.jumlahRute; i++) {
            Rute r = Main.daftarRute[i];
            if (r.punyaHalte(dari) && (ke.isEmpty() || r.punyaHalte(ke))) {
                hasil += teksRute(r)
                        + "   Halte terdekat dari asal: " + r.getNamaHalte(r.cariHalte(dari)) + "\n\n";
            }
        }
        return hasil.isEmpty() ? "Rute tidak ditemukan." : hasil;
    }

    private String teksJadwal() {
        String hasil = "";
        for (int i = 0; i < Main.jumlahRute; i++) {
            Rute r = Main.daftarRute[i];
            hasil += "Rute " + r.getKode() + " (" + r.getNama() + ")\n"
                    + "   Berangkat " + r.getJamBerangkat() + ", operasional " + r.jamOperasional() + "\n\n";
        }
        return hasil.isEmpty() ? "Belum ada rute." : hasil;
    }

    private String teksHalte() {
        String hasil = "";
        for (int i = 0; i < Main.jumlahRute; i++) {
            Rute r = Main.daftarRute[i];
            hasil += "Halte rute " + r.getKode() + " (" + r.getNama() + "):\n";
            for (int j = 0; j < r.getJumlahHalte(); j++) {
                hasil += "   " + (j + 1) + ". " + r.getNamaHalte(j) + " - " + r.getKeteranganHalte(j) + "\n";
            }
            hasil += "\n";
        }
        return hasil.isEmpty() ? "Belum ada rute." : hasil;
    }

    private String teksJalur() {
        String hasil = "";
        for (int i = 0; i < Main.jumlahRute; i++) {
            Rute r = Main.daftarRute[i];
            hasil += "Rute " + r.getKode() + ": " + r.jalur() + "\n\n";
        }
        return hasil.isEmpty() ? "Belum ada rute." : hasil;
    }

    private String teksPengumuman() {
        if (Main.jumlahPengumuman == 0) return "Belum ada pengumuman.";
        String hasil = "";
        for (int i = 0; i < Main.jumlahPengumuman; i++) {
            Pengumuman p = Main.daftarPengumuman[i];
            hasil += (i + 1) + ". (" + p.getTanggal() + ") " + p.getJudul() + "\n   " + p.getIsi() + "\n\n";
        }
        return hasil;
    }

    private String teksAkun() {
        String hasil = "";
        for (int i = 0; i < Main.jumlahUser; i++) {
            User u = Main.daftarUser[i];
            hasil += "- " + u.getUsername() + " (" + u.getNama() + ") : " + u.getRole() + "\n";
        }
        return hasil;
    }
}