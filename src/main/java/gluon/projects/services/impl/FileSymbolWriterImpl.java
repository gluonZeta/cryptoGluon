package gluon.projects.services.impl;

import gluon.projects.exceptions.TechnicalException;
import gluon.projects.services.SymbolWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FileSymbolWriterImpl implements SymbolWriter {

    private final Path file;

    public FileSymbolWriterImpl(Path file) {
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
