package block_editor.blocks;

import java.io.*;
import java.util.LinkedList;

import javafx.scene.shape.*;
import javafx.geometry.Bounds;

import block_editor.types.*;

public class Scheme {
    private LinkedList<Block> blocks;
    private Integer next_id;

    public Scheme() {
        this.blocks = new LinkedList<Block>();
        this.next_id = 0;
    }

    // increments actual highest ID and retuns new value
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
        for (Block block : this.blocks) {
            System.out.println("  " + block.getName() + " " + block.getID());
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

    public boolean searchBlock(double x, double y, Type srcType) {
        Line l = srcType.lines.getLast();
        boolean set = false;
        for (Block b : this.blocks) {
            int idx = 0;
            for (Circle circle : b.inNodes) {
                Bounds bounds = circle.localToScene(b.border.getBoundsInLocal());
                double diff_x = x - bounds.getMinX();
                double diff_y = y - bounds.getMinY();
                if (diff_x*diff_x + diff_y*diff_y < 25) {
                    // todo
                    Type dst = b.inputs.get(idx);
                    if (dst.name == srcType.name) {
                        l.setEndX(bounds.getMinX());
                        l.setEndY(bounds.getMinY());
                        srcType.connect(dst);
                        set = true;
                    } else {
                        System.out.println(dst.name+" != "+srcType.name);
                    }
                }
                idx += 1;
            }
        }
        return set;
    }
} 