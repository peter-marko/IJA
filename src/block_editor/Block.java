package block_editor;
import java.util.LinkedList;

public abstract class Block {
    protected String name;
    protected LinkedList<Type> outputs = new LinkedList();
    protected LinkedList<Type> inputs = new LinkedList();

    // compute value and set it to outputs
    public abstract void execute ();
    public void outConnect (int n, Type out) {
        this.outputs.get(n).connect(out);

    }
    // propagate outputs to next block
    public void step () {
        for (Type tmp_out : outputs)
            tmp_out.step();
    }
    public int getNumberOfOutputs() {
        return this.outputs.size();
    }

    public int getNumberOfInputs() {
        return this.inputs.size();
    }

    public Type getOutputPort(int n) {
        return this.outputs.get(n);
    }

    public Type getInputPort(int n) {
        return this.inputs.get(n);
    }
}
