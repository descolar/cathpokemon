package com.descolar.catchpokemon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurar el idioma antes de cargar el contenido
        setAppLanguage();

        setContentView(R.layout.activity_splash);

        // Temporizador para mostrar la splash screen durante 2 segundos
        new Handler().postDelayed(() -> {
            // Inicia MainActivity después de 2 segundos
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cierra la SplashActivity
        }, 2000); // 2000 milisegundos = 2 segundos
    }

    private void setAppLanguage() {
        // Cargar el idioma desde SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String languageCode = prefs.getString("My_Lang", Locale.getDefault().getLanguage());

        // Configurar el idioma seleccionado
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
