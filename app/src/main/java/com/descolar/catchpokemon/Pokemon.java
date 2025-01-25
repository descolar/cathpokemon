package com.descolar.catchpokemon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pokemon implements Serializable {
    private int id;
    private String index;
    private String name;
    private List<String> types;
    private int height; // Cambiado a String
    private int weight; // Cambiado a String
    private List<String> abilities;
    private String imageUrl;

    public Pokemon(int id, String name, List<String> types, int height, int weight, List<String> abilities, String imageUrl) {
        this.id = id;
        this.name = name;
        this.types = types;
        this.height = height;
        this.weight = weight;
        this.abilities = abilities;
        this.imageUrl = imageUrl;
    }

    // Constructor sin argumentos
    public Pokemon() {
        this.id = 0;
        this.name = "";
        this.types = new ArrayList<>();
        this.height = 0;
        this.weight = 0;
        this.abilities = new ArrayList<>();
        this.imageUrl = "";
    }

    public int getId() {
        return id;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getTypes() {
        return types;
    }

    public String getTypesAsString() {
        if (types == null || types.isEmpty()) {
            return "Desconocido";
        }
        return String.join(", ", types);
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public String getAbilitiesAsString() {
        if (abilities == null || abilities.isEmpty()) {
            return "Desconocido";
        }
        return String.join(", ", abilities);
    }
}



