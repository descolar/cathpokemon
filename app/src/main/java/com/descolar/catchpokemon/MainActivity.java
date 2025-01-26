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

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserPreferences userPreferences = new UserPreferences(this);

        // Aplicar el idioma guardado
        setAppLanguage(userPreferences.getLanguage());

        setContentView(R.layout.activity_main);

        // Configurar el NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Configurar el BottomNavigationView con el NavController
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    public void onPokemonCaptured(Pokemon pokemon) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            Fragment fragment = navHostFragment.getChildFragmentManager()
                    .findFragmentById(R.id.nav_capturedPokemonsFragment);
            if (fragment instanceof CapturedPokemonsFragment) {
                ((CapturedPokemonsFragment) fragment).addCapturedPokemon(pokemon);
            }
        }
    }

    private void setAppLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

}
