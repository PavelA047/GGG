package com.star.app.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.GameController;
import com.star.app.game.Renderer;

public class GameScreen extends AbstractScreen {
    private SpriteBatch batch;
    private GameController gc;
    private Renderer renderer;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        this.gc = new GameController();
        this.renderer = new Renderer(gc, batch);
    }

    @Override
    public void render(float delta) {
        gc.update(delta);
        renderer.render();
    }
}
