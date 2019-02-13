package backend.domain;

import java.util.List;

public class Yard {
    private List<Stack> stacks;
    private Inventory inventory; // TODO needed if inventory static?
    private Charger charger;

    public Yard(List<Stack> stacks, Inventory inventory, Charger charger) {
        setCharger(charger);
        setStacks(stacks);
        setInventory(inventory);
    }

    public List<Stack> getStacks() {
        return stacks;
    }

    public void setStacks(List<Stack> stacks) {
        this.stacks = stacks;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Charger getCharger() {
        return charger;
    }

    public void setCharger(Charger charger) {
        this.charger = charger;
    }
}
