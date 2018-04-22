package block_editor.blocks;

import java.io.*;
import java.util.LinkedList;
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
} 