package com.descolar.catchpokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Path;

public interface PokeApiService {
    @GET("pokemon")
    Call<PokeApiResponse> getPokemonList(
            @Query("offset") int offset,
            @Query("limit") int limit
    );

    @GET("pokemon/{id}")
    Call<Object> getPokemonDetails(@Path("id") int id);
}

