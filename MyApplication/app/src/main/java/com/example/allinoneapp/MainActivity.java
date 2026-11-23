

package com.example.allinoneapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.allinoneapp.ui.login.LoginActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static FusedLocationProviderClient fusedLocationProviderClient;
    public static LocationRequest locationRequest;
    public static LocationCallback locationCallback;
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int DEFAULT_FAST_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private static final int PERMISSIONS_COARSE_LOCATION = 44;

    public static String UserLoginID;
    public static String gps_latitude = "";
    public static String gps_longitude = "";
    public static String gps_speed = "";
    public static String gps_altitude = "";
    public static String gps_accuracy = "";
    public static String gps_address = "";
    public static String gps_updates = "";

    public static double realtime_latitude;
    public static double realtime_longitude;

    public static FirebaseDatabase database;
    public static DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);

            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true); //stores data when offline



        locationRequest = new LocationRequest();
            locationRequest.setInterval(1000 + DEFAULT_UPDATE_INTERVAL);
            locationRequest.setFastestInterval(1000 * DEFAULT_FAST_INTERVAL);
            locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    setCoordinates(locationResult.getLastLocation());
                }
            };

            updateGPS();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();

                } else {
                    Toast.makeText(this, "This app requires Location permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task =  fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    setCoordinates(location);
                }
            });
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, PERMISSIONS_FINE_LOCATION);
            }
        }

    }


    private void setCoordinates(Location location) {

        if (location != null){

            realtime_latitude = location.getLatitude();
            realtime_longitude = location.getLongitude();

            gps_latitude = String.valueOf(location.getLatitude());
            gps_longitude = String.valueOf(location.getLongitude());
            gps_speed = String.valueOf(location.getSpeed());
            gps_accuracy = String.valueOf(location.getAccuracy());

            if (location.hasSpeed()) {
                gps_speed = String.valueOf(location.getSpeed());
            } else {
                gps_speed = "Not available";
            }

            if (location.hasAltitude()) {
                gps_altitude = String.valueOf(location.getAltitude());
            } else {
                gps_altitude = "Not available";
            }

            Geocoder geocoder = new Geocoder(MainActivity.this);

            try {
                List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                gps_address = addressList.get(0).getAddressLine(0);
            } catch (Exception e) {
                gps_address = "Unable to get street address.";
            }


        }
    }

    public  void startLocationUpdates() {
        gps_updates = "Location is being tracked";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "This app requires GPS permission", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        updateGPS();
    }

    public void stopLocationUpdates() {
        gps_updates = "Location is not being tracked";
        gps_latitude = "Not tracking location";
        gps_longitude = "Not tracking location";
        gps_speed = "Not tracking location";
        gps_accuracy = "Not tracking location";
        gps_altitude = "Not tracking location";
        gps_address = "Not tracking location";
    }

}