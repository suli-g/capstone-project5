package Factories;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.io.IOException;

import IO.DataSource;

/**
 * Represents a DataSourceFactory
 */
public class DataSourceFactory {
    private static Path root;
    private HashMap<Path, DataSource> dataSources;
    private static DataSourceFactory factoryInstance;
    
    private DataSourceFactory(Path rootDirectory) {
        root = rootDirectory;
        dataSources = new HashMap<>();
    }

    /**
     * Sets the root directory for all {@link DataSource} files and returns the DataSourceFactory object associated 
     * with the current Java application.
     * 
     * @param directoryName the directory name.
     * @return the {@code DataSourceFactory} associated with current Java application.
     * @throws IOException if the root directory cannot be created.
     */
    public static DataSourceFactory setRootDirectory(String directoryName) throws IOException {
        if (factoryInstance == null) {
            Path rootDirectory = Path.of(directoryName);
            try {
                Files.createDirectory(rootDirectory);
            } catch(FileAlreadyExistsException ignored){}

            factoryInstance = new DataSourceFactory(rootDirectory);
        }
        return factoryInstance;
    }

    /**
     * @param sourceFileName the path filename (in {@link #root}) to which the {@link DataSource} is mapped.
     * @return the {@code IO.DataSource} mapped to {@code sourceFileName}, or null if the path is unmapped.
     */
    public DataSource getDataSource(String sourceFileName) {
        return dataSources.get(root.resolve(sourceFileName));
    }

    /**
     * Creates the file at {@code filePath} {@link #root}/{@code sourceFileName} if it does not exist.
     * Maps a new {@link DataSource} to the {@link Path} with the value {@code filePath}.
     * 
     * @param sourceFileName the name of the file to create and map.
     * @return this {@code DataSourceFactory}
     * @throws IOException if the path is invalid, or {@code filePath} could not be created.
     */
    public DataSourceFactory addDataSource(String sourceFileName) throws IOException {
        DataSource currentDataSource = null;
        Path fullPath = root.resolve(sourceFileName);
        // Check if the path exists so Files.createFile does not throw a FileAlreadyExistsException.
        if (!Files.exists(fullPath)) {
            Files.createFile(fullPath);
        }
        currentDataSource = new DataSource(fullPath);
        dataSources.put(fullPath, currentDataSource);
        return this;
    }
}
