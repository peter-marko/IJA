package block_editor.blocks;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * @author zella.
 */
public class InternalWindow extends Region {

    private Scheme parent_scheme;
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

    public void makeFocusable() {
        this.setOnMouseClicked(mouseEvent -> {
            toFront();
        });
    }

    //we can select nodes that react drag event
    public void makeDragable(Node what, Pane canvas) {
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
            if (next_x < 0) {
                setLayoutX(0);
            } else if (next_x > max_x) {
                setLayoutX(max_x);
            } else {
                setLayoutX(next_x);
            }
            if (next_y < 0) {
                setLayoutY(0);
            } else if (next_y > max_y) {
                setLayoutY(max_y);
            } else {
                setLayoutY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
    }

    public void setCloseButton(Button btn) {
        btn.setOnAction(event -> {
            System.out.println("Deleting block " + this.getBlockID());
            this.parent_scheme.deleteBlock(this.getBlockID());
            ((Pane) getParent()).getChildren().remove(this);
        });
    }

    //just for encapsulation
    private class Delta {
        double x, y;
    }
}