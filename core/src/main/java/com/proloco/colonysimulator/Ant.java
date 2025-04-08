package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ant {
    private float x, y;
    private Texture texture;
    private float direction; // Direzione in radianti

    public Ant(float x, float y) {
        this.x = x;
        this.y = y;
        this.direction = (float) (Math.random() * 2 * Math.PI); // Direzione casuale
        this.texture = new Texture("ant.png"); // Assicurati che ant.png sia nella cartella assets
    }

    public void render(SpriteBatch batch) {
        float width = texture.getWidth();
        float height = texture.getHeight();
        float originX = width / 2;
        float originY = height / 2;
        float rotation = (float) Math.toDegrees(direction); // Converti la direzione in gradi
        batch.draw(texture, x - originX, y - originY, originX, originY, width, height, 1, 1, rotation + 90, 0, 0, (int) width, (int) height, false, false);
    }

    public void move(float speed) {
        x += Math.cos(direction) * speed;
        y += Math.sin(direction) * speed;
    }

    public void dispose() {
        texture.dispose();
    }
}
