package com.example.pikalti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;

    private Button ttsButton;
    private Button vibrationButton;
    private Button exitButton;

    private int ttsFlag;
    private int vibrationFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        ttsFlag = pref.getInt("ttsFlag", 1);
        vibrationFlag = pref.getInt("vibrationFlag", 1);
        System.out.println("hello" + ttsFlag + vibrationFlag);

        ttsButton = (Button) findViewById(R.id.toggleTTS);
        vibrationButton = (Button) findViewById(R.id.toggleVibration);
        exitButton = (Button) findViewById(R.id.exitButton);

        if (ttsFlag == 0) {
            ttsButton.setText("Activer");
        } else {
            ttsButton.setText("DESACTIVER");
        }
        if (vibrationFlag == 0) {
            vibrationButton.setText("Activer");
        } else {
            vibrationButton.setText("DESACTIVER");
        }



        ttsButton.setOnClickListener(v -> {
            if (ttsFlag == 0) {
                ttsFlag = 1; // 1 => Button ON
                editor.putInt("ttsFlag",1);
                editor.commit();
                ttsButton.setText("DESACTIVER");
            } else {
                ttsFlag = 0; // 0 => Button OFF
                editor.putInt("ttsFlag",0);
                editor.commit();
                ttsButton.setText("Activer");
            }
        });

        vibrationButton.setOnClickListener(v -> {
            if (vibrationFlag == 0) {
                vibrationFlag = 1; // 1 => Button ON
                editor.putInt("vibrationFlag",1);
                editor.commit();
                vibrationButton.setText("DESACTIVER");
            } else {
                vibrationFlag = 0; // 0 => Button OFF
                editor.putInt("vibrationFlag",0);
                editor.commit();
                vibrationButton.setText("Activer");
            }
        });

        exitButton.setOnClickListener(v -> {
            Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SettingsActivity.this.startActivity(myIntent);
        });
    }
}