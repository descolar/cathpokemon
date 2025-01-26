package com.descolar.catchpokemon;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase que gestiona las preferencias del usuario, como el idioma seleccionado.
 * Permite guardar y recuperar las configuraciones de manera persistente.
 */
public class UserPreferences {

    // Nombre del archivo de preferencias
    private static final String PREFERENCES_FILE = "user_preferences";

    // Clave para guardar el idioma
    private static final String KEY_LANGUAGE = "language";

    // Instancia de SharedPreferences
    private final SharedPreferences sharedPreferences;

    /**
     * Constructor que inicializa el archivo de preferencias del usuario.
     *
     * @param context Contexto de la aplicación o actividad.
     */
    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    /**
     * Guarda el idioma seleccionado por el usuario en las preferencias.
     *
     * @param language Código del idioma a guardar (por ejemplo, "es" o "en").
     */
    public void setLanguage(String language) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LANGUAGE, language);
        editor.apply(); // Aplicamos los cambios
    }

    /**
     * Obtiene el idioma guardado en las preferencias.
     * Si no se encuentra ningún idioma guardado, devuelve "es" como predeterminado.
     *
     * @return Código del idioma guardado (por ejemplo, "es" o "en").
     */
    public String getLanguage() {
        return sharedPreferences.getString(KEY_LANGUAGE, "es");
    }
}
