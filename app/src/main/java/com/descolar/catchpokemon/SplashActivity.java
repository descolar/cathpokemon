package com.descolar.catchpokemon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

/**
 * Actividad de inicio que muestra una pantalla de bienvenida (splash screen)
 * y configura el idioma de la aplicación antes de cargar el contenido principal.
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * Método que se ejecuta al crear la actividad.
     * Configura el idioma y muestra la pantalla de bienvenida.
     *
     * @param savedInstanceState Estado guardado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuramos el idioma antes de cargar el contenido
        setAppLanguage();

        setContentView(R.layout.activity_splash);

        // Mostramos la splash screen durante 2 segundos
        new Handler().postDelayed(() -> {
            // Iniciamos MainActivity después del temporizador
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cerramos la SplashActivity
        }, 2000); // 2000 milisegundos = 2 segundos
    }

    /**
     * Configura el idioma de la aplicación basado en las preferencias del usuario.
     */
    private void setAppLanguage() {
        // Obtenemos el idioma guardado en las preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String languageCode = prefs.getString("My_Lang", Locale.getDefault().getLanguage());

        // Configuramos el idioma seleccionado
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);

        // Actualizamos las configuraciones de recursos con el idioma seleccionado
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
