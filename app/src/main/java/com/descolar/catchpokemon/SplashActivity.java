package com.descolar.catchpokemon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private TextView loadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Inicializamos el TextView del mensaje de carga
        loadingText = findViewById(R.id.loading_text);

        // Verificar el idioma seleccionado
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isEnglish = prefs.getBoolean("isEnglish", false); // Default to Spanish if not set

        // Cambiar el texto según el idioma
        if (isEnglish) {
            loadingText.setText("Loading...");
        } else {
            loadingText.setText("Cargando...");
        }

        // Temporizador para mostrar la splash screen durante 2 segundos
        new Handler().postDelayed(() -> {
            // Inicia MainActivity después de 2 segundos
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cierra la SplashActivity
        }, 2000); // 2000 milisegundos = 2 segundos
    }

    // Método para cambiar el idioma dinámicamente
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
