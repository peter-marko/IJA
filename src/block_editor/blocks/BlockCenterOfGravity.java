package block_editor.blocks;

import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;
import java.util.Map;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;
import block_editor.types.*;
public class BlockCenterOfGravity extends Block {
    public BlockCenterOfGravity (String newName, Integer newID) {
        this.name = newName;
        this.id = newID;
        this.inputs.add(new TypeCoordinate2D(newID));
        this.outputs.add(new TypeCoordinate2D(newID));
    }
    private void newPort(GridPane grid) {
        GridPane parentGrid = (GridPane)grid.getParent();
        if (parentGrid.getColumnIndex(grid) != 0)
            return;
        Type next = new TypeCoordinate2D(this.getID());
        next.putVal("x", 0);
        next.putVal("y", 0);
        next.setStatus(2);

        int idx = parentGrid.getRowIndex(this.inputs.getLast().getNode().getParent());

        System.out.println("row "+idx);
        idx += 1;
        newPort(parentGrid, next, idx, 0, null);
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
    protected void getUserInput(GridPane portGrid, Type type, Boolean load) {
        super.getUserInput(portGrid, type, false);
        GridPane parentGrid = (GridPane)portGrid.getParent();
        System.out.println("left "+parentGrid.getRowIndex(portGrid)+" right "+this.inputs.size());
        if (parentGrid.getRowIndex(portGrid) == this.inputs.size() - 1)
            newPort(portGrid);
    }

    public void execute () {
        double sumX = 0;
        double sumY = 0;
        int numOfPoints = 0;
        for (java.util.Iterator<Type> it = this.inputs.iterator(); it.hasNext();) {
            Type input = it.next();
            if (it.hasNext()) {
                sumX += input.getVal("x");
                sumY += input.getVal("y");
                numOfPoints += 1;
            }
        }
        Type cur = outputs.getFirst();
        cur.putVal("x", sumX/numOfPoints);
        cur.putVal("y", sumY/numOfPoints);
        cur.step();
        this.setShadow(Color.GREEN);
        this.showValues();
    }
}
