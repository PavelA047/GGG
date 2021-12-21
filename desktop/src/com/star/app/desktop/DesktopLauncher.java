package com.star.app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.star.app.MyStarGame;
import com.star.app.ScreenManager;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = ScreenManager.SCREEN_WIDTH;
        config.height = ScreenManager.SCREEN_HEIGHT;
        new LwjglApplication(new MyStarGame(), config);
    }
}
