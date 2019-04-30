package helpers;

import domain.controllers.LarmanController;
import domain.entities.Yard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Deque;

public class UndoRedo {
    private static Deque<byte[]> undo = new ArrayDeque<>();
    private static Deque<byte[]> redo = new ArrayDeque<>();

    private static int sizeMax = 50;

    private UndoRedo() {}

    public static void addCurrentYard() {
        try {
            if (sizeMax > 0) {
                if (undo.size() == sizeMax) {
                    undo.removeLast();
                }
                Yard prevYard = LarmanController.getInstance().getYard();
                undo.push(serializeYard(prevYard));
            }
            redo.clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Yard undo() throws Exception {
        if (!undo.isEmpty()) {
            Yard newYard = deserializeYard(undo.pop());
            Yard oldYard = LarmanController.getInstance().getYard();
            redo.push(serializeYard(oldYard));
            return newYard;
        } else {
            return LarmanController.getInstance().getYard();
        }
    }

    public static void undoUndo() {undo.removeFirst();}

    public static int getUndoSize() {
        return undo.size();
    }

    public static int getRedoSize() {
        return redo.size();
    }

    public static Yard redo() throws Exception {
        if (!redo.isEmpty()) {
            Yard newYard = deserializeYard(redo.pop());
            Yard oldYard = LarmanController.getInstance().getYard();
            undo.push(serializeYard(oldYard));
            return newYard;
        } else {
            return LarmanController.getInstance().getYard();
            
        }
    }

    private static byte[] serializeYard(Yard yard) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(yard);
        objectOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static Yard deserializeYard(byte[] serializedYard) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedYard);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Yard yard = (Yard) objectInputStream.readObject();
        return yard;
    }

    public static void undoAction(){
        try{
            LarmanController.getInstance().setYard(UndoRedo.undo());
            redo.removeFirst();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
