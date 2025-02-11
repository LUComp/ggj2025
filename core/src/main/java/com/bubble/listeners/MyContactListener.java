package com.bubble.listeners;

import com.badlogic.gdx.physics.box2d.*;
import com.bubble.helpers.Constants;
import com.bubble.scenes.HUD;
import com.bubble.screens.ScreenManager;
import com.bubble.tools.MyResourceManager;
import com.bubble.tools.UtilityStation;
import com.bubble.world.EntityHandler;
import com.bubble.entities.Bubble;
import com.bubble.entities.Player;

public class MyContactListener implements ContactListener {

    // Fixtures partaking in collision
    private Fixture fa;
    private Fixture fb;
    private final UtilityStation util;

    public MyContactListener(UtilityStation util, HUD hud, ScreenManager screenManager, MyResourceManager resourceManager) {
        this.util = util;
    }

    // Triggers when a fixture collides with a sensor
    @Override
    public void beginContact(Contact contact) {

        if (handleFixtures(contact)) return;

        EntityHandler entityHandler = util.getEntityHandler();

        if (fa.getUserData() instanceof Player || fb.getUserData() instanceof Player) {
            if (fa.getUserData() instanceof Bubble || fb.getUserData() instanceof Bubble) {
                Bubble bubble = (Bubble) (fa.getUserData() instanceof Bubble ? fa.getUserData() : fb.getUserData());
                Player player = (Player) (fa.getUserData() instanceof Player ? fa.getUserData() : fb.getUserData());
                if (bubble.creator != player) {
                    float dmg = bubble.damage();
                    player.takeDamage(dmg);

                    if (player.getHealth() <= 0) {
                        player.die();
                    }

                    bubble.pop();
                    return;
                }
            }
        } else if (fa.getUserData() instanceof Bubble && fb.getUserData() instanceof Bubble) {
            Bubble b1 = (Bubble) fa.getUserData();
            Bubble b2 = (Bubble) fb.getUserData();
            b1.bubbleMerge(b2);
        } else if (fa.getUserData().equals("vert") || fa.getUserData().equals("hor")) {
            Bubble bubble = (Bubble) entityHandler.getEntity(fa.getBody());
            bubble.bounce(fa.getUserData().equals("vert") ? true : false);
        } else if (fb.getUserData().equals("vert") || fb.getUserData().equals("hor")) {
            Bubble bubble = (Bubble) entityHandler.getEntity(fb.getBody());
            if (!bubble.isStateActive(Constants.BSTATE.BOUNCING)) {
                bubble.bounce(fb.getUserData().equals("vert") ? true : false);
                bubble.addState(Constants.BSTATE.BOUNCING);
            }
        }
    }

    // Triggered when a fixture stops colliding with a sensor
    @Override
    public void endContact(Contact contact) {

    }

    // Method to collect both fixtures
    public boolean handleFixtures(Contact contact) {
        fa = contact.getFixtureA();
        fb = contact.getFixtureB();

        return fa == null || fb == null;
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
