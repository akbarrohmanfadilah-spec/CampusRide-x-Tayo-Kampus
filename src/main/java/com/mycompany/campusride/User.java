package com.mycompany.campusride;

public class User {
    public static final String MAHASISWA = "mahasiswa";
    public static final String ADMIN = "admin";

    private String username;
    private String password;
    private String nama;

    public User(String username, String password, String nama) {
        this.username = username;
        this.password = password;
        this.nama = nama;
    }

    public String getUsername() { return username; }
    public String getNama() { return nama; }

    public boolean setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            System.out.println("Nama tidak boleh kosong!");
            return false;
        }
        this.nama = nama;
        return true;
    }

    public boolean cekPassword(String pw) {
        return password.equals(pw);
    }

    public String getRole() {
        return "user";
    }

    public String getHakAkses() {
        return "Tidak ada hak akses khusus.";
    }

    public void tampilkan() {
        System.out.println("- " + username + " (" + nama + ") : " + getRole());
    }

    public void menu() {
        System.out.println("Menu belum tersedia untuk akun ini.");
    }
}
