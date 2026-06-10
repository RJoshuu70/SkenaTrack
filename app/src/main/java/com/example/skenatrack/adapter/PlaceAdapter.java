package com.example.skenatrack.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skenatrack.R;
import com.example.skenatrack.model.Place;
import com.example.skenatrack.utils.FavoriteManager;

import java.util.List;
import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SKELETON = 0;
    private static final int TYPE_PLACE    = 1;

    // Interface callback untuk item click (menggantikan lambda Kotlin)
    public interface OnItemClickListener {
        void onItemClick(Place place);
    }

    private List<Place> places;
    private final OnItemClickListener listener;
    private boolean isSkeleton   = false;
    private int     skeletonCount = 0;

    public PlaceAdapter(List<Place> places, OnItemClickListener listener) {
        this.places   = new ArrayList<>(places);
        this.listener = listener;
    }

    // -----------------------------------------------------------------------
    // INNER CLASS: PlaceViewHolder — INHERITANCE dari RecyclerView.ViewHolder
    // -----------------------------------------------------------------------
    public class PlaceViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgPlace;
        private final ImageView imgFavorite;
        private final TextView  tvName;
        private final TextView  tvCategory;
        private final TextView  tvLocation;
        private final TextView  tvRating;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlace    = itemView.findViewById(R.id.imgPlace);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            tvName      = itemView.findViewById(R.id.tvName);
            tvCategory  = itemView.findViewById(R.id.tvCategory);
            tvLocation  = itemView.findViewById(R.id.tvLocation);
            tvRating    = itemView.findViewById(R.id.tvRating);
        }

        public void bind(Place place) {
            tvName.setText(place.getName());
            tvCategory.setText(place.getCategory().name());
            tvLocation.setText(place.getLocation());
            tvRating.setText("⭐ " + place.getRating());
            imgPlace.setImageResource(place.getImageRes());

            boolean isFav = FavoriteManager.isFavorite(itemView.getContext(), place.getName());
            imgFavorite.setVisibility(isFav ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(v -> listener.onItemClick(place));
        }
    }

    // -----------------------------------------------------------------------
    // INNER CLASS: SkeletonViewHolder — INHERITANCE, loading placeholder
    // -----------------------------------------------------------------------
    public static class SkeletonViewHolder extends RecyclerView.ViewHolder {
        public SkeletonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Skeleton tidak perlu binding — tampilannya sudah statis di XML
        }
    }

    // -----------------------------------------------------------------------
    // POLYMORPHISM: getItemViewType menentukan tipe ViewHolder yang dibuat
    // -----------------------------------------------------------------------
    @Override
    public int getItemViewType(int position) {
        return isSkeleton ? TYPE_SKELETON : TYPE_PLACE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_SKELETON) {
            View view = inflater.inflate(R.layout.item_place_skeleton, parent, false);
            return new SkeletonViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_place, parent, false);
            return new PlaceViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PlaceViewHolder) {
            ((PlaceViewHolder) holder).bind(places.get(position));
        }
        // SkeletonViewHolder tidak perlu binding
    }

    @Override
    public int getItemCount() {
        return isSkeleton ? skeletonCount : places.size();
    }

    // === Public methods untuk update state ===

    public void showSkeleton(int count) {
        isSkeleton    = true;
        skeletonCount = count;
        notifyDataSetChanged();
    }

    public void updateData(List<Place> newPlaces) {
        isSkeleton = false;
        places     = new ArrayList<>(newPlaces);
        notifyDataSetChanged();
    }

    public List<Place> getCurrentList() {
        return new ArrayList<>(places);
    }
}
