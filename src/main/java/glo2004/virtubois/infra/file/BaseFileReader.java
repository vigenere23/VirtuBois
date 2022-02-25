package glo2004.virtubois.infra.file;

import glo2004.virtubois.domain.shared.FileReader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Path;

public class BaseFileReader implements FileReader {
    @Override
    public <T> T read(Path path) {
        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream;
        try {
            fileInputStream = new FileInputStream(path.toFile());
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
