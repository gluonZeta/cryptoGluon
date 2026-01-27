package gluon.projects.services.impl;

import gluon.projects.exceptions.TechnicalException;
import gluon.projects.services.SymbolWriter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileSymbolWriter implements SymbolWriter {

    private final Path file;

    public FileSymbolWriter(Path file) {
        this.file = file;
    }

    @Override
    public void write(String symbol) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                file,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            writer.write(symbol);
            writer.newLine();
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
    }
}
