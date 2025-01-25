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
import com.descolar.catchpokemon.CapturedPokemonsAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CapturedPokemonsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CapturedPokemonsAdapter adapter;
    private final List<Pokemon> capturedPokemons = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_captured_pokemons, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCapturedPokemons);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Mostrar 3 Pokémon por fila

        loadCapturedPokemons();

        return view;
    }

    private void loadCapturedPokemons() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemons")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Pokemon> pokemons = queryDocumentSnapshots.toObjects(Pokemon.class);
                        adapter = new CapturedPokemonsAdapter(pokemons);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), "No Pokémon captured yet.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load captured Pokémon: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Método para agregar un Pokémon a la lista y actualizar el adaptador
    public void addCapturedPokemon(Pokemon pokemon) {
        capturedPokemons.add(pokemon);
        adapter.notifyItemInserted(capturedPokemons.size() - 1);
    }

    private void showPokemonDetailsDialog(Pokemon pokemon) {
        PokemonDetailsDialog dialog = PokemonDetailsDialog.newInstance(pokemon);
        dialog.show(getChildFragmentManager(), "PokemonDetailsDialog");
    }

}
