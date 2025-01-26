package com.descolar.catchpokemon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

/**
 * Fragmento de ajustes de la aplicación.
 * Permite cambiar el idioma, gestionar preferencias y cerrar sesión.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private UserPreferences userPreferences;

    /**
     * Método que se llama al crear las preferencias del fragmento.
     *
     * @param savedInstanceState Estado guardado del fragmento.
     * @param rootKey            Clave raíz del archivo de preferencias.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Inicializamos las preferencias del usuario
        userPreferences = new UserPreferences(requireContext());

        // Configuramos el idioma de la aplicación
        configureLanguagePreference();

        // Configuramos el Switch para habilitar o deshabilitar la eliminación de Pokémon
        configureDeleteSwitch();

        // Configuramos la opción "Acerca de"
        configureAboutPreference();

        // Configuramos la opción "Cerrar sesión"
        configureLogoutPreference();
    }

    /**
     * Configura la preferencia para cambiar el idioma de la aplicación.
     */
    private void configureLanguagePreference() {
        ListPreference languagePreference = findPreference("idioma");
        if (languagePreference != null) {
            // Establecemos el idioma actual como valor predeterminado
            languagePreference.setValue(userPreferences.getLanguage());

            // Configuramos el cambio de idioma
            languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String selectedLanguage = newValue.toString();
                userPreferences.setLanguage(selectedLanguage);
                applyLanguageChange(selectedLanguage);
                return true;
            });
        }
    }

    /**
     * Configura el Switch para habilitar o deshabilitar la eliminación de Pokémon.
     */
    private void configureDeleteSwitch() {
        SwitchPreferenceCompat deleteSwitch = findPreference("eliminar_todos_pokemon");
        if (deleteSwitch != null) {
            deleteSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isEnabled = (Boolean) newValue;

                // Guardamos el estado del Switch en las preferencias
                SharedPreferences.Editor editor = requireContext()
                        .getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
                        .edit();
                editor.putBoolean("eliminar_habilitado", isEnabled);
                editor.apply();

                return true;
            });
        }
    }

    /**
     * Configura la opción "Acerca de" en el fragmento de ajustes.
     */
    private void configureAboutPreference() {
        Preference aboutPreference = findPreference("about");
        if (aboutPreference != null) {
            aboutPreference.setOnPreferenceClickListener(preference -> {
                showAboutDialog();
                return true;
            });
        }
    }

    /**
     * Configura la opción "Cerrar sesión" en el fragmento de ajustes.
     */
    private void configureLogoutPreference() {
        Preference logoutPreference = findPreference("logout");
        if (logoutPreference != null) {
            logoutPreference.setOnPreferenceClickListener(preference -> {
                new android.app.AlertDialog.Builder(getContext())
                        .setTitle(R.string.logout)
                        .setMessage(R.string.pregunta_cerrar_sesion)
                        .setIcon(R.drawable.ic_logout)
                        .setPositiveButton("Sí", (dialog, which) -> {
                            FirebaseAuth.getInstance().signOut();   // Cerrar sesión en Firebase
                            signOut();   // Cerrar sesión en Google
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            });
        }
    }

    /**
     * Aplica el cambio de idioma a la aplicación.
     *
     * @param languageCode Código del idioma seleccionado.
     */
    private void applyLanguageChange(String languageCode) {
        // Configuramos el idioma seleccionado
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        // Actualizamos las configuraciones globales de la aplicación
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Guardamos el idioma seleccionado en las preferencias globales
        SharedPreferences.Editor editor = requireContext()
                .getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
                .edit();
        editor.putString("language", languageCode);
        editor.apply();

        // Reiniciamos la actividad principal para aplicar los cambios
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Muestra el diálogo "Acerca de".
     */
    private void showAboutDialog() {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle(R.string.menu_acerca_de)
                .setIcon(R.drawable.ic_settings)
                .setMessage(R.string.about_message)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Cierra la sesión de Google y redirige al usuario al Login.
     */
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        GoogleSignIn.getClient(
                requireContext(),
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        ).signOut().addOnCompleteListener(task -> {
            Toast.makeText(getContext(), R.string.men_cerrar_sesion, Toast.LENGTH_SHORT).show();
            // Redirigir a LoginActivity
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
    }

}
