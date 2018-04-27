package block_editor.types;
public class TypeCoordinate2D extends Type {
    
    public TypeCoordinate2D() {
        this.set = false;
        this.name = "Coordinate2D";
        this.putVal("x");
        this.putVal("y");
    }

    /**
     * \brief checks if port has set value
     * \return true if value is set, false if port value is null 
     */
    public boolean isPrepared() {
        if(this.items.get("x") != null && this.items.get("y") != null)
        {
            return true;
        }
        return false;
    }
}