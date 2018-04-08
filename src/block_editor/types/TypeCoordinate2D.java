package block_editor.types;

public class TypeCoordinate2D extends Type {

    public TypeCoordinate2D(double x, double y) {
        this.name = "coordinate";
        this.putVal("x", x);
        this.putVal("y", y);
    }
}