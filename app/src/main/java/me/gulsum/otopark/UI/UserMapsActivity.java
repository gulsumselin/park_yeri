package me.gulsum.otopark.UI;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import me.gulsum.otopark.R;
import me.gulsum.otopark.UI.Model.ParkAlani;

public class UserMapsActivity extends AppCompatActivity implements OnMapReadyCallback{

        private final int FINE_PERMISSION_CODE = 1;
        private GoogleMap mMap;
        private SearchView mapSearchView;

        Location currentLocation;
        FusedLocationProviderClient fusedLocationProviderClient;
        private List<ParkAlani> parkAlanlari;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.maps);

            mapSearchView = findViewById(R.id.mapSearch);

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            getLastLocation();

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {

                    String location = mapSearchView.getQuery().toString();
                    List<Address> addressList = null;

                    if(location != null){
                        Geocoder geocoder = new Geocoder(me.gulsum.otopark.UI.UserMapsActivity.this);

                        try{
                            addressList = geocoder.getFromLocationName(location, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

                    }


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });

            mapFragment.getMapAsync(me.gulsum.otopark.UI.UserMapsActivity.this);

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
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(me.gulsum.otopark.UI.UserMapsActivity.this);
                    }
                }
            });
        }

        private void parkAlanlariniOku() {
            parkAlanlari = new ArrayList<>();
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
                    Intent intent = new Intent(me.gulsum.otopark.UI.UserMapsActivity.this, UserRezervasyonActivity.class);
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
                    startActivity(intent);
                    return false;
                }
            });
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == FINE_PERMISSION_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
