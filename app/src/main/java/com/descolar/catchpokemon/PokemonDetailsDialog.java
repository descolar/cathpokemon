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

/**
 * Diálogo que muestra los detalles de un Pokémon capturado.
 * Permite eliminar un Pokémon de la base de datos si la opción está habilitada.
 */
public class PokemonDetailsDialog extends DialogFragment {

    private static final String ARG_POKEMON = "pokemon"; // Clave para pasar el Pokémon como argumento
    private Pokemon pokemon; // Pokémon cuyos detalles se mostrarán

    /**
     * Interfaz para notificar al fragmento padre cuando se elimina un Pokémon.
     */
    public interface OnPokemonDeletedListener {
        void onPokemonDeleted(Pokemon pokemon);
    }

    private OnPokemonDeletedListener listener; // Listener para notificar al fragmento padre

    /**
     * Configura el listener que notifica al fragmento cuando se elimina un Pokémon.
     *
     * @param listener Listener que manejará el evento.
     */
    public void setOnPokemonDeletedListener(OnPokemonDeletedListener listener) {
        this.listener = listener;
    }

    /**
     * Crea una nueva instancia del diálogo con un Pokémon específico.
     *
     * @param pokemon Pokémon cuyos detalles se mostrarán.
     * @return Instancia del diálogo.
     */
    public static PokemonDetailsDialog newInstance(Pokemon pokemon) {
        PokemonDetailsDialog dialog = new PokemonDetailsDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_POKEMON, pokemon); // Pasamos el Pokémon como argumento
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pokemon = (Pokemon) getArguments().getSerializable(ARG_POKEMON); // Recuperamos el Pokémon de los argumentos
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el layout del diálogo
        View view = inflater.inflate(R.layout.dialog_pokemon_details, container, false);

        // Referencias a los elementos del layout
        ImageView closeButton = view.findViewById(R.id.dialogCloseButton);
        ImageView deleteButton = view.findViewById(R.id.dialogDeleteButton);
        ImageView imageView = view.findViewById(R.id.dialogPokemonImage);
        TextView nameTextView = view.findViewById(R.id.dialogPokemonName);
        TextView indexTextView = view.findViewById(R.id.dialogPokemonIndex);
        TextView typesTextView = view.findViewById(R.id.dialogPokemonTypes);
        TextView weightHeightTextView = view.findViewById(R.id.dialogPokemonWeightHeight);

        // Configuramos el botón de cerrar
        closeButton.setOnClickListener(v -> dismiss());

        // Verificamos si la eliminación está habilitada en las preferencias
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_preferences", Context.MODE_PRIVATE);
        boolean isDeleteEnabled = sharedPreferences.getBoolean("eliminar_habilitado", false);

        if (isDeleteEnabled) {
            deleteButton.setVisibility(View.VISIBLE); // Mostramos el botón de eliminar
            deleteButton.setOnClickListener(v -> deletePokemonFromDatabase(pokemon)); // Configuramos la acción de eliminación
        } else {
            deleteButton.setVisibility(View.GONE); // Ocultamos el botón si no está habilitado
        }

        // Cargamos los datos del Pokémon en el layout
        Glide.with(requireContext())
                .load(pokemon.getImageUrl())
                .into(imageView);

        nameTextView.setText(pokemon.getName());
        indexTextView.setText(getString(R.string.pokemon_index, pokemon.getId()));
        typesTextView.setText(getString(R.string.pokemon_types, pokemon.getTypesAsString()));
        weightHeightTextView.setText(getString(R.string.pokemon_weight_height, pokemon.getWeight(), pokemon.getHeight()));

        return view;
    }

    /**
     * Elimina el Pokémon de Firestore.
     *
     * @param pokemon Pokémon que será eliminado.
     */
    private void deletePokemonFromDatabase(Pokemon pokemon) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("captured_pokemons")
                .document(String.valueOf(pokemon.getId())) // Usamos el ID del Pokémon como clave
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), pokemon.getName() + " eliminado", Toast.LENGTH_SHORT).show();

                    // Notificamos al fragmento padre
                    if (listener != null) {
                        listener.onPokemonDeleted(pokemon);
                    }

                    dismiss(); // Cerramos el diálogo
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al eliminar el Pokémon", Toast.LENGTH_SHORT).show();
                });
    }
}
