package com.descolar.catchpokemon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un Pokémon con sus atributos básicos.
 * Implementa Serializable para permitir su transferencia entre actividades y fragmentos.
 */
public class Pokemon implements Serializable {

    // Atributos principales del Pokémon
    private int id; // ID del Pokémon
    private String index; // Índice en la Pokédex (opcional)
    private String name; // Nombre del Pokémon
    private List<String> types; // Tipos del Pokémon
    private int height; // Altura del Pokémon en decímetros
    private int weight; // Peso del Pokémon en hectogramos
    private List<String> abilities; // Habilidades del Pokémon
    private String imageUrl; // URL de la imagen del Pokémon

    /**
     * Constructor con todos los parámetros.
     *
     * @param id        ID del Pokémon.
     * @param name      Nombre del Pokémon.
     * @param types     Lista de tipos del Pokémon.
     * @param height    Altura del Pokémon.
     * @param weight    Peso del Pokémon.
     * @param abilities Lista de habilidades del Pokémon.
     * @param imageUrl  URL de la imagen del Pokémon.
     */
    public Pokemon(int id, String name, List<String> types, int height, int weight, List<String> abilities, String imageUrl) {
        this.id = id;
        this.name = name;
        this.types = types != null ? types : new ArrayList<>();
        this.height = height;
        this.weight = weight;
        this.abilities = abilities != null ? abilities : new ArrayList<>();
        this.imageUrl = imageUrl;
    }

    /**
     * Constructor sin argumentos.
     * Inicializa los valores con datos predeterminados.
     */
    public Pokemon() {
        this.id = 0;
        this.name = "";
        this.types = new ArrayList<>();
        this.height = 0;
        this.weight = 0;
        this.abilities = new ArrayList<>();
        this.imageUrl = "";
    }

    // Métodos Getter

    /**
     * Obtiene el ID del Pokémon.
     *
     * @return ID del Pokémon.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el índice del Pokémon en la Pokédex.
     *
     * @return Índice del Pokémon.
     */
    public String getIndex() {
        return index;
    }

    /**
     * Obtiene el nombre del Pokémon.
     *
     * @return Nombre del Pokémon.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la URL de la imagen del Pokémon.
     *
     * @return URL de la imagen del Pokémon.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Obtiene la lista de tipos del Pokémon.
     *
     * @return Lista de tipos del Pokémon.
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * Convierte la lista de tipos a un String concatenado.
     *
     * @return Tipos concatenados en un String o "Desconocido" si no hay tipos.
     */
    public String getTypesAsString() {
        if (types == null || types.isEmpty()) {
            return "Desconocido";
        }
        return String.join(", ", types);
    }

    /**
     * Obtiene la altura del Pokémon.
     *
     * @return Altura del Pokémon.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Obtiene el peso del Pokémon.
     *
     * @return Peso del Pokémon.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Obtiene la lista de habilidades del Pokémon.
     *
     * @return Lista de habilidades del Pokémon.
     */
    public List<String> getAbilities() {
        return abilities;
    }

    /**
     * Convierte la lista de habilidades a un String concatenado.
     *
     * @return Habilidades concatenadas en un String o "Desconocido" si no hay habilidades.
     */
    public String getAbilitiesAsString() {
        if (abilities == null || abilities.isEmpty()) {
            return "Desconocido";
        }
        return String.join(", ", abilities);
    }
}
