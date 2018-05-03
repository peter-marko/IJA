package block_editor.types;

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
import javafx.geometry.Bounds;
import org.w3c.dom.*;
import block_editor.blocks.*;
import javafx.scene.shape.Circle;

public abstract class Type implements TypeInterface {
    protected String name;
    protected boolean set;
    protected boolean fromUser;
    protected Circle node;
    protected LinkedList<Line> lines = new LinkedList();
    protected LinkedList<Type> dst = new LinkedList();   // connected to
    protected Integer ID; // ID of source block
    protected LinkedList<Integer> dstID = new LinkedList(); // ID of connected block
    protected Map<String, Double> items = new HashMap<String, Double>();
    
    public void serialize(Document doc, Element root, int portIdx) {

        Element currElem = doc.createElement("type");

        String tmpStr = this.getClass().getName();
        Attr attr = doc.createAttribute("className");
        attr.setValue(tmpStr.substring(tmpStr.lastIndexOf(".") + 1));
        currElem.setAttributeNode(attr);

        attr = doc.createAttribute("IDX");
        attr.setValue(Integer.toString(portIdx));
        currElem.setAttributeNode(attr);

        attr = doc.createAttribute("hash");
        attr.setValue(Integer.toString(this.hashCode()));
        currElem.setAttributeNode(attr);

        attr = doc.createAttribute("set");
        attr.setValue(Boolean.toString(set));
        currElem.setAttributeNode(attr);

        attr = doc.createAttribute("fromUser");
        attr.setValue(Boolean.toString(fromUser));
        currElem.setAttributeNode(attr);


        int idx = 0;
        for (Line l : lines) {
            Element lineElem = doc.createElement("line");
            attr = doc.createAttribute("IDX");
            attr.setValue(Integer.toString(idx));
            lineElem.setAttributeNode(attr);
            attr = doc.createAttribute("startX");
            attr.setValue(Integer.toString(l.hashCode()));
            lineElem.setAttributeNode(attr);
            attr = doc.createAttribute("startX");
            attr.setValue(Double.toString(l.getStartX()));
            lineElem.setAttributeNode(attr);
            attr = doc.createAttribute("startY");
            attr.setValue(Double.toString(l.getStartY()));
            lineElem.setAttributeNode(attr);
            attr = doc.createAttribute("endX");
            attr.setValue(Double.toString(l.getEndX()));
            lineElem.setAttributeNode(attr);
            attr = doc.createAttribute("endY");
            attr.setValue(Double.toString(l.getEndY()));
            lineElem.setAttributeNode(attr);
            currElem.appendChild(lineElem);
            idx += 1;
        }
        idx = 0;
        for (Type d : dst) {
            Element dstElem = doc.createElement("dst");
            attr = doc.createAttribute("IDX");
            attr.setValue(Integer.toString(idx));
            dstElem.setAttributeNode(attr);
            attr = doc.createAttribute("hash");
            attr.setValue(Integer.toString(d.hashCode()));
            dstElem.setAttributeNode(attr);
            currElem.appendChild(dstElem);
            idx += 1;
        }

        idx = 0;
        for (Integer i : dstID) {
            Element dstIDElem = doc.createElement("dstID");
            attr = doc.createAttribute("IDX");
            attr.setValue(Integer.toString(idx));
            dstIDElem.setAttributeNode(attr);
            attr = doc.createAttribute("num");
            attr.setValue(Integer.toString(i));
            dstIDElem.setAttributeNode(attr);
            currElem.appendChild(dstIDElem);
            idx += 1;
        }
        idx = 0;
        for (Map.Entry<String, Double> entry: this.items.entrySet()) {
            Element entryElem = doc.createElement("entry");
            attr = doc.createAttribute("IDX");
            attr.setValue(Integer.toString(idx));
            entryElem.setAttributeNode(attr);
            attr = doc.createAttribute("key");
            attr.setValue(entry.getKey());
            entryElem.setAttributeNode(attr);
            attr = doc.createAttribute("value");
            if (entry.getValue() == null)
                attr.setValue("null");
            else
                attr.setValue(Double.toString(entry.getValue()));
            entryElem.setAttributeNode(attr);
            currElem.appendChild(entryElem);
            idx += 1;
        }
        root.appendChild(currElem);
    }

    public Type searchOppositeType(Scheme parent_scheme, NodeList nList, String blockIdx, String portIdx, Block curr) {
        for (int i = 0; i < nList.getLength(); i++) {
            org.w3c.dom.Node nNode = nList.item(i);
            Element elem = (Element) nNode;
            if (elem.getAttribute("id").equals(blockIdx)) {
                NodeList inList = elem.getElementsByTagName("inputs");
                org.w3c.dom.Node inNode = inList.item(0);
                Element inElem = (Element) inNode;
                inList = inElem.getElementsByTagName("type");
                for (int temp = 0; temp < inList.getLength(); temp++) {
                    inNode = inList.item(temp);
                    inElem = (Element) inNode;
                    if (inElem.getAttribute("IDX").equals(portIdx)) {
                        Block oppBlock = parent_scheme.getBlocks().get(i);
                        Type oppPort = oppBlock.getInputPort(temp);
                        Bounds startBounds = this.getNode().localToScene(curr.getBorder().getBoundsInLocal());
                        Bounds endBounds = oppPort.getNode().localToScene(oppBlock.getBorder().getBoundsInLocal());
                        this.lines.addLast(new Line(startBounds.getMinX() + 5, startBounds.getMinY(),
                        endBounds.getMinX() - 5, endBounds.getMinY()));
                        this.connect(oppPort, oppBlock.getID());
                    }
                        // return parent_scheme.getBlocks().get(i).getInputPort(temp);
                }
            }
        }
        return null;
    }

    // public void deserializeIn(Element elem, Element root) {
        
    // }
    public void deserializeOut(Scheme parent_scheme, NodeList nList, String blockIdx, String portIdx, Block curr) {
        Type opposite = searchOppositeType(parent_scheme, nList, blockIdx, portIdx, curr);
        // Bounds startBounds = this.getNode().localToScene(curr.getBorder().getBoundsInLocal());
        // Bounds endBounds = this.getNode().localToScene(curr.getBorder().getBoundsInLocal());
        // this.node.get
        // Double startX
        // this.lines.addLast(new Line());
        // this.connect(opposite, opposite.ID);
        // System.out.println("opposite "+opposite);
    }
    public void clearValues() {
        for (Map.Entry<String, Double> entry: this.items.entrySet()) {
            entry.setValue(null);
        }
    }
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
     * \brief Set connection variable dst and checks types
     * \param dst output type, connection to other block
     * \param dst_block_id ID of block to which is port connected
     */
    public void connect(Type dst, Integer dst_block_id) {
        dst.fromUser = false;
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
