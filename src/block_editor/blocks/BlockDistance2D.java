package block_editor.blocks;

import java.util.Map;
import block_editor.types.*;
public class BlockDistance2D extends Block {
    public BlockDistance2D (String newName, Type t1, Type t2) {
        this.name = newName;
        this.inputs.add(t1);
        this.inputs.add(t2);
        this.inTypes.add(0, TypeCoordinate2D.name);
        this.inTypes.add(1, TypeCoordinate2D.name);
        this.outTypes.add(0, TypeSimple.name);
        this.outputs.add(new TypeSimple(0));
    }
    public void execute () {
        Type i1 = inputs.get(0);
        Type i2 = inputs.get(1);
        double output = Math.sqrt(Math.pow(i1.getVal("x") - i2.getVal("x"),2) + 
            Math.pow(i1.getVal("y") - i2.getVal("y"), 2));
        this.outputs.get(0).putVal("simple", output);
    }
}
