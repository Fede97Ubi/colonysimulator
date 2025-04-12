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
    private MarkerGrid markerGrid;

    @Override
    public void create() {
        batch = new SpriteBatch();
        System.out.println("Ant Speed: " + ConfigManager.getAntSpeed());
        shapeRenderer = new ShapeRenderer();

        // Inizializza SceneManager
        sceneManager = new SceneManager();
        sceneManager.initializeScene("world_01");

        simulationRunning = true; // Prepara la simulazione
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (simulationRunning) {
            float delta = Gdx.graphics.getDeltaTime();
        }

        // Delego il rendering al SceneManager
        sceneManager.render(batch, shapeRenderer);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        sceneManager.dispose();
    }
}
