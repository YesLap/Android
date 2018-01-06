package com.sendlook.yeslap.model;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class gps {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private android.location.Location location;
    private LocationManager locationManager;
    private Address address;

    double latitude = 0.0;
    double longitude = 0.0;

    public void getLocation(Context context) throws IOException {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        address = getAddress(latitude, longitude, context);

        Utils.toastyInfo(context, "Locality: " + address.getLocality() + "\nCountry: " + address.getAdminArea() + "\n" + address.getCountryName());

    }

    public Address getAddress(double latitude, double longitude, Context context) throws IOException {

        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;

        geocoder = new Geocoder(context);

        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        if (addresses.size() > 0) {
            address = addresses.get(0);
        }

        return address;

    }

}
