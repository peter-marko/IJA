

import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
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


import javafx.scene.input.MouseEvent;

import java.io.IOException;

import block_editor.blocks.*;
import block_editor.types.*;

//import java.util.LinkedList;

public class main extends Application {
    private File file;
    private GridPane grid;
    private Pane root;
    private Pane canvas;

    private Scheme actual_scheme = new Scheme();

    //public LinkedList<Block> blocks = new LinkedList<Block>();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new Pane();
        canvas = new Pane();
        canvas.setLayoutY(40);
        GUI();
        root.getChildren().add(grid);
        root.getChildren().add(canvas);
        // root.getChildren().add(constructWindow());
        Scene s = new Scene(root, 1024, 768);
        primaryStage.setTitle("Block editor");
        primaryStage.setScene(s);
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

        MenuItem itemNew = new MenuItem("New"); // ----------------------------------------------------- new
        itemNew.setOnAction(e -> {
            // TODO
            System.out.println("Item New Clicked");
            canvas.getChildren().clear(); // clear all visual blocks
            actual_scheme.clear(); // clear all block objects in list
        });

        MenuItem itemOpen = new MenuItem("Open"); // ----------------------------------------------------- open
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
     
        MenuItem itemSave = new MenuItem("Save"); // ----------------------------------------------------- save
        itemSave.setOnAction(e -> {
            // TODO
            System.out.println("Item Save Clicked");
            actual_scheme.print();
        });

        MenuItem itemSaveAs = new MenuItem("Save as"); // --------------------------------------------------- save as
        itemSaveAs.setOnAction(e -> {
            // TODO
            System.out.println("Item Save as Clicked");
        });
           
        menuFile.getItems().addAll(itemNew, itemOpen, itemSave, itemSaveAs);

        MenuItem itemNewDistance2D = new MenuItem("Distance 2D"); // --------------------------------------- distance 2D
        itemNewDistance2D.setOnAction(e -> {
            Block b = new BlockDistance2D("2D distance", actual_scheme.getBlockID()); // block with new ID in scheme
            canvas.getChildren().add(b.constructWindow(root, actual_scheme, b.getID()));
            System.out.println("Creating block " + b.getName() + " " + b.getID());
            actual_scheme.addBlock(b); // add block object to list
        });

        MenuItem itemNewSimpleAdd = new MenuItem("Simple add"); // --------------------------------------- Simple
        itemNewSimpleAdd.setOnAction(e -> {
            Block b = new BlockAdd("Simple add", actual_scheme.getBlockID()); // block with new ID in scheme
            canvas.getChildren().add(b.constructWindow(root, actual_scheme, b.getID()));
            System.out.println("Creating block " + b.getName() + " " + b.getID());
            actual_scheme.addBlock(b); // add block object to list
        });

        
        Button step = new Button(); // --------------------------------------------------------------------- step
        step.setText("Step");
        step.setOnAction(e -> {
            // TODO
            System.out.println("Step button");
        });
        GridPane.setConstraints(step, 1, 0);

        Button run = new Button(); // ---------------------------------------------------------------------- run
        run.setText("Run");
        run.setOnAction(e -> {
            // TODO
            System.out.println("Run button");
        });
        GridPane.setConstraints(run, 2, 0);

        menuBlock.getItems().addAll(itemNewDistance2D, itemNewSimpleAdd);

        GridPane.setConstraints(menuBar, 0, 0);
        menuBar.getMenus().addAll(menuFile, menuBlock);
        grid.getChildren().addAll(menuBar, step, run);
    }

    public static void main(String[] args) {
        launch(args);
    }
}