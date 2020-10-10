package com.dmi.googlemap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private ImageView imgTerrain, imgSatellite, imgHybrid;
    Location mLocation;
    Marker _marker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        imgTerrain = findViewById(R.id.imgTerrain);
        imgSatellite = findViewById(R.id.imgSatellite);
        imgHybrid = findViewById(R.id.imgHybrid);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        imgTerrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap == null) {
                    Toast.makeText(MapsActivity.this, "Google map not available", Toast.LENGTH_SHORT).show();
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
            }
        });

        imgSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap == null) {
                    Toast.makeText(MapsActivity.this, "Google map not available", Toast.LENGTH_LONG).show();
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });

        imgHybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap == null) {
                    Toast.makeText(MapsActivity.this, "Google map not available", Toast.LENGTH_LONG).show();
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(19.9975, 73.7898);
        mMap.addMarker(new
                MarkerOptions().
                position(sydney).
                title("Marker in Nashik"));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 14);
        mMap.animateCamera(cameraUpdate);*/
        mMap.getUiSettings().

                setZoomControlsEnabled(true);

    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (_marker != null) {
            _marker.remove();
        }
        LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("My Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        _marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}