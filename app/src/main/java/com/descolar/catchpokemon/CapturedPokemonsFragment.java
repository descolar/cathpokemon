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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento para mostrar la lista de Pokémon capturados.
 * Nos permite visualizar los Pokémon almacenados en Firestore y eliminarlos si es necesario.
 */
public class CapturedPokemonsFragment extends Fragment implements PokemonDetailsDialog.OnPokemonDeletedListener {

    // RecyclerView para mostrar la lista de Pokémon capturados
    private RecyclerView recyclerView;
    // Adaptador para gestionar la lista de Pokémon
    private CapturedPokemonsAdapter adapter;
    // Lista de Pokémon capturados
    private final List<Pokemon> capturedPokemons = new ArrayList<>();

    /**
     * Infla el layout del fragmento y configura el RecyclerView.
     *
     * @param inflater           Objeto LayoutInflater para inflar vistas.
     * @param container          Contenedor padre del fragmento.
     * @param savedInstanceState Estado guardado de la instancia (opcional).
     * @return Vista inflada del fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflamos el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_captured_pokemons, container, false);

        // Configuramos el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewCapturedPokemons);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Mostrar 3 Pokémon por fila

        // Inicializamos el adaptador
        adapter = new CapturedPokemonsAdapter(capturedPokemons, this::showPokemonDetailsDialog);
        recyclerView.setAdapter(adapter);

        // Cargamos los Pokémon capturados desde Firestore
        loadCapturedPokemons();

        return view;
    }

    /**
     * Carga la lista de Pokémon capturados desde Firestore.
     * Actualiza el RecyclerView con los datos obtenidos.
     */
    private void loadCapturedPokemons() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Limpiamos la lista local y la actualizamos con los datos de Firestore
                        capturedPokemons.clear();
                        capturedPokemons.addAll(queryDocumentSnapshots.toObjects(Pokemon.class));
                        adapter.notifyDataSetChanged(); // Actualizamos el RecyclerView
                    } else {
                        Toast.makeText(getContext(), "No Pokémon captured yet.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load captured Pokémon: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Recarga los Pokémon capturados.
     * Limpia la lista actual y vuelve a cargar los datos desde Firestore.
     */
    public void reloadCapturedPokemons() {
        capturedPokemons.clear(); // Limpia la lista actual
        adapter.notifyDataSetChanged(); // Notifica al adaptador que la lista está vacía
        loadCapturedPokemons(); // Recarga los datos desde Firestore
    }

    /**
     * Añade un Pokémon capturado a la lista y actualiza el adaptador.
     *
     * @param pokemon Pokémon capturado que se añadirá.
     */
    public void addCapturedPokemon(Pokemon pokemon) {
        capturedPokemons.add(pokemon);
        adapter.notifyItemInserted(capturedPokemons.size() - 1); // Notifica la adición al adaptador
    }

    /**
     * Muestra el diálogo con los detalles de un Pokémon seleccionado.
     *
     * @param pokemon Pokémon seleccionado.
     */
    private void showPokemonDetailsDialog(Pokemon pokemon) {
        PokemonDetailsDialog dialog = PokemonDetailsDialog.newInstance(pokemon);
        dialog.setOnPokemonDeletedListener(this); // Configuramos el listener para manejar eliminaciones
        dialog.show(getChildFragmentManager(), "PokemonDetailsDialog");
    }

    /**
     * Callback cuando se elimina un Pokémon desde el diálogo de detalles.
     *
     * @param pokemon Pokémon que se eliminó.
     */
    @Override
    public void onPokemonDeleted(Pokemon pokemon) {
        // Buscamos el Pokémon eliminado en la lista local
        for (int i = 0; i < capturedPokemons.size(); i++) {
            if (capturedPokemons.get(i).getId() == pokemon.getId()) {
                capturedPokemons.remove(i); // Lo eliminamos de la lista local
                adapter.notifyItemRemoved(i); // Notificamos al adaptador que el elemento fue eliminado
                break;
            }
        }

        // Mostramos un mensaje confirmando la eliminación
        Toast.makeText(getContext(), pokemon.getName() + " eliminado de la lista", Toast.LENGTH_SHORT).show();
    }
}
