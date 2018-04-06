import java.util.LinkedList;
import java.util.Queue;

public abstract class Type {
    protected String name;
    protected LinkedList items = new LinkedList();

    public Type(String nsme,  LinkedList items) {
        this.name = name;
        this.items = items;
    }
    public int getNumberOfItems() {
        return this.items.size();
    }

    public Type getItem(int n) {
        return (Type)this.items.get(n);
    }
}
