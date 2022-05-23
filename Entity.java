/**
 * Represents an Entity
 */

public abstract class Entity {
    protected String name;
    protected String address;
    protected String type;
    protected int number;
    /**
     * 
     * @param name The name of this entity
     * @param address The physical address of this entity
     * @param type The type of this entity
     * @param number The number of this entity
     */
    public Entity(String name, String address, String type, int number) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }
}
