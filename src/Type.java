import java.util.LinkedList;
import java.util.Queue;

public abstract class Type {
    String name;
    LinkedList items = new LinkedList();

    public int getNumberOfItems() {
        return this.items.size();
    }

    public Type getItem(int n) {
        return (Type)this.items.get(n);
    }
}
