package block_editor.blocks;

import block_editor.types.Type;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class visualBlock extends Region {

    private Scheme parent_scheme;
    // Creating scheme for keeping track of currently visible blocks
    public void setParentScheme(Scheme new_parent_scheme) {
        this.parent_scheme = new_parent_scheme;
    }
    private Integer blockID; // id of block object
    public void setBlockID(Integer new_id) {
        this.blockID = new_id;
    }
    public Integer getBlockID() {
        return this.blockID;
    } 

    //current state

    public void setRoot(Node node) {
        getChildren().add(node);
    }
    // set focus to current visual block
    public void makeFocusable() {
        this.setOnMouseClicked(mouseEvent -> {
            toFront();
        });
    }
    /**
     * \brief Function used for setting coordinates of lines when visual block changed position
     * \param block Metadata about currently moved block
     * \param x_diff X position chage
     * \param y_diff y position chage
     */
    void update_lines(Block block, double x_diff, double y_diff) {
        for (Type t : block.inputs) {
            if (t != null && t.getLines() != null) {
                for (Line l : t.getLines()) {
                    l.setEndX(l.getEndX() + x_diff);
                    l.setEndY(l.getEndY() + y_diff);
                }
            }
        }
        for (Type t : block.outputs) {
            if (t != null && t.getLines() != null) {
                for (Line l : t.getLines()) {
                    l.setStartX(l.getStartX() + x_diff);
                    l.setStartY(l.getStartY() + y_diff);
                }
            }
        }
    }

    /**
     * \brief Function for removing connections from block
     * \param block specifies connection of which block should be cleares
     */
    // this function is used when erasing block
    public void remove_lines(Block block) {
        System.out.println("\nchange");
        for (Type t : block.inputs) {
            // get opposite port to which is t connected
            if (t != null) {
                for (Type dst : t.getDst()) {
                    dst.clearDst(t);
                }
            }
        }
        System.out.println("\nchange");
        for (Type t : block.outputs) {
            t.clearDst(null);
        }
    }
    /**
     * \brief Implements, that user can drag visualBlock within canvas
     * \param what Determines which part should react for drag, eg. title_bar, not entire visualBlock
     * \param canvas Space where is drawn visualWindow, determines boundries even when user expands vindow
     * \param block Metadata about currently moved block
     */
    public void makeDragable(Node what, Pane canvas, Block block) {
        final Delta dragDelta = new Delta();
        what.setOnMousePressed(mouseEvent -> {
            dragDelta.x = getLayoutX() - mouseEvent.getScreenX();
            dragDelta.y = getLayoutY() - mouseEvent.getScreenY();
            //also bring to front when moving
            toFront();
        });
        what.setOnMouseDragged(mouseEvent -> {
            double next_x = mouseEvent.getScreenX() + dragDelta.x;
            double next_y = mouseEvent.getScreenY() + dragDelta.y;
            double max_x = canvas.getWidth() - getWidth();
            // 40 size of menu in first line
            double max_y = canvas.getHeight() - getHeight() - 40;
            double final_x, final_y;
            if (next_x < 0) {
               final_x = 0;
            } else if (next_x > max_x) {
                final_x = max_x;
            } else {
                final_x = next_x;
            }
            if (next_y < 0) {
                final_y = 0;
            } else if (next_y > max_y) {
                final_y = max_y;
            } else {
                final_y = next_y;
            }
            update_lines(block, final_x - getLayoutX(), final_y - getLayoutY());
            setLayoutX(final_x);
            setLayoutY(final_y);
            // todo
        });
    }

    /**
     * \brief Funcion sets this button to remove and disconnect, current visualBlock
     * \param canvas space where is current block located
     * \param bloc Metadata corresponding to this visualBlock
     */
    public void setCloseButton(Button btn, Pane canvas, Block block) {
        btn.setOnMouseClicked(event -> {
            if(event.getButton().equals(javafx.scene.input.MouseButton.PRIMARY)) {
                remove_lines(block);
                System.out.println("Deleting block " + this.getBlockID());
                this.parent_scheme.deleteBlock(this.getBlockID());
                ((Pane) getParent()).getChildren().remove(this);
                block.clear();
            } 
            // temporal part for debugging right click executes window
            else {
                block.execute();
            }
        });
    }

    //just for encapsulation
    private class Delta {
        double x, y;
    }
}