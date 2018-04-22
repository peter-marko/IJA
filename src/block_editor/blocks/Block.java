package block_editor.blocks;

import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;


import java.util.LinkedList;

import block_editor.types.*;

public abstract class Block implements BlockInterface {
    protected String name;
    protected Integer id;
    protected LinkedList<Type> outputs = new LinkedList();
    protected LinkedList<Type> inputs = new LinkedList();
    protected LinkedList<String> inTypes = new LinkedList();
    protected LinkedList<String> outTypes = new LinkedList();

    // compute value and set it to outputs
    public abstract void execute ();

    public InternalWindow constructWindow(Pane canvas, Scheme parent_scheme, Integer block_id) {
        // content
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        int idx = 0;
        for (Type input : inputs) {
            Circle circle = new Circle(0, 0, 3);
            GridPane.setConstraints(circle, 0, 2 * idx + 1);
            Label inputType = new Label(inTypes.get(idx));
            GridPane.setConstraints(inputType, 0, 2 * idx);
            grid.getChildren().addAll(circle, inputType);
            idx += 1;
        }

        idx = 0;
        for (Type output : outputs) {
            Circle circle = new Circle(0, 0, 3);
            GridPane.setConstraints(circle, 1, 2 * idx + 1);
            GridPane.setHalignment(circle, HPos.RIGHT);
            Label outPutType = new Label(outTypes.get(idx));
            GridPane.setConstraints(outPutType, 1, 2 * idx);
            grid.getChildren().addAll(circle, outPutType);
            idx += 1;
        }
        // title bar
        BorderPane titleBar = new BorderPane();
        titleBar.setStyle("-fx-background-color: green; -fx-padding: 3");
        Label label = new Label(this.name);
        titleBar.setLeft(label);
        Button closeButton = new Button("x");
        titleBar.setRight(closeButton);
        // title bar + content
        BorderPane windowPane = new BorderPane();
        windowPane.setStyle("-fx-border-width: 1; -fx-border-color: black");
        windowPane.setTop(titleBar);
        windowPane.setCenter(grid);

        //apply layout to InternalWindow
        InternalWindow interalWindow = new InternalWindow();
        interalWindow.setStyle("-fx-background-color: white");
        interalWindow.setRoot(windowPane);
        //drag only by title
        interalWindow.makeDragable(titleBar, canvas);
        interalWindow.makeDragable(label, canvas);
        interalWindow.makeFocusable();
        closeButton.setOnAction(e -> {System.out.println("Block deleted");});
        interalWindow.setCloseButton(closeButton);

        interalWindow.setParentScheme(parent_scheme);
        interalWindow.setBlockID(block_id);

        return interalWindow;
    }

    public void outConnect (int n, Type out) {
        this.outputs.get(n).connect(out);

    }
    // propagate outputs to next block
    public void step () {
        for (Type tmp_out : outputs)
            tmp_out.step();
    }
    public int getNumberOfOutputs() {
        return this.outputs.size();
    }

    public int getNumberOfInputs() {
        return this.inputs.size();
    }

    public Type getOutputPort(int n) {
        return this.outputs.get(n);
    }

    public Type getInputPort(int n) {
        return this.inputs.get(n);
    }

    public String getName() {
        return this.name;
    }

    public Integer getID() {
        return this.id;
    }
}
