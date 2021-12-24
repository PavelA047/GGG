package com.star.app.game;

public class GameController {
    private Background background;
    private Hero hero;
    private Asteroid asteroid;
    private BulletController bulletController;

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.asteroid = new Asteroid(this);
        this.bulletController = new BulletController();
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public Asteroid getAsteroid() {
        return asteroid;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        asteroid.update(dt);
        bulletController.update(dt);
    }
}
