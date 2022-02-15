package glo2004.virtubois.helpers.file.opener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class BaseFileReader implements FileReader {
    @Override
    public <T> T read(File file) {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            objectInputStream = new ObjectInputStream(fileInputStream);
            T object = (T) objectInputStream.readObject();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new CannotOpenFileException(e.getMessage());
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new CannotOpenFileException("Invalid type provided for the file.");
        }
    }
}
