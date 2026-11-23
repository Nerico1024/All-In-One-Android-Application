package com.example.allinoneapp.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.allinoneapp.LoginLocation;
import com.example.allinoneapp.MainActivity;
import com.example.allinoneapp.R;
import com.example.allinoneapp.ServiceProviderProfile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private static List<LoginLocation>  loginLocations;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        Bundle bundle = this.getArguments();
        String service = bundle.get("selectedService").toString();
        loginLocations= new ArrayList<LoginLocation>();

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(MainActivity.realtime_latitude,MainActivity.realtime_longitude);
                markerOptions.position(latLng);
                markerOptions.title("My Location");
                googleMap.clear();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                //googleMap.addMarker(markerOptions);

                MainActivity.database.getInstance().getReference().child("LoginLocation")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    LoginLocation loginLocation = snapshot1.getValue(LoginLocation.class);

                                    if (loginLocation.getAddress().isEmpty() == false){

                                        MarkerOptions markerOptions2 = new MarkerOptions();
                                        LatLng latLng = new LatLng(Double.valueOf(loginLocation.getLatitude()),Double.valueOf(loginLocation.getLongtitude()));
                                        markerOptions2.position(latLng);
                                        markerOptions2.title( loginLocation.getLaborerName());
                                        markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.personnel_icon));
                                        googleMap.addMarker(markerOptions2);
                                        loginLocations.add(loginLocation);
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { /*DO NOTHING*/}
                        });



                         googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {

                                        if (marker.getTitle() != "My Location") {

                                            LoginLocation l =  FindLocationViaLaborer(marker.getTitle(), loginLocations);
                                            if(l != null ) {

                                                String addressfromDB = l.getAddress();
                                                String mobilefromDB =  l.getMobileNumber();
                                                String namefromDB = l.getLaborerName();

                                                Toast.makeText(getActivity(), marker.getTitle() + " has been selected", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getActivity(), ServiceProviderProfile.class);
                                                intent.putExtra("StreetAddress",addressfromDB);
                                                intent.putExtra("Service", service );
                                                intent.putExtra("FullName", namefromDB);
                                                intent.putExtra("MobileNumber", mobilefromDB);
                                                startActivity(intent);

                                            }

                                        }

                                        return false;
                                    }
                         });

//               googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(LatLng latLng) {
//                        MarkerOptions markerOptions = new MarkerOptions();
//                        markerOptions.position(latLng);
//                        markerOptions.title(latLng.latitude+ ":"+latLng.longitude);
//                        googleMap.clear();
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
//                        googleMap.addMarker(markerOptions);
//                    }
//               });
            }
        });

        return root;
    }

    public LoginLocation FindLocationViaLaborer(
            String name, List<LoginLocation> loginLocations) {

        for (LoginLocation l : loginLocations) {
            if (l.getLaborerName().equals(name)) {
                return l;
            }
        }
        return null;
    }
}