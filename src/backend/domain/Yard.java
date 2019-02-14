package backend.domain;

import java.util.List;

public class Yard {
    private List<Stack> stacks;
    private Charger charger;

    public Yard(List<Stack> stacks, Charger charger) {
        setCharger(charger);
        setStacks(stacks);
    }

    public List<Stack> getStacks() {
        return stacks;
    }

    public void setStacks(List<Stack> stacks) {
        this.stacks = stacks;
    }

    public Charger getCharger() {
        return charger;
    }

    public void setCharger(Charger charger) {
        this.charger = charger;
    }
}
