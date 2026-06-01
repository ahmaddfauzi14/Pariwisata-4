<p align="center">
  <img width="648" height="242" alt="Frame 2" src="https://github.com/user-attachments/assets/236ad162-de8d-428a-a3c5-008ee04c64c3" />
</p>

# 🧭 WisataRasa

Aplikasi desktop berbasis **JavaFX** untuk menjelajahi destinasi wisata dan kuliner di kota Makassar. Dibangun dengan arsitektur berlapis (DAO – Service – UI) menggunakan database SQLite yang ringan dan tidak memerlukan instalasi server.

---

## 📋 Deskripsi

WisataRasa adalah aplikasi manajemen informasi pariwisata yang memudahkan pengguna menemukan tempat wisata dan kuliner lokal. Tersedia tiga peran pengguna: **Guest (Tamu)** yang dapat menjelajahi destinasi tanpa perlu login; **User** yang dapat menjelajahi, memberikan ulasan, dan menyimpan destinasi favorit; serta **Admin** yang mengelola seluruh data destinasi dan pengguna melalui dashboard khusus.

---

## ✨ Fitur-Fitur

### 👤 Fitur User
| Fitur | Keterangan |
|---|---|
| Registrasi & Login | Autentikasi akun dengan enkripsi password BCrypt |
| Homepage | Menampilkan daftar destinasi wisata & kuliner dengan Hero Section |
| Pencarian | Mencari destinasi berdasarkan nama atau kategori |
| Detail Destinasi | Melihat informasi lengkap: deskripsi, harga, jam operasional, lokasi maps, dan media sosial |
| Ulasan (Review) | Memberikan ulasan dan rating pada destinasi |
| Wishlist | Menyimpan destinasi favorit untuk dikunjungi nanti |
| Profil | Mengelola data akun dan mengganti password |
| Logout | Keluar dari sesi pengguna dengan konfirmasi dialog |

### 🙋 Fitur Guest (Tamu)
> Dapat diakses langsung tanpa registrasi — pilih **"Masuk sebagai Tamu"** di halaman login.

| Fitur | Keterangan |
|---|---|
| Homepage | Menampilkan daftar destinasi wisata & kuliner dengan Hero Section |
| Pencarian | Mencari destinasi berdasarkan nama atau kategori |
| Detail Destinasi | Melihat informasi lengkap: deskripsi, harga, jam operasional, lokasi maps, dan media sosial |
| ~~Ulasan (Review)~~ | ❌ Tidak tersedia — perlu login sebagai User |
| ~~Wishlist~~ | ❌ Tidak tersedia — perlu login sebagai User |
| ~~Profil~~ | ❌ Tidak tersedia — perlu login sebagai User |
| Logout | Keluar dari sesi tamu dengan konfirmasi dialog |

### 🛡️ Fitur Admin
| Fitur | Keterangan |
|---|---|
| Dashboard | Ringkasan statistik total destinasi dan pengguna terdaftar |
| Kelola Wisata | Tambah, edit, dan hapus data destinasi wisata |
| Kelola Kuliner | Tambah, edit, dan hapus data destinasi kuliner |
| Kelola Pengguna | Melihat dan mengelola daftar akun pengguna |
| Logout | Keluar dari sesi admin dengan konfirmasi dialog |

---

## 🔄 Alur Penggunaan Aplikasi

<img width="1149" height="1369" alt="c242438b-eb07-4963-ad35-1686e818fb3d" src="https://github.com/user-attachments/assets/3c20b1e1-ab0c-4c40-a8ad-fa8e8ff9f71a" />


**Alur Guest secara detail:**
1. Buka aplikasi → masuk ke halaman **Login**
2. Klik **"Masuk sebagai Tamu"** — tanpa perlu registrasi
3. Masuk ke **Homepage** dan dapat menjelajahi seluruh daftar destinasi
4. Gunakan **fitur pencarian** untuk menemukan destinasi tertentu
5. Klik destinasi untuk melihat **halaman detail**
6. Untuk mengakses Wishlist atau membuat Ulasan, perlu **daftar / login** terlebih dahulu

**Alur User secara detail:**
1. Buka aplikasi → otomatis masuk ke halaman **Login**
2. Jika belum punya akun, klik **Daftar** untuk membuat akun baru
3. Setelah login, masuk ke **Homepage** yang menampilkan daftar destinasi
4. Gunakan **fitur pencarian** untuk menemukan destinasi tertentu
5. Klik destinasi untuk membuka **halaman detail** (jam buka, harga tiket, lokasi, ulasan)
6. Tambahkan ke **Wishlist** jika ingin disimpan
7. Tulis **ulasan** untuk berbagi pengalaman
8. Kelola akun melalui menu **Profil**

**Alur Admin secara detail:**
1. Login menggunakan akun admin
2. Masuk ke **Dashboard** untuk melihat ringkasan statistik
3. Navigasi ke menu **Wisata** atau **Kuliner** untuk mengelola destinasi
4. Buka menu **Pengguna** untuk melihat daftar akun yang terdaftar

---

## 🔑 Akun Default

