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

import block_editor.types.*;
public class Block1 extends Block {
    public Block1 (String newName) {
        this.name = newName;
        this.outputs.add(new TypeA ("out"));
        this.inputs.add(new TypeA("in1"));
        this.inputs.add(new TypeA("in2"));
    }

    public void execute () {
        
    }
}
