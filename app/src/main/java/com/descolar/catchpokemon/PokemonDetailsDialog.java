package com.descolar.catchpokemon;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.descolar.catchpokemon.Pokemon;
import com.descolar.catchpokemon.R;

public class PokemonDetailsDialog extends DialogFragment {

    private static final String ARG_POKEMON = "pokemon";
    private Pokemon pokemon;

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

        // Encuentra el botón de cerrar
        ImageView closeButton = view.findViewById(R.id.dialogCloseButton);

        // Configura la acción del botón para cerrar el diálogo
        closeButton.setOnClickListener(v -> dismiss());

        // Referencias a los elementos del layout
        ImageView imageView = view.findViewById(R.id.dialogPokemonImage);
        TextView nameTextView = view.findViewById(R.id.dialogPokemonName);
        TextView indexTextView = view.findViewById(R.id.dialogPokemonIndex);
        TextView typesTextView = view.findViewById(R.id.dialogPokemonTypes);
        TextView weightHeightTextView = view.findViewById(R.id.dialogPokemonWeightHeight);

        // Cargar datos del Pokémon
        Glide.with(requireContext())
                .load(pokemon.getImageUrl())
                .into(imageView);

        nameTextView.setText(pokemon.getName());
        indexTextView.setText(getString(R.string.pokemon_index, pokemon.getId()));
        typesTextView.setText(getString(R.string.pokemon_types, pokemon.getTypesAsString()));
        weightHeightTextView.setText(getString(R.string.pokemon_weight_height, pokemon.getWeight(), pokemon.getHeight()));



        // Eliminar (botón no funcional aún)
        ImageView deleteButton = view.findViewById(R.id.dialogDeleteButton);
        deleteButton.setOnClickListener(v -> {
            // Por ahora no hacemos nada
        });

        // Configurar acción del botón de cerrar
        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }
}

