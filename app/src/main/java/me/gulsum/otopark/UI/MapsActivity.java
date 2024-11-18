package me.gulsum.otopark.UI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import me.gulsum.otopark.R;
import me.gulsum.otopark.UI.Model.ParkAlani;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_CODE = 1;
    private static final String TAG = "MapsActivity";
    private static final int DEFAULT_ZOOM = 15;
    private static final int DEFAULT_RADIUS = 100000;

    private GoogleMap googleMap;
    private SearchView searchView;
    private Location currentLocation;
    private FusedLocationProviderClient locationProviderClient;

    private Circle currentCircle;
    private final List<ParkAlani> parkAlanlari = new ArrayList<>();
    private String kullaniciAdi;
    private String kullaniciEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

        initializeUI();
        loadIntentExtras();
        loadLastKnownLocation();
        loadParkAlanlari();
    }

    private void initializeUI() {
        searchView = findViewById(R.id.mapSearch);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handleSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void loadIntentExtras() {
        kullaniciAdi = getIntent().getStringExtra("kullanici_adi");
        kullaniciEmail = getIntent().getStringExtra("kullanici_email");
    }

    private void loadLastKnownLocation() {
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            return;
        }

        locationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                Log.d(TAG, "Current location found: " + currentLocation.toString());
            } else {
                Log.w(TAG, "Current location is null");
            }
        });
    }



    private void loadParkAlanlari() {
        try (InputStream is = getResources().openRawResource(R.raw.park_alanlari)) {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String json = new String(buffer, StandardCharsets.UTF_8);
            parseParkAlanlariJSON(json);
        } catch (Exception e) {
            Log.e(TAG, "Error loading park alanlari: " + e.getMessage(), e);
        }
    }


    private void parseParkAlanlariJSON(String json) throws Exception {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            parkAlanlari.add(new ParkAlani(
                    obj.getString("name"),
                    obj.getDouble("lat"),
                    obj.getDouble("lng"),
                    obj.getInt("kontenjan"),
                    obj.getInt("giren")
            ));
        }
    }


    private void handleSearchQuery(String location) {
        if (location == null || location.isEmpty()) {
            Toast.makeText(this, "Lütfen geçerli bir adres girin.", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(location, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                addMarkerAndCircle(latLng, location);
            } else {
                Toast.makeText(this, "Adres bulunamadı: " + location, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error geocoding location: " + location, e);
        }
    }

    private void addMarkerAndCircle(LatLng latLng, String title) {
        googleMap.addMarker(new MarkerOptions().position(latLng).title(title));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, calculateZoomLevel(DEFAULT_RADIUS)));

        if (currentCircle != null) {
            currentCircle.remove();
        }

        currentCircle = googleMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(DEFAULT_RADIUS)
                .strokeColor(0xFF0000FF)
                .fillColor(0x220000FF));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        this.googleMap = map;
        setupMap();
        populateMarkers();
    }

    private void setupMap() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);

            if (currentLocation != null) {
                LatLng userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, DEFAULT_ZOOM));
                addCircle(userLocation, DEFAULT_RADIUS);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
    }

    private void populateMarkers() {
        for (ParkAlani parkAlani : parkAlanlari) {
            LatLng position = new LatLng(parkAlani.lat, parkAlani.lng);
            googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(parkAlani.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }

        googleMap.setOnMarkerClickListener(marker -> {
            openReservationActivity(marker);
            return true;
        });
    }



    private void openReservationActivity(Marker marker) {
        Intent intent = new Intent(this, RezervasyonActivity.class);
        intent.putExtra("park_adi", marker.getTitle());
        for (ParkAlani parkAlani : parkAlanlari) {
            if (parkAlani.name.equals(marker.getTitle())) {
                intent.putExtra("latitude", parkAlani.lat);
                intent.putExtra("longitude", parkAlani.lng);
                intent.putExtra("kontenjan", parkAlani.kontenjan);
                intent.putExtra("giren", parkAlani.giren);
                intent.putExtra("bosYer", parkAlani.bosYer);
                break;
            }
        }
        intent.putExtra("kullanici_adi", kullaniciAdi);
        intent.putExtra("kullanici_email", kullaniciEmail);
        startActivity(intent);
    }

    private void addCircle(LatLng center, double radius) {
        googleMap.addCircle(new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeColor(0xFF0000FF)
                .fillColor(0x220000FF));
    }

    private float calculateZoomLevel(double radius) {
        double scale = radius / 500;
        return (float) (16 - Math.log(scale) / Math.log(2));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadLastKnownLocation();
            } else {
                Toast.makeText(this, "Konum izinleri reddedildi.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}