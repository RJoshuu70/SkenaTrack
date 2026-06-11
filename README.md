<p align="center">
  <img src="app/src/main/res/drawable/ic_spotlight_logo.png" width="120" alt="SkenaTrack Logo"/>
</p>

<h1 align="center">SkenaTrack</h1>

<p align="center">
  Aplikasi rekomendasi tempat hits — kafe, museum, kuliner, dan taman — di Jabodetabek.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Min%20SDK-26-1E7FA6"/>
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

**SkenaTrack** adalah aplikasi Android berbasis Kotlin yang membantu pengguna menemukan tempat-tempat menarik di area Jabodetabek. Pengguna dapat menjelajahi rekomendasi kafe, museum, kuliner, dan taman, lalu menyimpan tempat favorit mereka secara lokal menggunakan SharedPreferences.

Aplikasi ini dikembangkan sebagai **Final Project** mata kuliah *Pemrograman Berorientasi Objek* dengan menerapkan konsep-konsep inti OOP pada platform Android.

---

## Fitur

| Fitur | Deskripsi |
|---|---|
| 🔍 **Pencarian** | Cari tempat by nama dengan debounce 2 detik + skeleton loading |
| 🏷️ **Filter Kategori** | Chip filter: Semua · CAFE · MUSEUM · KULINER · TAMAN |
| ↕️ **Sorting** | Urutkan by rating tertinggi/terendah atau nama A-Z/Z-A |
| ❤️ **Favorit** | Tandai tempat favorit, tersimpan lokal via SharedPreferences |
| 📋 **Detail Tempat** | Bottom Sheet berisi foto, kategori, lokasi, rating, dan tombol aksi |
| 🗺️ **Buka Maps** | Langsung buka Google Maps dari halaman detail tempat |
| 🌙 **Dark Mode** | Toggle dark/light mode dari menu toolbar, preferensi disimpan lokal |
| ✨ **Splash Screen** | Splash screen dengan latar gradient ungu-gelap selama 2 detik |

---

## Screenshot

> _Tambahkan screenshot aplikasi di sini setelah build pertama._

---

## Arsitektur & OOP

### Konsep OOP yang Diterapkan

#### 1. Encapsulation

Semua field di data class `Place` bersifat `val` (immutable) dan hanya dapat dibaca dari luar — tidak bisa diubah setelah dibuat. `FavoriteManager` menyembunyikan detail implementasi SharedPreferences di balik method publik yang sederhana. `DataSource` menyembunyikan logika filter dan sorting sehingga pemanggil (`HomeFragment`) tidak perlu tahu cara kerjanya.

```kotlin
// Place.kt — field immutable, hanya bisa dibaca via property
data class Place(
    val name: String,
    val category: PlaceCategory,
    val location: String,
    val rating: Float,
    val imageRes: Int,
    val mapUrl: String
) : Parcelable
```

```kotlin
// FavoriteManager.kt — implementasi SharedPreferences tersembunyi
object FavoriteManager {
    private fun getPrefs(context: Context): SharedPreferences { ... }
    fun isFavorite(context: Context, placeName: String): Boolean { ... }
    fun addFavorite(context: Context, placeName: String) { ... }
    fun removeFavorite(context: Context, placeName: String) { ... }
}
```

#### 2. Inheritance

Setiap komponen Android mewarisi class dari framework:

| Class | Mewarisi dari |
|---|---|
| `SplashActivity`, `MainActivity` | `AppCompatActivity` |
| `HomeFragment`, `AboutFragment`, `AboutAppFragment`, `ProfileFragment` | `Fragment` |
| `PlaceDetailBottomSheet` | `BottomSheetDialogFragment` |
| `PlaceAdapter` | `RecyclerView.Adapter<RecyclerView.ViewHolder>` |
| `PlaceViewHolder` | `RecyclerView.ViewHolder` |
| `SkeletonViewHolder` | `RecyclerView.ViewHolder` |
| `AboutPagerAdapter` | `FragmentStateAdapter` |

#### 3. Polymorphism

`PlaceAdapter` meng-override `getItemViewType()`, `onCreateViewHolder()`, dan `onBindViewHolder()` sehingga satu adapter dapat menangani dua tipe tampilan berbeda — item normal (`PlaceViewHolder`) dan skeleton loading (`SkeletonViewHolder`) — secara transparan berdasarkan state `isSkeleton`.

```kotlin
// PlaceAdapter.kt
override fun getItemViewType(position: Int): Int {
    return if (isSkeleton) TYPE_SKELETON else TYPE_PLACE
}

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
        TYPE_SKELETON -> SkeletonViewHolder(...)
        else          -> PlaceViewHolder(...)
    }
}
```

#### 4. Abstraction *(bonus)*

`PlaceDetailBottomSheet` mengekspos property `onFavoriteChanged: (() -> Unit)?` sebagai kontrak callback tanpa mengekspos implementasi internalnya ke `HomeFragment`. Pemanggil cukup mengisi closure-nya tanpa tahu bagaimana dialog memproses aksi favorit.

---

## Dependencies

