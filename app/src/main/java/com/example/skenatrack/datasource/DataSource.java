package com.example.skenatrack.datasource;

import com.example.skenatrack.R;
import com.example.skenatrack.model.Place;
import com.example.skenatrack.model.PlaceCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DataSource {

    private static final List<Place> ALL_PLACES = new ArrayList<>();

    static {
        ALL_PLACES.add(new Place(
            "Kopi Nako Depok", PlaceCategory.CAFE, "Depok", 4.4f,
            R.drawable.kopi_nako_depok,
            "https://maps.google.com/?q=Kopi+Nako+Depok",
            "Kafe estetik di Depok dengan menu kopi susu premium dan dessert pilihan."
        ));
        ALL_PLACES.add(new Place(
            "MATCHAMAN, Blok M", PlaceCategory.CAFE, "Jakarta Selatan", 4.7f,
            R.drawable.matchaman_blok_m,
            "https://maps.google.com/?q=MATCHAMAN+Blok+M",
            "Spesialis matcha dengan suasana Jepang modern di pusat kota Jakarta."
        ));
        ALL_PLACES.add(new Place(
            "Museum MACAN", PlaceCategory.MUSEUM, "Jakarta Barat", 4.8f,
            R.drawable.museum_macan,
            "https://maps.google.com/?q=Museum+MACAN+Jakarta",
            "Museum seni kontemporer dan modern terbesar di Indonesia."
        ));
        ALL_PLACES.add(new Place(
            "Museum Nasional", PlaceCategory.MUSEUM, "Jakarta Pusat", 4.6f,
            R.drawable.museum_nasional,
            "https://maps.google.com/?q=Museum+Nasional+Jakarta",
            "Museum bersejarah dengan koleksi artefak budaya Nusantara terlengkap."
        ));
        ALL_PLACES.add(new Place(
            "Obihiro Nikudon", PlaceCategory.KULINER, "Jakarta Selatan", 4.6f,
            R.drawable.obihiro_nikudon,
            "https://maps.google.com/?q=Obihiro+nikudon+Jakarta",
            "Restoran Jepang autentik dengan menu nikudon dan ramen khas Hokkaido."
        ));
        ALL_PLACES.add(new Place(
            "Waduk Brigif", PlaceCategory.TAMAN, "Depok", 4.7f,
            R.drawable.waduk_brigif,
            "https://maps.google.com/?q=Waduk+Brigif+Depok",
            "Taman wisata air dengan pemandangan danau yang asri di pinggiran Depok."
        ));
        ALL_PLACES.add(new Place(
            "Taman Ismail Marzuki", PlaceCategory.TAMAN, "Jakarta Pusat", 4.5f,
            R.drawable.taman_ismail_marzuki,
            "https://maps.google.com/?q=Taman+Ismail+Marzuki+Jakarta",
            "Pusat kebudayaan Jakarta dengan taman hijau, galeri seni, dan planetarium."
        ));
    }

    public static List<Place> getPlaces(String category, String sortBy) {
        List<Place> result = new ArrayList<>(ALL_PLACES);

        // Filter by category
        if (category != null && !category.isEmpty()) {
            List<Place> filtered = new ArrayList<>();
            for (Place p : result) {
                if (p.getCategory().name().equals(category)) {
                    filtered.add(p);
                }
            }
            result = filtered;
        }

        // Sort
        if (sortBy == null) sortBy = "rating_desc";
        switch (sortBy) {
            case "rating_asc":
                Collections.sort(result, (a, b) -> Float.compare(a.getRating(), b.getRating()));
                break;
            case "name_asc":
                Collections.sort(result, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
                break;
            case "name_desc":
                Collections.sort(result, (a, b) -> b.getName().compareToIgnoreCase(a.getName()));
                break;
            default: // rating_desc
                Collections.sort(result, (a, b) -> Float.compare(b.getRating(), a.getRating()));
                break;
        }

        return result;
    }

    public static List<Place> getAllPlaces() {
        return new ArrayList<>(ALL_PLACES);
    }
}
