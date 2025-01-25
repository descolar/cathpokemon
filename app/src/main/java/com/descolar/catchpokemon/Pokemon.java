package com.descolar.catchpokemon;

import java.io.Serializable;
import java.util.List;

public class Pokemon implements Serializable {
    private String name;
    private String index;
    private List<String> types;
    private String weight;
    private String height;


    // Constructor sin argumentos requerido por Firebase
    public Pokemon() {}

    // Constructor completo
    public Pokemon(String name, String index, String type, String weight, String height) {
        this.name = name;
        this.index = index;
        this.types = types;
        this.weight = weight;
        this.height = height;
    }

    // Getters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }


    public String getType() {
        return types;
    }


    public String getWeight() {
        return weight;
    }


    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
