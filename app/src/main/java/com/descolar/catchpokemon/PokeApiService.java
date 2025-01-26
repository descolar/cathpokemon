package com.descolar.catchpokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interfaz para definir los endpoints de la API de Pokémon.
 * Utilizamos Retrofit para realizar las solicitudes HTTP.
 */
public interface PokeApiService {

    /**
     * Obtiene una lista de Pokémon desde la API.
     *
     * @param offset Punto de inicio en la lista de Pokémon (paginación).
     * @param limit  Número máximo de Pokémon a obtener.
     * @return Llamada que devuelve un objeto PokeApiResponse con la lista de Pokémon.
     */
    @GET("pokemon")
    Call<PokeApiResponse> getPokemonList(
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    /**
     * Obtiene los detalles de un Pokémon específico desde la API.
     *
     * @param id ID o índice del Pokémon en la Pokédex.
     * @return Llamada que devuelve los detalles del Pokémon en formato genérico (Object).
     */
    @GET("pokemon/{id}")
    Call<Object> getPokemonDetails(@Path("id") int id);
}
