

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
import java.lang.reflect.*;

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
    /**
     * \brief Function for addin new blocks into menu
     * \param name Class name of new block, located in block_editor.blocks.
     * \param subName Name of block in GUI and menu
     * \param menuBlock panel containing menus
     */
    private void addBlockToMenu(String name, String subName, Menu menuBlock) {
        name = "block_editor.blocks."+name;
        try {
            Class c = Class.forName(name);
            Constructor<Block> ctor = c.getConstructor(String.class, Integer.class);
            MenuItem menuItem = new MenuItem(subName);
            menuItem.setOnAction(e -> {
                try {
                    Block b = ctor.newInstance(subName, actual_scheme.getBlockID()); // block with new ID in scheme
                    canvas.getChildren().add(b.constructWindow(root, actual_scheme, b.getID()));
                    System.out.println("Creating block " + b.getName() + " " + b.getID());
                    actual_scheme.addBlock(b); // add block object to list
                } catch (Exception except) {
                    System.out.println(except);
                }
            });
            menuBlock.getItems().add(menuItem);
        } catch (Exception e) {
            System.out.println(e);
        }
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
        });

        MenuItem itemSaveAs = new MenuItem("Save as"); // --------------------------------------------------- save as
        itemSaveAs.setOnAction(e -> {
            // TODO
            System.out.println("Item Save as Clicked");
        });

        MenuItem itemPrint = new MenuItem("*Print"); // --------------------------------------------------- *print (debug)
        itemPrint.setOnAction(e -> {
            // TODO
            System.out.println("Item Print Clicked");
            actual_scheme.print(); // print actual scheme
        });

        MenuItem itemCheck = new MenuItem("*Check"); // --------------------------------------------------- *check (debug)
        itemCheck.setOnAction(e -> {
            // TODO
            System.out.println("Item Check Clicked");
            if(actual_scheme.checkCycles()){
                System.out.println("No cycles detected."); 
            }
            else{
                System.out.println("Cycles check failed!");
            }
        });
        
        menuFile.getItems().addAll(itemNew, itemOpen, itemSave, itemSaveAs, itemPrint, itemCheck);
        
        // creating new menu items 
        //              name of class  name of visual block, menu
        addBlockToMenu("BlockDistance2D", "Distance 2D", menuBlock);
        addBlockToMenu("BlockVector", "Vector", menuBlock);
        addBlockToMenu("BlockSum", "Sum", menuBlock);
        addBlockToMenu("BlockAdd", "Add", menuBlock);

        MenuItem itemNewSimpleAdd = new MenuItem("Simple add"); // --------------------------------------- Simple Add
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
            actual_scheme.executeNext();
        });
        GridPane.setConstraints(step, 1, 0);

        Button run = new Button(); // ---------------------------------------------------------------------- run
        run.setText("Run");
        run.setOnAction(e -> {
            // TODO
            System.out.println("Run button");
            actual_scheme.executeAll();
        });
        GridPane.setConstraints(run, 2, 0);

        GridPane.setConstraints(menuBar, 0, 0);
        menuBar.getMenus().addAll(menuFile, menuBlock);
        grid.getChildren().addAll(menuBar, step, run);
    }

    public static void main(String[] args) {
        launch(args);
    }
}