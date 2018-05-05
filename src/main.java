import java.util.LinkedList;
import java.util.List;
import javafx.stage.FileChooser;
import java.io.*;
import javafx.geometry.Insets;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import java.lang.reflect.*;
import javafx.scene.input.MouseEvent;

import block_editor.blocks.*;
import block_editor.types.*;

//import java.util.LinkedList;

public class main extends Application {
    public GridPane grid;
    public Pane root;
    public Pane canvas;
    private programState state = new programState();


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
     *  Function for addin new blocks into menu
     * @param name Class name of new block, located in block_editor.blocks.
     * @param subName Name of block in GUI and menu
     * @param menuBlock panel containing menus
     */
    private void addBlockToMenu(String name, String subName, Menu menuBlock) {
        name = "block_editor.blocks."+name;
        try {
            Class c = Class.forName(name);
            Constructor<Block> ctor = c.getConstructor(String.class, Integer.class);
            MenuItem menuItem = new MenuItem(subName);
            menuItem.setOnAction(e -> {
                try {
                    Block b = null;
                    b = ctor.newInstance(subName, state.scheme.getBlockID()); // block with new ID in scheme
                    canvas.getChildren().add(b.constructWindow(root, state.scheme, b.getID(), 0, 0));
                    System.out.println("Creating block " + b.getName() + " " + b.getID());
                    state.scheme.addBlock(b); // add block object to list
                } catch (Exception except) {
                    // System.out.println(except);
                    except.printStackTrace();
                }
            });
            menuBlock.getItems().add(menuItem);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void chooseSrc() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Block editor Files (.block)", "*.block"),
                new FileChooser.ExtensionFilter("Text Files (.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
            state.file = fileChooser.showOpenDialog(new Stage()).getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void chooseDst() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Block editor Files (.block)", "*.block"),
                new FileChooser.ExtensionFilter("Text Files (.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
            File file = fileChooser.showSaveDialog(new Stage());
            state.file = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void GUI() {
        state.scheme = new Scheme();
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
            state.scheme.clear(root); // clear all block objects in list

            root.getChildren().add(grid);
            root.getChildren().add(canvas);
        });

        MenuItem itemOpen = new MenuItem("Open"); // ----------------------------------------------------- open
        itemOpen.setOnAction(e -> {

            canvas.getChildren().clear(); // clear all visual blocks
            state.scheme.clear(root); // clear all block objects in list

            root.getChildren().add(grid);
            root.getChildren().add(canvas);
            chooseSrc();
            state.deserialize(root, canvas);
        });
        
        MenuItem itemSave = new MenuItem("Save"); // ----------------------------------------------------- save
        itemSave.setOnAction(e -> {
            if (state.file == null) {
                chooseDst();
            }
            state.serialize();
        });

        MenuItem itemSaveAs = new MenuItem("Save as"); // --------------------------------------------------- save as
        itemSaveAs.setOnAction(e -> {
            chooseDst();
            state.serialize();
        });

        MenuItem itemPrint = new MenuItem("*Print"); // --------------------------------------------------- *print (debug)
        itemPrint.setOnAction(e -> {
            // TODO
            System.out.println("Item Print Clicked");
            state.scheme.print(); // print actual scheme
        });

        MenuItem itemCheck = new MenuItem("*Check"); // --------------------------------------------------- *check (debug)
        itemCheck.setOnAction(e -> {
            // TODO
            System.out.println("Item Check Clicked");
            if(state.scheme.checkCycles()){
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
        addBlockToMenu("BlockCenterOfGravity", "Center of gravity", menuBlock);
        
        Button step = new Button(); // --------------------------------------------------------------------- step
        step.setText("Step");
        step.setOnAction(e -> {
            // TODO
            System.out.println("Step button");
            state.scheme.executeNext();
        });
        GridPane.setConstraints(step, 1, 0);

        Button run = new Button(); // ---------------------------------------------------------------------- run
        run.setText("Run");
        run.setOnAction(e -> {
            // TODO
            System.out.println("Run button");
            state.scheme.executeAll();
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