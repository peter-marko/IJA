package block_editor.types;
public class TypeA extends Type {

    public TypeA(String newName) {
        this.name = newName;
        this.putVal("val", 0.0);
    }

    /**
     * \brief checks if port has set value
     * \return true if value is set, false if port value is null 
     */
    public boolean isPrepared() {
        if(this.items.get("val") != null)
        {
            return true;
        }
        return false;
    }
}
