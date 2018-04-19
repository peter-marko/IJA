package block_editor.blocks;

import javafx.scene.layout.Pane;
import java.util.LinkedList;
import block_editor.types.*;
public interface BlockInterface {
    public abstract void execute ();
    public InternalWindow constructWindow(Pane canvas);
    public void outConnect (int n, Type out);
    public void step ();
    public int getNumberOfOutputs();
    public int getNumberOfInputs();
    public Type getOutputPort(int n);
    public Type getInputPort(int n);
}