package gluon.projects.infra;

import java.util.List;

public interface FileStorageService {
    void write(String symbol);
    void cleanFolder();
    List<String> readSymbolExistingFile();
}
