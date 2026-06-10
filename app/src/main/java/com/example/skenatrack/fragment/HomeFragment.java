package com.example.skenatrack.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skenatrack.PlaceDetailBottomSheet;
import com.example.skenatrack.R;
import com.example.skenatrack.adapter.PlaceAdapter;
import com.example.skenatrack.datasource.DataSource;
import com.example.skenatrack.model.Place;
import com.example.skenatrack.model.PlaceCategory;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private PlaceAdapter adapter;
    private RecyclerView  rvPlaces;
    private ChipGroup     chipGroupCategory;

    private String  currentCategory = null;
    private String  currentSort     = "rating_desc";
    private String  currentQuery    = "";

    private final Handler  searchHandler  = new Handler(Looper.getMainLooper());
    private Runnable       searchRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPlaces         = view.findViewById(R.id.rvPlaces);
        chipGroupCategory = view.findViewById(R.id.chipGroupCategory);
        EditText  searchEditText = view.findViewById(R.id.searchEditText);
        ImageButton btnSort     = view.findViewById(R.id.btnSort);

        setupRecyclerView();
        setupChips();
        setupSearch(searchEditText);
        setupSort(btnSort);

        loadData();
    }

    private void setupRecyclerView() {
        adapter = new PlaceAdapter(new ArrayList<>(), place -> {
            PlaceDetailBottomSheet sheet = PlaceDetailBottomSheet.newInstance(place);
            sheet.setOnFavoriteChangedListener(() ->
                    adapter.updateData(adapter.getCurrentList()));
            sheet.show(getChildFragmentManager(), "place_detail");
        });
        rvPlaces.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvPlaces.setAdapter(adapter);
    }

    private void setupChips() {
        int primary       = ContextCompat.getColor(requireContext(), R.color.purple_500);
        int white         = ContextCompat.getColor(requireContext(), R.color.white);
        int secondaryText = ContextCompat.getColor(requireContext(), R.color.text_secondary);

        ColorStateList chipBg = new ColorStateList(
                new int[][]{ new int[]{ android.R.attr.state_checked }, new int[]{} },
                new int[]  { primary, white }
        );
        ColorStateList chipText = new ColorStateList(
                new int[][]{ new int[]{ android.R.attr.state_checked }, new int[]{} },
                new int[]  { white, secondaryText }
        );

        // Chip "Semua"
        addChip("Semua", null, chipBg, chipText);

        // Chip per kategori
        for (PlaceCategory cat : PlaceCategory.values()) {
            addChip(cat.name(), cat.name(), chipBg, chipText);
        }

        // Select chip pertama
        if (chipGroupCategory.getChildCount() > 0) {
            chipGroupCategory.getChildAt(0).performClick();
        }
    }

    private void addChip(String label, String categoryValue,
                         ColorStateList bgColor, ColorStateList textColor) {
        Chip chip = new Chip(requireContext());
        chip.setText(label);
        chip.setClickable(true);
        chip.setCheckable(true);
        chip.setChipBackgroundColor(bgColor);
        chip.setTextColor(textColor);
        chip.setOnClickListener(v -> {
            currentCategory = categoryValue;
            loadData();
        });
        chipGroupCategory.addView(chip);
    }

    private void setupSearch(EditText searchEditText) {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (searchRunnable != null) searchHandler.removeCallbacks(searchRunnable);
                currentQuery = s != null ? s.toString() : "";
                if (!currentQuery.isBlank()) {
                    adapter.showSkeleton(2);
                }
                searchRunnable = HomeFragment.this::loadData;
                searchHandler.postDelayed(searchRunnable, 2000L);
            }
        });
    }

    private void setupSort(ImageButton btnSort) {
        btnSort.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), v);
            popup.getMenu().add(0, 0, 0, "Rating Tertinggi");
            popup.getMenu().add(0, 1, 1, "Rating Terendah");
            popup.getMenu().add(0, 2, 2, "Nama A-Z");
            popup.getMenu().add(0, 3, 3, "Nama Z-A");
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case 0: currentSort = "rating_desc"; break;
                    case 1: currentSort = "rating_asc";  break;
                    case 2: currentSort = "name_asc";    break;
                    case 3: currentSort = "name_desc";   break;
                }
                loadData();
                return true;
            });
            popup.show();
        });
    }

    private void loadData() {
        List<Place> places = DataSource.getPlaces(currentCategory, currentSort);
        if (!currentQuery.isBlank()) {
            List<Place> filtered = new ArrayList<>();
            for (Place p : places) {
                if (p.getName().toLowerCase().contains(currentQuery.toLowerCase())) {
                    filtered.add(p);
                }
            }
            places = filtered;
        }
        adapter.updateData(places);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (searchRunnable != null) searchHandler.removeCallbacks(searchRunnable);
    }
}
