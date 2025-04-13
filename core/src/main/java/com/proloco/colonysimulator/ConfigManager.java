package com.proloco.colonysimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json; 
import java.util.Random;

public class ConfigManager {
    private static int GRID_SPACING = 20; // Spaziatura della griglia predefinita
    private static int ANTS_QUANTITY = 40; // Numero di formiche predefinito
    private static float ANT_SPEED = 3.0f; // Velocità delle formiche predefinita
    private static int ANT_RANGE = 3; // Raggio di ricerca delle formiche predefinito
    private static float ANT_TIME_DISTANCE = 40.0f; // Valore di default
    private static BlockConfig[] BLOCKS; // Array di configurazioni dei blocchi

    static {
        String jsonBlocks = generateBlocksJSON();
        System.out.println(jsonBlocks);
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
                BLOCKS = data.BLOCKS; // Carica i blocchi
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

    public static String generateBlocksJSON() {
        int AVAILABLE_WIDTH = 1920;
        int AVAILABLE_HEIGHT = 960;
        int numberOfBlocks = 20;
        StringBuilder sb = new StringBuilder();
        sb.append("{\n  \"BLOCKS\": [\n");
        // Calcola il numero minimo di unità per ottenere dimensioni > 50
        int minUnits = (int) Math.ceil(80.0 / GRID_SPACING);
        // Limite massimo in unità in base a un blocco non superiore a 350
        int maxUnits = 350 / GRID_SPACING;
        Random rand = new Random();

        for (int i = 0; i < numberOfBlocks; i++) {
            // Genera casualmente il numero di unità per larghezza (m) e altezza (n)
            int m = rand.nextInt(maxUnits - minUnits + 1) + minUnits;
            int n = rand.nextInt(maxUnits - minUnits + 1) + minUnits;

            int blockWidth = m * GRID_SPACING;
            int blockHeight = n * GRID_SPACING;

            // Calcola la posizione x seguendo la regola:
            // Se m è pari, x è multiplo di GRID_SPACING; se dispari, x è multiplo di GRID_SPACING + GRID_SPACING/2
            // Calcolo della coordinata x in base a m (numero di unità in larghezza)
            int x;          // m dispari -> x = multiplo di GRID_SPACING + GRID_SPACING/2
                int maxK = (AVAILABLE_WIDTH - blockWidth - GRID_SPACING / 2) / GRID_SPACING;
                int k = rand.nextInt(maxK + 1);
                x = k * GRID_SPACING + GRID_SPACING / 2;

            // Stessa logica per y in base a n
            int y;        // n dispari -> y = multiplo di GRID_SPACING + GRID_SPACING/2
                int maxJ = (AVAILABLE_HEIGHT - blockHeight - GRID_SPACING / 2) / GRID_SPACING;
                int j = rand.nextInt(maxJ + 1);
                y = j * GRID_SPACING + GRID_SPACING / 2;

            sb.append("    { \"x\": ").append(x)
                .append(", \"y\": ").append(y)
                .append(", \"width\": ").append(blockWidth)
                .append(", \"height\": ").append(blockHeight)
                .append(" }");
            
            if (i != numberOfBlocks - 1) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        
        sb.append("  ]\n}");
        return sb.toString();
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

    public static BlockConfig[] getBlocks() {
        return BLOCKS; // Restituisce i dettagli dei blocchi
    }

    // Classe interna per mappare i dati JSON
    private static class ConfigData {
        public int DEFAULT_GRID_SPACING;
        public int DEFAULT_ANTS_QUANTITY;
        public float DEFAULT_ANT_SPEED;
        public int DEFAULT_ANT_RANGE;
        public float DEFAULT_TIME_DISTANCE;
        public BlockConfig[] BLOCKS; // Array di blocchi
    }

    public static class BlockConfig {
        public float x;
        public float y;
        public float width;
        public float height;
    }
}
