package com.fgdev.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.fgdev.game.utils.Assets;

public class JoystickOverlay implements Disposable {

    private final Stage stage;

    private boolean upPressed, downPressed, leftPressed, rightPressed, throwPressed, meleePressed, jumpThrowPressed, climPressed;

    public JoystickOverlay(final Batch batch, OrthographicCamera camera) {
        stage = new Stage(new ScreenViewport(camera), batch);

        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.left().bottom();

        Image imgUp = new Image(Assets.instance.joystick.up);
        imgUp.setSize(50, 50);
        imgUp.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                Gdx.app.error("Touch", "true");
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Image imgDown = new Image(Assets.instance.joystick.down);
        imgDown.setSize(50, 50);
        imgDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                downPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Image imgLeft = new Image(Assets.instance.joystick.left);
        imgLeft.setSize(50, 50);
        imgLeft.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Image imgRight = new Image(Assets.instance.joystick.right);
        imgRight.setSize(50, 50);
        imgRight.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Image imgThrow = new Image(Assets.instance.joystick.attackThrow);
        imgThrow.setSize(50, 50);
        imgThrow.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                throwPressed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                throwPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Image imgMelee = new Image(Assets.instance.joystick.melee);
        imgMelee.setSize(50, 50);
        imgMelee.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                meleePressed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                meleePressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Image imgJumpThrow = new Image(Assets.instance.joystick.jumpThrow);
        imgJumpThrow.setSize(50, 50);
        imgJumpThrow.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jumpThrowPressed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jumpThrowPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Image imgClimb = new Image(Assets.instance.joystick.climb);
        imgClimb.setSize(50, 50);
        imgClimb.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                climPressed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                climPressed = false;
                super.touchUp(event, x, y, pointer, button);
            }
        });

        table.add();
        table.add(imgUp).size(imgUp.getWidth(), imgUp.getHeight());
        table.add();
        table.row().pad(5, 5, 5, 5);
        table.add(imgLeft).size(imgLeft.getWidth(), imgLeft.getHeight());
        table.add();
        table.add(imgRight).size(imgRight.getWidth(), imgRight.getHeight());
        table.row().padBottom(5);
        table.add();
        table.add(imgDown).size(imgDown.getWidth(), imgDown.getHeight());
        table.add();

        stage.addActor(table);
    }

    public void render(final float delta) {

        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
        stage.getBatch().begin();
    }

    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isThrowPressed() {
        return throwPressed;
    }

    public boolean isMeleePressed() {
        return meleePressed;
    }

    public boolean isJumpThrowPressed() {
        return jumpThrowPressed;
    }

    public boolean isClimPressed() {
        return climPressed;
    }
}
