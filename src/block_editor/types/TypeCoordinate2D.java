package block_editor.types;
public class TypeCoordinate2D extends Type {
    
    public TypeCoordinate2D(Integer block_id) {
        this.set = false;
        this.status = 0;
        this.name = "Coordinate2D";
        this.putVal("x");
        this.putVal("y");
        this.ID = block_id;
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

    /**
     * \brief sets all values to null
     */
    public void deleteValues() {
        this.items.clear();
        this.putVal("x");
        this.putVal("y");
    }
}