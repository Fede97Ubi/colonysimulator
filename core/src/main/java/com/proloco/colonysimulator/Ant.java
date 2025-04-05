// package com.proloco.colonysimulator;

// import com.badlogic.gdx.Gdx;
// import com.badlogic.gdx.math.MathUtils;
// import com.badlogic.gdx.math.Rectangle;
// import com.badlogic.gdx.math.Vector2;
// import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
// import com.badlogic.gdx.graphics.Color;
// import com.badlogic.gdx.utils.Array;

// public class Ant {
//     private static int nextId = 1; // Variabile statica per generare ID univoci
//     private final int id;         // ID univoco della formica
//     public float x, y;
//     private float vx, vy;
//     public float width, height;
//     public float rotation;
//     public Rectangle bounds;
//     private boolean hasFood = false;
//     private float directionChangeTimer;
//     private float directionChangeInterval;
//     // private static final float SPEED = 180;
//     private float SPEED = 180;
//     private float lifeTime = 15f;
//     private boolean isHeadingToFood;
//     private boolean isHeadingToBase;
//     private static final float move_rotation = 40f;
//     private static final float min_rotation_interval = 1f;
//     private static final float max_rotation_interval = 5f;
//     private String behaviorType;
//     private boolean onCollision; // Indica se la formica è in stato di collisione
//     private boolean colliding;   // Indica se la formica sta attualmente collidendo
//     private float cellSize; // Dimensione di una cella nella griglia
//     private float logTimer = 0; // Timer per il logging
//     private static float simulationTime = 0; // Tempo dall'inizio della simulazione
//     private float matrixCheckTimer = 0; // Timer per il controllo della matrice

//     public Ant(float x, float y, float width, float height, String behaviorType, float cellSize) {
//         this.id = nextId++; // Assegna un ID e incrementa il contatore
//         this.x = x;
//         this.y = y;
//         this.width = width;
//         this.height = height;
//         this.behaviorType = behaviorType;
//         this.cellSize = cellSize;
//         bounds = new Rectangle(x, y, width, height);
//         setRandomDirection();
//         this.hasFood = false;
//         this.isHeadingToFood = false;
//         this.isHeadingToBase = false;
//         resetDirectionChangeTimer();
//     }

//     private void resetDirectionChangeTimer() {
//         directionChangeInterval = MathUtils.random(min_rotation_interval, max_rotation_interval);
//         directionChangeTimer = directionChangeInterval;
//     }

//     private void setRandomDirection() {
//         float angle = MathUtils.random(360f);
//         this.vx = SPEED * MathUtils.cosDeg(angle);
//         this.vy = SPEED * MathUtils.sinDeg(angle);
//         this.rotation = calculateRotation();
//     }

//     private void adjustDirection(float targetX, float targetY) {
//         float dx = targetX - x;
//         float dy = targetY - y;
//         float angle = MathUtils.atan2(dy, dx) * MathUtils.radiansToDegrees;
//         this.vx = SPEED * MathUtils.cosDeg(angle);
//         this.vy = SPEED * MathUtils.sinDeg(angle);
//         this.rotation = calculateRotation();
//     }

//     private float calculateRotation() {
//         return (float) Math.toDegrees(MathUtils.atan2(vy, vx));
//     }

//     public void update(float delta, Zone foodZone, Zone baseZone) {
//         simulationTime += delta; // Incrementa il tempo della simulazione
//         matrixCheckTimer += delta;

//         if (matrixCheckTimer >= 0.25f) {
//             matrixCheckTimer = 0;

//             for (int i = -2; i <= 2; i++) {
//                 StringBuilder rowValues = new StringBuilder();
//                 for (int j = -2; j <= 2; j++) {
//                     int cellX = (int) ((x + width / 2) / cellSize) + i; // Usa il centro della formica
//                     int cellY = (int) ((y + height / 2) / cellSize) + j;

