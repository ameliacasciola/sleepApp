package com.example.mygooglemapapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnInfoWindowClickListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Marker currentUserLocationMarker;
    private Marker currentTappedLocationMarker;
    private static final int Request_user_location_Code = 99;
    private Location LastLocation;
    private LatLng currentUserLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();

            mMap.setMyLocationEnabled(true);
        }

    }

    public void onZoom(View view)
    {
        if(view.getId() == R.id.zoomIn)
        {
            mMap.moveCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId() == R.id.zoomOut)
        {
            mMap.moveCamera(CameraUpdateFactory.zoomOut());
        }
        if(view.getId() == R.id.up)
        {
            mMap.moveCamera(CameraUpdateFactory.scrollBy(0, -100));
        }
        if(view.getId() == R.id.down)
        {
            mMap.moveCamera(CameraUpdateFactory.scrollBy(0, 100));
        }
        if(view.getId() == R.id.left)
        {
            mMap.moveCamera(CameraUpdateFactory.scrollBy(-100, 0));
        }
        if(view.getId() == R.id.right)
        {
            mMap.moveCamera(CameraUpdateFactory.scrollBy(100, 0));
        }

    }

    public void onType(View view)
    {
        if(view.getId() == R.id.normal)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if(view.getId() == R.id.hybrid)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        if(view.getId() == R.id.satellite)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        if(view.getId() == R.id.terrain)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

    }

    public boolean checkUserLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, Request_user_location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, Request_user_location_Code);
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case Request_user_location_Code:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied...", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

        @Override
        public void onLocationChanged(Location location) {
            LastLocation = location;

            if (currentUserLocationMarker != null) {
                currentUserLocationMarker.remove();
            }

            LatLng latlang = new LatLng(location.getLatitude(), location.getLongitude());
            currentUserLL = latlang;

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latlang);
            markerOptions.title("Current Location");

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            currentUserLocationMarker = mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlang));
            mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

            if (googleApiClient != null)
            {
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            }
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1100);
            locationRequest.setFastestInterval(1100);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }
        }



        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Selected Location");


        if (currentTappedLocationMarker != null) {
            double i = 0.0001;
            LatLng tmp = currentTappedLocationMarker.getPosition();
            if(tmp.latitude-i < latLng.latitude
                    && tmp.latitude+i > latLng.latitude && tmp.longitude-i < latLng.longitude
                    && tmp.longitude+i > latLng.longitude) {
                Intent intent = new Intent(getApplicationContext(), StreetViewActivity.class);
                intent.putExtra("EXTRA_LATTTT", latLng.latitude);
                intent.putExtra("EXTRA_LONGGGG", latLng.longitude);
                startActivity(intent);
                return;
            }
            else
                currentTappedLocationMarker.remove();
        }

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentTappedLocationMarker = mMap.addMarker(markerOptions);
        currentTappedLocationMarker.showInfoWindow();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info Window Clicked", Toast.LENGTH_SHORT).show();
    }
}
