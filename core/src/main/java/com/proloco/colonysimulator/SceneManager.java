package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // Import aggiunto per SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;


public class SceneManager {
    private Array<SceneObject> objects;
    private Background background;
    private Zone baseZone;
    private Zone foodZone;
    private MarkerGrid markerGrid;
    private AntManager antManager;
    private static final float ANT_SPEED = 2f; // Velocità delle formiche

    public SceneManager(MarkerGrid markerGrid) {
        this.markerGrid = markerGrid;
        objects = new Array<>();
    }

    public void addObject(SceneObject object) {
        objects.add(object);
    }

    public Array<SceneObject> getObjects() {
        return objects;
    }

    public void initializeScene(String sceneName) {
        objects.clear(); // Pulisce gli oggetti esistenti

        if ("world_01".equals(sceneName)) {
            background = new Background(false); // Usa il pattern

            // Posizione e dimensioni della zona base
            baseZone = new Zone(150, 650, 80, new Color(0.5f, 0.5f, 0.5f, 0.5f), "base");

            // Posizione e dimensioni della zona cibo
            foodZone = new Zone(1650, 150, 70, new Color(1f, 1f, 0f, 0.5f), "food");

            float blockWidth1 = 100;
            float blockHeight1 = 200;
            float centerX1 = (com.badlogic.gdx.Gdx.graphics.getWidth() - blockWidth1) / 2;
            float centerY1 = (com.badlogic.gdx.Gdx.graphics.getHeight() - blockHeight1) / 2;
            addObject(new Block(centerX1, centerY1, blockWidth1, blockHeight1)); // Primo blocco

            float blockWidth2 = 50;
            float blockHeight2 = 100;
            float centerX2 = centerX1 - 20; // 20 px più a sinistra
            float centerY2 = centerY1 - 200; // 200 px più in basso
            addObject(new Block(centerX2, centerY2, blockWidth2, blockHeight2)); // Secondo blocco

            antManager = new AntManager(10, baseZone); // Passa la zona base
        } else if ("world_02".equals(sceneName)) {
            background = new Background(true); // Usa un'immagine
        }

        // Puoi aggiungere altre scene qui
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        // Aggiorna le matrici
        markerGrid.updateMatrices();
        // Aggiorna le formiche prima del rendering
        if (antManager != null) {
            antManager.update(ANT_SPEED); // Sposta le formiche di 10 unità per frame
        }

        // Rendering con SpriteBatch (ad esempio per lo sfondo)
        batch.begin();
        if (background != null) {
            background.render(batch);
        }
        batch.end();
    
        // Abilita blending per ShapeRenderer
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    
        // Rendering della griglia
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (markerGrid != null) {
            markerGrid.renderActiveMatrix(shapeRenderer); // Renderizza la matrice attiva
        }
        shapeRenderer.end();
    
        // Rendering delle zone (base e cibo)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (baseZone != null) {
            baseZone.render(shapeRenderer);
        }
        if (foodZone != null) {
            foodZone.render(shapeRenderer);
        }
        shapeRenderer.end();
    
        // Rendering dei blocchi sopra la griglia e le zone
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (SceneObject object : objects) {
            object.render(shapeRenderer); // Renderizza i blocchi
        }
        shapeRenderer.end();
    
        // Rendering delle formiche sopra tutti gli altri oggetti
        batch.begin();
        if (antManager != null) {
            antManager.render(batch); // Renderizza le formiche
        }
        batch.end();
    
        // (Opzionale) Disabilita blending se non necessario per altri disegni
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    

    public void dispose() {
        if (background != null) {
            background.dispose();
        }
        if (antManager != null) {
            antManager.dispose();
        }
    }
}
