package block_editor.blocks;

/**
 * Holds data common for blocks, creates visual blocks
 * @author Stanislav Mechl
 * @author Peter Marko
 */
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.geometry.Bounds;
import java.util.Map;
import java.util.LinkedList;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.util.Iterator;
import java.text.DecimalFormat;
import java.lang.reflect.Constructor;

import block_editor.types.*;
/**
 *  Class holding data about logic blocks
 */
public abstract class Block implements BlockInterface, java.io.Serializable {
    protected Scheme parentScheme;
    protected Integer id;
    protected String name;
    protected LinkedList<Type> inputs = new LinkedList();
    protected LinkedList<Type> outputs = new LinkedList();
    protected transient BorderPane border;
    protected double x;
    protected double y;

    /**
     *  getX X coordinate of visual block within canvas
     * @return X coordinate of left upper corner
     */
    public double getX() {
        return this.x;
    }
    /**
     *  getY Y coordinate of visual block within canvas
     * @return Y coordinate of left upper corner
     */
    public double getY() {
        return this.y;
    }
    /**
     *  Actualize info before serialized
     */
    public void serialize() {
        Bounds bounds = this.border.localToScene(this.border.getBoundsInLocal());
        this.x = bounds.getMinX();
        this.y = bounds.getMinY();
    }

    /**
     *  Function for adding next port to visual block
     * @param parentGrid input or output grid of visual block
     * @param next metadata for setting up new port
     * @param row row of new port within input or output grid
     * @param canvas main pane, where blocks and lines are located
     */
    protected void newPort(GridPane parentGrid, Type next, int row, Pane canvas) {
        GridPane portGrid = new GridPane();
        Circle circle = new Circle(0, 0, 5);
        Label inputType = new Label(next.getName());
        inputType.setStyle("-fx-font: 12 arial;");
        portGrid.setConstraints(inputType, 0, 0);
        portGrid.getChildren().addAll(circle, inputType);
        portGrid.setConstraints(circle, 0, 1);
        parentGrid.setConstraints(portGrid, 0, row);
        parentGrid.getChildren().addAll(portGrid);
        setValue(circle, portGrid, next);
        next.setNode(circle);
        if (canvas == null)
            this.setValue(circle, portGrid, next);
        else
        {
            portGrid.setHalignment(circle, HPos.RIGHT);
            lineFromCircle(circle, canvas, next);
        }
        if (next.getStatus() == 1 && !next.isSet()) {
            getUserInput(portGrid, next, true);
        }
        if (next.getStatus() == 3 && !next.isSet()) {
            showValue(next);
        }
    }

    /**
     *  Function for actualization of visual block, when added inputText, for user input
     * @param pause wait few milliseconds for scene to be redrawn, then actualize
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
                    Bounds boundsOpposite = type.getDst().get(0).getNode().localToScene(this.border.getBoundsInLocal());
                    l.setEndX(boundsInBorder.getMinX() - 5);
                    l.setEndY(boundsInBorder.getMinY());
                    l.setStartX(boundsOpposite.getMinX() + 5);
                    l.setStartY(boundsOpposite.getMinY());
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
    /**
     *  Function for creating text field inside of visual block and 
     * loading input from user
     * @param portGrid port where text field should be created
     * @param type Metadata for writing output
     * @param load true if metadata are loaded from file at deserialization,
     * than dont make checks
     */
    protected void getUserInput(GridPane portGrid, Type type, Boolean load) {
        if (!load) {
            type.setStatus(1);
            type.clearValues();
        }
        int idx = 2;
        for (Map.Entry<String, Double> entry: type.getItems().entrySet()) {

            // clearing old line if they existed
            if (!load) {
                for (Type dst : type.getDst()) {
                    dst.clearDst(type);
                }
            }
            type.set(false);
            // Label label = new Label(entry.getKey());
            TextField text = new TextField();
            text.setPromptText(entry.getKey());
            text.setStyle("-fx-font: 11 arial;");
            text.setPrefWidth(60);
            if (load && entry.getValue() != null) {
                text.setText(entry.getValue().toString());
            }
            portGrid.setConstraints(text, 0, idx);
            portGrid.getChildren().add(text);
            if (!load)
                lineActualize(1);
            idx += 1;
            // todo aktualizovat vsetky pozicie ciar v tomto bloku
            text.textProperty().addListener((obs, oldText, newText) -> {
                try {
                    entry.setValue(Double.parseDouble(newText));
                    text.setStyle("-fx-text-fill: black; -fx-font: 11 arial;");
                } catch (Exception exception) {
                    text.setStyle("-fx-text-fill: red; -fx-font: 11 arial;");
                    entry.setValue(null);
                }
                parentScheme.setBlockAsChanged(this.id);
            });
        }
    }
    /**
     *  Function for user input, cretes TextField where user enters input value
     * @param circle Node when clicked 2 times textfield created
     * @param portGrid internal gridPane containing circle and input fields
     * @param type input type
     * \todo propagate valu to type
     */
    protected void setValue(Circle circle, GridPane portGrid, Type type) {
        circle.setOnMouseClicked(e -> {
            if(e.getButton().equals(javafx.scene.input.MouseButton.PRIMARY) && e.getClickCount() == 2){
                getUserInput(portGrid, type, false);
            }
        });
    }
    /**
     *  prints value at the output port
     * @param out Port where should be value printed
     */
    protected void showValue(Type out) {
        GridPane portGrid = (GridPane)out.getNode().getParent();
            int idx = 2;
            out.setStatus(3);
            removeGridVals(portGrid, false);
            for (Map.Entry<String, Double> entry: out.getItems().entrySet()) {
                System.out.println("out "+idx);
                DecimalFormat df = new DecimalFormat("#.####");
                Label text = new Label(entry.getKey()+" : "+df.format(entry.getValue()));
                portGrid.setConstraints(text, 0, idx);
                portGrid.getChildren().add(text);
                // this.lineActualize(1);
                idx += 1;
            }
    }
    /**
     *  Function shows final output of calculation, if port not connected
     */
    protected void showValues() {
        for (Type out : this.outputs) {
            if (out.getLines().isEmpty()) {
                showValue(out);
            } else {
                System.out.println("num of lines "+out.getLines().size());
            }
            this.lineActualize(1);
        }
        this.lineActualize(1);
    }

