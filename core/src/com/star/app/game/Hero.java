package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero extends Ship {
    public enum Skills {
        HP_MAX(20, 10),
        HP(20, 10),
        WEAPON(100, 1),
        MAGNET(100, 1);

        int cost;
        int power;

        Skills(int cost, int power) {
            this.cost = cost;
            this.power = power;
        }
    }

    private int score;
    private int scoreView;
    private int money;
    private StringBuilder sb;
    private Shop shop;
    private Circle magnetArea;

    public Circle getMagnetArea() {
        return magnetArea;
    }

    public Shop getShop() {
        return shop;
    }

    public int getScore() {
        return score;
    }

    public int getMoney() {
        return money;
    }

    public Hero(GameController gc) {
        super(gc, 100, 500);
        this.position = new Vector2(ScreenManager.SCREEN_WIDTH / 2, ScreenManager.SCREEN_HEIGHT / 2);
        this.velocity = new Vector2(0, 0);
        this.money = 150;
        this.sb = new StringBuilder();
        this.magnetArea = new Circle(position, 64);
        this.shop = new Shop(this);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.hitArea = new Circle(position, 29);
        this.ownerType = OwnerType.PLAYER;
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        sb.setLength(0);
        sb.append("SCORE: ").append(scoreView).append("\n");
        sb.append("HP: ").append(hp).append("/").append(hpMax).append("\n");
        sb.append("BULLETS: ").append(curWeapon.getCurBullets()).append("/").append(curWeapon.getMaxBullets()).append("\n");
        sb.append("$: ").append(money).append("\n");
        font.draw(batch, sb, 20, 700);
    }

    public void update(float dt) {
        super.update(dt);
        updateScore(dt);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            tryToFire();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            accelerate(dt);

            float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
            for (int i = 0; i < 3; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4),
                        by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20),
                        velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f, 1.0f, 0.5f,
                        0, 1, 1, 1, 1, 0);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            brake(dt);

            float bx = position.x + MathUtils.cosDeg(angle + 90) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4),
                        by + MathUtils.random(-4, 4),
                        velocity.x * -0.1f + MathUtils.random(-20, 20),
                        velocity.y * -0.1f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f, 1.0f, 0.5f,
                        0, 1, 1, 1, 1, 0);
            }
            bx = position.x + MathUtils.cosDeg(angle - 90) * 20;
            by = position.y + MathUtils.sinDeg(angle - 90) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(bx + MathUtils.random(-4, 4),
                        by + MathUtils.random(-4, 4),
                        velocity.x * -0.1f + MathUtils.random(-20, 20),
                        velocity.y * -0.1f + MathUtils.random(-20, 20),
                        0.4f, 1.2f, 0.2f, 1.0f, 0.5f,
                        0, 1, 1, 1, 1, 0);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            shop.setVisible(true);
            gc.setPause(true);
        }

        magnetArea.setPosition(position);

    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 4000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void consume(PowerUps p) {
        sb.setLength(0);
        switch (p.getType()) {
            case MEDKIT:
                int oldHp = hp;
                hp += p.getPower();
                if (hp > hpMax) {
                    hp = hpMax;
                }
                sb.append("HP + ").append(hp - oldHp);
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb.toString(), Color.GREEN);
                break;
            case MONEY:
                money += p.getPower();
                sb.append("MONEY + ").append(p.getPower());
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb.toString(), Color.YELLOW);
                break;
            case AMMOS:
                curWeapon.addAmmos(p.getPower());
                sb.append("AMMOS + ").append(p.getPower());
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, sb.toString(), Color.ORANGE);
                break;
        }
    }

    public boolean isMoneyEnough(int amount) {
        return money >= amount;
    }

    public void decreaseMoney(int amount) {
        money -= amount;
    }

    public boolean upgrade(Skills skills) {
        switch (skills) {
            case HP_MAX:
                hpMax += Skills.HP_MAX.power;
                return true;
            case HP:
                if (hp + Skills.HP.power <= hpMax) {
                    hp += Skills.HP.power;
                    return true;
                }
                break;
            case WEAPON:
                if (weaponNum < weapons.length - 1) {
                    weaponNum++;
                    curWeapon = weapons[weaponNum];
                    return true;
                }
                break;
            case MAGNET:
                magnetArea.setRadius(magnetArea.radius * 2);
                Skills.MAGNET.cost *= 2;
                return true;
        }
        return false;
    }

    public void offPause(boolean pause) {
        gc.setPause(pause);
    }
}
