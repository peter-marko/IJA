package block_editor.blocks;

import javafx.scene.paint.Color;
import java.util.Map;
import block_editor.types.*;
public class BlockVector extends Block {
    public BlockVector (String newName, Integer newID) {
        this.name = newName;
        this.id = newID;
        this.inputs.add(new TypeCoordinate2D(newID));
        this.inputs.add(new TypeCoordinate2D(newID));
        this.outputs.add(new TypeCoordinate2D(newID));
    }
    public void execute () {
        Type i1 = inputs.get(0);
        Type i2 = inputs.get(1);
        double output_x = i2.getVal("x") - i1.getVal("x");
        double output_y = i2.getVal("y") - i1.getVal("y");
        Type cur = outputs.getFirst();
        cur.putVal("x", output_x);
        cur.putVal("y", output_y);
        cur.step();
        this.setShadow(Color.GREEN);
        System.out.println("final output x:"+ output_x + ", y:" + output_y);
        this.showValues();
    }
}
