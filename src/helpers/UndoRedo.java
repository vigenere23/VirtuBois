package helpers;

import domain.controllers.LarmanController;
import domain.entities.Yard;

import java.util.ArrayDeque;
import java.util.Deque;

public class UndoRedo {
    private static Deque<Yard> undo = new ArrayDeque<>();
    private static Deque<Yard> redo = new ArrayDeque<>();

    private static int sizeMax = 20;

    private UndoRedo(){}

    public static void add(Yard yard) {
        if (sizeMax > 0) {
            if(undo.size() == sizeMax) {
                undo.removeLast();
            }
            Yard prevYard = new Yard(yard);
            undo.push(prevYard);
            redo.clear();
        }
    }

    public static Yard undo() {
        if(!undo.isEmpty()) {
            Yard yard = undo.pop();
            redo.push(yard);
            return yard;
        } else {
            return LarmanController.getInstance().getYard();
        }
    }

    public static int getUndo() {
        return undo.size();
    }

    public static int getRedo() {
        return redo.size();
    }

    public static Yard redo() {
        if(!redo.isEmpty()) {
            Yard yard = redo.pop();
            undo.push(yard);
            return yard;
        } else {
            return LarmanController.getInstance().getYard();
        }
    }
}
