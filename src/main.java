

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.Scanner;
import java.io.IOException;
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

import java.io.IOException;

import block_editor.blocks.*;
import block_editor.types.*;
public class main extends Application {
    File file;
    @Override
    public void start(Stage primaryStage) throws Exception {
        // content
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Pane root = new Pane();
        Pane canvas = new Pane();
        canvas.setLayoutY(40);

        Button btn2 = new Button();
        btn2.setText("New BlockDistance2D");
        btn2.setOnAction(e -> {
            Type coordA = new TypeCoordinate2D(0.0,0.0);
            Type coordB = new TypeCoordinate2D(3.0,4.0);
            Block b = new BlockDistance2D("Novy block", coordA, coordB);
            canvas.getChildren().add(b.constructWindow(root));
        });
        Button btn1 = new Button();
        btn1.setText("Open scheme");
        btn1.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            // file = fc.showOpenDialog(primaryStage);
            file = fc.showOpenDialog(new Stage());
            if (file != null) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException ex) {
                    System.out.println("Error : opening file");
                }
            }
        });
        GridPane.setConstraints(btn1, 0, 0);
        GridPane.setConstraints(btn2, 1, 0);
        grid.getChildren().addAll(btn1, btn2);

        root.getChildren().add(grid);
        root.getChildren().add(canvas);
        // root.getChildren().add(constructWindow());
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}