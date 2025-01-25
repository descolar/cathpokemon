package com.descolar.catchpokemon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.descolar.catchpokemon.adapters.PokedexAdapter;
import com.descolar.catchpokemon.Pokemon;
import com.descolar.catchpokemon.PokeApiResponse;
//import com.descolar.catchpokemon.PokemonResult;
import com.descolar.catchpokemon.PokeApiService;
import com.descolar.catchpokemon.RetrofitClient;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
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
            String id = urlParts[urlParts.length - 1]; // Extraer el índice del URL
            pokemons.add(new Pokemon(
                    result.getName(),
                    id,
                    "Unknown Type", // Valores iniciales
                    "Unknown Weight",
                    "Unknown Height"
            ));
        }
        return pokemons;
    }

    private void capturePokemon(Pokemon pokemon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemons")
                .document(pokemon.getIndex()) // Usamos el índice como ID único
                .set(pokemon)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Captured: " + pokemon.getName(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to capture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
