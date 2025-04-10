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
    private float speed; // Variabile per memorizzare la velocità
    private float direction; // Direzione in radianti
    private boolean hasFood = false; // Stato per il cibo

    public static final Color COLOR_WITHOUT_FOOD = new Color(1, 1, 1, 0f); // Bianco semitrasparente
    public static final Color COLOR_WITH_FOOD = new Color(1, 1, 0, 0.7f);    // Giallo semitrasparente

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
        this.hasFood = value;
        System.out.println("Ant has food: " + hasFood);
    }
    
    // Metodo per poter aggiornare (e possibilmente modificare) la direzione
    public float getDirection() {
        return direction;
    }
    
    public void setDirection(float direction) {
        this.direction = direction;
    }

    // Per comodità, mettiamo anche i getter per le coordinate e il raggio di collisione
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
        x += Math.cos(direction) * speed; // Usa la velocità memorizzata
        y += Math.sin(direction) * speed; // Usa la velocità memorizzata

        // Controlla se la formica è sopra una zona food
        if (!hasFood && isOnFoodZone()) {
            hasFood = true;
        }

        // Controlla se la formica è sopra una zona base
        if (hasFood && isOnBaseZone()) {
            hasFood = false;
        }
    }

    private boolean isOnFoodZone() {
        // Implementa la logica per verificare se la formica è sopra una zona food
        // Ad esempio, controlla le coordinate (x, y) rispetto alle zone food
        return false; // Sostituisci con la logica reale
    }

    private boolean isOnBaseZone() {
        // Implementa la logica per verificare se la formica è sopra una zona base
        // Ad esempio, controlla le coordinate (x, y) rispetto alle zone base
        return false; // Sostituisci con la logica reale
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        // Disegna l'immagine della formica
        float width = texture.getWidth();
        float height = texture.getHeight();
        float originX = width / 2;
        float originY = height / 2;
        float rotation = MathUtils.radiansToDegrees * direction; 
        batch.begin();
        batch.draw(texture, x - originX, y - originY, originX, originY, width, height, 1, 1, rotation + 90, 0, 0, (int) width, (int) height, false, false);
        batch.end();

        // Abilita il blending per la trasparenza
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Disegna il cerchietto con il colore appropriato
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(hasFood ? COLOR_WITH_FOOD : COLOR_WITHOUT_FOOD);
        shapeRenderer.circle(x, y, 4); // Disegna il centro come un piccolo cerchio
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(new Color(1, 1, 1, 0.2f)); // Colore semitrasparente per la circonferenza
        shapeRenderer.circle(x, y, height / 2); // Disegna la circonferenza con raggio pari all'altezza dell'immagine
        shapeRenderer.end();

        // Disabilita il blending dopo il disegno
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        texture.dispose();
    }

    public void onCollision(Collidable other, Vector2 collisionPoint, Vector2 collisionNormal) {
        // Calcola il vettore direzionale attuale della formica.
        Vector2 velocity = new Vector2(MathUtils.cos(direction), MathUtils.sin(direction));
        // Calcola la riflessione: R = V - 2*(V·N)*N
        float dot = velocity.dot(collisionNormal);
        Vector2 reflected = velocity.sub(new Vector2(collisionNormal).scl(2 * dot));
        // Aggiorna la direzione in radianti
        direction = reflected.angleRad();

        // Verifica se il centro della formica è penetrato nel blocco,
        // e se necessario sposta la formica lungo la normale per essere fuori.
        Vector2 center = new Vector2(x, y);
        float distance = center.dst(collisionPoint);
        if (distance < getCollisionRadius()) {
            float penetration = getCollisionRadius() - distance + 1; // aggiungiamo un piccolo offset
            Vector2 correction = new Vector2(collisionNormal).scl(penetration);
            x += correction.x;
            y += correction.y;
        }
    }
}
