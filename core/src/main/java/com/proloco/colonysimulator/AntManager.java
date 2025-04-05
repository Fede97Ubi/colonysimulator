package com.proloco.colonysimulator;

import com.badlogic.gdx.Gdx; // Import necessario per Gdx
import com.badlogic.gdx.math.MathUtils; // Import necessario per MathUtils
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public class AntManager {
    private Array<Ant> ants;
    private Zone foodZone;
    private Zone baseZone;
    private Texture antTexture;
    private float[][] baseDistanceMatrix; // Inizializza la matrice delle distanze verso la base
    private float[][] foodDistanceMatrix; // Inizializza la matrice delle distanze verso il cibo
    private float cellSize; // Inizializza la dimensione della cella

    public AntManager(Zone foodZone, Zone baseZone, int antsCounter, Texture antTexture) {
        this.foodZone = foodZone;
        this.baseZone = baseZone;
        this.antTexture = antTexture;
        this.ants = new Array<>();

        // Inizializza le matrici delle distanze
        int gridWidth = Gdx.graphics.getWidth() / MarkerGrid.GRID_SPACING;
        int gridHeight = Gdx.graphics.getHeight() / MarkerGrid.GRID_SPACING;
        this.baseDistanceMatrix = new float[gridWidth][gridHeight];
        this.foodDistanceMatrix = new float[gridWidth][gridHeight];
        this.cellSize = MarkerGrid.GRID_SPACING;

        createAnts(antsCounter);
    }

    private void createAnts(int antsCounter) {
        float baseCenterX = baseZone.getCenterX(); // Centro X della base
        float baseCenterY = baseZone.getCenterY(); // Centro Y della base
        float baseRadius = baseZone.getRadius();  // Raggio della base

        for (int i = 0; i < antsCounter; i++) {
            // Genera un raggio casuale all'interno della base
            float distance = MathUtils.random(0, baseRadius);
            // Genera un angolo casuale
            float angle = MathUtils.random(0, 360);
            // Calcola le coordinate polari
            float x = baseCenterX + distance * MathUtils.cosDeg(angle);
            float y = baseCenterY + distance * MathUtils.sinDeg(angle);

            createAnt(x, y);
        }
    }

    public void createAnt(float x, float y) {
        Ant ant = new Ant(x, y, 20, 20, "ai_test", cellSize); 
        ants.add(ant);
    }

    public void updateAnts(float delta) {
        for (Ant ant : ants) {
            ant.update(delta, foodZone, baseZone);

            // Calcola il centro della formica
            float centerX = ant.getX() + ant.width / 2;
            float centerY = ant.getY() + ant.height / 2;

            // Calcola la posizione della cella nella griglia basandosi sul centro
            int row = (int) (centerY / MarkerGrid.GRID_SPACING);
            int col = (int) (centerX / MarkerGrid.GRID_SPACING);

            // Incrementa il valore della cella nella matrice "trample"
            MarkerGrid.setCell(row, col, MarkerGrid.getCellValue(row, col, "trample") + 0.025f, "trample");

            // Aggiorna baseDistanceMatrix o foodDistanceMatrix in base a hasFood
            if (!ant.hasFood()) {
                float currentBaseDistance = MarkerGrid.getCellValue(row, col, "baseDistance");
                float currentLifeTime = ant.getLifeTime();
                if (currentBaseDistance < currentLifeTime) {
                    MarkerGrid.setCell(row, col, currentLifeTime, "baseDistance");
                }
                
            } else {
                float currentFoodDistance = MarkerGrid.getCellValue(row, col, "foodDistance");
                float currentLifeTime = ant.getLifeTime();
                if (currentFoodDistance < currentLifeTime) {
                    MarkerGrid.setCell(row, col, currentLifeTime, "foodDistance");
                };
            }
        }
    }

    public void renderAnts(SpriteBatch batch) {
        for (Ant ant : ants) {
            batch.draw(antTexture, 
                ant.x, ant.y,                    // Posizione
                ant.width / 2, ant.height / 2,   // Origine della rotazione (centro dell'immagine)
                ant.width, ant.height,           // Dimensioni
                1, 1,                            // Scale X e Y
                ant.rotation + 90,               // Rotazione
                0, 0,                            // Source X e Y nella texture
                antTexture.getWidth(), antTexture.getHeight(),  // Source width e height
                false, false);                   // Flip X e Y
        }
    }

    public void renderFoodIndicators(ShapeRenderer shapeRenderer) {
        for (Ant ant : ants) {
            if (ant.hasFood()) {
                shapeRenderer.setColor(Color.YELLOW);
                shapeRenderer.circle(ant.x + ant.width / 2, ant.y + ant.height + 5, 5);
            }
            if (ant.getId() == 1) {
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.circle(ant.x + ant.width / 2, ant.y - 5, 5); // Pallino rosso sotto
            }
        }
    }

    public Array<Ant> getAnts() {
        return ants;
    }

    public void dispose() {
        antTexture.dispose();
    }

    
}