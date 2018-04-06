public class TypeA extends Type {

    public TypeA(String newName) {
        this.name = newName;
        this.items.add(new Item("val"));
    }

    /*public double getNumberOfItems() {
        return this.items.size();
    }

    public Item getItem(int n) {
        return (Item)this.items.get(n);
    }*/
}
