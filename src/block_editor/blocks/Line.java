package block_editor.blocks;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import java.util.Map;
import block_editor.types.*;

public class Line {
    private javafx.scene.shape.Line visible1, visible2, visible3, transparent1, transparent2, transparent3;
    private double startX, startY, endX, endY;

    private void setCoordinates(double x1, double y1, double x2, double y2, javafx.scene.shape.Line l) {
        l.setStartX(x1);
        l.setStartY(y1);
        l.setEndX(x2);
        l.setEndY(y2);
    }
    private void redraw() {
        double offset = 20;
        double centerX = (startX + endX) / 2;
        setCoordinates(startX, startY, centerX, startY, visible1);
        setCoordinates(startX + offset, startY, centerX, startY, transparent1);
        setCoordinates(centerX, startY, centerX, endY, visible2);
        setCoordinates(centerX, startY, centerX, endY, transparent2);
        setCoordinates(centerX, endY, endX, endY, visible3);
        setCoordinates(centerX, endY, endX - offset, endY, transparent3);
    }
    private javafx.scene.shape.Line newVisible() {
        return new javafx.scene.shape.Line();
    }
    private javafx.scene.shape.Line newTransparent() {
        javafx.scene.shape.Line l = new javafx.scene.shape.Line();
        l.setStrokeWidth(20);
        l.setStroke(Color.rgb(0,0,0,0));
        return l;
    }
    public Line(double startX, double startY, double endX, double endY) {
        this.visible1 = newVisible();
        this.visible2 = newVisible();
        this.visible3 = newVisible();
        this.transparent1 = newTransparent();
        this.transparent2 = newTransparent();
        this.transparent3 = newTransparent();
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        redraw();
    }
    public void addAll(javafx.scene.layout.Pane canvas) {
        canvas.getChildren().addAll(visible1, visible2, visible3, transparent1, transparent2, transparent3);
    }
    private void removeLine(javafx.scene.shape.Line l) {
        ((javafx.scene.layout.Pane) l.getParent()).getChildren().remove(l);
    }
    public void setStartX(double X) {
        this.startX = X;
        redraw();
    }
    public void setStartY(double Y) {
        this.startY = Y;
        redraw();
    }
    public void setEndX(double X) {
        this.endX = X;
        redraw();
    }
    public void setEndY(double Y) {
        this.endY = Y;
        redraw();
    }
    public double getStartX() {
        return startX;
    }
    public double getStartY() {
        return startY;
    }
    public double getEndX() {
        return endX;
    }
    public double getEndY() {
        return endY;
    }
    public void remove() {
        removeLine(this.visible1);
        removeLine(this.visible2);
        removeLine(this.visible3);
        removeLine(this.transparent1);
        removeLine(this.transparent2);
        removeLine(this.transparent3);
    }
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
    public Label addLine(Type type) {
        Label label = new Label();
        // position of the text 
        label.setVisible(false);
        label.setStyle("-fx-background-color: white; -fx-border-color: black;");
        this.transparent1.setOnMouseEntered(e -> {
            mouseEntered(e, label, type);
        });
        this.transparent2.setOnMouseEntered(e -> {
            mouseEntered(e, label, type);
        });
        this.transparent3.setOnMouseEntered(e -> {
            mouseEntered(e, label, type);
        });
        this.transparent1.setOnMouseExited(e -> {
            label.setVisible(false);
        });
        this.transparent2.setOnMouseExited(e -> {
            label.setVisible(false);
        });
        this.transparent3.setOnMouseExited(e -> {
            label.setVisible(false);
        });
        type.getLines().addLast(this);
        return label;
    }
} 