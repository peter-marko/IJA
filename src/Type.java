import java.util.LinkedList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Iterator;
import java.util.Map;

public class Type {
    public String name;
    private Map<String, Double> items = new HashMap<String, Double>();
    private Type dst;   // connected to

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

    /**
     * \brief Set connection variable dst and checks types
     * \param dst output type, connection to other block
     */
    public void connect (Type dst) {
        if (this.name != dst.name) {
            throw new RuntimeException("Names of types are different");
        }
        Iterator tmp_in = this.items.entrySet().iterator();
        Iterator tmp_out = dst.items.entrySet().iterator();
        // iterate and check types of all items in types
        while (tmp_in.hasNext() && tmp_out.hasNext()) {
            Map.Entry o = (Map.Entry)tmp_out.next();
            Map.Entry i = (Map.Entry)tmp_in.next();
            
            if (o.getKey() != i.getKey()) {
                throw new RuntimeException("Names of types are different");
            }
            tmp_in.remove(); // avoids a ConcurrentModificationException
            tmp_out.remove();
        }
        this.dst = dst;
    }

    /**
     * \brief Propagates values to dst
     */
    public void step () {
        // type check + data transfer
        if (this.dst == null || this.name != this.dst.name) {
            throw new RuntimeException("Names of types are different"+this.name+ this.dst.name);
        }
        this.dst.name = this.name;
        Iterator tmp_in = this.items.entrySet().iterator();
        while (tmp_in.hasNext()) {
            Map.Entry i = (Map.Entry)tmp_in.next();
            tmp_in.remove(); // avoids a ConcurrentModificationException
            this.dst.items.put((String)i.getKey(), (double)i.getValue());
            System.out.println((String)i.getKey() + " = " + (double)i.getValue());
            // o.setValue(i.getValue());
        }
    }

    public Type getConnection() {
        return this.dst;
    }
    
}
