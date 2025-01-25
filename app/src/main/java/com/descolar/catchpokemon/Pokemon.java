package com.descolar.catchpokemon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pokemon implements Serializable {
    private int id;
    private String index;
    private String name;
    private List<String> types;
    private String height; // Cambiado a String
    private String weight; // Cambiado a String
    private List<String> abilities;

    public Pokemon(int id, String name, List<String> types, String height, String weight, List<String> abilities) {
        this.id = id;
        this.name = name;
        this.types = types;
        this.height = "0"; // Valor predeterminado
        this.weight = "0"; // Valor predeterminado
        this.abilities = abilities;
    }

    // Constructor sin argumentos
    public Pokemon() {
        this.id = 0;
        this.name = "";
        this.types = new ArrayList<>();
        this.height = "";
        this.weight = "";
        this.abilities = new ArrayList<>();
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

    public List<String> getTypes() {
        return types;
    }

    public String getTypesAsString() {
        if (types == null || types.isEmpty()) {
            return "Desconocido";
        }
        return String.join(", ", types);
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
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



