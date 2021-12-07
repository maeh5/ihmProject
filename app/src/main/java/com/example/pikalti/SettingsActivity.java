package com.example.pikalti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import com.example.pikalti.databinding.ActivitySettingsBinding;
import android.speech.tts.TextToSpeech;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class SettingsActivity extends AppCompatActivity {
    
    private @NonNull ActivitySettingsBinding binding;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String BoolTTS = "TTS";
    public static final String BoolVIB = "VIB";
    private int ttsIsClicked = 0;
    private int vibIsClicked = 0;
    private int texte = 0;
    private int texte2 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        Button btn = binding.button;
       // final Button btn1 = findViewById(R.id.button1);
       // Button btn1 = (Button) findViewById(R.id.button1);  //retour audio
        Button btn1 = binding.button1;
        Button btn2 = binding.button2; //vibration

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                SettingsActivity.this.startActivity(myIntent);
                System.out.println(btn1.getText());
            }
        });
        System.out.println(btn1.getText());
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(btn1.getText());
                switch(texte){
                    case 0:
                        btn1.setText("Désactiver");
                        texte = 1;
                        break;
                    case 1:
                        btn1.setText("Activer");
                        texte = 0;
                        break;
                }
                if (ttsIsClicked==1){
                    ttsIsClicked=0;
                }
                else if (ttsIsClicked==0){
                    ttsIsClicked=1;
                }
                if (ttsIsClicked==1){
                    editor.putInt(BoolTTS, 1);
                }
                else if (ttsIsClicked==0){
                    editor.putInt(BoolTTS, 0);
                }
                editor.commit();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(texte2){
                    case 0:
                        btn2.setText("Désactiver");
                        texte = 1;
                        break;
                    case 1:
                        btn2.setText("Activer");
                        texte = 0;
                        break;
                }
                if (vibIsClicked==1){
                    vibIsClicked=0;
                }
                else if (vibIsClicked==0){
                    vibIsClicked=1;
                }
                if (vibIsClicked==1){
                    editor.putInt(BoolVIB, 1);
                }
                else if (vibIsClicked==0){
                    editor.putInt(BoolVIB, 0);
                }
                editor.commit();
            }
        });
    }
}