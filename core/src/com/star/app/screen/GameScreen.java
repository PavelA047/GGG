package com.star.app.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.GameController;
import com.star.app.game.Renderer;
import com.star.app.screen.utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController gc;
    private Renderer renderer;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.gc = new GameController();
        this.renderer = new Renderer(gc, batch);
    }

    @Override
    public void render(float delta) {
        gc.update(delta);
        renderer.render();
    }
}
