package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.star.app.game.Background;
import com.star.app.game.Hero;
import com.star.app.screen.utils.Assets;

public class GameOverScreen extends AbstractScreen {
    private Background background;
    private BitmapFont font72;
    private BitmapFont font48;
    private BitmapFont font24;
    private StringBuilder sb;
    private Hero hero;
    private Music music;

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
        this.sb = new StringBuilder();
    }

    @Override
    public void show() {
        this.music = Assets.getInstance().getAssetManager().get("audio/music.mp3");
        this.music.setLooping(true);
        this.music.play();
        this.background = new Background(null);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font48 = Assets.getInstance().getAssetManager().get("fonts/font48.ttf");
    }

    public void update(float dt) {
        background.update(dt);
        if (Gdx.input.justTouched()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "YOUR GAME IS OVER", 0, 600, ScreenManager.SCREEN_WIDTH, Align.center, false);
        sb.setLength(0);
        sb.append("SCORE: ").append(hero.getScore()).append("\n");
        sb.append("$: ").append(hero.getMoney()).append("\n");
        font48.draw(batch, sb, 0, 400, ScreenManager.SCREEN_WIDTH, Align.center, false);
        font24.draw(batch, "Tap to continue", 0, 60, ScreenManager.SCREEN_WIDTH, Align.center, false);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
