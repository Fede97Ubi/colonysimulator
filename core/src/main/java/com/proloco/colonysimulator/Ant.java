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
    private Texture texture;
    private float x, y;
    private float speed; // Velocità della formica
    private float direction; // Direzione in radianti
    private boolean hasFood = false; // Stato per il cibo

    // Costante e variabile per timeDistance
    public static final float DEFAULT_TIME_DISTANCE = ConfigManager.getDefaultTimeDistance();
    private float timeDistance = DEFAULT_TIME_DISTANCE;

    public static final Color COLOR_WITHOUT_FOOD = new Color(1, 1, 1, 0.1f); // Bianco semitrasparente
    public static final Color COLOR_WITH_FOOD = new Color(1, 1, 0, 0.1f);    // Giallo semitrasparente

    public Ant(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed; // Inizializza la velocità
        this.direction = (float) (Math.random() * 2 * Math.PI); // Direzione casuale
        this.texture = new Texture("ant.png"); // Assicurati che ant.png sia nella cartella assets
    }

    // Getter e setter per hasFood
    public boolean hasFood() {
        return hasFood;
    }
    
    public void setHasFood(boolean value) {
        // Se cambia lo stato, reimposta timeDistance a DEFAULT_TIME_DISTANCE
        this.hasFood = value;
        resetTimeDistance();
    }
    
    // Getter per timeDistance
    public float getTimeDistance() {
        return timeDistance;
    }
    
    // Metodo per resettare il timeDistance
    private void resetTimeDistance() {
        this.timeDistance = DEFAULT_TIME_DISTANCE;
    }
    
    public float getDirection() {
        return direction;
    }
    
    public void setDirection(float direction) {
        this.direction = direction;
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
        x += Math.cos(direction) * speed;
        y += Math.sin(direction) * speed;

        // Decrementa timeDistance ad ogni update (si può limitare a 0 se necessario)
        timeDistance -= 0.02f;
        if (timeDistance < 0) {
            timeDistance = 0;
        }
        
        // Controlla se la formica è sopra una zona food o base
        // (Questi metodi restituiscono true se le coordinate sono nel range; 
        // implementali secondo la logica reale dell'applicazione)
        if (!hasFood && isOnFoodZone()) {
            setHasFood(true);
        }
        if (hasFood && isOnBaseZone()) {
            setHasFood(false);
        }
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
        // Rendering dell'immagine della formica
        float width = texture.getWidth();
        float height = texture.getHeight();
        float originX = width / 2;
        float originY = height / 2;
        float rotation = MathUtils.radiansToDegrees * direction;
        batch.begin();
        batch.draw(texture, x - originX, y - originY, originX, originY, width, height, 1, 1, rotation + 90, 0, 0, (int) width, (int) height, false, false);
        batch.end();

        // Abilita blending per la trasparenza
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Disegna il cerchietto con il colore appropriato
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(hasFood ? COLOR_WITH_FOOD : COLOR_WITHOUT_FOOD);
        shapeRenderer.circle(x, y, 1);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(1, 1, 1, 0.2f));
        shapeRenderer.circle(x, y, height / 2);
        shapeRenderer.end();

        // Disabilita blending
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        texture.dispose();
    }

    public void onCollision(Collidable other, Vector2 collisionPoint, Vector2 collisionNormal) {
        // Calcola il vettore attuale della formica
        Vector2 velocity = new Vector2(MathUtils.cos(direction), MathUtils.sin(direction));
        // Calcola la riflessione: R = V - 2*(V·N)*N
        float dot = velocity.dot(collisionNormal);
        Vector2 reflected = velocity.sub(new Vector2(collisionNormal).scl(2 * dot));
        // Aggiorna la direzione (in radianti)
        direction = reflected.angleRad();

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
}
