package com.descolar.catchpokemon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {

    private UserPreferences userPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Inicializar preferencias de usuario
        userPreferences = new UserPreferences(requireContext());

        // Configuración del idioma
        ListPreference languagePreference = findPreference("idioma");
        if (languagePreference != null) {
            languagePreference.setValue(userPreferences.getLanguage());
            languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String selectedLanguage = newValue.toString();
                userPreferences.setLanguage(selectedLanguage);
                applyLanguageChange(selectedLanguage);
                return true;
            });
        }

        // Configuración del Switch de eliminación
        SwitchPreferenceCompat eliminarTodosPokemonPreference = findPreference("eliminar_todos_pokemon");
        if (eliminarTodosPokemonPreference != null) {
            eliminarTodosPokemonPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean isEnabled = (Boolean) newValue;

                // Guardar el estado del Switch en SharedPreferences
                SharedPreferences.Editor editor = requireContext()
                        .getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
                        .edit();
                editor.putBoolean("eliminar_habilitado", isEnabled);
                editor.apply();

                return true;
            });
        }

        // Configuración de la opción "Acerca de"
        Preference aboutPreference = findPreference("about");
        if (aboutPreference != null) {
            aboutPreference.setOnPreferenceClickListener(preference -> {
                showAboutDialog();
                return true;
            });
        }

        // Configuración de la opción "Cerrar sesión"
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
                            Toast.makeText(getContext(), R.string.men_cerrar_sesion, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            });
        }
    }

    private void applyLanguageChange(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        requireActivity().recreate();
    }

    private void showAboutDialog() {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle(R.string.menu_acerca_de)
                .setIcon(R.drawable.ic_settings)
                .setMessage(R.string.mensaje_acerca)
                .setPositiveButton("OK", null)
                .show();
    }

    private void signOut() {
        GoogleSignIn.getClient(getContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut()
                .addOnCompleteListener(requireActivity(), task -> {
                    Toast.makeText(getContext(), R.string.men_cerrar_sesion, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    requireActivity().finish();
                });
    }
}
