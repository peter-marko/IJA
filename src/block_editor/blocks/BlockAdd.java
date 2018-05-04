package block_editor.blocks;

import javafx.scene.paint.Color;
import java.util.Map;
import block_editor.types.*;
public class BlockAdd extends Block {
    public BlockAdd (String newName, Integer newID) {
        this.name = newName;
        this.id = newID;
        this.inputs.add(new TypeSimple(newID));
        this.inputs.add(new TypeSimple(newID));
        this.outputs.add(new TypeSimple(newID));
    }
    public void execute () {
        Type i1 = inputs.get(0);
        Type i2 = inputs.get(1);
        double output = i1.getVal("simple") + i2.getVal("simple");
        // this.outputs.get(0).putVal("simple", output);
        Type cur = outputs.getFirst();
        cur.putVal("simple", output);
        cur.step();
        this.setShadow(Color.GREEN);
        System.out.println("final output "+output);
        this.showValues();
    }
}
