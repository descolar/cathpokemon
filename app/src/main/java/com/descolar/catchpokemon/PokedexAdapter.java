package com.descolar.catchpokemon.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.descolar.catchpokemon.OnPokemonCaptureListener;
import com.descolar.catchpokemon.Pokemon;
import com.descolar.catchpokemon.R;
import com.descolar.catchpokemon.PokeApiResponse;
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

        // Supongamos que el poder del Pokémon es un valor calculado o extraído
        String power = "Power: " + calculatePokemonPower(pokemon);
        holder.power.setText(power);

        // URL de la imagen del Pokémon
        String imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemon.getIndex() + ".png";

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPokemonClick(pokemon);
            }
        });
    }

    // Método para calcular el poder del Pokémon (puedes ajustar la lógica)
    private int calculatePokemonPower(Pokemon pokemon) {
        try {
            // Validar y convertir peso
            String weight = pokemon.getWeight();
            int parsedWeight = weight != null ? Integer.parseInt(weight) : 0;

            // Validar y convertir altura
            String height = pokemon.getHeight();
            int parsedHeight = height != null ? Integer.parseInt(height) : 0;

            // Calcular poder (puedes ajustar la lógica)
            return parsedWeight * parsedHeight;
        } catch (NumberFormatException e) {
            // Manejar cualquier excepción y usar un valor predeterminado
            return 0;
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
