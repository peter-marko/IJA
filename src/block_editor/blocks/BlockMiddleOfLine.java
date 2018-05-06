package block_editor.blocks;

/**
 * Block for getting a middle point of the line
 * @author Stanislav Mechl
 */

import javafx.scene.paint.Color;
import java.util.Map;
import block_editor.types.*;
public class BlockMiddleOfLine extends Block {
    public BlockMiddleOfLine (String newName, Integer newID) {
        this.name = newName;
        this.id = newID;
        this.inputs.add(new TypeLine(newID));
        this.outputs.add(new TypeCoordinate2D(newID));
    }
    public void execute () {
        Type in = inputs.get(0);
        double output_x = (in.getVal("x1") + in.getVal("x2")) / 2;
        double output_y = (in.getVal("y1") + in.getVal("y2")) / 2;
        Type cur = outputs.getFirst();
        cur.putVal("x", output_x);
        cur.putVal("y", output_y);
        cur.step();
        this.setShadow(Color.GREEN);
        System.out.println("final output x:"+ output_x + ", y:" + output_y);
        this.showValues();
    }
}
