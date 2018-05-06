package block_editor.types;
/**
 * Type declaring line in 2d coordinate system
 * @author Stanislav Mechl
 */
public class TypeLine extends Type {
    
    public TypeLine(Integer block_id) {
        this.set = false;
        this.status = 0;
        this.name = "Line";
        this.putVal("x1");
        this.putVal("y1");
        this.putVal("x2");
        this.putVal("y2");
        this.ID = block_id;
    }

    /**
     *  checks if port has set value
     * @return true if value is set, false if port value is null 
     */
    public boolean isPrepared() {
        if(this.items.get("x1") != null && this.items.get("y1") != null && this.items.get("x2") != null && this.items.get("y2") != null)
        {
            return true;
        }
        return false;
    }

    /**
     *  sets all values to null
     */
    public void deleteValues() {
        this.items.clear();
        this.putVal("x1");
        this.putVal("y1");
        this.putVal("x2");
        this.putVal("y2");
    }
}