package com.descolar.catchpokemon;

import java.util.List;

/**
 * Clase que representa la respuesta de la API de Pokémon.
 * Contiene una lista de resultados con información básica de cada Pokémon.
 */
public class PokeApiResponse {

    // Lista de resultados de la API
    private List<PokemonResult> results;

    /**
     * Obtiene la lista de resultados de la API.
     *
     * @return Lista de resultados (Pokémon).
     */
    public List<PokemonResult> getResults() {
        return results;
    }

    /**
     * Establece la lista de resultados obtenida de la API.
     *
     * @param results Lista de resultados (Pokémon).
     */
    public void setResults(List<PokemonResult> results) {
        this.results = results;
    }

    /**
     * Clase interna que representa un resultado individual de la API.
     * Contiene información básica como el nombre y la URL del Pokémon.
     */
    public static class PokemonResult {
        private String name; // Nombre del Pokémon
        private String url;  // URL con detalles del Pokémon

        /**
         * Obtiene el nombre del Pokémon.
         *
         * @return Nombre del Pokémon.
         */
        public String getName() {
            return name;
        }

        /**
         * Establece el nombre del Pokémon.
         *
         * @param name Nombre del Pokémon.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * Obtiene la URL de los detalles del Pokémon.
         *
         * @return URL con detalles del Pokémon.
         */
        public String getUrl() {
            return url;
        }

        /**
         * Establece la URL de los detalles del Pokémon.
         *
         * @param url URL con detalles del Pokémon.
         */
        public void setUrl(String url) {
            this.url = url;
        }
    }
}
