package com.example.allinoneapp.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.allinoneapp.LoginLocation;
import com.example.allinoneapp.MainActivity;
import com.example.allinoneapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static com.example.allinoneapp.MainActivity.database;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = usernameEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                String URL ="http://3.143.42.93/api/Login/GetUserEmailDetail/email?emailAdd="+email;
                RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        URL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    JSONObject obj = new JSONObject(response.toString());
                                    final String emailfromDB = obj.getString("EmailAddress");
                                    final String firstNamefromDB = obj.getString("FirstName");
                                    final String lastNamefromDB  = obj.getString("LastName");
                                    final int usertypefromDB = obj.getInt("UserTypeID");
                                    final String mobilefromDB = String.valueOf( obj.getInt("MobileNumber"));

                                    MainActivity.UserLoginID = String.valueOf(obj.getInt("LoginID"));

                                    if (emailfromDB.equals(email)){

                                        String currentTime = Calendar.getInstance().getTime().toString();

                                        if (usertypefromDB>1) { // if User is a Laborer

                                            // Log gps coordinates to realtime db (firebase)
                                            DatabaseReference myRef = database.getReference("LoginLocation/" + MainActivity.UserLoginID);

                                            LoginLocation location1 = new LoginLocation(MainActivity.UserLoginID,
                                                    MainActivity.gps_longitude, MainActivity.gps_latitude,
                                                    currentTime, MainActivity.gps_address, firstNamefromDB+' '+lastNamefromDB,
                                                    mobilefromDB);
                                            myRef.setValue(location1);
                                        }

                                        // get realtime data
                                        MainActivity.databaseReference = database.getInstance().getReference("LoginLocation");
                                        MainActivity.databaseReference.child(MainActivity.UserLoginID).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                //LoginLocation loginLocation = snapshot.getValue(LoginLocation.class);
                                                //Toast.makeText(getApplicationContext(),loginLocation.getAddress() , Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                // Failed to read value
                                                Log.e("Realtime data error", "Failed to read value.", error.toException());
                                            }
                                        });


                                        Toast.makeText(getApplicationContext(), "Welcome "+
                                                firstNamefromDB, Toast.LENGTH_SHORT).show();

                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Incorrect Email Address / Password.",
                                                                                  Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("error api: ",error.toString());
                            }
                        });

                requestQueue.add(objectRequest);
            }
        });
    }


}