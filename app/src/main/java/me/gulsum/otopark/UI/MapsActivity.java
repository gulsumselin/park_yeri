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
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import me.gulsum.otopark.R;
import me.gulsum.otopark.UI.Model.ParkAlani;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap mMap;
    private SearchView mapSearchView;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final static int REQUEST_CODE = 101;

    private Circle currentCircle;

    private List<ParkAlani> parkAlanlari = new ArrayList<>();
    private String kullaniciAdi;
    private String kullaniciEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

        mapSearchView = findViewById(R.id.mapSearch);

        kullaniciAdi = getIntent().getStringExtra("kullanici_adi");
        kullaniciEmail = getIntent().getStringExtra("kullanici_email");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = mapSearchView.getQuery().toString();

                if (location != null && !location.isEmpty()) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    List<Address> addressList = null;

                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (addressList != null && !addressList.isEmpty()) {
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, calculateZoomLevel(100000)));


                        if (currentCircle != null) {
                            currentCircle.remove();
                        }

                        currentCircle = mMap.addCircle(new CircleOptions()
                                .center(latLng)
                                .radius(100000)
                                .strokeColor(0xFF0000FF)
                                .fillColor(0x220000FF));

                    } else {
                        Toast.makeText(MapsActivity.this, "Adres bulunamadı: " + location, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MapsActivity.this, "Lütfen geçerli bir adres girin.", Toast.LENGTH_SHORT).show();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        parkAlanlariniOku();
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Log.d("MapsActivity", "Current location found: " + currentLocation.toString());
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                } else {
                    Log.d("MapsActivity", "Current location is null");
                }
            }
        });
    }

    private void parkAlanlariniOku() {
        try {
            InputStream is = getResources().openRawResource(R.raw.park_alanlari);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String name = obj.getString("name");
                double lat = obj.getDouble("lat");
                double lng = obj.getDouble("lng");
                int kontenjan = obj.getInt("kontenjan");
                int giren = obj.getInt("giren");
                parkAlanlari.add(new ParkAlani(name, lat, lng, kontenjan, giren));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            if (currentLocation != null) {
                LatLng userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                mMap.addCircle(new CircleOptions()
                        .center(userLocation)
                        .radius(100000)
                        .strokeColor(0xFF0000FF)
                        .fillColor(0x220000FF));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, calculateZoomLevel(100000)));
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
        }

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);

        for (ParkAlani parkAlani : parkAlanlari) {
            LatLng position = new LatLng(parkAlani.lat, parkAlani.lng);
            MarkerOptions options = (new MarkerOptions().position(position).title(parkAlani.name));
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(options);
        }

        if (!parkAlanlari.isEmpty() && currentLocation == null) {
            LatLng firstLocation = new LatLng(parkAlanlari.get(0).lat, parkAlanlari.get(0).lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10));
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Intent intent = new Intent(MapsActivity.this, RezervasyonActivity.class);
                intent.putExtra("park_adi", marker.getTitle());
                for (ParkAlani parkAlani : parkAlanlari) {
                    if (parkAlani.name.equals(marker.getTitle())) {
                        intent.putExtra("latitude", parkAlani.lat);
                        intent.putExtra("longitude", parkAlani.lng);
                        intent.putExtra("kontenjan", parkAlani.kontenjan);
                        intent.putExtra("giren", parkAlani.giren);
                        break;
                    }
                }
                intent.putExtra("kullanici_adi", kullaniciAdi);
                intent.putExtra("kullanici_email", kullaniciEmail);
                startActivity(intent);
                return false;
            }
        });
    }

    private float calculateZoomLevel(double radius) {
        double scale = radius / 500;
        return (float) (16 - Math.log(scale) / Math.log(2));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Konum izni reddedildi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
