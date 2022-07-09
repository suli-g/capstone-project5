package IO;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;


public class DataSource {
    private Scanner scanner;
    private Path sourcePath;
    public DataSource(Path sourcePath) throws FileNotFoundException, IOException {
        this.sourcePath = sourcePath;
        scanner = new Scanner(sourcePath);
    }
    
    public boolean hasNext() {
        return scanner.hasNext();
    }

    public String getLine()  {
        if (scanner.hasNext()) {
            return scanner.nextLine();
        } else {
            return null;
        }
    }

    public void addLine(String line) throws IOException {
        FileWriter writer = new FileWriter(sourcePath.toFile(), true);
        writer.append(line);
        writer.close();
    }

    @Override
    public String toString() {
        return sourcePath.toString();
    }
}