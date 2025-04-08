package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class AntManager {
    private Array<Ant> ants;

    public AntManager(int numberOfAnts, Zone baseZone) {
        ants = new Array<>();
        for (int i = 0; i < numberOfAnts; i++) {
            float angle = (float) (Math.random() * 2 * Math.PI);
            float radius = (float) (Math.random() * baseZone.getRadius());
            float x = baseZone.getCenterX() + (float) Math.cos(angle) * radius;
            float y = baseZone.getCenterY() + (float) Math.sin(angle) * radius;
            ants.add(new Ant(x, y));
        }
    }

    public void render(SpriteBatch batch) {
        for (Ant ant : ants) {
            ant.render(batch);
        }
    }

    public void update(float speed) {
        for (Ant ant : ants) {
            ant.move(speed);
        }
    }

    public void dispose() {
        for (Ant ant : ants) {
            ant.dispose();
        }
    }
}
