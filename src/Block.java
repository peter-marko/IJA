import java.util.LinkedList;

public abstract class Block {
    String name;
    LinkedList<Port> outputs = new LinkedList();
    LinkedList<Port> inputs = new LinkedList();

    public int getNumberOfOutputs() {
        return this.outputs.size();
    }

    public int getNumberOfInputs() {
        return this.inputs.size();
    }

    public Port getOutputPort(int n) {
        return this.outputs.get(n);
    }

    public Port getInputPort(int n) {
        return this.inputs.get(n);
    }
}
