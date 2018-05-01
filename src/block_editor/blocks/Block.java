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
import java.text.DecimalFormat;
import block_editor.types.*;

public abstract class Block implements BlockInterface {
    protected Scheme parentScheme;
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
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.1));
            pauseTransition.setOnFinished(event -> lineActualize(0));
            pauseTransition.play();
        }
        for (Type type : inputs) {
            Bounds boundsInBorder = type.getNode().localToScene(this.border.getBoundsInLocal());
            if (!inputs.isEmpty()) {
                for (Line l : type.getLines()) {
                    l.setEndX(boundsInBorder.getMinX() - 5);
                    l.setEndY(boundsInBorder.getMinY());
                }
            }
        }
        for (Type type : outputs) {
            Bounds boundsInBorder = type.getNode().localToScene(this.border.getBoundsInLocal());
            for (Line l : type.getLines()) {
                l.setStartX(boundsInBorder.getMinX() + 5);
                l.setStartY(boundsInBorder.getMinY());
            }
        }
    }
    protected void getUserInput(Circle circle, GridPane portGrid, Type type) {
        type.setFromUser();
        type.clearValues();
        int idx = 2;
        for (Map.Entry<String, Double> entry: type.getItems().entrySet()) {

            // clearing old line if they existed
            for (Type dst : type.getDst()) {
                dst.clearDst(type);
            }
            type.set(false);
            // Label label = new Label(entry.getKey());
            TextField text = new TextField();
            text.setPromptText(entry.getKey());
            text.setStyle("-fx-font: 11 arial;");
            text.setPrefWidth(60);
            portGrid.setConstraints(text, 0, idx);
            portGrid.getChildren().add(text);
            lineActualize(1);
            idx += 1;
            // todo aktualizovat vsetky pozicie ciar v tomto bloku
            text.textProperty().addListener((obs, oldText, newText) -> {
                try {
                    entry.setValue(Double.parseDouble(newText));
                    text.setStyle("-fx-text-fill: black; -fx-font: 11 arial;");
                    parentScheme.setBlockAsChanged(this.id);
                } catch (Exception exception) {
                    text.setStyle("-fx-text-fill: red; -fx-font: 11 arial;");
                }
            });
        }
    }
    /**
     * \brief Function for user input, cretes TextField where user enters input value
     * \param circle Node when clicked 2 times textfield created
     * \param portGrid internal gridPane containing circle and input fields
     * \param type input type
     * \todo propagate valu to type
     */
    protected void setValue(Circle circle, GridPane portGrid, Type type) {
        circle.setOnMouseClicked(e -> {
            if(e.getButton().equals(javafx.scene.input.MouseButton.PRIMARY) && e.getClickCount() == 2 && (!type.isFromUser() || !type.isSet())){
                getUserInput(circle, portGrid, type);
            }
        });
    }
    
    /**
     * \brief Function shows final output of calculation, if port not connected
     */
    protected void showValues() {
        int idx = 2;
        for (Type out : this.outputs) {
            if (out.getLines().isEmpty()) {
                GridPane portGrid = (GridPane)out.getNode().getParent();
                for (Map.Entry<String, Double> entry: out.getItems().entrySet()) {
                    removeGridVals(portGrid);
                    DecimalFormat df = new DecimalFormat("#.####");
                    Label text = new Label(entry.getKey()+" : "+df.format(entry.getValue()));
                    portGrid.setConstraints(text, 0, idx);
                    portGrid.getChildren().add(text);
                this.lineActualize(1);
                }
            } else {
                System.out.println("num of lines "+out.getLines().size());
            }
            idx += 1;
            this.lineActualize(1);
        }
        this.lineActualize(1);
    }

    /**
     * \brief Removes line if error happened, or connection unsuccessful
     * \param type Source type of line/connection
     * \param cnvas Pane containing line objects
     */
    private void removeIncompleteLine(Type type, Pane canvas) {
        Line l = type.getLines().getLast();
        l.remove();
        type.getLines().removeLast();
    }

    /**
     * \brief Removes output or input labels with index higher than 1
     * because first label is type name, second is port node/circle
     * \param grid gridpain, which can contain unwanted labels
     */
    public int removeGridVals(GridPane grid) {
        int i = 0;
        LinkedList<javafx.scene.Node> nodesToRemove = new LinkedList<>();

        for (javafx.scene.Node child : grid.getChildren()) {
            // get index from child
            Integer columnIndex = grid.getColumnIndex(child);
    
            if (i > 1) {
                nodesToRemove.addLast(child);
            }
            i += 1;
        }
        grid.getChildren().removeAll(nodesToRemove);
        return i;
    }
    /**
     * \brief handles drag events from output ports
     * \param circle node, whic is being connected
     * \param border Pane containing block scheme
     * \param canvas global pane for drawing lines
     * \param type output data structure, which should be linked
     * \param parent_scheme global scheme for storing visual blocks
     */
    private void lineFromCircle(Circle circle, BorderPane border, Pane canvas, Type type) {
        
        circle.setOnMousePressed(e -> {
            GridPane grid = (GridPane) circle.getParent();

            if (removeGridVals(grid) > 1) {
                this.lineActualize(1);
            }
            Bounds boundsInBorder = circle.localToScene(border.getBoundsInLocal());
            double x = boundsInBorder.getMinX();
            double y = boundsInBorder.getMinY();
            javafx.scene.control.Label t = type.addLine(x + 5,y, x + e.getX(), y + e.getY());
            Line last = type.getLines().getLast();
            last.setEndX(x + e.getX());
            last.addAll(canvas);
            canvas.getChildren().add(t);
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
            Integer result = parentScheme.searchBlock(e.getSceneX(), e.getSceneY(), type, this.getID());
            if (result == -2) {
                removeIncompleteLine(type, canvas);
                parentScheme.msgCycleFound();
            }
            if (result == -1) {
                removeIncompleteLine(type, canvas);
                parentScheme.msgTypeError();
            }
            else if (result >= 0) {
                System.out.println("Connecting from [" + this.getID() + "] to [" + result + "]");
            }
        });

    }

    /**
     * \brief Function for setting shadow of visual window
     * \param color if null offset set to 0 and clorBlack
     */
    public void setShadow(Color color) {
        DropShadow ds = new DropShadow();
        if (color == null) {
            color = Color.GRAY;
            ds.setOffsetY(1);
            ds.setOffsetX(1);
        } else {
            ds.setOffsetY(3.0);
            ds.setOffsetX(3.0);
        }
        ds.setColor(color);
        this.border.getParent().setEffect(ds);
    }
    /**
     * \brief Function for drawing new interactive visual block
     * \param cavas space, where is block drawn
     * \param parent_scheme global scheme for storing visual blocks
     * \param block_id identifier of current block
     */
    public visualBlock constructWindow(Pane canvas, Scheme parent_scheme, Integer block_id) {
        this.parentScheme = parent_scheme;
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
            Label inputType = new Label(input.getName());
            inputType.setStyle("-fx-font: 12 arial;");
            portGrid.getChildren().addAll(circle, inputType);
            portGrid.setConstraints(inputType, 0, 0);
            portGrid.setConstraints(circle, 0, 1);
            GridPane.setConstraints(portGrid, 0, idx);
            grid.getChildren().addAll(portGrid);
            input.setNode(circle);
            setValue(circle, portGrid, input);
            idx += 1;
        }

        idx = 0;
        for (Type output : outputs) {
            GridPane portGrid = new GridPane();
            Circle circle = new Circle(0, 0, 5);
            Label outputType = new Label(output.getName());
            outputType.setStyle("-fx-font: 12 arial;");
            portGrid.setConstraints(outputType, 0, 0);
            portGrid.setHalignment(circle, HPos.RIGHT);
            portGrid.setConstraints(circle, 0, 1);
            portGrid.getChildren().addAll(circle, outputType);
            GridPane.setConstraints(portGrid, 1, idx);

            grid.getChildren().addAll(portGrid);
            output.setNode(circle);
            lineFromCircle(circle, windowPane, canvas, output);
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
        this.setShadow(null);

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

    public LinkedList<Type> getInputs() {
        return this.inputs;
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

    public LinkedList<Integer> getConnections(){
        return this.outputs.getFirst().getAllDstID();
    }
}
