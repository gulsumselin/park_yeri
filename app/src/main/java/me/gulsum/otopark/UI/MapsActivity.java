
package me.gulsum.otopark.UI;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import me.gulsum.otopark.R;
import me.gulsum.otopark.UI.Model.ParkAlani;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<ParkAlani> parkAlanlari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        parkAlanlariniOku();
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
                int giren = obj.getInt(("giren"));
                parkAlanlari.add(new ParkAlani(name, lat, lng, kontenjan, giren));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


        for (ParkAlani parkAlani : parkAlanlari) {
            LatLng position = new LatLng(parkAlani.lat, parkAlani.lng);
            mMap.addMarker(new MarkerOptions().position(position).title(parkAlani.name));
        }

        if (!parkAlanlari.isEmpty()) {
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
                        intent.putExtra("kontenjan",parkAlani.kontenjan);
                        intent.putExtra("giren", parkAlani.giren);
                        intent.putExtra("bosYer",parkAlani.bosYer);

                        break;
                    }
                }
                startActivity(intent);
                return false;
            }
        });
    }
}