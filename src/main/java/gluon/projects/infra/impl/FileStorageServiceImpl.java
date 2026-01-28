package gluon.projects.infra.impl;

import gluon.projects.exceptions.TechnicalException;
import gluon.projects.infra.FileStorageService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FileStorageServiceImpl implements FileStorageService {

    private final Path file;

    public FileStorageServiceImpl(Path file) {
        this.file = file;
    }

    @Override
    public void write(String symbol) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), true))) {
            writer.write(symbol);
            writer.newLine();
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
    }
}
