package block_editor.types;
// one dimensional coordinate
public class TypeSimple extends Type {

    public TypeSimple(double x) {
        this.name = "simple";
        this.putVal("simple", x);
    }
}