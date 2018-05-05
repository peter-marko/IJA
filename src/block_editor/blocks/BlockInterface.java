package block_editor.blocks;

/**
 * Interface for comunication with blocks
 * @author Stanislav Mechl
 */
import javafx.scene.layout.Pane;
import java.util.LinkedList;
import block_editor.types.*;
public interface BlockInterface {
    public abstract void execute ();
    public visualBlock constructWindow(Pane canvas, Scheme parent_scheme, Integer block_id, double X, double Y);
    public void step ();
    public int getNumberOfOutputs();
    public int getNumberOfInputs();
    public Type getOutputPort(int n);
    public Type getInputPort(int n);
    public String getName();
    public Integer getID();
}