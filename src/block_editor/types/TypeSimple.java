package block_editor.types;
// one dimensional coordinate
public class TypeSimple extends Type {
    
    public TypeSimple(Integer block_id) {
        this.name = "Simple";
        this.set = false;
        this.status = 0;
        this.putVal("simple");
        this.ID = block_id;
    }

    /**
     *  checks if port has set value
     * @return true if value is set, false if port value is null 
     */
    public boolean isPrepared() {
        if(this.items.get("simple") != null)
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
        this.putVal("simple");
    }
}