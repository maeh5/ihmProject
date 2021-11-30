package com.example.pikalti;

import static android.Manifest.permission.INTERNET;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.example.pikalti.lib.grelib.ClientReferenceMode;
import com.example.pikalti.lib.grelib.GestureRecognitionClient;
import com.example.pikalti.lib.grelib.GestureRecognitionResponseListener;
import com.example.pikalti.lib.grelib.RemoteGestureRecognitionClient;

import java.util.List;


public class BicycleModeActivity extends AppCompatActivity {


    private static final String TAG = "BicycleModeActivity";
    private static final int REQUEST_INTERNET = 0;
    private ArrayAdapter<String> gesturesListAdapter = null;
    private ListView recognizedGesturesList;
    private ToggleButton toggleButton = null;
    private Vibrator vibrator;
    private int menu = 0;
    private int menuItem = 0;

    private GestureRecognitionClient gestureRecognitionClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_mode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gestureRecognitionClient = new RemoteGestureRecognitionClient(this);
        gestureRecognitionClient.setReferenceMode(ClientReferenceMode.DEVICE_REFERENCE);//change this to USER_FACING if needed.
        gesturesListAdapter = new ArrayAdapter<>(this,R.layout.gesture_item);
        recognizedGesturesList = findViewById(R.id.recognizedGesturesList);
        recognizedGesturesList.setAdapter(gesturesListAdapter);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        gestureRecognitionClient.connect("wss://sdk.motiongestures.com/recognition?api_key=rJwpH669ntncdi12KOoJrgiDqWgo5fJVUihM3IZdWKdGOvbKtT");


        gestureRecognitionClient.setGestureRecognitionResponseListener(new GestureRecognitionResponseListener() {
            @Override
            public void gesturesRecognized(final List<String> names, final List<Integer> labels, float confidence) {
                vibrate();
                runOnUiThread(() -> {
                    int size = Math.min(names.size(),labels.size());
                    for(int i =0;i<size;i++) {
                        gesturesListAdapter.add("Recognized gesture " + names.get(i) + " with label " + labels.get(i));
                        executeAction(labels.get(i));

                    }
                });
            }

            @Override
            public void gesturesRejected(final List<String> names, final List<Integer> labels, float confidence) {
                vibrate();
                runOnUiThread(() -> {
                    int size = Math.min(names.size(),labels.size());
                    for(int i =0;i<size;i++) {
                        gesturesListAdapter.add("Rejected gesture " + names.get(i) + " with label " + labels.get(i));
                    }
                });
            }

            @Override
            public void gestureTooLong() {
                vibrate();
                gesturesListAdapter.add("Gesture Too Long");
            }

        });
    }

    private void executeAction(Integer gestureLabel) {

        switch (gestureLabel){
            case 4:
                menu = (menu == 0) ? 2 : menu-1;

                System.out.println(Data.menueTitles[menu]);
                break;
            case 5:
                menu = (menu == 2) ? 0 : menu+1;

                System.out.println(Data.menueTitles[menu]);
                break;
            default:
                System.out.println("khrejt");
        }
    }


    @Override
    protected void onPause() {
        gestureRecognitionClient.pause();
        toggleButton.setChecked(false);

        super.onPause();
    }

    @Override
    protected void onResume() {
        gestureRecognitionClient.resume();
        super.onResume();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_INTERNET) {
            toggleButton.setEnabled(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }

    public void vibrate(){

        Runnable runnable = () -> {
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            }else {
                vibrator.vibrate(100);
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }



}
