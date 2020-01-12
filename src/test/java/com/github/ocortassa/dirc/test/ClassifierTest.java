package com.github.ocortassa.dirc.test;

import com.github.ocortassa.dirc.cli.Launcher;
import org.junit.Test;

/**
 * Created by [OC] on 18/08/2014.
 */
public class ClassifierTest {

    /*
    @Parameter(names = {"-i", "-input"}, description = "Percorso documenti input")
    @Parameter(names = {"-o", "-output"}, description = "Percorso documenti output")
    @Parameter(names = {"-m", "-mapping"}, description = "File con i mapping delle estensioni")
    @Parameter(names = {"-l", "-log"}, description = "Directory in cui scaricare il log dell'esecuzione")
    @Parameter(names = {"-steps"}, description = "Numero di directory da esplorare")
    @Parameter(names = {"-dryrun"}, description = "Flag per simulazione", converter = ClassifierParamsConverter.class, arity = 1)
    */

    @Test
    public void doTest() {
        String[] args = new String[] {
                "-i", "./Restore/in",
                "-o", "./Restore/out",
                "-m", "./mapping.properties",
                "-log", "./classifier/",
                "-steps", "25",
                "-dryrun", "false"
        };

        Launcher.main(args);
    }
}
