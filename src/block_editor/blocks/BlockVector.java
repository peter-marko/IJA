package block_editor.blocks;

/**
 * Block for calculation of vector representing line
 * @author Stanislav Mechl
 */

import javafx.scene.paint.Color;
import java.util.Map;
import block_editor.types.*;
public class BlockVector extends Block {
    public BlockVector (String newName, Integer newID) {
        this.name = newName;
        this.id = newID;
        this.inputs.add(new TypeLine(newID));
        this.outputs.add(new TypeCoordinate2D(newID));
    }
    public void execute () {
        Type in = inputs.get(0);
        double output_x = in.getVal("x2") - in.getVal("x1");
        double output_y = in.getVal("y2") - in.getVal("y1");
        Type cur = outputs.getFirst();
        cur.putVal("x", output_x);
        cur.putVal("y", output_y);
        cur.step();
        this.setShadow(Color.GREEN);
        System.out.println("final output x:"+ output_x + ", y:" + output_y);
        this.showValues();
    }
}
