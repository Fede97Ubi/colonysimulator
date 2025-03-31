package com.proloco.colonysimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Ant {
    public float x, y;
    private float vx, vy;
    public float width, height;
    public float rotation;
    public Rectangle bounds;
    private boolean hasFood;
    private float directionChangeTimer;
    private float directionChangeInterval;
    private static final float SPEED = 80;
    private boolean isHeadingToFood;
    private boolean isHeadingToBase;
    private static final float move_rotation = 20f;
    private static final float min_rotation_interval = 1f;
    private static final float max_rotation_interval = 5f;

    public Ant(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        bounds = new Rectangle(x, y, width, height);
        setRandomDirection();
        this.hasFood = false;
        this.isHeadingToFood = false;
        this.isHeadingToBase = false;
        resetDirectionChangeTimer();
    }

    private void resetDirectionChangeTimer() {
        directionChangeInterval = MathUtils.random(min_rotation_interval, max_rotation_interval);
        directionChangeTimer = directionChangeInterval;
    }

    private void setRandomDirection() {
        float angle = MathUtils.random(360f);
        this.vx = SPEED * MathUtils.cosDeg(angle);
        this.vy = SPEED * MathUtils.sinDeg(angle);
        this.rotation = calculateRotation();
    }

    private void adjustDirection(float targetX, float targetY) {
        float dx = targetX - x;
        float dy = targetY - y;
        float angle = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;
        this.vx = SPEED * MathUtils.cosDeg(angle);
        this.vy = SPEED * MathUtils.sinDeg(angle);
        this.rotation = calculateRotation();
    }

    private float calculateRotation() {
        return (float) Math.toDegrees(MathUtils.atan2(vy, vx));
    }

    public void update(float delta, Zone foodZone, Zone baseZone) {
        if (!hasFood && !isHeadingToFood && foodZone.isNearby(x, y, 100)) {
            isHeadingToFood = true;
            adjustDirection(foodZone.getCenterX(), foodZone.getCenterY());
        } else if (hasFood && !isHeadingToBase) {
            isHeadingToBase = true;
            isHeadingToFood = false;
            adjustDirection(baseZone.getCenterX(), baseZone.getCenterY());
        } else if (!isHeadingToFood && !isHeadingToBase) {
            directionChangeTimer -= delta;
            if (directionChangeTimer <= 0) {
                float currentAngle = MathUtils.atan2(vy, vx) * MathUtils.radiansToDegrees;
                float newAngle = currentAngle + MathUtils.random(-move_rotation, move_rotation);
                this.vx = SPEED * MathUtils.cosDeg(newAngle);
                this.vy = SPEED * MathUtils.sinDeg(newAngle);
                this.rotation = calculateRotation();
                resetDirectionChangeTimer();
            }
        }

        x += vx * delta;
        y += vy * delta;
        bounds.setPosition(x, y);

        // Check zone interactions
        if (!hasFood && foodZone.contains(x, y)) {
            hasFood = true;
            isHeadingToFood = false;
            isHeadingToBase = true;
            adjustDirection(baseZone.getCenterX(), baseZone.getCenterY());
        } else if (hasFood && baseZone.contains(x, y)) {
            hasFood = false;
            isHeadingToBase = false;
            setRandomDirection();
        }

        // Screen boundaries
        if (x < 0 || x > Gdx.graphics.getWidth() - width) {
            vx = -vx;
            x = MathUtils.clamp(x, 0, Gdx.graphics.getWidth() - width);
            rotation = calculateRotation();
        }
        if (y < 0 || y > Gdx.graphics.getHeight() - height) {
            vy = -vy;
            y = MathUtils.clamp(y, 0, Gdx.graphics.getHeight() - height);
            rotation = calculateRotation();
        }
    }

    public boolean hasFood() {
        return hasFood;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public void setRotation(float angle) {
        this.rotation = angle;
        // Aggiorna l'immagine o la rappresentazione visiva dell'entità, se necessario
    }

    public float getRotation() {
        return this.rotation;
    }
}
