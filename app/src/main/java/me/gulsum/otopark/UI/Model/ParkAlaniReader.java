package me.gulsum.otopark.UI.Model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import me.gulsum.otopark.R;

public class ParkAlaniReader {
    private Context context;

    public ParkAlaniReader(Context context) {
        this.context = context;
    }

    public List<ParkAlani> parkAlanlariniOku() {
        List<ParkAlani> parkAlanlari = new ArrayList<>();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.park_alanlari); // JSON dosyasını raw klasörüne koyun
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
                int bosYer = obj.getInt("bosYer");
                parkAlanlari.add(new ParkAlani(name, lat, lng, kontenjan, giren));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parkAlanlari;
    }
}
