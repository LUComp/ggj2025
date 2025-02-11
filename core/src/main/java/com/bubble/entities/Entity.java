package com.bubble.entities;

import com.bubble.tools.MyResourceManager;
import com.bubble.sprites.B2Sprite;

//Every entity has a sprite attached to it
public abstract class Entity extends B2Sprite {
    protected int ID;
    protected final MyResourceManager resourceManager;
    protected int lives;

    //Instantiate Entity with ID and access to resource manager
    protected Entity(int ID, MyResourceManager resourceManager) {
        this.ID = ID;
        this.resourceManager = resourceManager;
    }

    public int getID() {
        return ID;
    }

    public int getLives() {
        return lives;
    }

    public void die() {}    //Method to override for player and base goblin
}