//                     if (cellX >= 0 && cellX < MarkerGrid.getMatrixByType("foodDistance").length &&
//                         cellY >= 0 && cellY < MarkerGrid.getMatrixByType("foodDistance")[0].length) {
//                         float value = MarkerGrid.getCellValue(cellX, cellY, hasFood ? "baseDistance" : "foodDistance");
//                         rowValues.append(String.format("%.2f ", value));
//                     } else {
//                         rowValues.append("N/A ");
//                     }
//                 }
//                 if (id == 1) {
//                     System.out.println(rowValues.toString().trim());
//                 }
//             }
//         }

//         if ("casual".equals(behaviorType)) {
//             // Comportamento casual (logica attuale)
//             if (!hasFood && !isHeadingToFood && foodZone.isNearby(x, y, 100)) {
//                 isHeadingToFood = true;
//                 adjustDirection(foodZone.getCenterX(), foodZone.getCenterY());
//             } else if (hasFood && !isHeadingToBase) {
//                 isHeadingToBase = true;
//                 isHeadingToFood = false;
//                 adjustDirection(baseZone.getCenterX(), baseZone.getCenterY());
//             } else if (!isHeadingToFood && !isHeadingToBase) {
//                 directionChangeTimer -= delta;
//                 if (directionChangeTimer <= 0) {
//                     float currentAngle = MathUtils.atan2(vy, vx) * MathUtils.radiansToDegrees;
//                     float newAngle = currentAngle + MathUtils.random(-move_rotation, move_rotation);
//                     this.vx = SPEED * MathUtils.cosDeg(newAngle);
//                     this.vy = SPEED * MathUtils.sinDeg(newAngle);
//                     this.rotation = calculateRotation();
//                     resetDirectionChangeTimer();
//                 }
//             }

//             // Check zone interactions
//             if (!hasFood && foodZone.contains(x, y)) {
//                 hasFood = true; // Aggiorna il parametro hasFood
//                 isHeadingToFood = false;
//                 isHeadingToBase = true;
//                 lifeTime = 15f; // Resetta lifeTime
//                 adjustDirection(baseZone.getCenterX(), baseZone.getCenterY());
//             } else if (hasFood && baseZone.contains(x, y)) {
//                 hasFood = false; // Aggiorna il parametro hasFood
//                 isHeadingToBase = false;
//                 lifeTime = 15f; // Resetta lifeTime
//                 setRandomDirection();
//             }
//         } else if ("ai_test".equals(behaviorType)) {
//             float[][] distanceMatrix = hasFood ? MarkerGrid.getMatrixByType("baseDistance") : MarkerGrid.getMatrixByType("foodDistance");
//             boolean foundTarget = false;
//             float targetX = 0, targetY = 0;
//             float maxDistanceValue = 0;

//             // Check zone interactions
//             if (!hasFood && foodZone.contains(x, y)) {
//                 hasFood = true; // Aggiorna il parametro hasFood
//                 isHeadingToFood = false;
//                 isHeadingToBase = true;
//                 lifeTime = 15f; // Resetta lifeTime
//                 // adjustDirection(baseZone.getCenterX(), baseZone.getCenterY());
//             } else if (hasFood && baseZone.contains(x, y)) {
//                 hasFood = false; // Aggiorna il parametro hasFood
//                 isHeadingToFood = true;
//                 isHeadingToBase = false;
//                 lifeTime = 15f; // Resetta lifeTime
//                 // setRandomDirection();
//             }
            
//             // Controlla il raggio di 2 caselle
//             for (int i = -2; i <= 2; i++) {
//                 StringBuilder rowValues = new StringBuilder(); // Per costruire una riga di valori
//                 for (int j = -2; j <= 2; j++) {
//                     int cellX = (int) (x / cellSize) + i;
//                     int cellY = (int) (y / cellSize) + j;

//                     if (cellX >= 0 && cellY >= 0) {
//                         float value = MarkerGrid.getCellValue(cellX, cellY, hasFood ? "baseDistance" : "foodDistance");
//                         rowValues.append(String.format("%.2f ", value)); // Aggiungi il valore formattato
//                         if (value > maxDistanceValue) {
//                             maxDistanceValue = value;
//                             targetX = cellX * cellSize + cellSize / 2;
//                             targetY = cellY * cellSize + cellSize / 2;
//                             foundTarget = true;
//                         }
//                     } else {
//                         rowValues.append("N/A "); // Valore non valido
//                     }
//                 }
//                 if (id == 1) { // Stampa solo per la formica con ID 1
//                     System.out.println(rowValues.toString().trim()); // Stampa la riga
//                 }
//             }

