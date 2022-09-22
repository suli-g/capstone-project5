package Entities;

import Interfaces.Overviews;

/**
 * Represents an Entity
 */

public abstract class Entity implements Overviews{
    protected String name;
    protected String address;
    protected String type;
    protected int number;

    /**
     * Entity Constructor.
     * 
     * @param name    The name of this entity
     * @param address The physical address of this entity
     * @param type    The type of this entity
     * @param number  The number of this entity
     */
    public Entity(String name, String address, String type, int number) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.number = number;
    }

    /**
     * @return The name of this entity.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The address of this entity.
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return The type of this entity.
     */
    public String getType() {
        return type;
    }

    /**
     * @return The number of this entity.
     */
    public int getNumber() {
        return number;
    }
}