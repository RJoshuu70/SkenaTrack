package com.example.skenatrack;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME  = "spotlight_prefs";
    private static final String KEY_DARK    = "dark_mode";

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Terapkan dark mode preference sebelum setContentView
        applyDarkModeFromPrefs();

        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setLogo(R.drawable.ic_skenatrack_logo);
        }

        // Resize logo di toolbar
        toolbar.post(() -> resizeToolbarLogo(toolbar));

        // Setup Navigation
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        if (navController != null) {
            NavigationUI.setupWithNavController(bottomNav, navController);
        }
    }

    private void resizeToolbarLogo(MaterialToolbar toolbar) {
        int sizePx = (int) (36 * getResources().getDisplayMetrics().density);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View child = toolbar.getChildAt(i);
            if (child instanceof ImageView) {
                child.getLayoutParams().width  = sizePx;
                child.getLayoutParams().height = sizePx;
                ((ImageView) child).setScaleType(ImageView.ScaleType.FIT_CENTER);
                child.requestLayout();
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            showDarkModeDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDarkModeDialog() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(KEY_DARK, false);

        MaterialSwitch darkSwitch = new MaterialSwitch(this);
        darkSwitch.setChecked(isDarkMode);
        darkSwitch.setText("Dark Mode");
        int padding = (int) (12 * getResources().getDisplayMetrics().density);
        darkSwitch.setPadding(padding * 4, padding * 2, padding * 4, padding * 2);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Pengaturan")
                .setView(darkSwitch)
                .setPositiveButton("Tutup", (dialog, which) -> {
                    prefs.edit().putBoolean(KEY_DARK, darkSwitch.isChecked()).apply();
                    int mode = darkSwitch.isChecked()
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO;
                    AppCompatDelegate.setDefaultNightMode(mode);
                    dialog.dismiss();
                })
                .show();
    }

    private void applyDarkModeFromPrefs() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean(KEY_DARK, false);
        int mode = isDarkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
    }
}
