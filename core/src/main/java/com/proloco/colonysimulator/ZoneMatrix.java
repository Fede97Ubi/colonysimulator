package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

public class ZoneMatrix {
    private static final int GRID_SPACING = ConfigManager.getGridSpacing();
    private int[][] matrix; // Matrice per i valori delle celle
    private int rows, cols;
    private Color zoneColor;
    private String zoneName;
    private MarkerGrid markerGrid; // Riferimento alla griglia dei marker

    public ZoneMatrix(MarkerGrid markerGrid, int width, int height, Color zoneColor, String zoneName) {
        this.rows = height / GRID_SPACING;
        this.cols = width / GRID_SPACING;
        this.matrix = new int[rows][cols];
        this.zoneColor = zoneColor;
        this.zoneName = zoneName;
        this.markerGrid = markerGrid; // Inizializza il riferimento alla griglia dei marker
    }

    public void addZone(float centerX, float centerY, float radius, int initialContent) {
        int startRow = Math.max(0, (int) ((centerY - radius) / GRID_SPACING));
        int endRow = Math.min(rows - 1, (int) ((centerY + radius) / GRID_SPACING));
        int startCol = Math.max(0, (int) ((centerX - radius) / GRID_SPACING));
        int endCol = Math.min(cols - 1, (int) ((centerX + radius) / GRID_SPACING));

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                float cellCenterX = col * GRID_SPACING + GRID_SPACING / 2f;
                float cellCenterY = row * GRID_SPACING + GRID_SPACING / 2f;

                if (Math.hypot(cellCenterX - centerX, cellCenterY - centerY) <= radius) {
                    matrix[row][col] = initialContent; // Imposta il contenuto iniziale
                }
            }
        }
    }

    public void decrementCellContent(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols && matrix[row][col] > 0) {
            matrix[row][col]--;
            if (matrix[row][col] <= 0) {
                matrix[row][col] = 0; // Assicurati che il contenuto non scenda sotto zero
                markerGrid.setZone(zoneName, getArrayCells()); // Disabilita il marker se il contenuto è zero
                markerGrid.resetCell(row, col); // Resetta la cella nella matrice dei marker
            }
        }
    }

    public int getCellContent(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return matrix[row][col];
        }
        return 0;
    }

    public void render(ShapeRenderer shapeRenderer) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int content = matrix[row][col];
                if (content > 0) {
                    float alpha = Math.min(content / 20f, 1f); // Trasparenza proporzionale
                    shapeRenderer.setColor(new Color(zoneColor.r, zoneColor.g, zoneColor.b, alpha));
                    shapeRenderer.rect(col * GRID_SPACING, row * GRID_SPACING, GRID_SPACING, GRID_SPACING);
                }
            }
        }
    }

    public int[] getCellIndices(float x, float y) {
        int row = (int) (y / GRID_SPACING);
        int col = (int) (x / GRID_SPACING);
        return new int[]{row, col};
    }

    public Array<int[]> getArrayCells() {
        Array<int[]> cells = new Array<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (matrix[row][col] > 0) {
                    cells.add(new int[]{row, col});
                }
            }
        }
        return cells;
    }
}