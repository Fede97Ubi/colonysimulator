package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class AntManager {
    private Array<Ant> ants;

    public AntManager(int numberOfAnts) {
        ants = new Array<>();
        for (int i = 0; i < numberOfAnts; i++) {
            ants.add(new Ant(0 + i * 50, 0)); // Posizioni incrementali
        }
    }

    public void render(SpriteBatch batch) {
        for (Ant ant : ants) {
            ant.render(batch);
        }
    }

    public void dispose() {
        for (Ant ant : ants) {
            ant.dispose();
        }
    }
}
