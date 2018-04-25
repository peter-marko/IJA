package block_editor.types;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Iterator;
import java.util.Map;
import block_editor.blocks.*;
import javafx.scene.shape.*;

public abstract class Type implements TypeInterface {
    protected String name;
    protected boolean set;
    protected javafx.scene.shape.Circle node;
    protected LinkedList<Line> lines = new LinkedList();
    protected LinkedList<Type> dst = new LinkedList();   // connected to
    protected Map<String, Double> items = new HashMap<String, Double>();
    
    /**
     * \brief Set connection variable dst and checks types
     * \param dst output type, connection to other block
     */
    public void connect (Type dst) {
        if (dst.set && !dst.lines.isEmpty()) {
            for (Line currentLine : dst.lines) {
                ((javafx.scene.layout.Pane) currentLine.getParent()).getChildren().remove(currentLine);
            }
            for (Type currentType : dst.dst) {
                currentType.dst.remove(dst);
            }
            dst.lines.clear();
        }
        this.dst.addLast(dst);
        dst.lines.add(0, this.lines.getLast());
        dst.dst.add(this);
        dst.set = true;
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
    public Map<String, Double> getItems() {
        return this.items;
    }
    public void setItem() {
        
    }
    public LinkedList<Type> getDst() {
        return this.dst;
    }
    public LinkedList<Line> getLines() {
        return this.lines;
    }
    public void addLine(double x1, double y1, double x2, double y2) {
        this.lines.addLast(new Line(x1, y1, x2, y2));
    }
    public javafx.scene.shape.Circle getNode() {
        return this.node;
    }
    public void  setNode(javafx.scene.shape.Circle node) {
        this.node = node;
    }
    public boolean isSet() {
        return this.set;
    }
    public void set(boolean set) {
        this.set = set;
    }
    public int getNumberOfItems() {
        return this.items.size();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
}
