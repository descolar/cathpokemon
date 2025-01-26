package com.descolar.catchpokemon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {
    private UserPreferences userPreferences;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Cargar las preferencias desde el XML
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // Inicializar preferencias de usuario
        userPreferences = new UserPreferences(requireContext());
        // Configuración del idioma
        ListPreference languagePreference = findPreference("idioma");
        if (languagePreference != null) {
            // Establecer el idioma actual como valor seleccionado
            languagePreference.setValue(userPreferences.getLanguage());

            languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                String selectedLanguage = newValue.toString();

                // Guardar la preferencia y aplicar el idioma
                userPreferences.setLanguage(selectedLanguage);
                applyLanguageChange(selectedLanguage);

                return true; // Permitir el cambio
            });
        }
        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

                // Configuración de la opción "Eliminar Pokémon"
        SwitchPreferenceCompat eliminarTodosPokemonPreference = findPreference("eliminar_todos_pokemon");
        if (eliminarTodosPokemonPreference != null) {
            eliminarTodosPokemonPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean eliminarTodosPokemon = (Boolean) newValue;
                if (eliminarTodosPokemon) {
                    new android.app.AlertDialog.Builder(getContext())
                            .setTitle(R.string.eliminarPokemon)
                            .setMessage(R.string.eliminarPokemon)
                            .setIcon(R.drawable.ic_trash)
                            .setPositiveButton("Sí", (dialog, which) -> {
                                eliminarTodosLosPokemon();
                            }).setNegativeButton("No", (dialog, which) -> {
                                eliminarTodosPokemonPreference.setChecked(false);
                            }).show();
                }
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
                new android.app.AlertDialog.Builder(getContext()).setTitle(R.string.logout)
                        .setMessage(R.string.pregunta_cerrar_sesion)
                        .setIcon(R.drawable.ic_logout)
                        .setPositiveButton("Si", (dialog, which) -> {
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

    // Función para cambiar el idioma
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Guardar preferencia de idioma
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();

        // Reiniciar actividad para aplicar el cambio de idioma
        getActivity().recreate();
    }
    private void applyLanguageChange(String languageCode) {
        // Cambiar el idioma de la aplicación
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Reiniciar la actividad principal para aplicar los cambios
        Toast.makeText(getContext(), R.string.cambio_idioma_aplicado, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
    // Función para eliminar todos los Pokémon de Firestore
    private void eliminarTodosLosPokemon() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("capturados")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                        Toast.makeText(getContext(), R.string.mensaje_eliminados, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.error_eliminar_pokemon, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Mostrar el diálogo "Acerca de"
    private void showAboutDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.menu_acerca_de)
                .setIcon(R.drawable.ic_settings)
                .setMessage(R.string.mensaje_acerca)
                .setPositiveButton("OK", null).show();
    }

    // Cerrar sesión de Google
    private void signOut() {
        // Cerrar sesión en Google
        GoogleSignIn.getClient(getContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut()
                .addOnCompleteListener(getActivity(), task -> {
                    Toast.makeText(getContext(), R.string.men_cerrar_sesion, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                });
    }
}
