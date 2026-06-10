package com.example.skenatrack.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {

    // === ENCAPSULATION: semua field private ===
    private final String name;
    private final PlaceCategory category;
    private final String location;
    private final float rating;
    private final int imageRes;
    private final String mapUrl;
    private final String description;  // field tambahan untuk kebutuhan konten

    // Constructor utama
    public Place(String name, PlaceCategory category, String location,
                 float rating, int imageRes, String mapUrl, String description) {
        this.name = name;
        this.category = category;
        this.location = location;
        this.rating = rating;
        this.imageRes = imageRes;
        this.mapUrl = mapUrl;
        this.description = description;
    }

    // Constructor overload tanpa description (backward compat)
    public Place(String name, PlaceCategory category, String location,
                 float rating, int imageRes, String mapUrl) {
        this(name, category, location, rating, imageRes, mapUrl, "");
    }

    // === GETTERS (Encapsulation) ===
    public String getName()            { return name; }
    public PlaceCategory getCategory() { return category; }
    public String getLocation()        { return location; }
    public float getRating()           { return rating; }
    public int getImageRes()           { return imageRes; }
    public String getMapUrl()          { return mapUrl; }
    public String getDescription()     { return description; }

    // === PARCELABLE IMPLEMENTATION ===
    protected Place(Parcel in) {
        name        = in.readString();
        category    = PlaceCategory.valueOf(in.readString());
        location    = in.readString();
        rating      = in.readFloat();
        imageRes    = in.readInt();
        mapUrl      = in.readString();
        description = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) { return new Place(in); }
        @Override
        public Place[] newArray(int size)        { return new Place[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category.name());
        dest.writeString(location);
        dest.writeFloat(rating);
        dest.writeInt(imageRes);
        dest.writeString(mapUrl);
        dest.writeString(description);
    }
}
