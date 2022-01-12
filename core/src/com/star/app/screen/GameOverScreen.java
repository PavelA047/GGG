package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.utils.Assets;

public class GameOverScreen extends AbstractScreen {
    private BitmapFont font72;
    private BitmapFont font24;
    private int score;
    private int money;

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                    return true;
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        font72.draw(batch, "YOUR GAME IS OVER", 0, 600, 1280, Align.center, false);
        font24.draw(batch, "SCORE: " + score, 300, 500, 0, Align.left, false);
        font24.draw(batch, "$: " + money, 300, 450, 0, Align.left, false);
        batch.end();
    }

    public void heroIsDead(int score, int money) {
        this.score = score;
        this.money = money;
    }
}
