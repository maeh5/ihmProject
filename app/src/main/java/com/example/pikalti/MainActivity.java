package com.example.pikalti;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import com.example.pikalti.BicycleModeActivity;


import com.example.pikalti.lib.bikeModeDetection.LocationMonitor;
import com.example.pikalti.lib.bikeModeDetection.Utilities;
import com.example.pikalti.lib.grelib.ClientReferenceMode;
import com.example.pikalti.lib.grelib.GestureRecognitionClient;
import com.example.pikalti.lib.grelib.GestureRecognitionResponseListener;
import com.example.pikalti.lib.grelib.RemoteGestureRecognitionClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pikalti.ui.main.SectionsPagerAdapter;
import com.example.pikalti.databinding.ActivityMainBinding;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_INTERNET = 0;
    private ActivityMainBinding binding;
    private GestureRecognitionClient gestureRecognitionClient;
    private Vibrator vibrator;
    private LocationMonitor locationMonitor;
    private TextView speedTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get a new location monitor object once we set it up.
        this.locationMonitor = this.setupLocationMonitor();

        // Get the speed text view reference.
        this.speedTextView = findViewById(R.id.speedText);

        // Set the speed text as zero.
        this.speedTextView.setText(Utilities.formatSpeed(this, 0));

        // Check if the user has the granted location usage permission.
        this.checkLocationPermission();
        enable();



        gestureRecognitionClient = new RemoteGestureRecognitionClient(this);
        gestureRecognitionClient.setReferenceMode(ClientReferenceMode.DEVICE_REFERENCE);//change this to USER_FACING if needed.
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        gestureRecognitionClient.connect("wss://sdk.motiongestures.com/recognition?api_key=rJwpH669ntncdi12KOoJrgiDqWgo5fJVUihM3IZdWKdGOvbKtT");

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, BicycleModeActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });



        gestureRecognitionClient.setGestureRecognitionResponseListener(new GestureRecognitionResponseListener() {
            @Override
            public void gesturesRecognized(final List<String> names, final List<Integer> labels, float confidence) {
                vibrate();
                runOnUiThread(() -> {
                    int size = Math.min(names.size(), labels.size());
                    for (int i = 0; i < size; i++) {
//                        gesturesListAdapter.add("Recognized gesture " + names.get(i) + " with label " + labels.get(i));
                        executeAction(labels.get(i));

                    }
                });
            }

            @Override
            public void gesturesRejected(final List<String> names, final List<Integer> labels, float confidence) {
                vibrate();
                runOnUiThread(() -> {
                    int size = Math.min(names.size(), labels.size());
                    for (int i = 0; i < size; i++) {
//                        gesturesListAdapter.add("Rejected gesture " + names.get(i) + " with label " + labels.get(i));
                    }
                });
            }

            @Override
            public void gestureTooLong() {
                vibrate();
//                gesturesListAdapter.add("Gesture Too Long");
            }

        });
    }

    private void executeAction(Integer gestureLabel) {

        switch (gestureLabel) {
            case 0:
                Intent myIntent = new Intent(MainActivity.this, BicycleModeActivity.class);
                MainActivity.this.startActivity(myIntent);
                break;
            default:
                System.out.println("khrejt");
        }
    }


    @Override
    protected void onPause() {
        gestureRecognitionClient.pause();
        super.onPause();
        this.disable();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        gestureRecognitionClient.resume();
        super.onResume();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_INTERNET) {
        }

        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            // If fine location permission was denied, then disable
            // the speedometer button and show a message.

            showNoPermissionDialog();
        }
    }

    public void vibrate() {
        //Runnable runnable = () -> {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(100);
            }
       /* };
        Thread mythread = new Thread(runnable);
        mythread.start();*/

    }

    private LocationMonitor setupLocationMonitor() {
        return new LocationMonitor(this, new LocationMonitor.CustomCallback() {
            @Override
            public void onLocationReceived(LocationResult result) {
                // When a new location is received, handle speed accordingly.

                Location location = result.getLastLocation();

                float speed = location.getSpeed();
                float kmSpeed = Utilities.speedToKm(speed);

                // Update the speed text to the current km/h speed.
                speedTextView.setText(
                        Utilities.formatSpeed(getApplicationContext(), kmSpeed)
                );
            }

            @Override
            public void onLocationDisabled() {
                // When the location becomes unavailable then stop and show a message.

                showNoLocationDialog();
                disable();
            }
        });
    }

    private void enable() {
        this.locationMonitor.start(
                () -> {

                },
                () -> {
                    showNoLocationDialog();
                });
    }

    private void disable() {
        this.locationMonitor.stop(
                () -> {

                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // When speech to text sends us a new result, iterate through the parsed speech text
            // and decide if one of the options can be mapped to an action.


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkLocationPermission() {
        // Check for fine location permission and request if needed.

        boolean hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }

    private void showNoLocationDialog() {
        // The dialog shown when the user has turned off the device location.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.no_location);

        builder.show();
    }

    private void showNoPermissionDialog() {
        // The dialog shown when the user hasn't granted the required permissions.

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.no_permission);

        builder.show();
    }
}


