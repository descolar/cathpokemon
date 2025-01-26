package com.descolar.catchpokemon;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Clase que gestiona la configuración de Retrofit para realizar solicitudes HTTP a la API de Pokémon.
 * Utiliza el patrón Singleton para garantizar que solo haya una instancia de Retrofit.
 */
public class RetrofitClient {

    // URL base de la API de Pokémon
    private static final String BASE_URL = "https://pokeapi.co/api/v2/";

    // Instancia única de Retrofit
    private static Retrofit retrofit;

    /**
     * Devuelve la instancia de Retrofit.
     * Si no existe una instancia, se crea con la configuración inicial.
     *
     * @return Instancia de Retrofit configurada.
     */
    public static Retrofit getInstance() {
        if (retrofit == null) {
            // Creamos la instancia de Retrofit si aún no existe
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Configuramos la URL base de la API
                    .addConverterFactory(GsonConverterFactory.create()) // Usamos Gson para convertir JSON
                    .build();
        }
        return retrofit;
    }
}
