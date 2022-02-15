package glo2004.virtubois.helpers.file.opener;

import java.io.File;

public interface FileReader {
    <T> T read(File file);
}
