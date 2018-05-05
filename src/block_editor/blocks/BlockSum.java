package block_editor.blocks;

/**
 * Bloc calculating sum of multiple simple values from multiple ports
 * @author Peter Marko
 */

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
        next.setStatus(2);

        int idx = parentGrid.getRowIndex(this.inputs.getLast().getNode().getParent());

        idx += 1;
        newPort(parentGrid, next, idx, null);
        this.inputs.add(next);
    }
    @Override
    public int removeGridVals(GridPane grid, Boolean input) {
        int out = super.removeGridVals(grid, input);
        if (out <= 2 && input)
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
        double sum = 0;

        for (java.util.Iterator<Type> it = this.inputs.iterator(); it.hasNext();) {
            Type input = it.next();
            if (it.hasNext()) {
                sum += input.getVal("simple");
            }
        }
        Type cur = outputs.getFirst();
        cur.putVal("simple", sum);
        cur.step();
        this.setShadow(Color.GREEN);
        this.showValues();
    }
}
