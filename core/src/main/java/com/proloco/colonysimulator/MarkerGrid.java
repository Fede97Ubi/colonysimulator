package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class MarkerGrid {
    public static final int GRID_SPACING = ConfigManager.getGridSpacing();
    private float[][] trampleMatrix;
    private float[][] baseDistanceMatrix;
    private float[][] foodDistanceMatrix;
    private String activeMatrixType = "trampleMatrix"; // Variabile per la matrice attiva
    private int ANT_RANGE = ConfigManager.getAntRange(); // Raggio di ricerca per la matrice
    private Array<int[]> baseZoneCells; // Celle completamente incluse nella zona base
    private Array<int[]> foodZoneCells; // Celle completamente incluse nella zona cibo

    public MarkerGrid(Zone baseZone, Zone foodZone) {
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

        // Calcola le celle completamente incluse nelle zone
        baseZoneCells = calculateIncludedCells(baseZone);
        foodZoneCells = calculateIncludedCells(foodZone);
    }

    private Array<int[]> calculateIncludedCells(Zone zone) {
        Array<int[]> includedCells = new Array<>();
        int rows = trampleMatrix.length;
        int cols = trampleMatrix[0].length;
    
        // Ottieni il centro della zona
        float zoneCenterX = zone.getCenterX();
        float zoneCenterY = zone.getCenterY();
        // Soglia: raggio della zona meno la larghezza (o GRID_SPACING) della cella
        float threshold = zone.getRadius() - GRID_SPACING*2;
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                float cellX = j * GRID_SPACING;
                float cellY = i * GRID_SPACING;
                float cellCenterX = cellX + GRID_SPACING / 2f;
                float cellCenterY = cellY + GRID_SPACING / 2f;
    
                // Calcola la distanza dal centro della cella al centro della zona
                float distance = (float) Math.hypot(cellCenterX - zoneCenterX, cellCenterY - zoneCenterY);
                if (distance <= threshold) {
                    includedCells.add(new int[]{i, j});
                }
            }
        }
        return includedCells;
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
        System.out.println("Matrice non trovata: " + matrixType);
        return 0;
    }

    public float[][] getMatrixByType(String matrixType) {
        switch (matrixType) {
            case "trampleMatrix":
                return trampleMatrix;
            case "baseDistanceMatrix":
                return baseDistanceMatrix;
            case "foodDistanceMatrix":
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

        float defaultTimeDistance = ConfigManager.getDefaultTimeDistance() + 1f;

        // Aggiorna le celle della zona base
        for (int[] cell : baseZoneCells) {
            int i = cell[0];
            int j = cell[1];
            baseDistanceMatrix[i][j] = defaultTimeDistance;
        }
        for (int[] cell : foodZoneCells) {
            int i = cell[0];
            int j = cell[1];
            foodDistanceMatrix[i][j] = defaultTimeDistance;
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
                    float alpha = Math.min(value / 50f, 1f); // Calcola l'alpha (massimo 1)
                    shapeRenderer.setColor(new Color(1f, 1f, 1f, alpha)); // Colore basato sul valore
                    float x = j * GRID_SPACING;
                    float y = i * GRID_SPACING;
                    shapeRenderer.rect(x, y, GRID_SPACING, GRID_SPACING);
                }
            }
        }
    }

    public void setActiveMatrixType(String matrixType) {
        if (!matrixType.equals("trampleMatrix") && !matrixType.equals("baseDistanceMatrix") && !matrixType.equals("foodDistanceMatrix")) {
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

    public void updateFromAnts(Array<Ant> ants) {
        for (Ant ant : ants) {
            int i = (int) (ant.getY() / GRID_SPACING);
            int j = (int) (ant.getX() / GRID_SPACING);
    
            // Aggiorna sempre trampleMatrix con il valore timeDistance dell'ant
            if (i >= 0 && i < trampleMatrix.length && j >= 0 && j < trampleMatrix[0].length) {
                if (trampleMatrix[i][j] < ant.getTimeDistance()) {
                    trampleMatrix[i][j] = ant.getTimeDistance();
                }
            if (ant.hasFood()) {
                // Se invece ha cibo, aggiorna foodDistanceMatrix
                if (foodDistanceMatrix[i][j] < ant.getTimeDistance()) {
                    foodDistanceMatrix[i][j] = ant.getTimeDistance();
                }
            } else {
                // Se la formica NON ha cibo aggiorna anche baseDistanceMatrix
                if (baseDistanceMatrix[i][j] < ant.getTimeDistance()) {
                    baseDistanceMatrix[i][j] = ant.getTimeDistance();
                }
            }
            }
        }
    }
    
    public float getDesiredDirection(float antX, float antY, boolean hasFood) {
        // Determina la cella centrale in base alla posizione della formica
        int centerI = (int) (antY / GRID_SPACING);
        int centerJ = (int) (antX / GRID_SPACING);
        float maxVal = 0;
        int bestI = centerI;
        int bestJ = centerJ;
    
        // Scegli la matrice da ispezionare in base al valore di hasFood:
        // Se la formica ha cibo (hasFood == true) allora deve dirigersi verso la base;
        // altrimenti, se non ha cibo, deve dirigersi verso il cibo.
        float[][] matrixToSearch = hasFood ? baseDistanceMatrix : foodDistanceMatrix;
    
        // Scansiona un’area di 7x7 celle (range: -3 a +3 rispetto alla cella corrente)
        for (int i = centerI - ANT_RANGE; i <= centerI + ANT_RANGE; i++) {
            for (int j = centerJ - ANT_RANGE; j <= centerJ + ANT_RANGE; j++) {
                if (i < 0 || j < 0 || i >= matrixToSearch.length || j >= matrixToSearch[0].length)
                    continue;
                if (matrixToSearch[i][j] > maxVal) {
                    maxVal = matrixToSearch[i][j];
                    bestI = i;
                    bestJ = j;
                }
            }
        }
        // Se abbiamo trovato una cella con valore maggiore di 0, calcola la direzione
        if (maxVal > 0) {
            // Calcola il centro della cella corrente
            float currentCenterX = centerJ * GRID_SPACING + GRID_SPACING / 2f;
            float currentCenterY = centerI * GRID_SPACING + GRID_SPACING / 2f;
            // Calcola il centro della cella con il valore migliore
            float bestCenterX = bestJ * GRID_SPACING + GRID_SPACING / 2f;
            float bestCenterY = bestI * GRID_SPACING + GRID_SPACING / 2f;
            // Determina la differenza
            float dx = bestCenterX - currentCenterX;
            float dy = bestCenterY - currentCenterY;
            // Restituisce l'angolo (in radianti)
            return (float) ((Math.atan2(dy, dx) + 2 * Math.PI) % (2 * Math.PI));
        }
        // Se nessuna cella ha valore > 0, restituisce un indicatore (ad esempio -1) per non modificare la direzione
        return -1;
    }
    
}
