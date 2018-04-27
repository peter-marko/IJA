package block_editor.blocks;

import java.io.*;
import java.util.LinkedList;

//import javafx.util.Pair;

import javafx.scene.shape.*;
import javafx.geometry.Bounds;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType; 
import block_editor.types.*;

public class Scheme {
    private LinkedList<Block> blocks; // strores block objects (instacies of Block class)
    private LinkedList<Con> connections; // stores information about which blocks are connected to each other
    private Integer next_id; // actual ID value stored
    private LinkedList<Integer> queue; // queue with IDs of not computed blocks
    private boolean queue_set; // false if queue was not initialized
    private Alert anyPrepared; // error mesage when there is no prepared block in scheme
    private Alert allExecuted; // message when all block are computed

    /**
     * \brief Scheme constructor. Initializes empty lists and sets actual ID to zero (first will be 1)
     */
    public Scheme() {
        this.blocks = new LinkedList<Block>();
        this.connections = new LinkedList<Con>();
        this.next_id = 0;
        this.queue = new LinkedList<Integer>();
        this.queue_set = false;

        this.anyPrepared = new Alert(AlertType.INFORMATION);
        anyPrepared.setTitle("Block-editor");
        anyPrepared.setHeaderText("Any prepared block found!");
        anyPrepared.setContentText("Program can't find block which could be computed. Ensure that every block is connected or has set input value.");
        this.allExecuted = new Alert(AlertType.INFORMATION);
        allExecuted.setTitle("Block-editor");
        allExecuted.setHeaderText("All block were successfully computed!");
        allExecuted.setContentText("Clicking 'step' or 'run' button again will start new computation.");
    }

    /**
     * \brief gets new uniqe block ID value in scheme
     * \return ID value for next block
     */
    public Integer getBlockID() {
        this.next_id++;
        return this.next_id;
    }

    /**
     * \brief add new block to scheme
     * \param block Block class object to store
     */
    public void addBlock(Block block) {
        this.blocks.add(block);
    }

    /**
     * \brief reinitializes scheme, all block and connections are deleted and next ID value is 1 again
     */
    public void clear() {
        this.blocks.clear();
        this.connections.clear();
        next_id = 0;
        this.queue.clear();
        this.queue_set = false;
    }

    /**
     * \brief prints actual Scheme state into console
     */
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
        if(this.queue_set){
            System.out.println("  Queue: " + this.queue);
        }
        else{
            System.out.println("  Queue not loaded");
        }
    }

    /**
     * \brief removes block from scheme
     * \param id ID of block which should be deleted
     */
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
        this.blocks.remove(target);
    }

    /**
     * \brief search block which has port in specified location
     * \param x,y coordinates where block is supposed to be
     * \param srcType data type which port should have
     * \return ID of found block
     */
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

    /**
     * \brief stores information about connection into Scheme class
     * \param srcBlockID ID of source block
     * \param dstBlockID ID of destination block
     */
    public void connect(Integer srcBlockID, Integer dstBlockID){
        Con new_con = new Con();
        new_con.src = srcBlockID;
        new_con.dst = dstBlockID;
		this.connections.add(new_con);
    }

    /**
     * \brief checks if scheme contains cycles - calls recursive function for every block in Scheme
     * \return true if scheme is ok, false if cycle is detected
     */
    public boolean checkCycles(){
        for (Block block : this.blocks) {
            if(checkCyclesRecursive(block.getID(), new LinkedList<Integer>()) == false){
                return false;
            }
        }
        return true;
    }

    /**
     * \brief checks if actual block was already visited and calls same function for all following blocks
     * \param blockID ID of actual block
     * \param visited List containing ID of blocks which was already visited
     * \return true if everything is ok, false if this block was already visited or if false is returned from following block
     */
    public boolean checkCyclesRecursive(Integer blockID, LinkedList<Integer> visited){
        System.out.println("cc: (block " + blockID + ")   visited: " + visited);
        if(visited.contains(blockID)){
            return false;
        }
        visited.add(blockID);
        for (Con connection : this.connections) {
            if(connection.src == blockID){
                LinkedList<Integer> new_visited = new LinkedList(visited);
                if(checkCyclesRecursive(connection.dst, new_visited) == false){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * \brief finds block with given ID
     * \param blockID ID of serched block
     * \return searched block
     */
    public Block getBlockByID(Integer blockID){
        for (Block block : this.blocks) {
            if(block.getID() == blockID)
            {
                return block;
            }
        }
        return null;
    }

    /**
     * \brief loads block IDs to queue
     */
    public void loadQueue(){
        for (Block block : this.blocks) {
            this.queue.add(block.getID());
        }
        this.queue_set = true;
    }

    /**
     * \brief finds next prepared block in queue and execute him
     * \return true if computation should continue, false if queue is empty (all block were computed) or some value is missing
     */
    public boolean executeNext(){
        if(this.queue_set == false)
        {
            this.loadQueue();
        }
        Integer limit = this.queue.size();
        Integer act_ID = this.queue.getFirst();
        this.queue.removeFirst();
        while(this.getBlockByID(act_ID).isPrepared() == false){// block is not prepared -> move him to the end of queue ant take next one
            limit--;
            if(limit == 0)
            {
                System.out.println("Error - any prepared block!");
                this.queue.addLast(act_ID);
                this.msgAnyPrepared();
                return false;
            }
            this.queue.addLast(act_ID);
            act_ID = this.queue.getFirst();
            this.queue.removeFirst();
        }
        System.out.println("Executing block (" + act_ID + ")");
        this.getBlockByID(act_ID).execute();
        if(this.queue.isEmpty())
        {
            System.out.println("All blocks were computed!");
            this.msgAllExecuted();
            this.queue_set = false;
            return false;
        }
        return true;
    }

    /**
     * \brief executes all blocks in scheme
     */
    public void executeAll()
    {
        while(executeNext()) {}
    }

    /**
     * \brief shows dialog which says that no prepared block was found
     */
    public void msgAnyPrepared()
    {
        this.anyPrepared.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {}
        });
    }

    /**
     * \brief shows dialog which says that all blocks were executed
     */
    public void msgAllExecuted()
    {
        this.allExecuted.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {}
        });
    }

    //just for encapsulation
    private class Con {
        Integer src, dst;
    }
} 