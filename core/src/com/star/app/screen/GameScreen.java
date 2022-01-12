package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.star.app.game.GameController;
import com.star.app.game.Renderer;
import com.star.app.screen.utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController gc;
    private Renderer renderer;
    private Stage stage;
    private boolean pauseFlag;
    private BitmapFont font72;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.gc = new GameController();
        this.renderer = new Renderer(gc, batch);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.pauseFlag = true;

        Gdx.input.setInputProcessor(stage);

        final Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        ImageButton.ImageButtonStyle imageButtonStylePause = new ImageButton.ImageButtonStyle();
        imageButtonStylePause.up = skin.getDrawable("pause");
        imageButtonStylePause.down = skin.getDrawable("pausePressed");
        imageButtonStylePause.over = skin.getDrawable("pauseMouse");
        final Button buttonPause = new ImageButton(imageButtonStylePause);
        buttonPause.setPosition(ScreenManager.SCREEN_WIDTH - 100, 10);

        ImageButton.ImageButtonStyle imageButtonStyleResume = new ImageButton.ImageButtonStyle();
        imageButtonStyleResume.up = skin.getDrawable("resume");
        imageButtonStyleResume.down = skin.getDrawable("resumePressed");
        imageButtonStyleResume.over = skin.getDrawable("resumeMouse");
        final Button buttonResume = new ImageButton(imageButtonStyleResume);
        buttonResume.setPosition(ScreenManager.SCREEN_WIDTH - 100, 10);

        ImageButton.ImageButtonStyle imageButtonStyleStop = new ImageButton.ImageButtonStyle();
        imageButtonStyleStop.up = skin.getDrawable("stop");
        imageButtonStyleStop.down = skin.getDrawable("stopPressed");
        imageButtonStyleStop.over = skin.getDrawable("stopMouse");
        Button buttonStop = new ImageButton(imageButtonStyleStop);
        buttonStop.setPosition(ScreenManager.SCREEN_WIDTH - 50, 10);

        buttonPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseFlag = false;
                buttonPause.remove();
                stage.addActor(buttonResume);
            }
        });

        buttonResume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseFlag = true;
                buttonResume.remove();
                stage.addActor(buttonPause);
            }
        });

        buttonStop.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        stage.addActor(buttonPause);
        stage.addActor(buttonStop);
        skin.dispose();
    }

    @Override
    public void render(float delta) {
        renderer.render();
        stage.act(delta);
        stage.draw();
        if (pauseFlag) {
            gc.update(delta);
        } else {
            batch.begin();
            font72.draw(batch, "PAUSE", 0, 600, 1280, Align.center, false);
            batch.end();
        }
    }
}
