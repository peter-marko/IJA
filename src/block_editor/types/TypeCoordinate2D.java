package block_editor.types;

public class TypeCoordinate2D extends Type {
    public static String name = "coordinate";
    public TypeCoordinate2D(double x, double y) {
        this.putVal("x", x);
        this.putVal("y", y);
    }
}