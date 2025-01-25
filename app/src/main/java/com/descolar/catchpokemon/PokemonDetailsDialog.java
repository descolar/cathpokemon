package com.descolar.catchpokemon;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.descolar.catchpokemon.Pokemon;
import com.descolar.catchpokemon.R;

public class PokemonDetailsDialog extends DialogFragment {

    private static final String ARG_POKEMON = "pokemon";

    public static PokemonDetailsDialog newInstance(Pokemon pokemon) {
        PokemonDetailsDialog dialog = new PokemonDetailsDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_POKEMON, pokemon);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pokemon_details, container, false);

        // Obtener los datos del Pokémon
        Pokemon pokemon = (Pokemon) getArguments().getSerializable(ARG_POKEMON);

        // Configurar la vista con los datos del Pokémon
        TextView nameTextView = view.findViewById(R.id.pokemonName);
        TextView typeTextView = view.findViewById(R.id.pokemonType);
        TextView heightTextView = view.findViewById(R.id.pokemonHeight);
        TextView weightTextView = view.findViewById(R.id.pokemonWeight);

        nameTextView.setText(pokemon.getName());
        typeTextView.setText(pokemon.getTypesAsString());
        heightTextView.setText(String.format("Altura: %s", pokemon.getHeight()));
        weightTextView.setText(String.format("Peso: %s", pokemon.getWeight()));

        return view;
    }
}