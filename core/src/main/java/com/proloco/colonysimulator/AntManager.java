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

    public AntManager(Zone foodZone, Zone baseZone, int antsCounter, Texture antTexture) {
        this.foodZone = foodZone;
        this.baseZone = baseZone;
        this.antTexture = antTexture;
        this.ants = new Array<>();
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

            Ant ant = new Ant(x, y, 20, 20); // Posiziona la formica
            ants.add(ant);
        }
    }

    public void updateAnts(float delta) {
        for (Ant ant : ants) {
            ant.update(delta, foodZone, baseZone);
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
        }
    }

    public Array<Ant> getAnts() {
        return ants;
    }

    public void dispose() {
        antTexture.dispose();
    }
}