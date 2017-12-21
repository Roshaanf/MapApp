package roshaan.mapapp;

/**
 * Created by Roshaann 2.7 gpa on 16/10/2017.
 */

public class RouteModel {
    String longitude;
    String latitude;
    String location;

    public RouteModel() {
    }

    public RouteModel(String longitude, String latitude, String location) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
