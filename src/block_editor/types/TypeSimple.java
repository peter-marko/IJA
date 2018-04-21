package block_editor.types;
// one dimensional coordinate
public class TypeSimple extends Type {
    static public String name = "simple";

    public TypeSimple(double x) {
        this.putVal("simple", x);
    }
}