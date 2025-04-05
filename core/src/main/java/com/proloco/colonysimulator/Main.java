package com.proloco.colonysimulator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.GL20;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private SceneManager sceneManager;
    private boolean simulationRunning = false;
    private static final float MAX_DELTA = 1 / 60f; // Limita il delta massimo a 1/30 di secondo (30 FPS)

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // Inizializza SceneManager per gestire gli oggetti statici
        sceneManager = new SceneManager();
        sceneManager.initializeScene("world_01"); // Configura la scena in base alla stringa
        simulationRunning = true; // Prepara la simulazione
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (simulationRunning) {
            float delta = Gdx.graphics.getDeltaTime();
            updateDynamicElements(delta); // Aggiorna gli elementi dinamici
        }

        // Delego il rendering al SceneManager
        sceneManager.render(batch, shapeRenderer);
    }

    private void updateDynamicElements(float delta) {
                // Esegui l'aggiornamento degli elementi dinamici
        // Nota: Qui puoi aggiungere la logica per gestire più thread in futuro
        // ...future dynamic update logic...
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        sceneManager.dispose();
    }
}
