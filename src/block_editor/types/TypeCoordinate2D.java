package block_editor.types;
public class TypeCoordinate2D extends Type {
    
    public TypeCoordinate2D() {
        this.set = false;
        this.name = "Coordinate2D";
        this.putVal("x");
        this.putVal("y");
    }
}