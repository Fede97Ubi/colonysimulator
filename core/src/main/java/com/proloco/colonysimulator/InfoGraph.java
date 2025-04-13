package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class InfoGraph {
    private static final int ONE_SECOND = 1000;
    private static final int TEN_SECONDS = 10;
    private static final int THIRTY_SECONDS = 30;
    private static final int THREE_MINUTES = 180;

    private Array<Long>[] timeBuckets; // Buckets for 10s, 30s, 3m
    private long lastUpdateTime;
    private String[] descriptions;
    private BitmapFont font;

    public InfoGraph() {
        timeBuckets = new Array[3];
        for (int i = 0; i < 3; i++) {
            timeBuckets[i] = new Array<>();
        }
        lastUpdateTime = System.currentTimeMillis();

        descriptions = new String[] {
            "AntManager Update",
            "Background Render",
            "MarkerGrid Render",
            "BaseZone and FoodZone Render",
            "SceneObject Render",
            "AntManager Render",
            "CollisionManager Process",
            "InteractionManager Process",
            "AntManager UpdateMatrix",
            "AntManager UpdateDirections"
        };

        font = new BitmapFont(); // Initialize font for rendering text
    }

    public void update(long[] executionTimes) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime >= ONE_SECOND) {
            for (int i = 0; i < executionTimes.length; i++) {
                timeBuckets[0].add(executionTimes[i]);
                if (timeBuckets[0].size > TEN_SECONDS) timeBuckets[0].removeIndex(0);

                timeBuckets[1].add(executionTimes[i]);
                if (timeBuckets[1].size > THIRTY_SECONDS) timeBuckets[1].removeIndex(0);

                timeBuckets[2].add(executionTimes[i]);
                if (timeBuckets[2].size > THREE_MINUTES) timeBuckets[2].removeIndex(0);
            }
            lastUpdateTime = currentTime;
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.begin();
        int yOffset = 20;
        for (int i = 0; i < descriptions.length; i++) {
            float avg10s = calculateAverage(timeBuckets[0], i);
            float avg30s = calculateAverage(timeBuckets[1], i);
            float avg3m = calculateAverage(timeBuckets[2], i);

            font.draw(
                batch,
                descriptions[i] + " - Avg (10s): " + avg10s + " ms, Avg (30s): " + avg30s + " ms, Avg (3m): " + avg3m + " ms",
                10,
                yOffset
            );
            yOffset += 20;
        }
        batch.end();
    }

    public void dispose() {
        font.dispose(); // Dispose of the font when no longer needed
    }

    private float calculateAverage(Array<Long> times, int index) {
        if (times.size == 0) return 0;
        long sum = 0;
        for (int i = index; i < times.size; i += descriptions.length) {
            sum += times.get(i);
        }
        return sum / (float) (times.size / descriptions.length);
    }
}
