package gluon.projects.infra.impl;

import gluon.projects.exceptions.TechnicalException;
import gluon.projects.infra.FileStorageService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileStorageServiceImpl implements FileStorageService {

    private final Path file;

    public FileStorageServiceImpl(Path file) {
        this.file = file;
    }

    @Override
    public void write(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
    }

    @Override
    public void cleanFolder() {
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
    }

    @Override
    public List<String> readSymbolExistingFile() {
        List<String> symbols = new ArrayList<String>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                symbols.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return symbols;
    }


}
