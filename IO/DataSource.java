package IO;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;


public class DataSource {
    private Scanner scanner;
    private Path sourcePath;
    public DataSource(Path sourcePath) throws FileNotFoundException, IOException {
        if (!Files.exists(sourcePath)) {
            throw new FileNotFoundException("The source file: '" + sourcePath.toString() +"' does not exist.");
        }
        this.sourcePath = sourcePath;
        scanner = new Scanner(sourcePath);
    }
    
    public String loadLine() {
        if (scanner.hasNext()) {
            return scanner.nextLine();
        } else {
            return null;
        }
    }

    public boolean saveData(String data) {
        try {
            FileWriter writer = new FileWriter(sourcePath.toFile());
            writer.write(data);
            writer.close();
            return true;
        } catch(IOException error) {
            System.out.println("Something went wrong while saving the data.");
            return false;
        }
    }

    @Override
    public String toString() {
        return sourcePath.toString();
    }
}