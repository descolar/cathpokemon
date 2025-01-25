package com.descolar.catchpokemon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CapturedPokemonsAdapter extends RecyclerView.Adapter<CapturedPokemonsAdapter.ViewHolder> {

    private List<Pokemon> pokemonList;
    private final OnPokemonClickListener listener;

    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    public CapturedPokemonsAdapter(List<Pokemon> pokemonList, OnPokemonClickListener listener) {
        this.pokemonList = pokemonList;
        this.listener = listener;
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
        // Mostrar el nombre del Pokémon
        holder.name.setText(pokemon.getName());
        // Mostrar el ID en lugar de "Power"
        holder.power.setText("ID: " + pokemon.getId());

        // Cargar la imagen del Pokémon
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getImageUrl())
                .into(holder.image);

        // Manejar clics
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPokemonClick(pokemon);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        TextView power;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pokemonName);
            image = itemView.findViewById(R.id.pokemonImage);
            power = itemView.findViewById(R.id.pokemonPower);
        }
    }
}
