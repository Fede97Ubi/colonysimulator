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
            FileHandle file = Gdx.files.internal("world/config.json");
            if (file.exists()) {
                System.out.println("File config.json trovato: " + file.path());
                Json json = new Json();
                ConfigData data = json.fromJson(ConfigData.class, file.readString());
                GRID_SPACING = data.DEFAULT_GRID_SPACING;
                ANTS_QUANTITY = data.DEFAULT_ANTS_QUANTITY;
                ANT_SPEED = data.DEFAULT_ANT_SPEED;
                ANT_RANGE = data.DEFAULT_ANT_RANGE;
                ANT_TIME_DISTANCE = data.DEFAULT_TIME_DISTANCE;
            } else {
                System.err.println("File config.json non trovato nel percorso specificato.");
            }
            System.out.println("Configurazione caricata: " +
                    "GRID_SPACING=" + GRID_SPACING +
                    ", ANTS_QUANTITY=" + ANTS_QUANTITY +
                    ", ANT_SPEED=" + ANT_SPEED +
                    ", ANT_RANGE=" + ANT_RANGE +
                    ", ANT_TIME_DISTANCE=" + ANT_TIME_DISTANCE);
        } catch (Exception e) {
            System.err.println("Errore durante il caricamento della configurazione: " + e.getMessage());
        }
    }

    public static float getDefaultTimeDistance() {
        return ANT_TIME_DISTANCE; // Restituisce il valore di default
    }

    public static int getAntsQuantity() {
        return ANTS_QUANTITY; // Usa la variabile aggiornata
    }

    public static float getAntSpeed() {
        return ANT_SPEED; // Usa la variabile aggiornata
    }

    public static int getGridSpacing() {
        return GRID_SPACING; // Usa la variabile aggiornata
    }

    public static int getAntRange() {
        return ANT_RANGE; // Usa la variabile aggiornata
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
