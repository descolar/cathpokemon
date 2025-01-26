package com.descolar.catchpokemon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

public class PokemonDetailsDialog extends DialogFragment {

    private static final String ARG_POKEMON = "pokemon";
    private Pokemon pokemon;

    // Interfaz para notificar al fragmento
    public interface OnPokemonDeletedListener {
        void onPokemonDeleted(Pokemon pokemon);
    }

    private OnPokemonDeletedListener listener;

    public void setOnPokemonDeletedListener(OnPokemonDeletedListener listener) {
        this.listener = listener;
    }

    public static PokemonDetailsDialog newInstance(Pokemon pokemon) {
        PokemonDetailsDialog dialog = new PokemonDetailsDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_POKEMON, pokemon);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pokemon = (Pokemon) getArguments().getSerializable(ARG_POKEMON);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pokemon_details, container, false);

        ImageView closeButton = view.findViewById(R.id.dialogCloseButton);
        ImageView deleteButton = view.findViewById(R.id.dialogDeleteButton);
        ImageView imageView = view.findViewById(R.id.dialogPokemonImage);
        TextView nameTextView = view.findViewById(R.id.dialogPokemonName);
        TextView indexTextView = view.findViewById(R.id.dialogPokemonIndex);
        TextView typesTextView = view.findViewById(R.id.dialogPokemonTypes);
        TextView weightHeightTextView = view.findViewById(R.id.dialogPokemonWeightHeight);

        closeButton.setOnClickListener(v -> dismiss());

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
        boolean isDeleteEnabled = sharedPreferences.getBoolean("eliminar_habilitado", false);

        if (isDeleteEnabled) {
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> deletePokemonFromDatabase(pokemon));
        } else {
            deleteButton.setVisibility(View.GONE);
        }

        Glide.with(requireContext())
                .load(pokemon.getImageUrl())
                .into(imageView);

        nameTextView.setText(pokemon.getName());
        indexTextView.setText(getString(R.string.pokemon_index, pokemon.getId()));
        typesTextView.setText(getString(R.string.pokemon_types, pokemon.getTypesAsString()));
        weightHeightTextView.setText(getString(R.string.pokemon_weight_height, pokemon.getWeight(), pokemon.getHeight()));

        return view;
    }

    private void deletePokemonFromDatabase(Pokemon pokemon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemons")
                .document(String.valueOf(pokemon.getId()))
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), pokemon.getName() + " eliminado", Toast.LENGTH_SHORT).show();

                    if (listener != null) {
                        listener.onPokemonDeleted(pokemon);
                    }

                    dismiss();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al eliminar el Pokémon", Toast.LENGTH_SHORT).show();
                });
    }
}
