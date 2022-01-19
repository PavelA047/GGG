package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Renderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font32;

    public Renderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
    }

    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getBulletController().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getParticleController().render(batch);
        gc.getHero().render(batch);
        gc.getHero().renderGUI(batch, font32);
        gc.getPowerUpsController().render(batch);
        gc.getInfoController().render(batch, font32);
        if (gc.isPause()) {
            font32.draw(batch, "PAUSE", 0, 600, ScreenManager.SCREEN_WIDTH, Align.center, false);
        }
        if (gc.isNextLevel()) {
            font32.draw(batch, "LEVEL: " + gc.getLevel(), 0, 600, ScreenManager.SCREEN_WIDTH, Align.center, false);
        }
        batch.end();
        gc.getStage().draw();
    }
}
