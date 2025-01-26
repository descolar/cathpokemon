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

/**
 * Fragmento que muestra la lista de Pokémon disponibles en la Pokédex.
 * Permite capturar Pokémon y guardarlos en Firestore.
 */
public class PokedexFragment extends Fragment {

    private RecyclerView recyclerView; // RecyclerView para mostrar la Pokédex
    private PokedexAdapter adapter; // Adaptador del RecyclerView
    private List<Pokemon> pokemonList = new ArrayList<>(); // Lista de Pokémon

    /**
     * Infla el layout del fragmento y configura el RecyclerView.
     *
     * @param inflater           Inflador del layout.
     * @param container          Contenedor padre.
     * @param savedInstanceState Estado guardado (opcional).
     * @return Vista inflada del fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokedex, container, false);

        // Configuramos el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewPokedex);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Mostrar 3 Pokémon por fila

        // Cargamos la lista de Pokémon desde la API
        fetchPokemonList();

        return view;
    }

    /**
     * Realiza una solicitud a la API para obtener la lista de Pokémon.
     * Configura el adaptador para mostrar los datos en el RecyclerView.
     */
    private void fetchPokemonList() {
        PokeApiService apiService = RetrofitClient.getInstance().create(PokeApiService.class);
        apiService.getPokemonList(0, 150).enqueue(new Callback<PokeApiResponse>() {
            @Override
            public void onResponse(Call<PokeApiResponse> call, Response<PokeApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Mapeamos los datos de la respuesta a nuestra lista de Pokémon
                    pokemonList = mapPokemonList(response.body().getResults());
                    // Configuramos el adaptador
                    adapter = new PokedexAdapter(pokemonList, PokedexFragment.this::capturePokemon);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar la lista de Pokémon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokeApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión con la API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Convierte la respuesta de la API en una lista de objetos Pokémon.
     *
     * @param results Lista de resultados de la API.
     * @return Lista de Pokémon mapeados.
     */
    private List<Pokemon> mapPokemonList(List<PokeApiResponse.PokemonResult> results) {
        List<Pokemon> pokemons = new ArrayList<>();
        for (PokeApiResponse.PokemonResult result : results) {
            String[] urlParts = result.getUrl().split("/");
            int id = Integer.parseInt(urlParts[urlParts.length - 1]);

            pokemons.add(new Pokemon(
                    id,
                    result.getName(),
                    Collections.singletonList("Unknown Type"), // Tipo desconocido inicialmente
                    0, // Peso desconocido inicialmente
                    0, // Altura desconocida inicialmente
                    Collections.singletonList("Unknown Ability"), // Habilidades desconocidas
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png" // Imagen del Pokémon
            ));
        }
        return pokemons;
    }

    /**
     * Captura un Pokémon y lo guarda en Firestore.
     * Realiza una solicitud a la API para obtener sus detalles completos.
     *
     * @param pokemon Pokémon que será capturado.
     */
    private void capturePokemon(Pokemon pokemon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        PokeApiService apiService = RetrofitClient.getInstance().create(PokeApiService.class);
        Call<Object> call = apiService.getPokemonDetails(pokemon.getId());

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Procesamos la respuesta JSON
                        JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));

                        // Extraemos los datos del Pokémon
                        String name = jsonObject.getString("name");
                        int id = jsonObject.getInt("id");
                        int height = jsonObject.getInt("height");
                        int weight = jsonObject.getInt("weight");

                        List<String> types = new ArrayList<>();
                        JSONArray typesArray = jsonObject.getJSONArray("types");
                        for (int i = 0; i < typesArray.length(); i++) {
                            JSONObject typeObject = typesArray.getJSONObject(i).getJSONObject("type");
                            types.add(typeObject.getString("name"));
                        }

                        String imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png";

                        // Creamos el objeto para Firestore
                        Map<String, Object> capturedPokemon = new HashMap<>();
                        capturedPokemon.put("id", id);
                        capturedPokemon.put("name", name);
                        capturedPokemon.put("types", types);
                        capturedPokemon.put("weight", weight);
                        capturedPokemon.put("height", height);
                        capturedPokemon.put("imageUrl", imageUrl);

                        // Guardamos en Firestore
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
