package com.descolar.catchpokemon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Locale;

/**
 * Clase SplashActivity
 * Esta clase muestra una pantalla de inicio con un temporizador y redirige al usuario a
 * la actividad correspondiente según el estado de autenticación.
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * Método onCreate
     * Configura la pantalla de inicio y determina si el usuario debe dirigirse
     * a LoginActivity o MainActivity en función de su estado de autenticación.
     *
     * @param savedInstanceState Estado previamente guardado de la actividad.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuramos el idioma antes de cargar el contenido
        setAppLanguage();

        // Establecemos el diseño de la pantalla Splash
        setContentView(R.layout.activity_splash);

        // Mostramos la pantalla de carga durante 2 segundos antes de redirigir
        new Handler().postDelayed(() -> {
            // Verificamos si el usuario está autenticado
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = auth.getCurrentUser();

            Intent intent;
            if (currentUser != null) {
                // Usuario autenticado, redirigir a MainActivity
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // Usuario no autenticado, redirigir a LoginActivity
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish(); // Cerramos la SplashActivity
        }, 2000); // 2000 milisegundos = 2 segundos
    }

    /**
     * Configura el idioma de la aplicación según las preferencias del usuario.
     * Este método asegura que la interfaz se muestre en el idioma seleccionado.
     */
    private void setAppLanguage() {
        // Cargamos el idioma guardado desde las preferencias
        String languageCode = getSharedPreferences("user_preferences", MODE_PRIVATE)
                .getString("language", "es"); // Idioma predeterminado: español

        // Configuramos el idioma seleccionado
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
