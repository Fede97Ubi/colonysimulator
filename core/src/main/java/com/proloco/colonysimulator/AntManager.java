package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class AntManager {
    private MarkerGrid markerGrid; // Dichiarazione spostata qui

    private Array<Ant> ants;
    private static final int ANTS_QUANTITY = ConfigManager.getAntsQuantity();

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
        for (Ant ant : ants) {
            ant.render(batch, shapeRenderer); // Passa entrambi i parametri
        }
    }

    public void update() {
        for (Ant ant : ants) {
            ant.update();
        }
    }

    public void updateMatrix() {
        markerGrid.updateFromAnts(ants);
    }

    public void updateAntsDirections() {
        for (Ant ant : ants) {
            ant.updateDirection(markerGrid);
        }
    }

    public void dispose() {
        for (Ant ant : ants) {
            ant.dispose();
        }
    }
}
