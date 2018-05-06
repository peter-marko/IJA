import java.util.LinkedList;
import java.util.List;
import javafx.stage.FileChooser;
import java.io.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import java.util.Iterator;
import block_editor.blocks.*;
import block_editor.types.*;
/**
 *  Class for holding information about whole program
 * this class is used for serialization
 */
public class programState implements java.io.Serializable {
    public String file = null;
    public Scheme scheme;
    
    /**
     *  Function which prepares serialization
     */
    public void serialize() {
        scheme.print();
        try {
            for (Block b : scheme.getBlocks()) {
                b.serialize();
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            String file = null;
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    /**
     *  Function which actualizes lines in ports after deserialization
     * @param t current port
     * @param canvas pane which will hold this line
     * @param in true when this line is from input, false otherwise
     */
    private void actualizeLine(Type t, Pane canvas, boolean in) {
        Iterator<Line> iter = t.getLines().iterator();
        LinkedList<Integer> linesToActualize = new LinkedList<>();
        LinkedList<Line> lines = t.getLines();
        while (iter.hasNext()) {
            Line l = iter.next();
            // l.actualize(canvas);
            linesToActualize.addLast(lines.indexOf(l));
        }
        if (linesToActualize.size() != 0) {
            // int idx = linesToActualize.getFirst();
            int i = 0;
            while (i < linesToActualize.size()) {
                int idx = linesToActualize.get(i);
                Line l = lines.get(idx);
                // l.actualize(canvas);
                if (in) {
                    javafx.scene.control.Label label = t.addLine(l.getStartX() - 5, l.getStartY(),  l.getEndX() + 5, l.getEndY());
                    l.copy(t.getLines().getLast());
                    t.getLines().removeLast();
                    l = t.getLines().getLast();
                    l.addAll(canvas);
                    System.out.println("if "+i+l);
                    canvas.getChildren().add(label);
                } else {
                    l = t.getDst().get(i).getLines().getFirst();
                    System.out.println("else "+i+l);
                }
                i++;
            }
        }

    }
    /**
     *  Function for deserialization of program state
     * @param root main pane in window
     * @param canvas user working pane
     */
    public void deserialize(Pane root, Pane canvas) {
        try {
           FileInputStream fileIn = new FileInputStream(file);
           ObjectInputStream in = new ObjectInputStream(fileIn);
           programState state = null;
           state = (programState) in.readObject();
           this.file = state.file;
           this.scheme = state.scheme;
           int idx = 0;
           for (Block b : this.scheme.getBlocks()) {

                canvas.getChildren().add(b.constructWindow(root, this.scheme, b.getID(), b.getX(), b.getY() - 40));
                for (Type i : b.getInputs()) {
                    actualizeLine(i, root, true);
                }
                for (Type o : b.getOutputs()) {
                    actualizeLine(o, root, false);
                }
                idx++;
           }
           in.close();
           fileIn.close();
        } catch (IOException i) {
           i.printStackTrace();
           return;
        } catch (ClassNotFoundException c) {
           System.out.println("Class not found");
           c.printStackTrace();
           return;
        }
    }
}