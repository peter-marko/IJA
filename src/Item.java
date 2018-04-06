public class Item {
    String name;
    double value;

    public Item (String newName) {
        this.name = newName;
        this.value = 0.0;
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
