package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // Import aggiunto per SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class SceneManager {
    private Array<SceneObject> objects;
    private Background background;

    public SceneManager() {
        objects = new Array<>();
    }

    public void addObject(SceneObject object) {
        objects.add(object);
    }

    public Array<SceneObject> getObjects() {
        return objects;
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (SceneObject object : objects) {
            object.render(shapeRenderer);
        }
    }

    public void initializeScene(String sceneName) {
        objects.clear(); // Pulisce gli oggetti esistenti

        if ("world_01".equals(sceneName)) {
            background = new Background(false); // Usa il pattern
            // Configura gli oggetti per la scena "world_01"
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
        } else if ("world_02".equals(sceneName)) {
            background = new Background(true); // Usa un'immagine
        }

        // Puoi aggiungere altre scene qui
    }

    public void renderBackground(SpriteBatch batch) {
        if (background != null) {
            background.render(batch);
        }
    }

    public void dispose() {
        if (background != null) {
            background.dispose();
        }
    }
}
