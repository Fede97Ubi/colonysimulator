package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Zone {
    private Circle bounds;
    private Color color;
    private String type;

    public Zone(float x, float y, float radius, Color color, String type) {
        this.bounds = new Circle(x, y, radius);
        this.color = color;
        this.type = type;
    }

    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    public boolean isNearby(float x, float y, float distance) {
        float centerDistance = Vector2.dst(x, y, bounds.x, bounds.y);
        return centerDistance <= bounds.radius + distance;
    }

    public float getCenterX() {
        return bounds.x;
    }

    public float getCenterY() {
        return bounds.y;
    }

    public float getRadius() {
        return bounds.radius;
    }

    public Color getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.circle(bounds.x, bounds.y, bounds.radius);
    }
}
