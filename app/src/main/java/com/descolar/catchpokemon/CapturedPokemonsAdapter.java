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

    public CapturedPokemonsAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
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


        Glide.with(holder.itemView.getContext())
                .load(pokemon.getImageUrl())
                .into(holder.image);

        // Abrir el diálogo al hacer clic
        holder.itemView.setOnClickListener(v -> {
            PokemonDetailsDialog dialog = PokemonDetailsDialog.newInstance(pokemon);
            dialog.show(((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager(), "PokemonDetailsDialog");
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pokemonName);
            image = itemView.findViewById(R.id.pokemonImage);
        }
    }
}
