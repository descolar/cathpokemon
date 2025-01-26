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

public class CapturedPokemonsFragment extends Fragment implements PokemonDetailsDialog.OnPokemonDeletedListener {

    private RecyclerView recyclerView;
    private CapturedPokemonsAdapter adapter;
    private final List<Pokemon> capturedPokemons = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_captured_pokemons, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCapturedPokemons);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Mostrar 3 Pokémon por fila

        adapter = new CapturedPokemonsAdapter(capturedPokemons, this::showPokemonDetailsDialog);
        recyclerView.setAdapter(adapter);

        loadCapturedPokemons();

        return view;
    }

    private void loadCapturedPokemons() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        capturedPokemons.clear();
                        capturedPokemons.addAll(queryDocumentSnapshots.toObjects(Pokemon.class));
                        adapter.notifyDataSetChanged(); // Actualiza el RecyclerView
                    } else {
                        Toast.makeText(getContext(), "No Pokémon captured yet.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load captured Pokémon: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void reloadCapturedPokemons() {
        capturedPokemons.clear(); // Limpia la lista actual
        adapter.notifyDataSetChanged(); // Notifica al adaptador que la lista está vacía
        loadCapturedPokemons(); // Recarga los datos desde Firestore
    }


    // Método para agregar un Pokémon a la lista y actualizar el adaptador
    public void addCapturedPokemon(Pokemon pokemon) {
        capturedPokemons.add(pokemon);
        adapter.notifyItemInserted(capturedPokemons.size() - 1);
    }

    private void showPokemonDetailsDialog(Pokemon pokemon) {
        PokemonDetailsDialog dialog = PokemonDetailsDialog.newInstance(pokemon);
        dialog.setOnPokemonDeletedListener(this); // Configurar el listener
        dialog.show(getChildFragmentManager(), "PokemonDetailsDialog");
    }

    @Override
    public void onPokemonDeleted(Pokemon pokemon) {
        // Buscar el Pokémon en la lista local por ID
        for (int i = 0; i < capturedPokemons.size(); i++) {
            if (capturedPokemons.get(i).getId() == pokemon.getId()) {
                capturedPokemons.remove(i); // Elimina el Pokémon de la lista local
                adapter.notifyItemRemoved(i); // Notifica al adaptador que el elemento fue eliminado
                break;
            }
        }

        // Muestra un mensaje confirmando la eliminación
        Toast.makeText(getContext(), pokemon.getName() + " eliminado de la lista", Toast.LENGTH_SHORT).show();
    }
}
