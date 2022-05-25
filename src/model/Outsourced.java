package model;


/**
 * creates class to show outsources parts
 */
public class Outsourced extends Part {

    private String companyName;

    /**
     * sets parameters for the outsourced part data
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * company name getter
     * @return
     */
    public String getCompanyName() {
        return companyName;
    }
}