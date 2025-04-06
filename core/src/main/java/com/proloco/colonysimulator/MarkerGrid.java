package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;

public class MarkerGrid {
    public static final int GRID_SPACING = 20; // Spaziatura della griglia
    private float[][] trampleMatrix;
    private float[][] baseDistanceMatrix;
    private float[][] foodDistanceMatrix;
    private String activeMatrixType = "trample"; // Variabile per la matrice attiva

    public MarkerGrid() {
        int rows = Gdx.graphics.getHeight() / GRID_SPACING;
        int cols = Gdx.graphics.getWidth() / GRID_SPACING;

        // Inizializza le matrici
        trampleMatrix = new float[rows][cols];
        baseDistanceMatrix = new float[rows][cols];
        foodDistanceMatrix = new float[rows][cols];

        // Inizializza i valori delle matrici a 0
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                trampleMatrix[i][j] = 0;
                baseDistanceMatrix[i][j] = 0;
                foodDistanceMatrix[i][j] = 0;
            }
        }
    }

    public void setCell(int row, int col, float value, String matrixType) {
        float[][] matrix = getMatrixByType(matrixType);
        if (matrix != null && row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length) {
            matrix[row][col] = value;
        }
    }

    public float getCellValue(int row, int col, String matrixType) {
        float[][] matrix = getMatrixByType(matrixType);
        if (matrix != null && row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length) {
            return matrix[row][col];
        }
        System.out.println("Matrice non trovata: " + matrixType); // Corretto "system" in "System"
        return 0;
    }

    public float[][] getMatrixByType(String matrixType) {
        switch (matrixType) {
            case "trample":
                return trampleMatrix;
            case "baseDistance":
                return baseDistanceMatrix;
            case "foodDistance":
                return foodDistanceMatrix;
            default:
                throw new IllegalArgumentException("Tipo di matrice non valido: " + matrixType);
        }
    }

    public void updateMatrices() {
        // Riduci i valori di tutte le matrici di 0.01
        for (int i = 0; i < trampleMatrix.length; i++) {
            for (int j = 0; j < trampleMatrix[i].length; j++) {
                trampleMatrix[i][j] = Math.max(trampleMatrix[i][j] - 0.01f, 0);
                baseDistanceMatrix[i][j] = Math.max(baseDistanceMatrix[i][j] - 0.01f, 0);
                foodDistanceMatrix[i][j] = Math.max(foodDistanceMatrix[i][j] - 0.01f, 0);
            }
        }
    }

    public void renderActiveMatrix(ShapeRenderer shapeRenderer) {
        // Ottieni la matrice attiva
        float[][] activeMatrix = getMatrixByType(activeMatrixType);

        // Renderizza solo la matrice attiva
        for (int i = 0; i < activeMatrix.length; i++) {
            for (int j = 0; j < activeMatrix[i].length; j++) {
                float value = activeMatrix[i][j];
                if (value > 0) {
                    float alpha = Math.min(value / 10f, 1f); // Calcola l'alpha (massimo 1)
                    shapeRenderer.setColor(new Color(1f, 1f, 1f, alpha)); // Colore basato sul valore
                    float x = j * GRID_SPACING;
                    float y = i * GRID_SPACING;
                    shapeRenderer.rect(x, y, GRID_SPACING, GRID_SPACING);
                }
            }
        }
    }

    public void setActiveMatrixType(String matrixType) {
        if (!matrixType.equals("trample") && !matrixType.equals("baseDistance") && !matrixType.equals("foodDistance")) {
            throw new IllegalArgumentException("Tipo di matrice non valido: " + matrixType);
        }
        activeMatrixType = matrixType;
    }

    public String getActiveMatrixType() {
        return activeMatrixType;
    }

    public void renderGrid(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.GRAY);
        for (int x = 0; x < Gdx.graphics.getWidth(); x += GRID_SPACING) {
            shapeRenderer.line(x, 0, x, Gdx.graphics.getHeight());
        }
        for (int y = 0; y < Gdx.graphics.getHeight(); y += GRID_SPACING) {
            shapeRenderer.line(0, y, Gdx.graphics.getWidth(), y);
        }
    }

    public float[][] getTrampleMatrix() {
        return trampleMatrix;
    }

    public float[][] getBaseDistanceMatrix() {
        return baseDistanceMatrix;
    }

    public float[][] getFoodDistanceMatrix() {
        return foodDistanceMatrix;
    }
}
