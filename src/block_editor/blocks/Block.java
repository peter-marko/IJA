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
import javafx.geometry.Bounds;
import java.util.Optional;
import java.util.Map;

import java.awt.Point;
import java.beans.IndexedPropertyChangeEvent;
import java.awt.MouseInfo;
import java.util.LinkedList;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import block_editor.types.*;

public abstract class Block implements BlockInterface {
    protected String name;
    protected Integer id;
    protected LinkedList<Type> outputs = new LinkedList();
    protected LinkedList<Type> inputs = new LinkedList();
    protected BorderPane border;

    /**
     * \brief Function for actualization of visual block, when added inputText, for user input
     * \param pause wait few milliseconds for scene to be redrawn, then actualize
     */
    public void lineActualize(int pause) {
        if (pause == 1) {
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.03));
            pauseTransition.setOnFinished(event -> lineActualize(0));
            pauseTransition.play();
        }
        for (Type type : inputs) {
            Bounds boundsInBorder = type.getNode().localToScene(this.border.getBoundsInLocal());
            if (!inputs.isEmpty()) {
                for (Line l : type.getLines()) {
                    l.setEndX(boundsInBorder.getMinX());
                    l.setEndY(boundsInBorder.getMinY());
                }
            }
        }
        for (Type type : outputs) {
            Bounds boundsInBorder = type.getNode().localToScene(this.border.getBoundsInLocal());
            for (Line l : type.getLines()) {
                l.setStartX(boundsInBorder.getMinX());
                l.setStartY(boundsInBorder.getMinY());
            }
        }
    }

    /**
     * \brief Function for user input, cretes TextField where user enters input value
     * \param circle Node when clicked 2 times textfield created
     * \param portGrid internal gridPane containing circle and input fields
     * \param type input type
     * \todo propagate valu to type
     */
    private void setValue(Circle circle, GridPane portGrid, Pane canvas, Type type) {
        circle.setOnMouseClicked(e -> {
            if(e.getButton().equals(javafx.scene.input.MouseButton.PRIMARY) && e.getClickCount() == 2){
                type.setFromUser();
                int idx = 1;
                for (Map.Entry<String, Double> entry: type.getItems().entrySet()) {

                    // clearing old line if they existed
                    for (Line l : type.getLines()) {
                        canvas.getChildren().remove(l);
                    }

                    type.set(false);
                    // Label label = new Label(entry.getKey());
                    TextField text = new TextField();
                    text.setPromptText(entry.getKey());
                    text.setStyle("-fx-font: 11 arial;");
                    text.setPrefWidth(60);
                    // portGrid.setConstraints(label, 0, idx + 1);
                    portGrid.setConstraints(text, 0, idx);
                    portGrid.getChildren().addAll(text);
                    // canvas.getChildren().addAll(border);
                    lineActualize(1);
                    idx += 1;
                    // todo aktualizovat vsetky pozicie ciar v tomto bloku
                    text.textProperty().addListener((obs, oldText, newText) -> {
                        try {
                            entry.setValue(Double.parseDouble(newText));
                            text.setStyle("-fx-text-fill: black; -fx-font: 11 arial;");
                        } catch (Exception exception) {
                            text.setStyle("-fx-text-fill: red; -fx-font: 11 arial;");
                        }
                    });
                }
            }
        });
    }

    /**
     * \brief handles drag events from output ports
     * \param circle node, whic is being connected
     * \param border Pane containing block scheme
     * \param canvas global pane for drawing lines
     * \param type output data structure, which should be linked
     * \param parent_scheme global scheme for storing visual blocks
     */
    private void lineFromCircle(Circle circle, BorderPane border, Pane canvas, Type type, Scheme parent_scheme) {
        
        circle.setOnMousePressed(e -> {
            Bounds boundsInBorder = circle.localToScene(border.getBoundsInLocal());
            double x = boundsInBorder.getMinX();
            double y = boundsInBorder.getMinY();
            type.addLine(x,y, x + e.getX(), y + e.getY());
            type.getLines().getLast().setEndX(x + e.getX());
            canvas.getChildren().add(type.getLines().getLast());
        });
        
        circle.setOnMouseDragged(e -> {    
            Bounds boundsInBorder = circle.localToScene(border.getBoundsInLocal());
            double x = boundsInBorder.getMinX();
            double y = boundsInBorder.getMinY();
            if (x > 0 && y > 0 && x + e.getX() > 0 && y + e.getY() > 0) {
                type.getLines().getLast().setEndX(x + e.getX());
                type.getLines().getLast().setEndY(y + e.getY());
            }
        });
        
        circle.setOnMouseReleased(e -> {
            Bounds boundsInBorder = circle.localToScene(border.getBoundsInLocal());
            Integer result = parent_scheme.searchBlock(e.getSceneX(), e.getSceneY(), type);
            if (result == -1) {
                canvas.getChildren().remove(type.getLines().getLast());
            }
            else{
                parent_scheme.connect(this.getID(), result);
                System.out.println("Connecting from [" + this.getID() + "] to [" + result + "]");
            }
        });

    }

    /**
     * \brief Function for drawing new interactive visual block
     * \param cavas space, where is block drawn
     * \param parent_scheme global scheme for storing visual blocks
     * \param block_id identifier of current block
     */
    public visualBlock constructWindow(Pane canvas, Scheme parent_scheme, Integer block_id) {
        // content
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);


        // title bar
        BorderPane titleBar = new BorderPane();
        titleBar.setStyle("-fx-background-color: #dbeacf; -fx-padding: 3");
        Label label = new Label(this.name);
        titleBar.setLeft(label);
        Button closeButton = new Button("x");
        closeButton.setStyle("-fx-font: 12 arial;");
        titleBar.setRight(closeButton);
        // title bar + content
        BorderPane windowPane = new BorderPane();
        windowPane.setStyle("-fx-border-width: 1; -fx-border-color: black");
        windowPane.setTop(titleBar);

        int idx = 0;
        for (Type input : inputs) {
            GridPane portGrid = new GridPane();
            Circle circle = new Circle(0, 0, 5);
            portGrid.setConstraints(circle, 0, 0);
            GridPane.setConstraints(portGrid, 0, 2 * idx + 1);
            portGrid.getChildren().add(circle);
            Label inputType = new Label(input.getName());
            inputType.setStyle("-fx-font: 12 arial;");
            GridPane.setConstraints(inputType, 0, 2 * idx);
            grid.getChildren().addAll(portGrid, inputType);
            input.setNode(circle);
            setValue(circle, portGrid, canvas, input);
            idx += 1;
        }

        idx = 0;
        for (Type output : outputs) {
            Circle circle = new Circle(0, 0, 5);
            GridPane.setConstraints(circle, 1, 2 * idx + 1);
            GridPane.setHalignment(circle, HPos.RIGHT);
            Label outputType = new Label(output.getName());
            outputType.setStyle("-fx-font: 12 arial;");
            GridPane.setConstraints(outputType, 1, 2 * idx);
            grid.getChildren().addAll(circle, outputType);
            output.setNode(circle);
            lineFromCircle(circle, windowPane, canvas, output, parent_scheme);
            idx += 1;
        }
        windowPane.setCenter(grid);

        //apply layout to visualBlock
        visualBlock window = new visualBlock();
        window.setStyle("-fx-background-color: white");
        window.setRoot(windowPane);
        this.border = windowPane;
        //drag only by title
        window.makeDragable(titleBar, canvas, this);
        window.makeDragable(label, canvas, this);
        window.makeFocusable();
        closeButton.setOnAction(e -> {System.out.println("Block deleted");});
        window.setCloseButton(closeButton, canvas, this);

        window.setParentScheme(parent_scheme);
        window.setBlockID(block_id);

        return window;
    }

    public void clear() {
        for (Type output : outputs) {
            for (Type dstPort : output.getDst()) {
                dstPort.set(false);
            }
        }
        this.outputs.clear();
        this.inputs.clear();
    }

    public BorderPane getBorder() {
        return this.border;
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

    /**
     * \brief checks if block is prepared to computation (has all input ports set)
     * \return true if block is prepared, false if some input value is missing
     */
    public boolean isPrepared() {
        for (Type input : this.inputs) {
            if(input.isPrepared() == false)
            {
                return false;
            }
        }
        return true;
    }

    /**
     * \brief delete values in connected input ports
     */
    public void deleteInputValues() {
        for (Type input : this.inputs) {
            if(input.isFromUser() == false){
                input.deleteValues();
            }

        }
    }
}
