package com.proloco.colonysimulator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Block extends SceneObject {
    String BLOCK_TYPE;

    public Block(float x, float y, float width, float height) {
        super(x, y, width, height); // Rettangolo con dimensioni specificate
        this.BLOCK_TYPE = "block"; // Tipo di blocco
    }

    public Block(float x, float y, float width, float height, String blockType) {
        super(x, y, width, height); // Rettangolo con dimensioni specificate
        this.BLOCK_TYPE = blockType; // Tipo di blocco
    }

    @Override
    public void render(ShapeRenderer shapeRenderer) {
        if (BLOCK_TYPE.equals("block")) {

            float cornerRadius = 40; // Raggio degli angoli stondati
            float borderCornerRadius = cornerRadius + 4; // Raggio ridotto per il bordo

            // Disegna il bordo
            shapeRenderer.setColor(borderColor);
            shapeRenderer.rect(x - 5 + borderCornerRadius, y - 5, width + 10 - 2 * borderCornerRadius, height + 10); // Parte centrale
            shapeRenderer.rect(x - 5, y - 5 + borderCornerRadius, width + 10, height + 10 - 2 * borderCornerRadius); // Parte verticale
            shapeRenderer.circle(x - 5 + borderCornerRadius, y - 5 + borderCornerRadius, borderCornerRadius); // Angolo in basso a sinistra
            shapeRenderer.circle(x - 5 + width + 10 - borderCornerRadius, y - 5 + borderCornerRadius, borderCornerRadius); // Angolo in basso a destra
            shapeRenderer.circle(x - 5 + borderCornerRadius, y - 5 + height + 10 - borderCornerRadius, borderCornerRadius); // Angolo in alto a sinistra
            shapeRenderer.circle(x - 5 + width + 10 - borderCornerRadius, y - 5 + height + 10 - borderCornerRadius, borderCornerRadius); // Angolo in alto a destra

            // Disegna l'interno
            shapeRenderer.setColor(fillColor);
            shapeRenderer.rect(x + cornerRadius, y, width - 2 * cornerRadius, height); // Parte centrale
            shapeRenderer.rect(x, y + cornerRadius, width, height - 2 * cornerRadius); // Parte verticale
            shapeRenderer.circle(x + cornerRadius, y + cornerRadius, cornerRadius); // Angolo in basso a sinistra
            shapeRenderer.circle(x + width - cornerRadius, y + cornerRadius, cornerRadius); // Angolo in basso a destra
            shapeRenderer.circle(x + cornerRadius, y + height - cornerRadius, cornerRadius); // Angolo in alto a sinistra
            shapeRenderer.circle(x + width - cornerRadius, y + height - cornerRadius, cornerRadius); // Angolo in alto a destra
        } else if (BLOCK_TYPE.equals("line")) {
            shapeRenderer.setColor(borderColor);
            shapeRenderer.rect(x, y, width, height);
            shapeRenderer.setColor(colorLine);
            shapeRenderer.rect(x + 4, y + 4, 4, height - 8);

        }

    }
}