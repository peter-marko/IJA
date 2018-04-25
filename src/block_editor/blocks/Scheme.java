package block_editor.blocks;

import java.io.*;
import java.util.LinkedList;

import javafx.util.Pair;

import javafx.scene.shape.*;
import javafx.geometry.Bounds;
import javafx.scene.layout.GridPane;
import block_editor.types.*;

public class Scheme {
    private LinkedList<Block> blocks; // strores block objects (instacies of Block class)
    private LinkedList<Con> connections; // stores information about which blocks are connected to each other
    private Integer next_id; // actual ID value stored

    public Scheme() {
        this.blocks = new LinkedList<Block>();
        this.connections = new LinkedList<Con>();
        this.next_id = 0;
    }

    // increments actual highest ID and returns new value
    public Integer getBlockID() {
        this.next_id++;
        return this.next_id;
    }

    public void addBlock(Block block) {
        this.blocks.add(block);
    }

    public void clear() {
        this.blocks.clear();
    }

    public void print() {
        System.out.println("Scheme print:");
        System.out.println("  Blocks:");
        for (Block block : this.blocks) {
            System.out.println("    " + block.getName() + " (" + block.getID() + ")");
        }
        System.out.println("  Connections:");
        for (Con connection : this.connections) {
            System.out.println("    [" + connection.src + "]---[" + connection.dst + "]");
        }
    }

    public void deleteBlock(Integer id) {
        Integer i = 0;
        Block target = null;
        for (Block block : this.blocks) {
            if(block.getID() == id){
                target = block;
                break;
            }
            i++;
        }
        blocks.remove(target);
    }

    public Integer searchBlock(double x, double y, Type srcType) {
        Line l = srcType.getLines().getLast();
        boolean set = false;
        for (Block b : this.blocks) {
            int idx = 0;
            for (Type inputType : b.inputs) {
                Circle circle = inputType.getNode();
                Bounds bounds = circle.localToScene(b.getBorder().getBoundsInLocal());
                double diff_x = x - bounds.getMinX();
                double diff_y = y - bounds.getMinY();
                if (diff_x*diff_x + diff_y*diff_y < 25) {
                    // todo
                    Type dst = b.inputs.get(idx);
                    if (dst.getName() == srcType.getName()) {
                        GridPane grid = (GridPane) circle.getParent();
                        // removing user inputText if set
                        LinkedList<javafx.scene.Node> nodesToRemove = new LinkedList<>();
                        for (javafx.scene.Node child : grid.getChildren()) {
                            // get index from child
                            Integer columnIndex = grid.getColumnIndex(child);
                    
                            if (child != circle) {
                                nodesToRemove.addLast(child);
                            }
                        }
                        grid.getChildren().removeAll(nodesToRemove);
                        b.lineActualize(1);

                        l.setEndX(bounds.getMinX());
                        l.setEndY(bounds.getMinY());
                        srcType.connect(dst);
                        return b.getID();
                    } else {
                        System.out.println(dst.getName()+" != "+srcType.getName());
                    }
                }
                idx += 1;
            }
        }
        return -1;
    }

    // should be called when line is made between two blocks
    public void connect(Integer srcBlockID, Integer dstBlockID){
        Con new_con = new Con();
        new_con.src = srcBlockID;
        new_con.dst = dstBlockID;
		this.connections.add(new_con);
    }

    //just for encapsulation
    private class Con {
        Integer src, dst;
    }
} 