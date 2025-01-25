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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Realizar petición a la API para obtener los detalles completos del Pokémon
        PokeApiService apiService = RetrofitClient.getInstance().create(PokeApiService.class);
        Call<Object> call = apiService.getPokemonDetails(pokemon.getId());

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Procesar el JSON manualmente
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        // Extraer los datos necesarios
                        String name = jsonObject.getString("name");
                        int id = jsonObject.getInt("id");
                        int height = jsonObject.getInt("height");
                        int weight = jsonObject.getInt("weight");

                        // Tipos
                        List<String> types = new ArrayList<>();
                        JSONArray typesArray = jsonObject.getJSONArray("types");
                        for (int i = 0; i < typesArray.length(); i++) {
                            JSONObject typeObject = typesArray.getJSONObject(i).getJSONObject("type");
                            types.add(typeObject.getString("name"));
                        }

                        // URL de la imagen
                        String imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";

                        // Crear el objeto para guardar en Firestore
                        Map<String, Object> capturedPokemon = new HashMap<>();
                        capturedPokemon.put("id", id);
                        capturedPokemon.put("name", name);
                        capturedPokemon.put("types", types);
                        capturedPokemon.put("weight", weight);
                        capturedPokemon.put("height", height);
                        capturedPokemon.put("imageUrl", imageUrl);

                        // Guardar en Firestore
                        db.collection("captured_pokemons")
                                .document(String.valueOf(id))
                                .set(capturedPokemon)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), name + " capturado!", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Error al capturar el Pokémon", Toast.LENGTH_SHORT).show();
                                });

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error al procesar los detalles del Pokémon", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al obtener los detalles del Pokémon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
