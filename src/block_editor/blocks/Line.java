package block_editor.blocks;


/**
 * Class Holding information about line connecting ports
 * @author Peter Marko
 */

import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import java.util.Map;
import block_editor.types.*;

public class Line implements java.io.Serializable {
    private transient javafx.scene.shape.Line []visible = new javafx.scene.shape.Line[3];
    private transient javafx.scene.shape.Line []transparent = new javafx.scene.shape.Line[3];
    private double startX, startY, endX, endY;
    
    /**
     *  Constructor for creating line composed of multiple sublines
     * @param startX start point X coordinate of new line
     * @param startY start point Y coordinate of new line
     * @param endX end point X coordinate of new line
     * @param endY end point Y coordinate of new line
     */
    public Line(double startX, double startY, double endX, double endY) {
        for (int i = 0; i < this.visible.length; i++) {
            this.visible[i] = newVisible();
        }
        for (int i = 0; i < this.transparent.length; i++) {
            this.transparent[i] = newTransparent();
        }
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        System.out.println("coord startX "+this.startX);
        System.out.println("coord startY "+this.startY);
        System.out.println("coord endX "+this.endX);
        System.out.println("coord endY "+this.endY);
        redraw();
    }
    /**
     * Fill this line with information from other line
     * @param copy source line which information is being transfered
     */
    public void copy(Line copy) {
        this.visible = new javafx.scene.shape.Line[3];
        this.transparent = new javafx.scene.shape.Line[3];
        for (int i = 0; i < this.visible.length; i++) {
            this.visible[i] = copy.visible[i];
            this.transparent[i] = copy.transparent[i];
        }
        this.startX = copy.startX;
        this.startY = copy.startY;
        this.endX = copy.endX;
        this.endY = copy.endY;
    }
    /**
     *  Function for setting coordinates of line;
     * @param x1 x coordinat of start point
     * @param y1 y coordinat of start point
     * @param x2 x coordinat of end point
     * @param y2 y coordinat of end point
     * @param l line to which coordinates will be set
     */
    public void setCoordinates(double x1, double y1, double x2, double y2, javafx.scene.shape.Line l) {
        l.setStartX(x1);
        l.setStartY(y1);
        l.setEndX(x2);
        l.setEndY(y2);
    }
    /**
     *  Function for redrawing lines of current connection
     */
    private void redraw() {
        double offset = this.transparent[0].getStrokeWidth();
        double offsetX = offset;
        double offsetY = offset;
        double centerX = (startX + endX) / 2;
        if (startX > endX)
            offsetX = -offsetX;
        if (startY > endY)
            offsetY = -offsetY;
        if (startX < endX - offset || startX > endX + offset) {
            setCoordinates(startX + offsetX, startY, centerX, startY, transparent[0]);
            setCoordinates(centerX, endY, endX - offsetX, endY, transparent[2]);
        } else {
            setCoordinates(0,0,0,0,transparent[0]);
            setCoordinates(0,0,0,0,transparent[2]);
        }
        setCoordinates(startX, startY, centerX, startY, visible[0]);
        setCoordinates(centerX, startY, centerX, endY, visible[1]);
        setCoordinates(centerX, startY + offsetY, centerX, endY - offsetY, transparent[1]);
        setCoordinates(centerX, endY, endX, endY, visible[2]);
    }
    /**
     *  Function for creating new Visible line
     */
    private javafx.scene.shape.Line newVisible() {
        return new javafx.scene.shape.Line();
    }
    /**
     *  Function for creating new transparent line
     */
    private javafx.scene.shape.Line newTransparent() {
        javafx.scene.shape.Line l = new javafx.scene.shape.Line();
        l.setStrokeWidth(15);
        l.setStroke(Color.rgb(0,0,0,0));
        return l;
    }
    /**
     * Function for adding new lines into global Pane canvas
     * @param canvas global pane for drawing lines
     */
    public void addAll(javafx.scene.layout.Pane canvas) {
        canvas.getChildren().addAll(visible[1], visible[2], visible[0], transparent[1], transparent[2], transparent[0]);
    }
    /**
     * @param X Set starting point of this line x coordinate
     */
    public void setStartX(double X) {
        this.startX = X;
        redraw();
    }
    /**
     * @param Y Set starting point of this line y coordinate
     */
    public void setStartY(double Y) {
        this.startY = Y;
        redraw();
    }
    /**
     * @param X Set end point of this line x coordinate
     */
    public void setEndX(double X) {
        this.endX = X;
        redraw();
    }
    /**
     * @param Y Set end point of this line y coordinate
     */
    public void setEndY(double Y) {
        this.endY = Y;
        redraw();
    }
    /**
     * @return starting point of this line x 
     */
    public double getStartX() {
        return startX;
    }
    /**
     * @return starting point of this line y 
     */
    public double getStartY() {
        return startY;
    }
    /**
     * @return end point of this line x coordinate
     */
    public double getEndX() {
        return endX;
    }
    /**
     * @return end point of this line y coordinate
     */
    public double getEndY() {
        return endY;
    }

    /**
     *  Reomoves sub line from parent pane canvas
     */
    private void removeLine(javafx.scene.shape.Line l) {
        ((javafx.scene.layout.Pane) l.getParent()).getChildren().remove(l);
    }
    /**
     *  Removes all sub lines from this connection
     */
    public void remove() {
        for (javafx.scene.shape.Line line : this.visible) {
            if (line != null)
                removeLine(line);
        }
        for (javafx.scene.shape.Line line : this.transparent) {
            if (line != null)
                removeLine(line);
        }
    }
    /**
     *  Show label when mouse inside visible line
     * @param e Mouse event
     * @param label Label for showing information about connection status
     * @param type Contains informatio about connection
     */
    private void mouseEntered(javafx.scene.input.MouseEvent e, Label label, Type type) {
        label.setLayoutX(e.getX()); 
        label.setLayoutY(e.getY() + 18);
        String text = new String();
        for (Map.Entry<String, Double> entry: type.getDst().getLast().getItems().entrySet()) {
            text += entry.getKey();
            text += " : ";
            text += entry.getValue();
            text += ", \n";
        }
        label.setText(text);
        label.setVisible(true);
        label.toFront();
    }
    /**
     *  Function for showing information about connection
     * @param type Contains informatio about connection
     * @return Label displaying status information about line
     */
    public Label enableStatusDisplay(Type type) {
        Label label = new Label();
        // position of the text 
        label.setVisible(false);
        label.setStyle("-fx-background-color: white; -fx-border-color: black;");
        for (javafx.scene.shape.Line line : this.transparent) {
            line.setOnMouseEntered(e -> {
                mouseEntered(e, label, type);
            });
        }
        for (javafx.scene.shape.Line line : this.transparent) {
            line.setOnMouseExited(e -> {
                label.setVisible(false);
            });
        }
        type.getLines().addLast(this);
        return label;
    }
} 