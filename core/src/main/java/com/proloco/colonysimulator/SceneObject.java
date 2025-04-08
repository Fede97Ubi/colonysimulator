package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SceneObject implements Collidable {
    protected float x, y, width, height;
    protected Color fillColor;
    protected Color borderColor;
    protected Rectangle bounds;

    public SceneObject(float x, float y, float width, float height, Color fillColor, Color borderColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void render(ShapeRenderer shapeRenderer) {
        // Disegna il bordo
        shapeRenderer.setColor(borderColor);
        shapeRenderer.rect(x - 5, y - 5, width + 10, height + 10);

        // Disegna l'interno
        shapeRenderer.setColor(fillColor);
        shapeRenderer.rect(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    // Implementazione di Collidable (vuota, perché per un block di default non è definita una reazione)
    @Override
    public void onCollision(Collidable other, Vector2 collisionPoint, Vector2 collisionNormal) {
        // Nessuna azione per default
    }
}
