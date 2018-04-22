

import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import java.io.IOException;

import block_editor.blocks.*;
import block_editor.types.*;
public class main extends Application {
    private File file;
    private GridPane grid;
    private Pane root;
    private Pane canvas;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new Pane();
        canvas = new Pane();
        canvas.setLayoutY(40);
        GUI();
        root.getChildren().add(grid);
        root.getChildren().add(canvas);
        // root.getChildren().add(constructWindow());
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private void GUI() {
        // content
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("File");
        Menu menuBlock = new Menu("New block");

        MenuItem itemNew = new MenuItem("New");
        itemNew.setOnAction(e -> {
            // TODO
            System.out.println("Item New Clicked");
        });

        MenuItem itemOpen = new MenuItem("Open");
        itemOpen.setOnAction(e -> {
            // TODO
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
     
        MenuItem itemSave = new MenuItem("Save");
        itemSave.setOnAction(e -> {
            // TODO
            System.out.println("Item Save Clicked");
        });

        MenuItem itemSaveAs = new MenuItem("Save as");
        itemSaveAs.setOnAction(e -> {
            // TODO
            System.out.println("Item Save as Clicked");
        });
           
        menuFile.getItems().addAll(itemNew, itemOpen, itemSave, itemSaveAs);

        MenuItem itemNewDistance2D = new MenuItem("Distance 2D");
        itemNewDistance2D.setOnAction(e -> {
            Block b = new BlockDistance2D("Novy block", null, null);
            canvas.getChildren().add(b.constructWindow(root));
        });

        Button step = new Button();
        step.setText("Step");
        step.setOnAction(e -> {
            // TODO
            System.out.println("Step button");
        });
        GridPane.setConstraints(step, 1, 0);

        Button run = new Button();
        run.setText("Run");
        run.setOnAction(e -> {
            // TODO
            System.out.println("Run button");
        });
        GridPane.setConstraints(run, 2, 0);

        menuBlock.getItems().addAll(itemNewDistance2D);

        GridPane.setConstraints(menuBar, 0, 0);
        menuBar.getMenus().addAll(menuFile, menuBlock);
        grid.getChildren().addAll(menuBar, step, run);
    }

    public static void main(String[] args) {
        launch(args);
    }
}