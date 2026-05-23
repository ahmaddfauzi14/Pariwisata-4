# 🏝️ Pariwisata-4
---

## 📋 Daftar Isi

- [Panduan Kontribusi](#-panduan-kontribusi)
- [Struktur Folder Proyek](#-struktur-folder-proyek)

---

## 🤝 Panduan Kontribusi

Ikuti langkah-langkah berikut untuk berkontribusi pada proyek ini:

### Langkah 1: Fork Repository
Fork repository ini ke akun GitHub kamu.

### Langkah 2: Clone Repository
Clone repository yang telah di-fork ke laptop kamu:
```bash
git clone https://github.com/username-kamu/Pariwisata-4.git
```

### Langkah 3: Buka di Code Editor
Buka project di Visual Studio Code dan buka terminal.

### Langkah 4: Ganti ke Branch Anda
Pindah ke branch yang telah dibuat sebelumnya:
```bash
git checkout nama-branch-anda
```
*Contoh: `git checkout Fauzi`*

### Langkah 5: Push ke Branch Personal
- Setiap perubahan di-push ke branch personal kamu, **bukan ke branch main**
- Setelah selesai, buat Pull Request ke branch utama (main)
- Kode akan direview sebelum digabungkan ke main
- Tujuannya untuk mencegah konflik dan menjaga integritas kode

### Langkah 6: Sinkronisasi dengan Main
Selalu sinkronisasi kode kamu dengan branch main secara berkala:
```bash
git checkout main
git pull origin main
git checkout nama-branch-anda
git merge main
```

---

## 📁 Struktur Folder Proyek

```
Pariwisata-4/
├── gradle.properties          # Konfigurasi Gradle
├── gradlew                    # Gradle Wrapper (Linux/Mac)
├── gradlew.bat               # Gradle Wrapper (Windows)
├── settings.gradle           # Pengaturan build Gradle
├── README.md                 # File dokumentasi ini
│
├── gradle/
│   ├── libs.versions.toml    # Versioning library dependencies
│   └── wrapper/
│       └── gradle-wrapper.properties
│
├── app/                      # Folder aplikasi utama
│   ├── build.gradle          # Build configuration untuk app
│   ├── build/                # Direktori build (generated)
│   ├── db/                   # Database configuration/scripts
│   │
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── pariwisata/
│       │   │       ├── App.java              # Main class aplikasi
│       │   │       ├── controller/           # Controller classes
│       │   │       ├── dao/                  # Data Access Objects
│       │   │       ├── database/             # Database utilities
│       │   │       ├── model/                # Model/Entity classes
│       │   │       └── service/              # Business logic/Services
│       │   │
│       │   └── resources/    # Resource files (config, templates, dll)
│       │
│       └── test/
│           ├── java/
│           │   └── pariwisata/
│           │       └── AppTest.java          # Unit tests
│           │
│           └── resources/    # Test resource files
│
└── build/                    # Root build directory (generated)
    ├── reports/
    └── tmp/
```

### Penjelasan Struktur:

| Folder | Deskripsi |
|--------|-----------|
| `gradle/` | Konfigurasi dan wrapper untuk Gradle build system |
| `app/` | Folder aplikasi utama berisi kode sumber |
| `app/src/main/` | Kode sumber aplikasi (production code) |
| `app/src/test/` | Kode test dan testing resources |
| `app/db/` | Script dan konfigurasi database |
| `app/build/` | Direktori build hasil kompilasi (auto-generated) |

---

**Happy Coding! 🚀**
