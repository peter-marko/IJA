package block_editor.types;
// one dimensional coordinate
public class TypeSimple extends Type {
    
    public TypeSimple() {
        this.name = "Simple";
        this.set = false;
        this.putVal("simple");
    }

    /**
     * \brief checks if port has set value
     * \return true if value is set, false if port value is null 
     */
    public boolean isPrepared() {
        if(this.items.get("simple") != null)
        {
            return true;
        }
        return false;
    }
}