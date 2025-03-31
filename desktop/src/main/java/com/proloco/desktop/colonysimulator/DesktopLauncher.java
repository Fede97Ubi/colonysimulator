package com.proloco.colonysimulator.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import com.proloco.colonysimulator.Main;

public class DesktopLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Colony Simulator");
        config.setWindowedMode(1800, 800); // Imposta larghezza e altezza
        config.setResizable(false);      // Blocca il ridimensionamento della finestra
        new Lwjgl3Application(new Main(), config);
    }
}
