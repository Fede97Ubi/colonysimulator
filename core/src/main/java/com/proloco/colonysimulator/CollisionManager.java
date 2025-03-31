package com.proloco.colonysimulator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;

public class CollisionManager {
    public void handleCollisions(Array<Ant> ants, Array<SceneObject> objects) {
        for (Ant ant : ants) {
            for (SceneObject object : objects) {
                if (object instanceof Block && ant.bounds.overlaps(object.getBounds())) {
                    // Calcola il centro della formica
                    Vector2 antCenter = new Vector2(ant.bounds.x + ant.bounds.width / 2, ant.bounds.y + ant.bounds.height / 2);

                    // Calcola il punto più vicino sul bordo del Block
                    Vector2 blockBounds = new Vector2(object.getBounds().x, object.getBounds().y);
                    float blockWidth = object.getBounds().width;
                    float blockHeight = object.getBounds().height;

                    // Trova il punto più vicino sul bordo del Block
                    float closestX = MathUtils.clamp(antCenter.x, blockBounds.x, blockBounds.x + blockWidth);
                    float closestY = MathUtils.clamp(antCenter.y, blockBounds.y, blockBounds.y + blockHeight);
                    Vector2 closestPoint = new Vector2(closestX, closestY);

                    // Calcola la normale basandosi sul punto più vicino
                    Vector2 normal = antCenter.sub(closestPoint).nor();

                    // Calcola la velocità come vettore
                    Vector2 velocity = new Vector2(ant.getVx(), ant.getVy());

                    // Riflette la velocità rispetto alla normale
                    Vector2 reflectedVelocity = velocity.sub(normal.scl(2 * velocity.dot(normal)));

                    // Imposta la nuova velocità
                    ant.setVx(reflectedVelocity.x);
                    ant.setVy(reflectedVelocity.y);

                    // Aggiorna l'orientamento dell'immagine in base alla nuova direzione
                    ant.setRotation(reflectedVelocity.angleDeg());

                    break;
                }
            }
        }
    }
}
