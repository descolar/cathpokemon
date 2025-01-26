package com.descolar.catchpokemon;

/**
 * Interfaz para manejar el evento de captura de un Pokémon.
 * Las clases que implementen esta interfaz deben definir cómo manejar la captura de un Pokémon.
 */
public interface OnPokemonCaptureListener {

    /**
     * Método llamado cuando se captura un Pokémon.
     *
     * @param pokemon El Pokémon que ha sido capturado.
     */
    void onPokemonCaptured(Pokemon pokemon);
}
