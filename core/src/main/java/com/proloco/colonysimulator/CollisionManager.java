package com.proloco.colonysimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CollisionManager {

    private SceneManager sceneManager;

    public CollisionManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
    
    // Metodo per processare le collisioni dopo gli spostamenti
    public void processCollisions(AntManager antManager) {
        Array<Ant> ants = antManager.getAnts(); // si presuppone di avere questo metodo
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        
        for (Ant ant : ants) {
            float radius = ant.getCollisionRadius();
            float antX = ant.getX();
            float antY = ant.getY();
            
            // -------- Collisione con il bordo della finestra --------
            // Se colpisce un bordo verticale, inverte la componente x
            if (antX - radius < 0 || antX + radius > screenWidth) {
                float currentDir = ant.getDirection();
                // Inverte l'angolo orizzontale: newDir = PI - currentDir
                ant.setDirection(MathUtils.PI - ant.getDirection());
                // Correzione di posizione per evitare rimanenze fuori dallo schermo
                antX = MathUtils.clamp(antX, radius, screenWidth - radius);
            }
            // Se colpisce un bordo orizzontale, inverte la componente y
            if (antY - radius < 0 || antY + radius > screenHeight) {
                float currentDir = ant.getDirection();
                // Inverte l'angolo verticale: newDir = - currentDir
                ant.setDirection(-ant.getDirection());
                antY = MathUtils.clamp(antY, radius, screenHeight - radius);
            }
            
            // ----- Collisione con i blocchi -----
            // Per ogni block (SceneObject), controlliamo la collisione con il cerchio della formica.
            for (SceneObject object : sceneManager.getObjects()) {
                CollisionInfo info = getCollisionInfo(antX, antY, radius, object.getBounds());
                if (info != null) {
                    // Se la collisione viene rilevata, invochiamo onCollision sulla formica,
                    // così da farla riflettere tenendo conto del vettore normale.
                    ant.onCollision(object, info.collisionPoint, info.collisionNormal);
                }
            }
            
            // // -------- Interazione con le zone --------
            // // Si assume che SceneManager gestisca le zone, per esempio in variabili separate:
            // // baseZone e foodZone
            // Zone baseZone = sceneManager.getBaseZone();
            // ZoneMatrix foodZone = sceneManager.getFoodZone();
            // // Converti le coordinate del mondo in coordinate della matrice
            // int[] cellIndices = foodZone.getCellIndices(antX, antY);
            // int row = cellIndices[0];
            // int col = cellIndices[1];

            // // Controlla se la formica è in una cella di cibo
            // if (foodZone.getCellContent(row, col) > 0 && !ant.hasFood()) {
            //     ant.setHasFood(true);
            //     foodZone.decrementCellContent(row, col);
            // }
            // // Se il centro della formica entra nella zona base e possiede cibo:
            // if (baseZone != null && baseZone.contains(antX, antY) && ant.hasFood()) {
            //     ant.setHasFood(false);
            // }
        }
    }
    
    /**
     * Calcola le informazioni di collisione tra un cerchio (centro cx,cy e raggio) e un rettangolo.
     * Se il cerchio interseca il rettangolo, restituisce un oggetto CollisionInfo contenente
     * il punto di contatto, la normale e il valore di penetrazione. Altrimenti restituisce null.
     */
    private CollisionInfo getCollisionInfo(float cx, float cy, float radius, Rectangle rect) {
        // Trova il punto più vicino sul rettangolo al centro del cerchio
        float nearestX = MathUtils.clamp(cx, rect.x, rect.x + rect.width);
        float nearestY = MathUtils.clamp(cy, rect.y, rect.y + rect.height);
        Vector2 collisionPoint = new Vector2(nearestX, nearestY);
        
        // Calcola la distanza dal centro del cerchio al punto più vicino
        Vector2 diff = new Vector2(cx - nearestX, cy - nearestY);
        float dist2 = diff.len2();
        
        if (dist2 < radius * radius) {
            float distance = diff.len();
            float penetration = radius - distance;
            Vector2 normal;
            if (distance == 0) {
                // Se il centro è esattamente sopra il punto più vicino (caso raro), scegliamo una normale arbitraria
                normal = new Vector2(0, 1);
            } else {
                normal = diff.nor();
            }
            return new CollisionInfo(collisionPoint, normal, penetration);
        }
        return null;
    }
}
