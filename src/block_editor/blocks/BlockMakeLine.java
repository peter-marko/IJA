package block_editor.blocks;

/**
 * Block for making a line from two points
 * @author Stanislav Mechl
 */

import javafx.scene.paint.Color;
import java.util.Map;
import block_editor.types.*;
public class BlockMakeLine extends Block {
    public BlockMakeLine (String newName, Integer newID) {
        this.name = newName;
        this.id = newID;
        this.inputs.add(new TypeCoordinate2D(newID));
        this.inputs.add(new TypeCoordinate2D(newID));
        this.outputs.add(new TypeLine(newID));
    }
    public void execute () {
        Type i1 = inputs.get(0);
        Type i2 = inputs.get(1);
        Type cur = outputs.getFirst();
        cur.putVal("x1", i1.getVal("x"));
        cur.putVal("y1", i1.getVal("y"));
        cur.putVal("x2", i2.getVal("x"));
        cur.putVal("y2", i2.getVal("y"));
        cur.step();
        this.setShadow(Color.GREEN);
        System.out.println("final output [x:"+ i1.getVal("x") + ", y:" + i1.getVal("y") + "]  [x:" + i2.getVal("x") + ", y:" + i2.getVal("y") + "]");
        this.showValues();
    }
}
