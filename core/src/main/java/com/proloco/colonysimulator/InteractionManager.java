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
        Zone foodZone = sceneManager.getFoodZone();
        Zone baseZone = sceneManager.getBaseZone();
        
        for (Ant ant : antManager.getAnts()) {
            float antX = ant.getX();
            float antY = ant.getY();
            
            // Se la formica è sopra la zona cibo e non ha cibo, impostala su true.
            if (foodZone != null && foodZone.contains(antX, antY) && !ant.hasFood()) {
                ant.setHasFood(true);
            }
            // Se la formica è sopra la zona base e possiede cibo, impostala su false.
            if (baseZone != null && baseZone.contains(antX, antY) && ant.hasFood()) {
                ant.setHasFood(false);
            }
        }
    }
}
