import java.util.LinkedList;

public abstract class Block {
    String name;
    LinkedList outputs = new LinkedList();
    LinkedList inputs = new LinkedList();

    public int getNumberOfOutputs() {
        return this.outputs.size();
    }

    public int getNumberOfInputs() {
        return this.inputs.size();
    }

    public Type getOutputType(int n) {
        return (Type)this.outputs.get(n);
    }

    public Type getInputType(int n) {
        return (Type)this.inputs.get(n);
    }
}
