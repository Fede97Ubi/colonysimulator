package com.proloco.colonysimulator;

public class InteractionManager {
    private SceneManager sceneManager;
    
    public InteractionManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
    
    /**
     * Controlla le interazioni tra le formiche ed i trigger (zone).
     * Se una formica passa sopra una zona cibo ed ha hasFood=false, allora imposta hasFood=true.
     * Se una formica passa sopra una zona base ed ha hasFood=true, allora imposta hasFood=false.
     */
    public void processInteractions(AntManager antManager) {
        ZoneMatrix foodZone = sceneManager.getFoodZone();
        Zone baseZone = sceneManager.getBaseZone();
        
        for (Ant ant : antManager.getAnts()) {
            float antX = ant.getX();
            float antY = ant.getY();
            
            // Converti le coordinate del mondo in coordinate della matrice
            int[] cellIndices = foodZone.getCellIndices(antX, antY);
            int row = cellIndices[0];
            int col = cellIndices[1];

            // Controlla se la formica è in una cella di cibo
            if (foodZone.getCellContent(row, col) > 0 && !ant.hasFood()) {
                ant.setHasFood(true);
                foodZone.decrementCellContent(row, col);
            }
            // Se la formica è sopra la zona base e possiede cibo, impostala su false.
            if (baseZone != null && baseZone.contains(antX, antY) && ant.hasFood()) {
                ant.setHasFood(false);
            }
        }
    }
}
