public class Item {
    private String name;
    private double value;
    
    public Item (String newName, double value) {
        this.name = newName;
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double newValue) {
        this.value = newValue;
    }

    public String getName() {
        return this.name;
    }

}
