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

/**
 * Adaptador para mostrar la lista de Pokémon capturados en un RecyclerView.
 * Nos aseguramos de que cada elemento tenga su nombre, imagen y ID visibles.
 */
public class CapturedPokemonsAdapter extends RecyclerView.Adapter<CapturedPokemonsAdapter.ViewHolder> {

    // Lista de Pokémon capturados que se mostrará
    private List<Pokemon> pokemonList;
    // Listener para manejar clics en los Pokémon
    private final OnPokemonClickListener listener;

    /**
     * Interfaz para manejar los clics en los elementos de la lista.
     */
    public interface OnPokemonClickListener {
        void onPokemonClick(Pokemon pokemon);
    }

    /**
     * Constructor del adaptador.
     *
     * @param pokemonList Lista de Pokémon capturados.
     * @param listener    Listener para manejar los clics en los Pokémon.
     */
    public CapturedPokemonsAdapter(List<Pokemon> pokemonList, OnPokemonClickListener listener) {
        this.pokemonList = pokemonList;
        this.listener = listener;
    }

    /**
     * Infla el layout de cada elemento del RecyclerView.
     *
     * @param parent   Vista padre donde se infla el layout.
     * @param viewType Tipo de vista (único en este caso).
     * @return ViewHolder con la vista inflada.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokedex, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Asigna los datos de un Pokémon al ViewHolder correspondiente.
     *
     * @param holder   ViewHolder que representa el elemento actual.
     * @param position Posición del Pokémon en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);

        // Asignamos el nombre del Pokémon
        holder.name.setText(pokemon.getName());

        // Mostramos el ID del Pokémon
        holder.power.setText("ID: " + pokemon.getId());

        // Cargamos la imagen del Pokémon utilizando Glide
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getImageUrl())
                .into(holder.image);

        // Configuramos el listener para manejar clics en el elemento
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPokemonClick(pokemon);
            }
        });
    }

    /**
     * Devuelve la cantidad de elementos en la lista.
     *
     * @return Número de Pokémon capturados.
     */
    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    /**
     * ViewHolder que contiene las vistas de cada elemento del RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name; // Nombre del Pokémon
        ImageView image; // Imagen del Pokémon
        TextView power; // ID del Pokémon

        /**
         * Constructor del ViewHolder. Enlazamos los elementos del layout.
         *
         * @param itemView Vista del elemento inflado.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pokemonName);
            image = itemView.findViewById(R.id.pokemonImage);
            power = itemView.findViewById(R.id.pokemonPower);
        }
    }
}
