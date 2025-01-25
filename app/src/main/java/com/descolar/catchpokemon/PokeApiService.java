package com.descolar.catchpokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokeApiService {
    @GET("pokemon")
    Call<PokeApiResponse> getPokemonList(
            @Query("offset") int offset,
            @Query("limit") int limit
    );
}
