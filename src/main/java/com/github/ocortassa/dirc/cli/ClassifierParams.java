package com.github.ocortassa.dirc.cli;

import com.beust.jcommander.Parameter;

/**
 * Parametri input launcher
 *
 * Created by [OC] on 18/08/2014.
 */
public class ClassifierParams {

    @Parameter(names = {"-i", "-input"}, description = "Percorso documenti input")
    public String source = "";

    @Parameter(names = {"-o", "-output"}, description = "Percorso documenti output")
    public String destination = "";

    @Parameter(names = {"-m", "-mapping"}, description = "File con i mapping delle estensioni")
    public String mapping = "mapping.properties";

    @Parameter(names = {"-l", "-log"}, description = "Directory in cui scaricare il log dell'esecuzione")
    public String logpath = "";

    @Parameter(names = {"-steps"}, description = "Numero di directory da esplorare")
    public int steps = 0;    // Se 0 allora non ci sono limiti

    @Parameter(names = {"-dryrun"}, description = "Flag per simulazione", converter = ClassifierParamsConverter.class, arity = 1)
    public boolean dryrun = true;
}
