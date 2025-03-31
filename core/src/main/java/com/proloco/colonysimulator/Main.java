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
    private Texture antTexture;
    private int antsCounter = 200; // Numero di formiche da creare
    private ShapeRenderer shapeRenderer;

    private Zone baseZone;
    private Zone foodZone;

    private SceneManager sceneManager;
    private AntManager antManager;
    private CollisionManager collisionManager; // Dichiarazione di collisionManager

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        antTexture = new Texture("ant.png");

        float baseRadius = 100f;
        float foodRadius = 70f;
        baseZone = new Zone(baseRadius * 2, Gdx.graphics.getHeight() - baseRadius * 2, 
                           baseRadius, new Color(0.5f, 0.5f, 0.5f, 0.5f), "base");
        foodZone = new Zone(Gdx.graphics.getWidth() - foodRadius * 2, 
                           foodRadius * 2, foodRadius, 
                           new Color(1f, 1f, 0f, 0.5f), "food");

        antManager = new AntManager(foodZone, baseZone, antsCounter, antTexture);

        sceneManager = new SceneManager();
        sceneManager.initializeScene("world_01"); // Inizializza la scena "world_01"

        collisionManager = new CollisionManager(); // Inizializzazione di collisionManager
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        antManager.updateAnts(delta);
        collisionManager.handleCollisions(antManager.getAnts(), sceneManager.getObjects());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        sceneManager.renderBackground(batch); // Disegna il background tramite SceneManager
        batch.end();

        // Disegna le zone con trasparenza
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        shapeRenderer.begin(ShapeType.Filled);
        // Disegna la zona base
        shapeRenderer.setColor(baseZone.getColor());
        shapeRenderer.circle(baseZone.getCenterX(), baseZone.getCenterY(), baseZone.getRadius());
        
        // Disegna la zona cibo
        shapeRenderer.setColor(foodZone.getColor());
        shapeRenderer.circle(foodZone.getCenterX(), foodZone.getCenterY(), foodZone.getRadius());
        shapeRenderer.end();
        
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
        batch.begin();
        antManager.renderAnts(batch); // Disegna tutte le formiche tramite AntManager
        batch.end();
        
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeType.Filled);
        antManager.renderFoodIndicators(shapeRenderer); // Disegna gli indicatori di cibo tramite AntManager
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        shapeRenderer.begin(ShapeType.Filled);
        sceneManager.render(shapeRenderer);
        shapeRenderer.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        antTexture.dispose();
        shapeRenderer.dispose();
        sceneManager.dispose(); // Libera le risorse gestite da SceneManager
        antManager.dispose(); // Libera le risorse gestite da AntManager
    }
}
