package me.gulsum.otopark.UI.Model;

public class ParkAlani {
    public String name;
    public double lat;
    public double lng;
    public int kontenjan;
    public int giren;
    public int bosYer;

    public ParkAlani(String name, double lat, double lng, int kontenjan, int giren) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.kontenjan = kontenjan;
        this.giren = giren;
        this.bosYer = kontenjan - giren;
    }
}