    /**
     *  Removes line if error happened, or connection unsuccessful
     * @param type Source type of line/connection
     * @param cnvas Pane containing line objects
     */
    private void removeIncompleteLine(Type type, Pane canvas) {
        Line l = type.getLines().getLast();
        l.remove();
        type.getLines().removeLast();
    }

    /**
     *  Removes output or input labels with index higher than 1
     * because first label is type name, second is port node/circle
     * @param grid gridpain, which can contain unwanted labels
     * @param input true when removing from input side of block, false otherwise
     * @return number of removed children
     */
    public int removeGridVals(GridPane grid, Boolean input) {
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
     *  handles drag events from output ports
     * @param circle node, whic is being connected
     * @param canvas global pane for drawing lines
     * @param type output data structure, which should be linked
     */
    private void lineFromCircle(Circle circle, Pane canvas, Type type) {
        // source circle was clicked -> initialization
        circle.setOnMousePressed(e -> {
            GridPane grid = (GridPane) circle.getParent();

            if (removeGridVals(grid, false) > 1) {
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
        // drawing line as user moves with mouse
        circle.setOnMouseDragged(e -> {    
            Bounds boundsInBorder = circle.localToScene(border.getBoundsInLocal());
            double x = boundsInBorder.getMinX();
            double y = boundsInBorder.getMinY();
            if (x > 0 && y > 0 && x + e.getX() > 0 && y + e.getY() > 0) {
                type.getLines().getLast().setEndX(x + e.getX());
                type.getLines().getLast().setEndY(y + e.getY());
            }
        });
        // connecting to input port
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
     *  Function for setting shadow of visual window
     * @param color if null offset set to 0 and clorBlack
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
     *  Function for drawing new interactive visual block
     * @param canvas space, where is block drawn
     * @param parent_scheme global scheme for storing visual blocks
     * @param block_id identifier of current block
     * @param X X coordinate of new visual within canvas
     * @param Y Y coordinate of new visual within canvas
     */
    public visualBlock constructWindow(Pane canvas, Scheme parent_scheme, Integer block_id, double X, double Y) {
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
        GridPane inGrid = new GridPane();
        grid.setConstraints(inGrid, 0, 0);
        grid.getChildren().add(inGrid);
        for (Type input : inputs) {
            newPort(inGrid, input, idx, null);
            idx += 1;
        }

        idx = 0;
        GridPane outGrid = new GridPane();
        grid.setConstraints(outGrid, 1, 0);
        grid.getChildren().add(outGrid);
        for (Type output : outputs) {
            newPort(outGrid, output, idx, canvas);
            idx += 1;
        }
        windowPane.setCenter(grid);

        //apply layout to visualBlock
        visualBlock window = new visualBlock();
        window.setStyle("-fx-background-color: white");
        window.setRoot(windowPane);
        window.setLayoutX(X);
        window.setLayoutY(Y);
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
    /**
     * Function for removing current block and all its sub information
     */
    public void clear() {
        for (Type output : outputs) {
            for (Type dstPort : output.getDst()) {
                dstPort.set(false);
            }
        }
        this.outputs.clear();
        this.inputs.clear();
    }
    /**
     * Loading border pane of current visual block
     * @return Border pane holding the visual information, about this block
     */
    public BorderPane getBorder() {
        return this.border;
    }
    /**
     * Propagates outputs to connected blocks
     */
    public void step () {
        for (Type tmp_out : outputs)
            tmp_out.step();
    }
    /**
     * Loads number of output ports
     * @return number of output ports
     */
    public int getNumberOfOutputs() {
        return this.outputs.size();
    }
    /**
     * Loads number of input ports
     * @return number of input ports
     */
    public int getNumberOfInputs() {
        return this.inputs.size();
    }
    /**
     * @param n Index specifying output port
     * @return Refrence to port at requested position
     */
    public Type getOutputPort(int n) {
        return this.outputs.get(n);
    }
    /**
     * @param n Index specifying input port
     * @return Refrence to port at requested position
     */
    public Type getInputPort(int n) {
        return this.inputs.get(n);
    }
    /**
     * @return List containing all input ports
     */
    public LinkedList<Type> getInputs() {
        return this.inputs;
    }
    /**
     * @return List containing all output ports
     */
    public LinkedList<Type> getOutputs() {
        return this.outputs;
    }
    /**
     * @return Name of current block
     */
    public String getName() {
        return this.name;
    }
    /**
     * @return Identifier of current block
     */
    public Integer getID() {
        return this.id;
    }

    /**
     *  checks if block is prepared to computation (has all input ports set)
     * @return true if block is prepared, false if some input value is missing
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
     *  delete values in connected input ports
     */
    public void deleteInputValues() {
        for (Type input : this.inputs) {
            if(input.isStatus() == false){
                input.deleteValues();
            }
        }
    }

    public LinkedList<Integer> getConnections(){
        return this.outputs.getFirst().getAllDstID();
    }
}
