package block_editor.types;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Iterator;
import java.util.Map;
import block_editor.blocks.*;

 public interface TypeInterface {
    public int getNumberOfItems();
    public String getName();
    public double getVal(String s);
    public void putVal(String s, double val);
    public void connect (Type dst);
    public void step ();
    public LinkedList<Type> getConnection();
    
 }