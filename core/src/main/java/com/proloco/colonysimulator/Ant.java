package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class Ant {
    private static int idCounter = 0; // Contatore statico per assegnare ID univoci
    private int id; // ID della formica
    private Texture texture;
    private float x, y;
    private float ANT_SPEED = ConfigManager.getAntSpeed();
    private float ANT_DIRECTION; // Direzione in radianti
    private boolean hasFood = false; // Stato per il cibo

    // Costante e variabile per timeDistance
    public static final float DEFAULT_TIME_DISTANCE = ConfigManager.getDefaultTimeDistance();
    private float timeDistance = this.DEFAULT_TIME_DISTANCE;

    public static final Color COLOR_WITHOUT_FOOD = new Color(1, 1, 1, 0.1f); // Bianco semitrasparente
    public static final Color COLOR_WITH_FOOD = new Color(1, 1, 0, 0.8f);    // Giallo semitrasparente

    public Ant(float x, float y) {
        this.id = ++idCounter; // Assegna un ID univoco
        this.x = x;
        this.y = y;
        this.ANT_DIRECTION = (float) (Math.random() * 2 * Math.PI); // Direzione casuale
        this.texture = new Texture("ant.png"); // Assicurati che ant.png sia nella cartella assets
    }

    // Getter e setter per hasFood
    public boolean hasFood() {
        return this.hasFood;
    }
    
    public void setHasFood(boolean value) {
        // Se cambia lo stato, reimposta timeDistance a DEFAULT_TIME_DISTANCE
        this.hasFood = value;
        resetTimeDistance(value);
    }
    
    // Getter per timeDistance
    public float getTimeDistance() {
        return timeDistance;
    }
    
    // Metodo per resettare il timeDistance
    private void resetTimeDistance(boolean value) {
        if ( value == true) {
            if (this.timeDistance > 0) {
                this.timeDistance = this.DEFAULT_TIME_DISTANCE - this.timeDistance + 1;
            } else {
                this.timeDistance = this.DEFAULT_TIME_DISTANCE;
            }
        } else {
            this.timeDistance = this.DEFAULT_TIME_DISTANCE;
        }
    }
    
    public float getDirection() {
        return this.ANT_DIRECTION;
    }
    
    public void setDirection(float direction) {
        this.ANT_DIRECTION = direction;
    }

    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    // Supponiamo che il raggio di collisione della formica sia la metà dell'altezza dell'immagine
    public float getCollisionRadius() {
        return texture.getHeight() / 2f;
    }

    public void update() {
        // Movimento della formica
        x += Math.cos(this.ANT_DIRECTION) * this.ANT_SPEED;
        y += Math.sin(this.ANT_DIRECTION) * this.ANT_SPEED;

        // Decrementa timeDistance ad ogni update (si può limitare a 0 se necessario)
        timeDistance -= 0.011f;
        if (timeDistance < 0) {
            timeDistance = 0;
        }
        
        // Controlla se la formica è sopra una zona food o base
        // (Questi metodi restituiscono true se le coordinate sono nel range; 
        // implementali secondo la logica reale dell'applicazione)
        if (!this.hasFood && isOnFoodZone()) {
            setHasFood(true);
        }
        if (this.hasFood && isOnBaseZone()) {
            setHasFood(false);
        }
    }

    public float perturbDirection(float currentDirection, float maxDelta) {
        // Calcola un delta casuale nell'intervallo [-maxDelta, maxDelta]
        float delta = (float)(Math.random() * 2 * maxDelta) - maxDelta;
        // Applica la variazione
        float newDirection = currentDirection + delta;
        // Normalizza l'angolo per renderlo compreso tra 0 e 2π
        newDirection = newDirection % ((float) (2 * Math.PI));
        if (newDirection < 0) {
            newDirection += 2 * Math.PI;
        }
        return newDirection;
    }

    public void updateDirection(MarkerGrid markerGrid) {
        // Il metodo restituisce -1 se nessuna cella rilevante viene trovata
        float newDirection = markerGrid.getDesiredDirection(this.x, this.y, this.hasFood);
        if (newDirection >= 0) {
            this.ANT_DIRECTION = newDirection;
        }
        this.ANT_DIRECTION = perturbDirection(this.ANT_DIRECTION, 0.1f);
    }

    // Questi metodi devono essere implementati in base alla logica della zona
    private boolean isOnFoodZone() {
        // Implementa la logica per verificare se la formica è sopra una zona food
        return false; // Sostituisci con la logica reale
    }

    private boolean isOnBaseZone() {
        // Implementa la logica per verificare se la formica è sopra una zona base
        return false; // Sostituisci con la logica reale
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if (batch != null) {
            // Rendering dell'immagine della formica
            float width = texture.getWidth();
            float height = texture.getHeight();
            float originX = width / 2;
            float originY = height / 2;
            float rotation = MathUtils.radiansToDegrees * ANT_DIRECTION;
            batch.draw(this.texture, this.x - originX, this.y - originY, originX, originY, width, height, 1, 1, rotation + 90, 0, 0, (int) width, (int) height, false, false);
        }

        if (shapeRenderer != null) {
            // Disegna il cerchietto con il colore appropriato
            shapeRenderer.setColor(this.hasFood ? this.COLOR_WITH_FOOD : this.COLOR_WITHOUT_FOOD);
            if (this.hasFood) {
                // float offsetX = (float) Math.cos(this.ANT_DIRECTION) * 15;
                // float offsetY = (float) Math.sin(this.ANT_DIRECTION) * 15;
                shapeRenderer.circle(this.x, this.y, 5); // Cerchio più grande per il cibo
            }

            // Disegna il cerchio esterno per indicare il raggio di collisione
            // shapeRenderer.setColor(new Color(1, 1, 1, 0.2f));
            // shapeRenderer.circle(this.x, this.y, texture.getHeight() / 2f);
        }

        // if (shapeRenderer != null && this.id == 1) {
        //     // Disegna un pallino rosso nell'angolo della formica con ID 1
        //     shapeRenderer.setColor(Color.RED);
        //     shapeRenderer.circle(this.x - 10, this.y + 10, 3);
        // }
    }

    public void dispose() {
        texture.dispose();
    }

    public void onCollision(Collidable other, Vector2 collisionPoint, Vector2 collisionNormal) {
        // Calcola il vettore attuale della formica
        Vector2 velocity = new Vector2(MathUtils.cos(ANT_DIRECTION), MathUtils.sin(ANT_DIRECTION));
        // Calcola la riflessione: R = V - 2*(V·N)*N
        float dot = velocity.dot(collisionNormal);
        Vector2 reflected = velocity.sub(new Vector2(collisionNormal).scl(2 * dot));
        // Aggiorna la direzione (in radianti)
        ANT_DIRECTION = reflected.angleRad();

        // Correzione di posizione se la formica è penetrata
        Vector2 center = new Vector2(x, y);
        float distance = center.dst(collisionPoint);
        if (distance < getCollisionRadius()) {
            float penetration = getCollisionRadius() - distance + 1;
            Vector2 correction = new Vector2(collisionNormal).scl(penetration);
            x += correction.x;
            y += correction.y;
        }
    }

    public int getId() {
        return id;
    }
}
