package com.descolar.catchpokemon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.descolar.catchpokemon.Pokemon;
import com.descolar.catchpokemon.R;
import java.util.List;

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.ViewHolder> {

    private List<Pokemon> pokemonList;
    private OnPokemonClickListener clickListener;

    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    public PokedexAdapter(List<Pokemon> pokemonList, OnPokemonClickListener clickListener) {
        this.pokemonList = pokemonList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokedex, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.name.setText(pokemon.getName());

        // Calcular el poder del Pokémon
        String power = "Power: " + calculatePokemonPower(pokemon);
        holder.power.setText(power);

        // Cargar la imagen usando Glide
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getImageUrl())
                .into(holder.image);

        // Manejar el clic en la card
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPokemonClick(pokemon);
            }
        });
    }

    // Método para calcular el poder del Pokémon
    private int calculatePokemonPower(Pokemon pokemon) {
        try {
            // Validar y convertir peso
            String weight = pokemon.getWeight();
            int parsedWeight = weight != null ? Integer.parseInt(weight) : 0;

            // Validar y convertir altura
            String height = pokemon.getHeight();
            int parsedHeight = height != null ? Integer.parseInt(height) : 0;

            // Calcular poder
            return parsedWeight * parsedHeight;
        } catch (NumberFormatException e) {
            return 0; // Valor predeterminado en caso de error
        }
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView power;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pokemonName);
            power = itemView.findViewById(R.id.pokemonPower);
            image = itemView.findViewById(R.id.pokemonImage);
        }
    }
}
