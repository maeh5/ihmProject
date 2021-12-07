package com.example.pikalti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.example.pikalti.lib.grelib.ClientReferenceMode;
import com.example.pikalti.lib.grelib.GestureRecognitionClient;
import com.example.pikalti.lib.grelib.GestureRecognitionResponseListener;
import com.example.pikalti.lib.grelib.RemoteGestureRecognitionClient;

import java.util.List;
import java.util.Locale;


public class BicycleModeActivity extends AppCompatActivity {


    private static final int REQUEST_INTERNET = 0;
    private ToggleButton toggleButton = null;
    private Vibrator vibrator;
    private int menu = 0;
    private int menuItem = 0;
    private boolean firstTimeInMenu = true;
    private TextToSpeech t1;
    private GestureRecognitionClient gestureRecognitionClient;

    private Button ttsButton;
    private Button vibrationButton;
    private int ttsFlag = 1;
    private int vibrationFlag = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bicycle_mode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gestureRecognitionClient = new RemoteGestureRecognitionClient(this);
        gestureRecognitionClient.setReferenceMode(ClientReferenceMode.DEVICE_REFERENCE);//change this to USER_FACING if needed.
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        gestureRecognitionClient.connect("wss://sdk.motiongestures.com/recognition?api_key=rJwpH669ntncdi12KOoJrgiDqWgo5fJVUihM3IZdWKdGOvbKtT");


        ttsButton = (Button) findViewById(R.id.toggleTTS);
        ttsButton.setOnClickListener(v -> {
            if (ttsFlag == 0) {
                ttsFlag = 1; // 1 => Button ON
                ttsButton.setBackgroundResource(R.drawable.unmute);
            } else {
                ttsFlag = 0; // 0 => Button OFF
                ttsButton.setBackgroundResource(R.drawable.mute);
            }
        });

        vibrationButton = (Button) findViewById(R.id.toggleVibration);
        vibrationButton.setOnClickListener(v -> {
            if (vibrationFlag == 0) {
                vibrationFlag = 1; // 1 => Button ON
                vibrationButton.setBackgroundResource(R.drawable.vibration);
            } else {
                vibrationFlag = 0; // 0 => Button OFF
                vibrationButton.setBackgroundResource(R.drawable.unvibration);
            }
        });


        t1 = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                t1.setLanguage(Locale.UK);
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
            case 2: //limmen chnage menue item
                menuItem = (Data.countMenuTitles[menu] - 1 == menuItem) ? 0 : menuItem + 1;
                menuItem = firstTimeInMenu ? 0 : menuItem;
                firstTimeInMenu = false;
                System.out.println(Data.countMenuTitles[menu]);
                System.out.println(menuItem);
                System.out.println(Data.menues[menu][menuItem]);
                textToSpeach(Data.menues[menu][menuItem]);
                break;

            case 3: //lisser chnage menue item
                menuItem = (menuItem == 0) ? Data.countMenuTitles[menu] - 1 : menuItem - 1;
                menuItem = firstTimeInMenu ? 0 : menuItem;
                firstTimeInMenu = false;
                System.out.println(menuItem);
                System.out.println(Data.menues[menu][menuItem]);
                textToSpeach(Data.menues[menu][menuItem]);
                break;

            case 4: //lisser chnage menu
                menu = (menu == 0) ? 2 : menu - 1;
                menuItem = 0;
                firstTimeInMenu = true;
                System.out.println(Data.menuTitles[menu]);
                textToSpeach(Data.menuTitles[menu]);
                break;
            case 5: //limen chnage menu
                menu = (menu == 2) ? 0 : menu + 1;
                menuItem = 0;
                firstTimeInMenu = true;
                System.out.println(Data.menuTitles[menu]);
                textToSpeach(Data.menuTitles[menu]);
                break;

            case 1:
                textToSpeach(Data.menuAction[menu] + Data.menues[menu][menuItem]);
                firstTimeInMenu = false;
                break;

            case 0:
                textToSpeach("Bike mode disabled");
                Intent myIntent = new Intent(BicycleModeActivity.this, MainActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                BicycleModeActivity.this.startActivity(myIntent);
                break;
            default:
                System.out.println("khrejt");
        }
    }


    @Override
    protected void onPause() {
        gestureRecognitionClient.pause();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }

        super.onPause();
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
            toggleButton.setEnabled(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }

    public void vibrate() {
        if (vibrationFlag == 1) {
            Runnable runnable = () -> {
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(100);
                }
            };
            Thread mythread = new Thread(runnable);
            mythread.start();
        }

    }

    public void textToSpeach(String tts) {
        if (ttsFlag == 1) t1.speak(tts, TextToSpeech.QUEUE_FLUSH, null);
    }


}
