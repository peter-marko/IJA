package block_editor.types;

/**
 * Holding common information about types which are used for storing date about ports and connection between blocks
 * @author Stanislav Mechl
 */
import java.util.LinkedList;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Queue;
import java.util.Iterator;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.*;
import javafx.scene.paint.*;
import org.w3c.dom.*;
import block_editor.blocks.*;
import javafx.scene.shape.Circle;

public abstract class Type implements TypeInterface, java.io.Serializable {
    protected String name;
    protected boolean set;
    protected int status;
    protected transient Circle node;
    protected LinkedList<Line> lines = new LinkedList();
    protected LinkedList<Type> dst = new LinkedList();   // connected to
    protected Integer ID; // ID of source block
    protected LinkedList<Integer> dstID = new LinkedList(); // ID of connected block
    protected Map<String, Double> items = new HashMap<String, Double>();
    
    public void clearValues() {
        for (Map.Entry<String, Double> entry: this.items.entrySet()) {
            entry.setValue(null);
        }
    }
    /**
     *  Function for clearing output port
     * @param type if null whole port is cleared
     * if set than cleared only connection pointing to type
     */
    public void clearDst(Type type) {
        if (!this.lines.isEmpty()) {
            Iterator<Type> iter = this.dst.iterator();
            LinkedList<Type> typesToRemove = new LinkedList<>();
            while (iter.hasNext()) {
                Type opposite = iter.next();
                if (type != null && type != opposite) {
                    continue;
                }
                LinkedList<Line> revLines = opposite.getLines();
                if (!revLines.isEmpty()) {
                    Line oppLine = revLines.getLast();
                    this.lines.remove(oppLine);
                    if(type != null){
                        this.dstID.remove(type.ID);// removes connection to type port
                    }
                    typesToRemove.addLast(opposite);
                    opposite.lines.remove(oppLine);
                    oppLine.remove();
                } else {
                    continue;
                }
                if (type == null) {
                    this.dstID.clear();// clears all dstID
                    opposite.dst.remove(opposite);
                    for (Map.Entry<String, Double> entry: opposite.items.entrySet()) {
                        entry.setValue(null);
                    }
                    System.out.println("remove");
                }
            }
            this.dst.removeAll(typesToRemove);
        }
        this.set = false;
    }
    /**
     *  Set connection variable dst and checks types
     * @param dst output type, connection to other block
     * @param dst_block_id ID of block to which is port connected
     */
    public void connect(Type dst, Integer dst_block_id) {
        dst.status = 0;
        if (dst.dst.isEmpty() == false)
            dst.dst.getFirst().clearDst(dst);
            // dst.clearDst(null);
        this.dst.addLast(dst);
        this.dstID.addLast(dst_block_id);
        dst.lines.add(0, this.lines.getLast());
        dst.dst.clear();
        dst.dst.add(this);
        dst.set = true;
        if(this.set){
            System.out.println("additonal propagation");
            dst.propagate();
        }
    }

    public void propagate() {
        for (Type dst_port : this.dst) {
            for (Map.Entry<String, Double> entry: dst_port.items.entrySet()) {
                dst_port.putVal(entry.getKey() , entry.getValue());
            }
        }
    }

    /**
     *  Propagates values to dst
     */
    public void step () {
        // type check + data transfer
        for (Type dst : this.dst) {
            System.out.println("propagujem\n");
            Iterator tmp_in = this.items.entrySet().iterator();
            while (tmp_in.hasNext()) {
                Map.Entry i = (Map.Entry)tmp_in.next();
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
    public Label addLine(double x1, double y1, double x2, double y2) {
        Line l = new Line(x1 + 5, y1, x2 - 5, y2);
        return l.enableStatusDisplay(this);
    }
    public javafx.scene.shape.Circle getNode() {
        return this.node;
    }
    public void setNode(javafx.scene.shape.Circle node) {
        this.node = node;
    }
    public boolean isSet() {
        return this.set;
    }
    public boolean isStatus(){
        if (status != 0)
            return true;
        return false;
    }
    public void setStatus(Integer val){
        this.status = val;
    }
    public Integer getStatus(){
        return this.status;
    }
    public void unsetStatus(){
        this.status = 0;
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
        //this.set = true;
    }
    public void putVal(String s) {
        this.items.put(s,null);
        //this.set = true;
    }
    public Integer getFirstDstID() {
        if(this.dstID.isEmpty()){
            return -1;
        }
        return this.dstID.getFirst();
    }
    public LinkedList<Integer> getAllDstID() {
        return this.dstID;
    }
}
