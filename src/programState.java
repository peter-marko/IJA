import java.awt.Desktop;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javafx.stage.FileChooser;
import java.io.*;
import java.util.Scanner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import java.util.Iterator;;
import block_editor.blocks.*;
import block_editor.types.*;

public class programState implements java.io.Serializable {
    public String file;
    public Scheme scheme;
    
    public void serialize() {
        scheme.print();
        try {
            for (Block b : scheme.getBlocks()) {
                b.serialize();
            }
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
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
            int idx = linesToActualize.getFirst();
            int i = 0;
            while (i < linesToActualize.size()) {
                Line l = lines.get(idx);
                // l.actualize(canvas);
                if (in) {
                    javafx.scene.control.Label label = t.addLine(l.getStartX() - 5, l.getStartY(),  l.getEndX() + 5, l.getEndY());
                    l.copy(t.getLines().getLast());
                    l = t.getLines().getLast();
                    l.addAll(canvas);
                    canvas.getChildren().add(label);
                }
                i++;
            }
        }

    }
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