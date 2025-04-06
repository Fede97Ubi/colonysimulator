package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ant {
    private float x, y;
    private Texture texture;

    public Ant(float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = new Texture("ant.png"); // Assicurati che ant.png sia nella cartella assets
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void dispose() {
        texture.dispose();
    }
}
