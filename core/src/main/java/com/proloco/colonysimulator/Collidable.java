package com.proloco.colonysimulator;

import com.badlogic.gdx.math.Vector2;

public interface Collidable {
    /**
     * Metodo chiamato in seguito a una collisione con un altro oggetto.
     * @param other L'altro oggetto coinvolto nella collisione.
     * @param collisionPoint Il punto di contatto calcolato.
     * @param collisionNormal La normale al piano di collisione (direzione in uscita).
     */
    void onCollision(Collidable other, Vector2 collisionPoint, Vector2 collisionNormal);
}