//             // Logging ogni mezzo secondo per la formica con ID 1
//             if (id == 1) {
//                 logTimer += delta;
//                 if (logTimer >= 0.5f) {
//                     if (foundTarget) {
//                         System.out.println("Ant ID: " + id + ", foundTarget: " + foundTarget +
//                                            ", targetX: " + targetX + ", targetY: " + targetY +
//                                            ", cellValue: " + maxDistanceValue +
//                                            ", simulationTime: " + Math.round(simulationTime * 100) + " centiseconds");
//                     } else {
//                         System.out.println("Ant ID: " + id + ", foundTarget: " + foundTarget +
//                                            ", simulationTime: " + Math.round(simulationTime * 100) + " centiseconds");
//                     }
//                     logTimer = 0;
//                 }
//                 System.out.println("--------------------------------------------------------");
//             }

//             if (foundTarget) {
//                 // Dirigiti verso il valore più alto
//                 adjustDirection(targetX, targetY);
//             } else {
//                 // Nessun valore diverso da 0 trovato, continua a vagare
//                 directionChangeTimer -= delta;
//                 if (directionChangeTimer <= 0) {
//                     float currentAngle = MathUtils.atan2(vy, vx) * MathUtils.radiansToDegrees;
//                     float newAngle = currentAngle + MathUtils.random(-move_rotation, move_rotation);
//                     this.vx = SPEED * MathUtils.cosDeg(newAngle);
//                     this.vy = SPEED * MathUtils.sinDeg(newAngle);
//                     this.rotation = calculateRotation();
//                     resetDirectionChangeTimer();
//                 }
//             }
//         }

//         // Rimuovi l'incremento errato di lifeTime
//         // Decrementa lifeTime correttamente
//         lifeTime -= delta;
//         if (lifeTime <= 0) {
//             lifeTime = 0; // Assicurati che non scenda sotto 0
//         }

//         // Movimento e interazioni ambientali (invariati)
//         x += vx * delta;
//         y += vy * delta;
//         bounds.setPosition(x, y);

//         // Screen boundaries
//         if (x < 0 || x > Gdx.graphics.getWidth() - width) {
//             vx = -vx;
//             x = MathUtils.clamp(x, 0, Gdx.graphics.getWidth() - width);
//             rotation = calculateRotation();
//         }
//         if (y < 0 || y > Gdx.graphics.getHeight() - height) {
//             vy = -vy;
//             y = MathUtils.clamp(y, 0, Gdx.graphics.getHeight() - height);
//             rotation = calculateRotation();
//         }
//     }

//     public boolean hasFood() {
//         return hasFood;
//     }

//     public float getVx() {
//         return vx;
//     }

//     public void setVx(float vx) {
//         this.vx = vx;
//     }

//     public float getVy() {
//         return vy;
//     }

//     public void setVy(float vy) {
//         this.vy = vy;
//     }

//     public void setRotation(float angle) {
//         this.rotation = angle;
//         // Aggiorna l'immagine o la rappresentazione visiva dell'entità, se necessario
//     }

//     public float getRotation() {
//         return this.rotation;
//     }

//     public boolean isOnCollision() {
//         return onCollision;
//     }

//     public void setOnCollision(boolean onCollision) {
//         this.onCollision = onCollision;
//     }

//     public boolean isColliding() {
//         return colliding;
//     }

//     public void setColliding(boolean colliding) {
//         this.colliding = colliding;
//     }

//     public float getX() {
//         return this.x;
//     }

//     public float getY() {
//         return this.y;
//     }

//     public int getId() {
//         return id; // Metodo per ottenere l'ID della formica
//     }

//     // Getter per lifeTime
//     public float getLifeTime() {
//         return lifeTime;
//     }
// }
