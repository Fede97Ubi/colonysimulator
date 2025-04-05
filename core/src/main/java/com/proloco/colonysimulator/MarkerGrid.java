package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;

public class MarkerGrid {
    public static final int GRID_SPACING = 20; // Spaziatura della griglia (uguale al background)
    private static float[][] trampleMatrix; // Matrice per identificare quali celle colorare
    private static float[][] baseDistanceMatrix; // Matrice per memorizzare informazioni sulla distanza dalla base
    private static float[][] foodDistanceMatrix; // Matrice per memorizzare informazioni sulla distanza dal cibo
    private static String activeMatrixType = "foodDistance"; // Valore predefinito: trampleMatrix

    // Commenta logiche dinamiche
    // public static void initializeMatrix(int width, int height) {
    //     int rows = height / GRID_SPACING;
    //     int cols = width / GRID_SPACING;
    //     trampleMatrix = new float[rows][cols];
    //     baseDistanceMatrix = new float[rows][cols]; // Inizializza baseDistanceMatrix
    //     foodDistanceMatrix = new float[rows][cols]; // Inizializza foodDistanceMatrix

    //     // Inizializza le matrici (esempio: tutte a 0)
    //     for (int i = 0; i < rows; i++) {
    //         for (int j = 0; j < cols; j++) {
    //             trampleMatrix[i][j] = 0;
    //             baseDistanceMatrix[i][j] = 0;
    //             foodDistanceMatrix[i][j] = 0;
    //         }
    //     }
    // }

    public static void setCell(int row, int col, float value, String matrixType) {
        float[][] matrix = getMatrixByType(matrixType);
        if (matrix != null && row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length) {
            matrix[row][col] = value;
        }
    }

    public static float getCellValue(int row, int col, String matrixType) {
        float[][] matrix = getMatrixByType(matrixType);
        if (matrix != null && row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length) {
            return matrix[row][col];
        }
        return 0;
    }

    // Commenta logiche dinamiche
    // public static void updateMatrix() {
    //     for (int row = 0; row < trampleMatrix.length; row++) {
    //         for (int col = 0; col < trampleMatrix[row].length; col++) {
    //             // Riduci i valori delle matrici più lentamente
    //             trampleMatrix[row][col] = Math.max(trampleMatrix[row][col] - 0.1f, 0);
    //             baseDistanceMatrix[row][col] = Math.max(baseDistanceMatrix[row][col] - 0.25f, 0);
    //             foodDistanceMatrix[row][col] = Math.max(foodDistanceMatrix[row][col] - 0.25f, 0);
    //         }
    //     }
    // }

    public static float[][] getMatrixByType(String matrixType) {
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

    public static void setActiveMatrixType(String matrixType) {
        if (!matrixType.equals("trample") && !matrixType.equals("base") && !matrixType.equals("food")) {
            throw new IllegalArgumentException("Tipo di matrice non valido: " + matrixType);
        }
        activeMatrixType = matrixType;
    }

    public static void renderGrid(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.GRAY);
        for (int x = 0; x < Gdx.graphics.getWidth(); x += GRID_SPACING) {
            shapeRenderer.line(x, 0, x, Gdx.graphics.getHeight());
        }
        for (int y = 0; y < Gdx.graphics.getHeight(); y += GRID_SPACING) {
            shapeRenderer.line(0, y, Gdx.graphics.getWidth(), y);
        }
    }
}
