package com.example.skenatrack.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class FavoriteManager {

    private static final String PREFS_NAME   = "spotlight_prefs";
    private static final String KEY_FAVORITES = "favorites";

    // Private constructor — mencegah instantiasi
    private FavoriteManager() {}

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isFavorite(Context context, String placeName) {
        Set<String> favorites = getPrefs(context).getStringSet(KEY_FAVORITES, new HashSet<>());
        return favorites != null && favorites.contains(placeName);
    }

    public static void addFavorite(Context context, String placeName) {
        SharedPreferences prefs = getPrefs(context);
        Set<String> current = new HashSet<>(prefs.getStringSet(KEY_FAVORITES, new HashSet<>()));
        current.add(placeName);
        prefs.edit().putStringSet(KEY_FAVORITES, current).apply();
    }

    public static void removeFavorite(Context context, String placeName) {
        SharedPreferences prefs = getPrefs(context);
        Set<String> current = new HashSet<>(prefs.getStringSet(KEY_FAVORITES, new HashSet<>()));
        current.remove(placeName);
        prefs.edit().putStringSet(KEY_FAVORITES, current).apply();
    }
}
