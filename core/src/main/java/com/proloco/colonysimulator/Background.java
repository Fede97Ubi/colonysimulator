package com.proloco.colonysimulator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    private Texture backgroundTexture;
    private Texture stripeTexture;
    private boolean useImageBackground;

    public Background(boolean useImageBackground) {
        this.useImageBackground = useImageBackground;

        if (useImageBackground) {
            stripeTexture = createStripeTexture(); // Crea il pattern diagonale
            backgroundTexture = new Texture("background.png"); // Carica l'immagine di sfondo
        } else {
            backgroundTexture = createPatternTexture(); // Crea il tema geometrico
        }
    }

    private Texture createPatternTexture() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        // Colori migliorati per un contrasto più visibile
        Color backgroundColor = new Color(0.25f, 0.25f, 0.25f, 1); // Più scuro
        Color gradientColor = new Color(0.15f, 0.15f, 0.15f, 1);   // Più chiaro
        Color lineColor = new Color(0.15f, 0.15f, 0.15f, 1);         // Ancora più chiaro

        pixmap.setColor(backgroundColor);
        pixmap.fill();

        int spacing = 20;
        int radius = 3;

        pixmap.setColor(gradientColor);
        for (int y = radius; y < height; y += spacing) {
            for (int x = 0; x < width; x++) {
                pixmap.drawPixel(x - radius-1, y - radius-1); // Sposta verso l'alto e sinistra
            }
        }
        for (int x = radius; x < width; x += spacing) {
            for (int y = 0; y < height; y++) {
                pixmap.drawPixel(x - radius-1, y - radius-1); // Sposta verso l'alto e sinistra
            }
        }
        for (int x = radius; x < width + spacing; x += spacing) {
            for (int y = radius; y < height + spacing; y += spacing) {
                pixmap.setColor(backgroundColor);
                pixmap.fillCircle(x - radius-1, y - radius-1, radius); // Sposta verso l'alto e sinistra
                pixmap.setColor(backgroundColor);
                pixmap.drawCircle(x - radius-1, y - radius-1, radius + 1); // Sposta verso l'alto e sinistra
                pixmap.setColor(lineColor);
                pixmap.drawCircle(x - radius-1, y - radius-1, radius); // Sposta verso l'alto e sinistra
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private Texture createStripeTexture() {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        // Colori delle strisce migliorati per un aspetto più morbido
        Color red = new Color(0.8f, 0.2f, 0.2f, 1);  // Rosso meno saturo
        Color white = new Color(0.9f, 0.9f, 0.9f, 1); // Bianco tendente al grigio

        // Disegna il pattern diagonale
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int diagonal = (x + y) % (40 * 2); // Strisce più larghe (40px)
                if (diagonal < 0) diagonal += 40 * 2; // Gestisce i valori negativi
                if (diagonal < 40) {
                    pixmap.setColor(white);
                } else {
                    pixmap.setColor(red);
                }
                pixmap.drawPixel(x, y);
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void render(SpriteBatch batch) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        if (useImageBackground) {
            // Disegna il pattern diagonale
            batch.draw(stripeTexture, 0, 0, screenWidth, screenHeight);

            // Disegna l'immagine centrata sopra il pattern
            int imgWidth = backgroundTexture.getWidth();
            int imgHeight = backgroundTexture.getHeight();
            int x = (screenWidth - imgWidth) / 2;
            int y = (screenHeight - imgHeight) / 2;
            batch.draw(backgroundTexture, x, y, imgWidth, imgHeight);
        } else {
            // Disegna il tema geometrico
            batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
        }
    }

    public void dispose() {
        if (useImageBackground) {
            stripeTexture.dispose();
            backgroundTexture.dispose();
        } else {
            backgroundTexture.dispose();
        }
    }
}