Saat pertama kali dijalankan, aplikasi secara otomatis membuat akun admin berikut:

| Field    | Value               |
|----------|---------------------|
| Email    | `admin01@gmail.com` |
| Password | `12345678`          |
| Role     | Admin               |

> **💡 Catatan:** Akun ini langsung tersedia tanpa perlu registrasi manual. Gunakan akun ini untuk mengakses fitur admin setelah clone dan menjalankan project.

---

## 📁 Struktur Folder

```
Pariwisata-4/
└── app/
    └── src/
        └── main/
            ├── java/pariwisata/
            │   ├── App.java                         # Entry point aplikasi
            │   ├── dao/                             # Data Access Object (query database)
            │   │   ├── DestinationRepository.java
            │   │   ├── MedsosRepository.java
            │   │   ├── ReviewRepository.java
            │   │   ├── UserRepository.java
            │   │   └── WishlistsRepository.java
            │   ├── database/                        # Koneksi & migrasi database
            │   │   ├── Db.java
            │   │   ├── DbMigration.java
            │   │   └── migration/
            │   │       ├── DestinationMigration.java
            │   │       ├── MediaSosialMigration.java
            │   │       ├── ReviewMigration.java
            │   │       ├── UserMigration.java
            │   │       └── WishlistMigration.java
            │   ├── model/                           # Model / entitas data
            │   │   ├── Destination.java
            │   │   ├── Medsos.java
            │   │   ├── Review.java
            │   │   ├── User.java
            │   │   └── Wishlist.java
            │   ├── service/                         # Business logic
            │   │   ├── DestinationService.java
            │   │   ├── MedsosService.java
            │   │   ├── ReviewService.java
            │   │   ├── UserService.java
            │   │   └── WishlistService.java
            │   ├── ui/
            │   │   ├── components/                  # Komponen UI yang dapat dipakai ulang
            │   │   │   ├── AdminSidebar.java
            │   │   │   ├── DestinationCard.java
            │   │   │   ├── HeroSection.java
            │   │   │   ├── LogoutConfirmDialog.java
            │   │   │   ├── SearchSection.java
            │   │   │   └── UserSidebar.java
            │   │   └── pages/
            │   │       ├── admin/                   # Halaman khusus admin
            │   │       │   ├── AdminDashboardPage.java
            │   │       │   ├── AdminKulinerPage.java
            │   │       │   ├── AdminUserPage.java
            │   │       │   └── AdminWisataPage.java
            │   │       ├── auth/                    # Halaman autentikasi
            │   │       │   ├── LoginPage.java
            │   │       │   └── RegisterPage.java
            │   │       └── user/                    # Halaman untuk user biasa
            │   │           ├── DetailDestinationPage.java
            │   │           ├── HomepagePage.java
            │   │           ├── ProfilePage.java
            │   │           └── WishlistPage.java
            │   └── util/                            # Utilitas pendukung
            │       ├── ImageHelper.java
            │       ├── Navigator.java
            │       └── Session.java
            └── resources/pariwisata/
                ├── assets/
                │   ├── fonts/                       # Font Poppins
                │   ├── icons/                       # Ikon aplikasi
                │   ├── images/                      # Gambar hero & auth
                │   └── logo/                        # Logo WisataRasa
                └── ui/style/
                    └── theme.css                    # Stylesheet global
```

---

## 🚀 Cara Menjalankan Project

### Prasyarat

Pastikan perangkat kamu sudah terinstal:
- **Java Development Kit (JDK) 21** atau lebih baru → [Download JDK](https://adoptium.net/)
- **Gradle** (sudah disertakan via Gradle Wrapper, tidak perlu install manual)

### Langkah-langkah

**1. Clone repository**
```bash
git clone <url-repository>
cd Pariwisata-4
```

**2. Jalankan aplikasi**
```bash
# Windows
gradlew.bat run

# Linux / macOS
./gradlew run
```

> Gradle akan otomatis mengunduh semua dependensi yang dibutuhkan pada saat pertama kali dijalankan. Pastikan koneksi internet tersedia.

**3. Login sebagai Admin**

Setelah aplikasi terbuka, langsung gunakan akun default berikut:
```
Email    : admin01@gmail.com
Password : 12345678
```

Atau **daftar akun baru** sebagai user biasa melalui halaman registrasi.

### Dependensi Utama

| Library | Versi | Fungsi |
|---|---|---|
| JavaFX | 21 | Framework UI |
| SQLite JDBC | 3.42.0.0 | Database lokal |
| jBCrypt | 0.4 | Enkripsi password |
| JUnit Jupiter | — | Unit testing |

---

## 🛠️ Teknologi yang Digunakan

- **Java 21** — Bahasa pemrograman utama
- **JavaFX 21** — Framework antarmuka grafis (GUI)
- **SQLite** — Database lokal berbasis file (`db/pariwisata.db`)
- **Gradle** — Build tool & dependency manager
- **BCrypt** — Enkripsi password pengguna

---

*WisataRasa — Jelajahi Wisata & Kuliner Makassar* 🌴
