package com.star.app.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.app.screen.utils.Assets;

public class Shop extends Group {
    private Hero hero;
    private BitmapFont font24;
    private BitmapFont font32;

    public Shop(final Hero hero) {
        this.hero = hero;
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");

        Pixmap pixmap = new Pixmap(400, 400, Pixmap.Format.RGB888);
        pixmap.setColor(0, 0, 0.5f, 1);
        pixmap.fill();

        Image image = new Image(new Texture(pixmap));
        this.addActor(image);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font32;
        skin.add("simpleSkin", textButtonStyle);

        final TextButton buttonClose = new TextButton("X", textButtonStyle);
        final Shop thisShop = this;

        buttonClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisShop.setVisible(false);
                hero.offPause(false);
            }
        });

        buttonClose.setTransform(true);
        buttonClose.setScale(0.5f);
        buttonClose.setPosition(340, 340);
        this.addActor(buttonClose);

        final TextButton buttonHpMax = new TextButton("HpMax", textButtonStyle);
        buttonHpMax.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skills.HP_MAX.cost)) {
                    if (hero.upgrade(Hero.Skills.HP_MAX)) {
                        hero.decreaseMoney(Hero.Skills.HP_MAX.cost);
                    }
                }
            }
        });
        buttonHpMax.setPosition(20, 300);
        this.addActor(buttonHpMax);

        final TextButton buttonHp = new TextButton("Hp", textButtonStyle);
        buttonHp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skills.HP.cost)) {
                    if (hero.upgrade(Hero.Skills.HP)) {
                        hero.decreaseMoney(Hero.Skills.HP.cost);
                    }
                }
            }
        });
        buttonHp.setPosition(20, 200);
        this.addActor(buttonHp);

        final TextButton buttonWeapon = new TextButton("Weapon", textButtonStyle);
        buttonWeapon.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skills.WEAPON.cost)) {
                    if (hero.upgrade(Hero.Skills.WEAPON)) {
                        hero.decreaseMoney(Hero.Skills.WEAPON.cost);
                    }
                }
            }
        });
        buttonWeapon.setPosition(20, 110);
        this.addActor(buttonWeapon);

        final TextButton buttonMagnetArea = new TextButton("Magnet (cost: " + Hero.Skills.MAGNET.cost + ")", textButtonStyle);
        buttonMagnetArea.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skills.MAGNET.cost)) {
                    if (hero.upgrade(Hero.Skills.MAGNET)) {
                        hero.decreaseMoney(Hero.Skills.MAGNET.cost / 2);
                        buttonMagnetArea.setText("Magnet (cost: " + Hero.Skills.MAGNET.cost + ")");
                    }
                }
            }
        });
        buttonMagnetArea.setPosition(20, 10);
        this.addActor(buttonMagnetArea);

        this.setPosition(20, 20);
        this.setVisible(false);
        skin.dispose();
    }
}
