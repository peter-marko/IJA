package block_editor.blocks;

import javafx.geometry.Insets;
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

import java.util.Map;
import block_editor.types.*;
public class BlockDistance2D extends Block {
    public BlockDistance2D (String newName, Type t1, Type t2) {
        this.name = newName;
        this.inputs.add(t1);
        this.inputs.add(t2);
        this.outputs.add(new TypeSimple(0));
    }
    public void execute () {
        Type i1 = inputs.get(0);
        Type i2 = inputs.get(1);
        double output = Math.sqrt(Math.pow(i1.getVal("x") - i2.getVal("x"),2) + 
            Math.pow(i1.getVal("y") - i2.getVal("y"), 2));
        this.outputs.get(0).putVal("simple", output);
    }

    public InternalWindow constructWindow(Pane canvas) {
        // content
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label L1 = new Label("uname:");
        GridPane.setConstraints(L1, 0, 0);
        Label L2 = new Label("2name:");
        GridPane.setConstraints(L2, 0, 2);
        
        Button btn1 = new Button();
        btn1.setText("'Hello World'");
        btn1.setOnAction(e -> {
            System.out.println("BlockDistance2D button click");
        });
        GridPane.setConstraints(btn1, 2, 0);
        grid.getChildren().addAll(L1, L2, btn1);

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
        interalWindow.setRoot(windowPane);
        //drag only by title
        interalWindow.makeDragable(titleBar, canvas);
        interalWindow.makeDragable(label, canvas);
        interalWindow.makeFocusable();
        interalWindow.setCloseButton(closeButton);
        return interalWindow;
    }
}
