package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Renderer {
    private GameController gc;
    private SpriteBatch batch;

    public Renderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
    }

    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getAsteroid().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        batch.end();
    }
}
