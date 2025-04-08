package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

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

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        // Disegna l'immagine della formica
        float width = texture.getWidth();
        float height = texture.getHeight();
        float originX = width / 2;
        float originY = height / 2;
        float rotation = (float) Math.toDegrees(direction); // Converti la direzione in gradi
        batch.begin();
        batch.draw(texture, x - originX, y - originY, originX, originY, width, height, 1, 1, rotation + 90, 0, 0, (int) width, (int) height, false, false);
        batch.end();

        // Abilita il blending per la trasparenza
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Disegna la circonferenza e il centro semitrasparente
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1, 1, 1, 0.2f)); // Colore semitrasparente per il centro
        shapeRenderer.circle(x, y, 5); // Disegna il centro come un piccolo cerchio
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(1, 1, 1, 0.5f)); // Colore semitrasparente per la circonferenza
        shapeRenderer.circle(x, y, height / 2); // Disegna la circonferenza con raggio pari all'altezza dell'immagine
        shapeRenderer.end();

        // Disabilita il blending dopo il disegno
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void move(float speed) {
        x += Math.cos(direction) * speed;
        y += Math.sin(direction) * speed;
    }

    public void dispose() {
        texture.dispose();
    }
}
