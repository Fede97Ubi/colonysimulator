package com.proloco.colonysimulator;

import com.badlogic.gdx.math.Vector2;

public class CollisionInfo {
    public Vector2 collisionPoint;
    public Vector2 collisionNormal;
    public float penetration;  // Penetrazione in pixel

    public CollisionInfo(Vector2 collisionPoint, Vector2 collisionNormal, float penetration) {
        this.collisionPoint = collisionPoint;
        this.collisionNormal = collisionNormal;
        this.penetration = penetration;
    }
}
