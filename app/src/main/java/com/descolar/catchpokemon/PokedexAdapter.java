package com.descolar.catchpokemon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Adaptador para el RecyclerView que muestra la Pokédex.
 * Muestra una lista de Pokémon con su nombre, imagen y un atributo calculado como "poder".
 */
public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.ViewHolder> {

    // Lista de Pokémon que se mostrarán
    private List<Pokemon> pokemonList;
    // Listener para manejar los clics en los elementos
    private final OnPokemonClickListener clickListener;

    /**
     * Interfaz para manejar los clics en los elementos de la lista.
     */
    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    /**
     * Constructor del adaptador.
     *
     * @param pokemonList   Lista de Pokémon a mostrar.
     * @param clickListener Listener para manejar los clics en los Pokémon.
     */
    public PokedexAdapter(List<Pokemon> pokemonList, OnPokemonClickListener clickListener) {
        this.pokemonList = pokemonList;
        this.clickListener = clickListener;
    }

    /**
     * Infla el layout para cada elemento de la lista.
     *
     * @param parent   Contenedor padre donde se infla el layout.
     * @param viewType Tipo de vista (único en este caso).
     * @return Un ViewHolder que contiene la vista inflada.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokedex, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Enlaza los datos del Pokémon al ViewHolder.
     *
     * @param holder   ViewHolder que representa el elemento actual.
     * @param position Posición del Pokémon en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);

        // Establecemos el nombre del Pokémon
        holder.name.setText(pokemon.getName());

        // Mostramos el "poder" del Pokémon usando su ID
        String power = "Power: " + calculatePokemonPower(pokemon);
        holder.power.setText(power);

        // Cargamos la imagen del Pokémon usando Glide
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getImageUrl()) // URL de la imagen
                .into(holder.image);

        // Configuramos el clic en el elemento
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPokemonClick(pokemon);
            }
        });
    }

    /**
     * Calcula el "poder" del Pokémon basado en su ID.
     *
     * @param pokemon Pokémon del que se calculará el poder.
     * @return Poder del Pokémon.
     */
    private int calculatePokemonPower(Pokemon pokemon) {
        return pokemon.getId(); // Usamos el ID como "poder"
    }

    /**
     * Devuelve el número de elementos en la lista.
     *
     * @return Tamaño de la lista de Pokémon.
     */
    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    /**
     * ViewHolder que contiene las vistas de cada elemento de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name; // Nombre del Pokémon
        TextView power; // "Poder" del Pokémon (basado en su ID)
        ImageView image; // Imagen del Pokémon

        /**
         * Constructor del ViewHolder. Enlaza las vistas con los elementos del layout.
         *
         * @param itemView Vista del elemento inflado.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pokemonName);
            power = itemView.findViewById(R.id.pokemonPower);
            image = itemView.findViewById(R.id.pokemonImage);
        }
    }
}
