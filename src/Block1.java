public class Block1 extends Block {
    public Block1 (String newName) {
        this.name = newName;
        this.outputs.add(new TypeA("out"));
        this.inputs.add(new TypeA("in1"));
        this.inputs.add(new TypeA("in2"));
    }
}
