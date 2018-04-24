package block_editor.types;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Iterator;
import java.util.Map;
import block_editor.blocks.*;
import javafx.scene.shape.*;

public abstract class Type implements TypeInterface {
    public String name;
    public boolean set;
    public LinkedList<Line> lines = new LinkedList();
    private LinkedList<Type> dst = new LinkedList();   // connected to
    private Map<String, Double> items = new HashMap<String, Double>();

    public int getNumberOfItems() {
        return this.items.size();
    }

    public String getName() {
        return this.name;
    }
    /* public Item getItem(int n) {
        return this.items.get(n);
    } */
    public double getVal(String s) {
        return this.items.get(s);
    }
    public void putVal(String s, double val) {
        this.items.put(s,val);
    }
    public void putVal(String s) {
        this.items.put(s,null);
    }

    /**
     * \brief Set connection variable dst and checks types
     * \param dst output type, connection to other block
     */
    public void connect (Type dst) {
        this.dst.addLast(dst);
        dst.lines.add(0, this.lines.getLast());
    }

    /**
     * \brief Propagates values to dst
     */
    public void step () {
        // type check + data transfer
        for (Type dst : this.dst) {
            if (this.name != dst.name) {
                throw new RuntimeException("Names of types are different"+this.name+ dst.name);
            }
            dst.name = this.name;
            Iterator tmp_in = this.items.entrySet().iterator();
            while (tmp_in.hasNext()) {
                Map.Entry i = (Map.Entry)tmp_in.next();
                tmp_in.remove(); // avoids a ConcurrentModificationException
                dst.items.put((String)i.getKey(), (double)i.getValue());
                System.out.println((String)i.getKey() + " = " + (double)i.getValue());
                // o.setValue(i.getValue());
            }
        }
    }

    public LinkedList<Type> getConnection() {
        return this.dst;
    }
    
}
