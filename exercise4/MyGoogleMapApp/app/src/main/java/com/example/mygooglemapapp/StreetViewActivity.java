package com.example.mygooglemapapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mygooglemapapp.ui.streetview.StreetViewFragment;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class StreetViewActivity extends FragmentActivity
    implements OnStreetViewPanoramaReadyCallback
    {

        private double LAT = 49.261773;
        private double LONG = -123.250177;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.street_view_fragment);

        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.streetviewpanorama);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
        }

        @Override
        public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
            Intent intent = getIntent();
            LAT = intent.getDoubleExtra("EXTRA_LATTTT", 0.0);
            LONG = intent.getDoubleExtra("EXTRA_LONGGGG", 0.0);
            streetViewPanorama.setPosition(new LatLng(LAT, LONG));
        }
    }

