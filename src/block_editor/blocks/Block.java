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
import javafx.scene.shape.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.geometry.Bounds;

import java.awt.Point;
import java.awt.MouseInfo;
import java.util.LinkedList;
import block_editor.types.*;

public abstract class Block implements BlockInterface {
    protected String name;
    protected LinkedList<Type> outputs = new LinkedList();
    protected LinkedList<Type> inputs = new LinkedList();
    protected LinkedList<String> inTypes = new LinkedList();
    protected LinkedList<String> outTypes = new LinkedList();

    // compute value and set it to outputs
    public abstract void execute ();

    private void lineFromCircle(Circle circle, BorderPane border, Pane canvas, Type type) {

        circle.setOnMousePressed(e -> {
            System.out.print("pressed ");
            Bounds boundsInBorder = circle.localToScene(border.getBoundsInLocal());
            double x = boundsInBorder.getMinX();
            double y = boundsInBorder.getMinY();
            type.lines.add(new Line(x,y, x + e.getX(), y + e.getY()));
            type.lines.getLast().setEndX(x + e.getX());
            canvas.getChildren().add(type.lines.getLast());
        });
        circle.setOnMouseDragged(e -> {    
            Bounds boundsInBorder = circle.localToScene(border.getBoundsInLocal());
            double x = boundsInBorder.getMinX();
            double y = boundsInBorder.getMinY();

            if (x > 0 && y > 0 && x + e.getX() > 0 && y + e.getY() > 0) {
                type.lines.getLast().setEndX(x + e.getX());
                type.lines.getLast().setEndY(y + e.getY());
            }

        });
    }

    public InternalWindow constructWindow(Pane canvas) {
        // content
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);


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

        int idx = 0;
        for (Type input : inputs) {
            Circle circle = new Circle(0, 0, 5);
            GridPane.setConstraints(circle, 0, 2 * idx + 1);
            Label inputType = new Label(inTypes.get(idx));
            GridPane.setConstraints(inputType, 0, 2 * idx);
            grid.getChildren().addAll(circle, inputType);
            lineFromCircle(circle, windowPane, canvas, input);
            idx += 1;
        }

        idx = 0;
        for (Type output : outputs) {
            Circle circle = new Circle(0, 0, 5);
            GridPane.setConstraints(circle, 1, 2 * idx + 1);
            GridPane.setHalignment(circle, HPos.RIGHT);
            Label outPutType = new Label(outTypes.get(idx));
            GridPane.setConstraints(outPutType, 1, 2 * idx);
            grid.getChildren().addAll(circle, outPutType);
            lineFromCircle(circle, windowPane, canvas, output);
            idx += 1;
        }
        windowPane.setCenter(grid);

        //apply layout to InternalWindow
        InternalWindow interalWindow = new InternalWindow();
        interalWindow.setRoot(windowPane);
        //drag only by title
        interalWindow.makeDragable(titleBar, canvas, this);
        interalWindow.makeDragable(label, canvas, this);
        interalWindow.makeFocusable();
        interalWindow.setCloseButton(closeButton, canvas, this);

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
    private class Delta {
        double x, y;
    }
}
