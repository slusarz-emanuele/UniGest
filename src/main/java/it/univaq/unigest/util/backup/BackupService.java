package it.univaq.unigest.util.backup;

import java.io.File;
import java.io.IOException;

public interface BackupService {

    boolean creaBackup() throws IOException;
    boolean ripristinaBackup(File file);

    //TODO: da finire questa funzione
    boolean verificaCadenzaBackup();

}
