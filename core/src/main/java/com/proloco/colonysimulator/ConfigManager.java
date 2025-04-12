package com.proloco.colonysimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class ConfigManager {
    private static int GRID_SPACING = 20; // Spaziatura della griglia predefinita
    private static int ANTS_QUANTITY = 40; // Numero di formiche predefinito
    private static float ANT_SPEED = 3.0f; // Velocità delle formiche predefinita
    private static int ANT_RANGE = 3; // Raggio di ricerca delle formiche predefinito
    private static float ANT_TIME_DISTANCE = 40.0f; // Valore di default

    static {
        // Carica il file JSON se esiste
        try {
            FileHandle file = Gdx.files.local("config.json");
            if (file.exists()) {
                Json json = new Json();
                ConfigData data = json.fromJson(ConfigData.class, file.readString());
                ANT_TIME_DISTANCE = data.DEFAULT_TIME_DISTANCE;
            }
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento della configurazione: " + e.getMessage());
        }
    }

    public static float getDefaultTimeDistance() {
        return ANT_TIME_DISTANCE; // Restituisce il valore di default
    }

    public static int getAntsQuantity() {
        return 40; // Numero di formiche predefinito
    }

    public static float getAntSpeed() {
        return ANT_SPEED; // Velocità delle formiche predefinita
    }

    public static int getGridSpacing() {
        return GRID_SPACING; // Spaziatura della griglia predefinita
    }

    public static int getAntRange() {
        return ANT_RANGE; // Raggio di ricerca delle formiche predefinito
    }

    // Classe interna per mappare i dati JSON
    private static class ConfigData {
        public int DEFAULT_GRID_SPACING;
        public int DEFAULT_ANTS_QUANTITY;
        public float DEFAULT_ANT_SPEED;
        public int DEFAULT_ANT_RANGE;
        public float DEFAULT_TIME_DISTANCE;
    }
}
