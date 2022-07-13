package IO;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Represents a DataSource that reads and writes to a single file.
 */
public class DataSource {
    private Scanner scanner;
    private Path sourcePath;

    /**
     * Creates a new DataSource object if {@code sourcePath} exists.
     * 
     * @param sourcePath the file to use as a data source.
     * @throws FileNotFoundException if {@code sourcePath} does not exist.
     * @throws IOException if the {@link Scanner} cannot access the file at {@code sourcePath}.
     */
    public DataSource(Path sourcePath) throws FileNotFoundException, IOException {
        if (!Files.exists(sourcePath)) {
            throw new FileNotFoundException("The source file: '" + sourcePath.toString() +"' does not exist.");
        }
        this.sourcePath = sourcePath;
        scanner = new Scanner(sourcePath);
    }
    
    /**
     * Reads a line from the file at {@code sourcePath}.
     * @return the line that was read as a {@link String}.
     */
    public String readLine() {
        if (scanner.hasNext()) {
            return scanner.nextLine();
        } else {
            return null;
        }
    }

    /**
     * Writes text to the file at {@code sourcePath}.
     * 
     * @param text the data to be written.
     * @return true if writing was successful, false otherwise.
     */
    public boolean write(String text) {
        try {
            FileWriter writer = new FileWriter(sourcePath.toFile());
            writer.write(text);
            writer.close();
            return true;
        } catch(IOException error) {
            System.out.println("Something went wrong while saving the data.");
            System.out.println(error.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        return sourcePath.toString();
    }
}