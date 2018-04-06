import java.util.LinkedList;
import java.util.Queue;

public abstract class Type {
    protected String name;
    protected LinkedList<Item> items = new LinkedList();

    public int getNumberOfItems() {
        return this.items.size();
    }

    public Item getItem(int n) {
        return this.items.get(n);
    }
}
