package block_editor.blocks;

import javafx.scene.paint.Color;
import java.util.Map;
import block_editor.types.*;
public class BlockDistance2D extends Block {
    public BlockDistance2D (String newName, Integer newID) {
        this.name = newName;
        this.id = newID;
        this.inputs.add(new TypeCoordinate2D());
        this.inputs.add(new TypeCoordinate2D());
        this.outputs.add(new TypeSimple());
    }
    public void execute () {
        Type i1 = inputs.get(0);
        Type i2 = inputs.get(1);
        double output = Math.sqrt(Math.pow(i1.getVal("x") - i2.getVal("x"),2) + 
            Math.pow(i1.getVal("y") - i2.getVal("y"), 2));
        for (Type dst : outputs.get(0).getDst()) {
            dst.putVal("simple", output);
        }
        for (Type cur : outputs) {
            cur.putVal("simple", output);
        }
        this.setShadow(Color.GREEN);
        System.out.println("final output "+output);
        this.showValues();
    }
}