```kotlin
// Core AndroidX
implementation(libs.androidx.core.ktx)          // v1.17.0
implementation(libs.androidx.lifecycle.runtime.ktx)

// Material Design 3
implementation("com.google.android.material:material:1.12.0")
// ↳ MaterialToolbar, BottomNavigationView, MaterialCardView,
//   BottomSheetDialogFragment, MaterialAlertDialogBuilder,
//   MaterialSwitch, Chip/ChipGroup, Snackbar, TextInputLayout

// Navigation Component
implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
// ↳ NavHostFragment, NavController, setupWithNavController()

// ViewPager2 (tab layout di AboutFragment)
implementation(libs.androidx.viewpager2)        // v1.0.0

// Kotlin Parcelize Plugin
id("kotlin-parcelize")
// ↳ @Parcelize pada data class Place (antar-Fragment via Bundle)
```

Versi lengkap tersedia di [`gradle/libs.versions.toml`](gradle/libs.versions.toml).

---

## Setup & Menjalankan

### Prasyarat

- Android Studio **Hedgehog** atau lebih baru
- JDK 11+
- Android SDK API 26 (Android 8.0) ke atas

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
#    Pilih emulator (API 26+) atau device fisik
```

### Catatan Penting

- `minSdk = 26` — menggunakan ViewBinding, Navigation Component, dan Kotlin Parcelize
- Seluruh UI menggunakan **XML layout** (bukan Jetpack Compose), meskipun Compose BOM tersedia di `build.gradle.kts` sebagai dependency bawaan template
- Data tempat bersifat statis (in-memory `DataSource`); siap dimigrasi ke SQLite/Room
- Favorit disimpan menggunakan **SharedPreferences** (tidak hilang saat app ditutup)

---

## Struktur Project

```
app/src/main/
├── java/com/example/spotlight/
│   ├── model/
│   │   ├── Place.kt                  ← Data class (Encapsulation + Parcelable)
│   │   └── PlaceCategory.kt          ← Enum kategori (CAFE, MUSEUM, KULINER, TAMAN)
│   ├── datasource/
│   │   └── DataSource.kt             ← Sumber data + logika filter & sort (7 tempat)
│   ├── adapter/
│   │   ├── PlaceAdapter.kt           ← RecyclerView adapter (Inheritance + Polymorphism)
│   │   └── AboutPagerAdapter.kt      ← FragmentStateAdapter untuk ViewPager2
│   ├── fragment/
│   │   ├── HomeFragment.kt           ← List tempat, search debounce, chip, sort
│   │   ├── AboutFragment.kt          ← Tab container ViewPager2 (TabLayout)
│   │   ├── AboutAppFragment.kt       ← Tab "Tentang App"
│   │   └── ProfileFragment.kt        ← Tab "Profil Developer"
│   ├── utils/
│   │   └── FavoriteManager.kt        ← SharedPreferences wrapper (Encapsulation)
│   ├── MainActivity.kt               ← Host Navigation + Toolbar + Dark Mode toggle
│   ├── SplashActivity.kt             ← Splash screen 2 detik → MainActivity
│   └── PlaceDetailBottomSheet.kt     ← BottomSheetDialogFragment (detail + favorit + maps)
└── res/
    ├── layout/                       ← 7 file XML layout
    │   ├── activity_main.xml         ← Toolbar + NavHostFragment + BottomNavigationView
    │   ├── activity_splash.xml       ← Logo + nama app + gradient background
    │   ├── fragment_home.xml         ← SearchBar + ChipGroup + RecyclerView
    │   ├── fragment_about.xml        ← TabLayout + ViewPager2
    │   ├── fragment_about_app.xml    ← Konten tab "Tentang App"
    │   ├── fragment_profile.xml      ← Foto + info developer
    │   ├── item_place.xml            ← Card item (foto + nama + kategori + lokasi + rating)
    │   ├── item_place_skeleton.xml   ← Skeleton loading placeholder
    │   └── bottom_sheet_place_detail.xml ← Detail + tombol Maps & Favorit
    ├── drawable/                     ← 7 foto tempat + ikon + gradient + selector
    ├── navigation/nav_graph.xml      ← Graf navigasi: HomeFragment ↔ AboutFragment
    ├── menu/                         ← bottom_nav_menu.xml + option_menu.xml
    ├── color/nav_item_color.xml      ← Color state list untuk bottom nav
    └── values/
        ├── colors.xml                ← Palette ungu + surface + text tokens
        ├── themes.xml                ← Theme.SpotLight (Light) + Theme.SpotLight.Dark
        ├── dimens.xml                ← Spacing & sizing tokens
        └── strings.xml              ← String resources
```

---

## Tim Developer

| Nama | NIM |
|---|---|
| Rapolo Joshua Napitupulu | 2410512001 |
| Fadilla Putra Karnasyah | 2410512009 |
| Alif Ilham Rhamdhan | 2410512023 |
| Sekar Nur Aini | 2410512029 |
| Okto Ramadhantyo Wibisono | 2410512032 |

---

<p align="center">
  Dibuat dengan ❤️ untuk Final Project Mata Kuliah Pemrograman Berorientasi Objek<br/>
  Universitas Pembangunan Nasional "Veteran" Jakarta — Sistem Informasi
</p>