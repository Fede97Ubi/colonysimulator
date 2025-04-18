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
    private ZoneMatrix foodZone;
    private MarkerGrid markerGrid; // Dichiarazione spostata qui
    private CollisionManager collisionManager;
    private InteractionManager interactionManager;
    private AntManager antManager;
    private InfoGraph infoGraph;
    private long[] executionTimes = new long[10]; // Array to store execution times
    int width = Gdx.graphics.getWidth();
    int height = Gdx.graphics.getHeight();
    int width_info_pannel = 400; // Larghezza del pannello delle informazioni

    public SceneManager() {
        objects = new Array<>();
        collisionManager = new CollisionManager(this);
        interactionManager = new InteractionManager(this);
        infoGraph = new InfoGraph();
    }

    public void addObject(SceneObject object) {
        objects.add(object);
    }

    public Array<SceneObject> getObjects() {
        return objects;
    }

    public Zone getBaseZone() {
        return baseZone;
    }
    
    public ZoneMatrix getFoodZone() {
        return foodZone;
    }

    public void initializeScene(String sceneName) {
        objects.clear(); // Pulisce gli oggetti esistenti

        if ("world_01".equals(sceneName)) {
            background = new Background(false, width - width_info_pannel, height); // Usa il pattern

            baseZone = new Zone(150, height - 150, 80, new Color(0.5f, 0.5f, 0.5f, 0.5f), "base");
            markerGrid = new MarkerGrid( width - width_info_pannel, height, baseZone);
            foodZone = new ZoneMatrix(markerGrid, width - width_info_pannel, height, new Color(1f, 1f, 0f, 0.5f), "food");
            foodZone.addZone(width - 150 - width_info_pannel, 150, 70, 20); // Aggiungi una zona cibo

            // aggiungere blocco per dividere da info_pannel
            addObject(new Block(width - width_info_pannel -12, -5, width_info_pannel + 12, height+10, "line"));

            // Itera sui blocchi definiti in ConfigManager
            for (ConfigManager.BlockConfig block : ConfigManager.getBlocks()) {
                addObject(new Block(block.x, block.y, block.width, block.height));
            }

            antManager = new AntManager(baseZone, markerGrid); // Passa la zona base
        } else if ("world_02".equals(sceneName)) {
            background = new Background(true, width - width_info_pannel, height); // Usa un'immagine
        }

        // Puoi aggiungere altre scene qui
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        // Aggiorna le matrici
        markerGrid.updateMatrices();
        // Aggiorna le formiche prima del rendering
        long start, end;

        start = System.nanoTime();
        if (antManager != null) {
            antManager.update(); // sposta le formiche
        }
        end = System.nanoTime();
        executionTimes[0] = (end - start) / 1_000_000;

        // Rendering con SpriteBatch (ad esempio per lo sfondo)
        start = System.nanoTime();
        batch.begin();
        if (background != null) {
            background.render(batch);
        }
        batch.end();
        end = System.nanoTime();
        executionTimes[1] = (end - start) / 1_000_000;
    
        // Abilita blending per ShapeRenderer
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    
        // Rendering della griglia
        start = System.nanoTime();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (markerGrid != null) {
            markerGrid.renderActiveMatrix(shapeRenderer); // Renderizza la matrice attiva
        }
        shapeRenderer.end();
        end = System.nanoTime();
        executionTimes[2] = (end - start) / 1_000_000;
    
        // Rendering delle zone (base e cibo)
        start = System.nanoTime();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if (baseZone != null) {
            baseZone.render(shapeRenderer);
        }
        if (foodZone != null) {
            foodZone.render(shapeRenderer);
        }
        shapeRenderer.end();
        end = System.nanoTime();
        executionTimes[3] = (end - start) / 1_000_000;
    
        // Rendering dei blocchi sopra la griglia e le zone
        start = System.nanoTime();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (SceneObject object : objects) {
            object.render(shapeRenderer); // Renderizza i blocchi
        }
        shapeRenderer.end();
        end = System.nanoTime();
        executionTimes[4] = (end - start) / 1_000_000;
    
        // Rendering delle formiche sopra tutti gli altri oggetti
        start = System.nanoTime();
        batch.begin();
        if (antManager != null) {
            batch.end(); // Chiudi il batch prima di usare ShapeRenderer
            antManager.render(batch, shapeRenderer); // Passa entrambi i parametri
        }
        else {
            batch.end();
        }
        end = System.nanoTime();
        executionTimes[5] = (end - start) / 1_000_000;

        // -------- Controllo collisioni dopo il movimento --------
        start = System.nanoTime();
        if (antManager != null) {
            collisionManager.processCollisions(antManager);
        }
        end = System.nanoTime();
        executionTimes[6] = (end - start) / 1_000_000;

        // -------- Controllo interazioni (trigger delle zone) --------
        start = System.nanoTime();
        if (antManager != null) {
            interactionManager.processInteractions(antManager);
        }
        end = System.nanoTime();
        executionTimes[7] = (end - start) / 1_000_000;

        start = System.nanoTime();
        if (antManager != null) {
            antManager.updateMatrix();
        }
        end = System.nanoTime();
        executionTimes[8] = (end - start) / 1_000_000;

        start = System.nanoTime();
        if (antManager != null) {
            antManager.updateAntsDirections();
        }
        end = System.nanoTime();
        executionTimes[9] = (end - start) / 1_000_000;
    
        // (Opzionale) Disabilita blending se non necessario per altri disegni
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Update and render InfoGraph
        infoGraph.update(executionTimes);
        infoGraph.render(batch, shapeRenderer);
    }
    

    public void dispose() {
        if (background != null) {
            background.dispose();
        }
        if (antManager != null) {
            antManager.dispose();
        }
        if (infoGraph != null) {
            infoGraph.dispose();
        }
    }
}
