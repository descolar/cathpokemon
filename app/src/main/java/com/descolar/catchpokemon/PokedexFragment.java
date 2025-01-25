package com.descolar.catchpokemon;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.descolar.catchpokemon.PokedexAdapter;
import com.descolar.catchpokemon.Pokemon;
import com.descolar.catchpokemon.PokeApiResponse;
//import com.descolar.catchpokemon.PokemonResult;
import com.descolar.catchpokemon.PokeApiService;
import com.descolar.catchpokemon.RetrofitClient;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokedexFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokedexAdapter adapter;
    private List<Pokemon> pokemonList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPokedex);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Mostrar 3 Pokémon por fila

        fetchPokemonList();

        return view;
    }

    private void fetchPokemonList() {
        PokeApiService apiService = RetrofitClient.getInstance().create(PokeApiService.class);
        apiService.getPokemonList(0, 150).enqueue(new Callback<PokeApiResponse>() {
            @Override
            public void onResponse(Call<PokeApiResponse> call, Response<PokeApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pokemonList = mapPokemonList(response.body().getResults());
                    adapter = new PokedexAdapter(pokemonList, PokedexFragment.this::capturePokemon);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PokeApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Pokemon> mapPokemonList(List<PokeApiResponse.PokemonResult> results) {
        List<Pokemon> pokemons = new ArrayList<>();
        for (PokeApiResponse.PokemonResult result : results) {
            String[] urlParts = result.getUrl().split("/");
            int id = Integer.parseInt(urlParts[urlParts.length - 1]);

            pokemons.add(new Pokemon(
                    id,
                    result.getName(),
                    Collections.singletonList("Unknown Type"),
                    "0",
                    "0",
                    Collections.singletonList("Unknown Ability"),
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png"
            ));
        }
        return pokemons;
    }



    private void capturePokemon(Pokemon pokemon) {
        // Verifica que el ID no sea nulo o vacío antes de proceder
        if (pokemon.getId() == 0) {
            Toast.makeText(getContext(), "Invalid Pokémon ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("PokedexFragment", "ID del Pokémon seleccionado: " + pokemon.getId());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemons")
                .document(String.valueOf(pokemon.getId())) // Usar el ID como documento
                .set(pokemon) // Guardar el objeto Pokémon completo
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Captured: " + pokemon.getName(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to capture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
