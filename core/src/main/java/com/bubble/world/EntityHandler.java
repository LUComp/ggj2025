package com.bubble.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.bubble.graphics.ShaderHandler;
import com.bubble.helpers.Constants;
import com.bubble.entities.Entity;
import com.bubble.entities.PlayableCharacter;
import java.util.HashMap;
import java.util.LinkedList;

// Class to handle risky operations outside world step and map entities to their id
public class EntityHandler {
    private final HashMap<Integer, Entity> entities;        // Map of entities with id
    private final LinkedList<EntityOp> entityOps;           // Entity operations to handle
    private final ShaderHandler shaderHandler;

    public EntityHandler(ShaderHandler shaderHandler) {
        entityOps = new LinkedList<>();
        entities = new HashMap<>();
        this.shaderHandler = shaderHandler;
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getID(), entity);
    }

    // Returning entity based on id
    public Entity getEntity(int id) {
        return entities.get(id);
    }

    // Returning entity based on box2d body
    public Entity getEntity(Body b2body) {
        for (Entity entity : entities.values()) {
            if (entity.getB2body().equals(b2body)) {
                return entity;
            }
        }
        return null;
    }

    public void addEntityOperation(Entity entity, String operation) {
        entityOps.add(new EntityOp(entity, operation));
    }

    // Handling all entity operations
    public void handleEntities() {
        for (EntityOp entityOp : entityOps) {
            entityOp.resolve();
        }
        entityOps.clear();
    }

    public void update(float delta) {
        
        // Updating all entities
        for (Entity entity : entities.values()) {
            entity.update(delta);
        }
        handleEntities();
    }

    public void render(SpriteBatch batch) {
        
        // Rendering entities
        for (Entity entity : entities.values()) {
            if (entity instanceof PlayableCharacter) {
                // Applying red mask if entity is hit
                if (((PlayableCharacter) entity).isStateActive(Constants.PSTATE.HIT)) {
                    batch.setShader(shaderHandler.getShaderProgram("redMask"));
                }
            }
            entity.render(batch);
            batch.setShader(null);
        }
    }

    public LinkedList<Entity> getEntities() {
        return new LinkedList<>(entities.values());
    }

    // Helper entity operation class mapping an entity with an operation
    private class EntityOp {
        public Entity entity;
        public String operation;

        public EntityOp(Entity entity, String op) {
            this.entity = entity;
            this.operation = op;
        }

        public void resolve() {
            if (operation.equals("die")) {
                entities.remove(entity.getID());
            }
        }
    }
}

