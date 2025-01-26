package com.descolar.catchpokemon;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

/**
 * Actividad principal de la aplicación.
 * Gestiona la navegación entre los fragmentos principales: Pokédex, Pokémon Capturados y Ajustes.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtenemos las preferencias del usuario
        UserPreferences userPreferences = new UserPreferences(this);

        // Aplicamos el idioma guardado en las preferencias
        setAppLanguage(userPreferences.getLanguage());

        // Configuramos el layout principal
        setContentView(R.layout.activity_main);

        // Configuramos el NavHostFragment para manejar la navegación
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Configuramos el BottomNavigationView para sincronizarlo con la navegación
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    /**
     * Método para manejar la captura de un Pokémon.
     * Añade el Pokémon capturado al fragmento de Pokémon Capturados.
     *
     * @param pokemon El Pokémon que se ha capturado.
     */
    public void onPokemonCaptured(Pokemon pokemon) {
        // Obtenemos el NavHostFragment actual
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            // Buscamos el fragmento de Pokémon Capturados
            Fragment fragment = navHostFragment.getChildFragmentManager()
                    .findFragmentById(R.id.nav_capturedPokemonsFragment);

            // Verificamos que el fragmento sea del tipo CapturedPokemonsFragment
            if (fragment instanceof CapturedPokemonsFragment) {
                // Añadimos el Pokémon capturado a la lista
                ((CapturedPokemonsFragment) fragment).addCapturedPokemon(pokemon);
            }
        }
    }

    /**
     * Configura el idioma de la aplicación según el código de idioma proporcionado.
     *
     * @param languageCode Código de idioma ("en" para inglés y "es" para español).
     */
    private void setAppLanguage(String languageCode) {
        // Configuramos el idioma seleccionado
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        // Actualizamos la configuración del sistema
        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }
}
