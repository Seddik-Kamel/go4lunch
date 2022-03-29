package com.example.go4lunch.utils.location;

import com.google.android.gms.maps.model.LatLng;

public class LocationUtils {

    public static LatLng getCoordinate(double lat0, double lng0, long dy, long dx) {
        double lat = lat0 + (180 / Math.PI) * (dy / 6378137);
        double lng = lng0 + (180 / Math.PI) * (dx / 6378137) / Math.cos(lat0);

        return new LatLng(lat, lng);
    }
}
