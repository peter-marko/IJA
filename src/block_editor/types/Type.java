package block_editor.types;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Iterator;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.*;
import block_editor.blocks.*;
import javafx.scene.shape.*;

public abstract class Type implements TypeInterface {
    protected String name;
    protected boolean set;
    protected boolean fromUser;
    protected javafx.scene.shape.Circle node;
    protected LinkedList<Line> lines = new LinkedList();
    protected LinkedList<Type> dst = new LinkedList();   // connected to
    protected Integer ID; // ID of source block
    protected LinkedList<Integer> dstID = new LinkedList(); // ID of connected block
    protected Map<String, Double> items = new HashMap<String, Double>();
    
    /**
     * \brief Function for clearing output port
     * \param type if null whole port is cleared
     * if set than cleared only connection pointing to type
     */
    public void clearDst(Type type) {
        if (!this.lines.isEmpty()) {
            Iterator<Type> iter = this.dst.iterator();
            LinkedList<Type> typesToRemove = new LinkedList<>();
            while (iter.hasNext()) {
                Type opposite = iter.next();
                if (type != null && type != opposite) {
                    System.out.println("continue");
                    continue;
                }
                LinkedList<Line> revLines = opposite.getLines();
                if (!revLines.isEmpty()) {
                    Line oppLine = revLines.getLast();
                    this.lines.remove(oppLine);
                    typesToRemove.addLast(opposite);
                    opposite.lines.remove(oppLine);
                    System.out.print("clear srcccccccccc\n"+type+" "+opposite);
                    ((javafx.scene.layout.Pane) oppLine.getParent()).getChildren().remove(oppLine);
                } else {
                    System.out.print("empty\n");
                }
                if (type == null) {
                    this.dstID.remove(opposite.ID);// removes dstID of opposite
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
     * \brief Set connection variable dst and checks types
     * \param dst output type, connection to other block
     * \param dst_block_id ID of block to which is port connected
     */
    public void connect (Type dst, Integer dst_block_id) {
        if (dst.set)
            dst.clearDst(null);
        this.dst.addLast(dst);
        this.dstID.addLast(dst_block_id);
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
    public Label addLine(double x1, double y1, double x2, double y2) {
        Line l = new Line(x1, y1, x2, y2);
        l.setStrokeWidth(3);
        // l.setStyle("-fx-border-width: 10px");
        Label t = new Label();
        // position of the text 
        t.setVisible(false);
        t.setLayoutX(0);
        t.setLayoutY(0);
        t.setStyle("-fx-background-color: white; -fx-border-color: black;");
        l.setOnMouseEntered(e -> {
            Double diff = l.getStartX() - e.getX();
            if (diff*diff > 100) {
                t.setLayoutX(e.getX()); 
                t.setLayoutY(e.getY() + 18);
                String text = new String();
                for (Map.Entry<String, Double> entry: this.dst.getLast().getItems().entrySet()) {
                    text += entry.getKey();
                    text += " : ";
                    text += entry.getValue();
                    text += ", \n";
                }
                t.setText(text);
                t.setVisible(true);
                t.toFront();
            }
        });
        l.setOnMouseExited(e -> {
            t.setVisible(false);
        });
        this.lines.addLast(l);
        return t;
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
    public boolean isFromUser(){
        return this.fromUser;
    }
    public void setFromUser(){
        this.fromUser = true;
    }
    public void unsetFromUser(){
        this.fromUser = false;
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
