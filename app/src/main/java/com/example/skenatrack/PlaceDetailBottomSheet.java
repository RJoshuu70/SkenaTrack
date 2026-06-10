package com.example.skenatrack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.skenatrack.model.Place;
import com.example.skenatrack.utils.FavoriteManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

public class PlaceDetailBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_PLACE = "arg_place";

    // Callback interface menggantikan nullable lambda Kotlin
    public interface OnFavoriteChangedListener {
        void onFavoriteChanged();
    }

    private OnFavoriteChangedListener favoriteChangedListener;

    public void setOnFavoriteChangedListener(OnFavoriteChangedListener listener) {
        this.favoriteChangedListener = listener;
    }

    /**
     * Factory method — cara idiomatis Android untuk membuat Fragment dengan argumen.
     */
    public static PlaceDetailBottomSheet newInstance(Place place) {
        PlaceDetailBottomSheet sheet = new PlaceDetailBottomSheet();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PLACE, place);
        sheet.setArguments(args);
        return sheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_place_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() == null) return;
        Place place = getArguments().getParcelable(ARG_PLACE);
        if (place == null) return;

        // Bind views
        ImageView  imgPlace    = view.findViewById(R.id.imgPlace);
        TextView   tvName      = view.findViewById(R.id.tvName);
        TextView   tvCategory  = view.findViewById(R.id.tvCategory);
        TextView   tvLocation  = view.findViewById(R.id.tvLocation);
        TextView   tvRating    = view.findViewById(R.id.tvRating);
        TextView   tvDesc      = view.findViewById(R.id.tvDescription);
        MaterialButton btnMaps = view.findViewById(R.id.btnMaps);
        MaterialButton btnFav  = view.findViewById(R.id.btnFavorite);

        imgPlace.setImageResource(place.getImageRes());
        tvName.setText(place.getName());
        tvCategory.setText(place.getCategory().name());
        tvLocation.setText(place.getLocation());
        tvRating.setText("⭐ " + place.getRating());
        if (tvDesc != null) tvDesc.setText(place.getDescription());

        // Tombol buka Google Maps
        btnMaps.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(place.getMapUrl()));
            startActivity(intent);
        });

        // Tombol favorit dengan konfirmasi dialog
        btnFav.setOnClickListener(v -> {
            boolean isFav = FavoriteManager.isFavorite(requireContext(), place.getName());
            String title    = isFav ? "Hapus Favorit"  : "Tambah Favorit";
            String message  = isFav
                    ? "Hapus tempat ini dari daftar favorit?"
                    : "Tambahkan tempat ini ke daftar favorit?";
            String snackMsg = isFav ? "Dihapus dari favorit" : "Ditambahkan ke favorit";

            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Ya", (dialog, which) -> {
                        if (isFav) {
                            FavoriteManager.removeFavorite(requireContext(), place.getName());
                        } else {
                            FavoriteManager.addFavorite(requireContext(), place.getName());
                        }
                        Snackbar.make(view, snackMsg, Snackbar.LENGTH_SHORT).show();
                        if (favoriteChangedListener != null) {
                            favoriteChangedListener.onFavoriteChanged();
                        }
                        dialog.dismiss();
                    })
                    .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}
