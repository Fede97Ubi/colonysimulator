package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import java.text.DecimalFormat;

public class InfoGraph {
    private static final int ONE_SECOND = 1000;
    private static final int TEN_SECONDS = 10;
    private static final int THIRTY_SECONDS = 30;
    private static final int THREE_MINUTES = 180;
    private static final int INFO_PANEL_WIDTH = 400; // Width of the info panel

    private Array<Long>[] timeBuckets; // Buckets for 10s, 30s, 3m
    private long lastUpdateTime;
    private String[] descriptions;
    private BitmapFont font;
    private DecimalFormat decimalFormat;

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
        decimalFormat = new DecimalFormat("#.000"); // Format for three decimal places
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
        int screenHeight = Gdx.graphics.getHeight();
        int sectionHeight = screenHeight / descriptions.length; // Divide height equally

        for (int i = 0; i < descriptions.length; i++) {
            float avg10s = calculateAverage(timeBuckets[0], i);
            float avg30s = calculateAverage(timeBuckets[1], i);
            float avg3m = calculateAverage(timeBuckets[2], i);

            String description = descriptions[i];
            String values = "10s: " + decimalFormat.format(avg10s) + " ms, " +
                            "30s: " + decimalFormat.format(avg30s) + " ms, " +
                            "3m: " + decimalFormat.format(avg3m) + " ms";

            int yPosition = screenHeight - (i * sectionHeight) - 20; // Position for description
            font.draw(batch, description, Gdx.graphics.getWidth() - INFO_PANEL_WIDTH + 10, yPosition);

            yPosition -= 20; // Position for values below the description
            font.draw(batch, values, Gdx.graphics.getWidth() - INFO_PANEL_WIDTH + 10, yPosition);
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
