package model;

/**
 * Sets parameters form Inhouse parts
 */
public class Inhouse extends Part {

    private int machineID;

    public Inhouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }
    /**
     * machine ID getter
     * @return
     */
    public int getMachineID() {
        return machineID;
    }

    /**
     * machine ID setter
     * @param machineID
     * @return
     */
    public int setMachineID(int machineID) {
        this.machineID = machineID;
        return machineID;
    }
}
