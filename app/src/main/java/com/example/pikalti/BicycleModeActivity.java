package com.example.pikalti;

import static android.Manifest.permission.INTERNET;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.List;


public class BicycleModeActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_INTERNET = 0;
    private ArrayAdapter<String> gesturesListAdapter = null;
    private ListView recognizedGesturesList;
    private ToggleButton toggleButton = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



}
