package com.star.app.game;

import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background;
    private Hero hero;
    private BulletController bulletController;
    private AsteroidController asteroidController;

    public GameController() {
        this.asteroidController = new AsteroidController(this);
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
        asteroidController.setup(ScreenManager.SCREEN_WIDTH + 100, -100, -40, 40);
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);
    }
}
