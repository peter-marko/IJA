package block_editor.blocks;

import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import java.util.Map;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import block_editor.types.*;
public class BlockSum extends Block {
    public BlockSum (String newName, Integer newID) {
        this.name = newName;
        this.id = newID;
        this.inputs.add(new TypeSimple(newID));
        this.outputs.add(new TypeSimple(newID));
    }
    private void newPort(GridPane grid) {
        GridPane parentGrid = (GridPane)grid.getParent();
        if (parentGrid.getColumnIndex(grid) != 0)
            return;
        Type next = new TypeSimple(this.getID());
        next.putVal("simple", 0);
        next.setFromUser();

        int idx = parentGrid.getRowIndex(this.inputs.getLast().getNode().getParent());

        System.out.println("row "+idx);
        idx += 1;
        GridPane portGrid = new GridPane();
        Circle circle = new Circle(0, 0, 5);
        Label inputType = new Label("Simple");
        inputType.setStyle("-fx-font: 12 arial;");
        portGrid.getChildren().addAll(circle, inputType);
        portGrid.setConstraints(inputType, 0, 0);
        portGrid.setConstraints(circle, 0, 1);
        parentGrid.setConstraints(portGrid, 0, idx);
        parentGrid.getChildren().addAll(portGrid);
        next.setNode(circle);
        this.setValue(circle, portGrid, next);
        this.inputs.add(next);
    }
    @Override
    public int removeGridVals(GridPane grid) {
        int out = super.removeGridVals(grid);
        if (out <= 2)
            newPort(grid);
        return out;
    }
    @Override
    protected void getUserInput(Circle circle, GridPane portGrid, Type type) {
        super.getUserInput(circle, portGrid, type);
        GridPane parentGrid = (GridPane)portGrid.getParent();
        System.out.println("left "+parentGrid.getRowIndex(portGrid)+" right "+this.inputs.size());
        if (parentGrid.getRowIndex(portGrid) == this.inputs.size() - 1)
            newPort(portGrid);
    }

    public void execute () {
        double sum = 0;
        for (Type input : inputs) {
            sum += input.getVal("simple");
        }
        for (Type dst : outputs.get(0).getDst()) {
            dst.putVal("simple", sum);
        }
        for (Type cur : outputs) {
            cur.putVal("simple", sum);
        }

        this.setShadow(Color.GREEN);
        this.showValues();
    }
}