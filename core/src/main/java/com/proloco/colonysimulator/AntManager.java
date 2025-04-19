package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class AntManager {
    private MarkerGrid markerGrid; // Dichiarazione spostata qui

    private Array<Ant> ants;
    private static final int ANTS_QUANTITY = ConfigManager.getAntsQuantity();
    private long lastPrintTime = 0;

    public AntManager(Zone baseZone, MarkerGrid markerGrid) {
        this.markerGrid = markerGrid;
        
        ants = new Array<>();
        for (int i = 0; i < ANTS_QUANTITY; i++) {
            float angle = (float) (Math.random() * 2 * Math.PI);
            float radius = (float) (Math.random() * baseZone.getRadius());
            float x = baseZone.getCenterX() + (float) Math.cos(angle) * radius;
            float y = baseZone.getCenterY() + (float) Math.sin(angle) * radius;
            ants.add(new Ant(x, y)); // Passa la velocità al costruttore
        }
    }

    public Array<Ant> getAnts() {
        return ants;
    }
    
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.begin(); // Inizia il batch rendering per tutte le formiche
        for (Ant ant : ants) {
            ant.render(batch, null); // Renderizza solo le texture con SpriteBatch
        }
        batch.end(); // Termina il batch rendering

        // Abilita blending per la trasparenza
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Ant ant : ants) {
            ant.render(null, shapeRenderer); // Renderizza i cerchi con ShapeRenderer
        }
        shapeRenderer.end();

        // Disabilita blending dopo il rendering
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void update() {
        for (Ant ant : ants) {
            ant.update();
        }
    }

    public void updateMatrix() {
        markerGrid.updateFromAnts(ants);

        // // Stampa la sotto-matrice ogni 2 secondi
        // long currentTime = System.currentTimeMillis();
        // if (currentTime - lastPrintTime >= 2000) {
        //     printFoodDistanceMatrixForAnt1();
        //     lastPrintTime = currentTime;
        // }
    }

    public void updateAntsDirections() {
        for (Ant ant : ants) {
            ant.updateDirection(markerGrid);
        }
    }

    private void printFoodDistanceMatrixForAnt1() {
        Ant ant1 = ants.first(); // Supponiamo che la formica con ID 1 sia la prima
        for (Ant ant : ants) {
            if (ant.getId() == 1) {
                ant1 = ant;
                break;
            }
        }

        int centerI = (int) (ant1.getY() / markerGrid.GRID_SPACING);
        int centerJ = (int) (ant1.getX() / markerGrid.GRID_SPACING);
        int range = ConfigManager.getAntRange();
        float[][] matrix = markerGrid.getFoodDistanceMatrix();

        System.out.println("Sotto-matrice foodDistanceMatrix per la formica con ID 1:");
        for (int i = centerI + range; i >= centerI - range; i--) { // Inverti l'ordine delle righe
            if (i < 0 || i >= matrix.length) continue; // Salta righe fuori dai limiti
            for (int j = centerJ - range; j <= centerJ + range; j++) {
                if (j < 0 || j >= matrix[0].length) continue; // Salta colonne fuori dai limiti
                int value = (int) matrix[i][j]; // Rimuovi i valori decimali
                System.out.print(value == 0 ? "_ " : value + " "); // Sostituisci 0 con _
            }
            System.out.println();
        }
    }

    public void dispose() {
        for (Ant ant : ants) {
            ant.dispose();
        }
    }
}
