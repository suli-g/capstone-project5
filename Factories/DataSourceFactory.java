package Factories;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.io.IOException;

import IO.DataSource;

public class DataSourceFactory {
    private static Path directory;
    private HashMap<Path, DataSource> dataSources;
    private static DataSourceFactory factoryInstance;
    
    private DataSourceFactory(Path rootDirectory) {
        directory = rootDirectory;
        dataSources = new HashMap<>();
    }

    public static DataSourceFactory getInstance(String dataSourceDirectoryName) throws IOException {
        if (factoryInstance == null) {
            Path rootDirectory = Path.of(dataSourceDirectoryName);
            try {
                Files.createDirectory(rootDirectory);
            } catch(FileAlreadyExistsException ignored){}
            factoryInstance = new DataSourceFactory(rootDirectory);
        }
        return factoryInstance;
    }

    public DataSource get(String sourceFileName) {
        return dataSources.get(directory.resolve(sourceFileName));
    }

    public DataSourceFactory add(String sourceFileName) throws IOException, UnsupportedOperationException{
        DataSource currentDataSource = null;
        Path fullPath = directory.resolve(sourceFileName);
        currentDataSource = new DataSource(fullPath);
        dataSources.put(fullPath, currentDataSource);
        return this;
    }
}
