<p align="center">
  <img src="app/src/main/res/drawable/ic_skenatrack_logo.png" width="120" alt="SkenaTrack Logo"/>
</p>

<h1 align="center">SkenaTrack</h1>

<p align="center">
  Aplikasi rekomendasi tempat hits — kafe, museum, kuliner, dan taman — di Jabodetabek.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Java-F89820?logo=java&logoColor=white"/>
  <img src="https://img.shields.io/badge/Min%20SDK-30-1E7FA6"/>
  <img src="https://img.shields.io/badge/Material-Design%203-F5A623"/>
</p>

---

## Daftar Isi

- [Tentang Aplikasi](#tentang-aplikasi)
- [Fitur](#fitur)
- [Screenshot](#screenshot)
- [Arsitektur & OOP](#arsitektur--oop)
- [Dependencies](#dependencies)
- [Setup & Menjalankan](#setup--menjalankan)
- [Struktur Project](#struktur-project)
- [Tim Developer](#tim-developer)

---

## Tentang Aplikasi

**SkenaTrack** adalah aplikasi Android berbasis Java yang membantu pengguna menemukan tempat-tempat menarik di area Jabodetabek. Pengguna dapat menjelajahi rekomendasi kafe, museum, kuliner, dan taman, lalu menyimpan tempat favorit mereka secara lokal.

Aplikasi ini dikembangkan sebagai **Final Project** mata kuliah *Pemrograman Berorientasi Objek* dengan menerapkan konsep-konsep inti OOP pada platform Android.

---

## Fitur

| Fitur | Deskripsi |
|---|---|
| 🔍 **Pencarian** | Cari tempat by nama dengan debounce 2 detik + skeleton loading |
| 🏷️ **Filter Kategori** | Chip filter: Semua · CAFE · MUSEUM · KULINER · TAMAN |
| ↕️ **Sorting** | Urutkan by rating tertinggi/terendah atau nama A-Z/Z-A |
| ❤️ **Favorit** | Tandai tempat favorit, tersimpan lokal via SharedPreferences |
| 📋 **Detail Tempat** | Bottom Sheet berisi foto, kategori, lokasi, rating, deskripsi |
| 🗺️ **Buka Maps** | Langsung buka Google Maps dari detail tempat |
| 🌙 **Dark Mode** | Toggle dark/light mode dari menu toolbar |
| ✨ **Splash Screen** | Animasi splash screen gradient biru-emas 2 detik |

---

## Screenshot

> _Tambahkan screenshot aplikasi di sini setelah build pertama._

---

## Arsitektur & OOP

### Konsep OOP yang Diterapkan

#### 1. Encapsulation
Semua field di class `Place` bersifat `private` dan hanya dapat diakses melalui getter. `FavoriteManager` menyembunyikan implementasi SharedPreferences di balik method publik. `DataSource` menyembunyikan logika filter dan sort dari pemanggil.

```java
// Place.java — semua field private, hanya bisa diakses via getter
public class Place implements Parcelable {
    private final String name;
    private final PlaceCategory category;
    private final float rating;
    // ...
    public String getName()  { return name; }
    public float  getRating(){ return rating; }
}
```

#### 2. Inheritance
Setiap komponen Android mewarisi class dari framework:

| Class | Mewarisi dari |
|---|---|
| `SplashActivity`, `MainActivity` | `AppCompatActivity` |
| `HomeFragment`, `AboutFragment`, dll. | `Fragment` |
| `PlaceDetailBottomSheet` | `BottomSheetDialogFragment` |
| `PlaceAdapter` | `RecyclerView.Adapter` |
| `PlaceViewHolder`, `SkeletonViewHolder` | `RecyclerView.ViewHolder` |
| `AboutPagerAdapter` | `FragmentStateAdapter` |

#### 3. Polymorphism
`PlaceAdapter` mengoverride `getItemViewType()`, `onCreateViewHolder()`, dan `onBindViewHolder()` sehingga satu adapter dapat menangani dua tipe tampilan berbeda (item normal vs. skeleton loading) secara transparan.

```java
@Override
public int getItemViewType(int position) {
    return isSkeleton ? TYPE_SKELETON : TYPE_PLACE;
}
```

#### 4. Abstraction *(bonus)*
Interface `OnItemClickListener` dan `OnFavoriteChangedListener` mendefinisikan kontrak tanpa mengekspos implementasi — memisahkan *what* dari *how*.

---

## Dependencies

```kotlin
// Core AndroidX
implementation(libs.appcompat)          // AppCompatActivity, dark mode
implementation(libs.activity)           // ComponentActivity base
implementation(libs.constraintlayout)   // Layout support

// Material Design 3
implementation(libs.material)
// ↳ MaterialToolbar, BottomNavigationView, MaterialCardView,
//   MaterialButton, MaterialAlertDialog, Chip/ChipGroup,
//   BottomSheetDialogFragment, MaterialSwitch, TabLayout,
//   TextInputLayout, Snackbar

// Navigation Component
implementation(libs.navigation.fragment)        // NavHostFragment, NavController
implementation(libs.navigation.ui)              // setupWithNavController()
implementation(libs.navigation.runtime.android) // runtime support

// RecyclerView
implementation(libs.recyclerview)

// ViewPager2 (diperlukan AboutFragment — tab layout)
implementation(libs.viewpager2)
```

Versi lengkap tersedia di [`gradle/libs.versions.toml`](gradle/libs.versions.toml).

---

## Setup & Menjalankan

### Prasyarat

- Android Studio **Hedgehog** atau lebih baru
- JDK 11+
- Android SDK API 30 (Android 11) ke atas

### Langkah

```bash
# 1. Clone repository
git clone https://github.com/RJoshuu70/SkenaTrack.git
cd SkenaTrack

# 2. Buka di Android Studio
#    File → Open → pilih folder SkenaTrack

# 3. Tunggu Gradle sync selesai
#    (pastikan koneksi internet aktif saat sync pertama)

# 4. Run
#    Tekan tombol ▶ atau Shift+F10
#    Pilih emulator (API 30+) atau device fisik
```

### Catatan Penting

- `minSdk = 30` — wajib menggunakan `String.isBlank()` (Java 11+)
- Tidak menggunakan Kotlin, Compose, atau ViewBinding
- Data tempat bersifat statis (in-memory `DataSource`); siap dimigrasi ke SQLite/Room

---

## Struktur Project

```
app/src/main/
├── java/com/example/skenatrack/
│   ├── model/
│   │   ├── Place.java            ← Data class (Encapsulation + Parcelable)
│   │   └── PlaceCategory.java   ← Enum kategori (CAFE, MUSEUM, KULINER, TAMAN)
│   ├── datasource/
│   │   └── DataSource.java      ← Sumber data + logika filter & sort
│   ├── adapter/
│   │   ├── PlaceAdapter.java    ← RecyclerView adapter (Inheritance + Polymorphism)
│   │   └── AboutPagerAdapter.java
│   ├── fragment/
│   │   ├── HomeFragment.java    ← List tempat, search, chip, sort
│   │   ├── AboutFragment.java   ← Tab container (ViewPager2)
│   │   ├── AboutAppFragment.java
│   │   └── ProfileFragment.java
│   ├── utils/
│   │   └── FavoriteManager.java ← SharedPreferences wrapper (Encapsulation)
│   ├── MainActivity.java        ← Host Navigation + Toolbar + Dark Mode
│   ├── SplashActivity.java      ← Splash screen 2 detik
│   └── PlaceDetailBottomSheet.java ← Detail tempat + favorit
└── res/
    ├── layout/                  ← XML layouts (8 file)
    ├── drawable/                ← Ikon, gambar, gradients
    ├── navigation/nav_graph.xml ← Peta navigasi antar fragment
    ├── menu/                    ← Bottom nav + options menu
    ├── color/                   ← Color state selectors
    └── values/                  ← colors.xml, themes.xml, dimens.xml, strings.xml
```

---

## Tim Developer

| Nama | NIM | GitHub |
|---|---|---|
| Abia Farrel Kaysan | 2510511167 | [@Farrelabia](https://github.com/Farrelabia) |

---

<p align="center">
  Dibuat dengan ❤️ untuk Final Project Mata Kuliah Pemrograman Berorientasi Objek<br/>
  Universitas Pembangunan Nasional "Veteran" Jakarta — Sistem Informasi
</p>
