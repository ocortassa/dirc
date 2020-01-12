package com.github.ocortassa.dirc.cli;

import com.beust.jcommander.JCommander;
import com.github.ocortassa.dirc.Classifier;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Launcher {
    private final static Logger LOGGER = LogManager.getLogger(Launcher.class);

    private ClassifierParams params = new ClassifierParams();

    public static void main(String[] args) {
        Launcher launcher = new Launcher(args);
        try {
            launcher.launch();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public Launcher(String[] args) {
        new JCommander(params, args);
    }

    private void launch() throws IOException {
        // Logging parametri di avvio
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n----------------------------------------------")
            .append("\nsource (-s): \t\t\t").append(params.source)
            .append("\ndestination (-d): \t\t").append(params.destination)
            .append("\nmapping (-m): \t\t\t").append(params.mapping)
            .append("\nlog (-l): \t\t\t\t").append(params.logpath)
            .append("\nsteps (-steps): \t\t").append(params.steps)
            .append("\ndryrun (-dryrun): \t").append(params.dryrun)
            .append("\n----------------------------------------------");
        LOGGER.info(buffer.toString());

        Classifier c = new Classifier();
//        c.setDryRun(true);
//        c.setSourcePath("/media/omar/88D0C54DD0C54264/Restore");
//        c.setDestPath("/media/omar/88D0C54DD0C54264/out");

        if (StringUtils.isEmpty(params.source)) {
            LOGGER.error("Parametro -s mancante - Impossibile procedere!");
            printUsage();
            return;
        }
        c.setSourcePath(params.source);

        if (StringUtils.isEmpty(params.destination)) {
            LOGGER.error("Parametro -d mancante - Impossibile procedere!");
            printUsage();
            return;
        }
        c.setDestPath(params.destination);

        if (StringUtils.isEmpty(params.mapping)) {
            LOGGER.warn("Parametro -m mancante - Viene utilizzato il file distribuito col pacchetto!");
            printUsage();
            return;
        }
        c.setMappingFile(params.mapping);

        c.setSteps(params.steps);
        c.setDryRun(params.dryrun);

        c.classify();
    }

    private void printUsage() {
        StringBuilder buffer = new StringBuilder("java -jar Classifier.jar \n");
        buffer.append("-s [Directory coi contenuti da classificare]")
                .append("-d [Directory in cui posizionare i risultati]")
                .append("-m [File di mapping per guidare la classificazione]")
                .append("-l [Directory in cui inserire i log]")
                .append("-steps [Step di elaborazione, se 0 nessun limite]")
                .append("-dryrun [indica se la procedura effettua una simulazione o no]");
        LOGGER.info(buffer.toString());
    }

    /*
    private void settingFileLogger(String logFile) throws IOException {
        FileAppender fileAppender = new RollingFileAppender();
        fileAppender.setName("fileAppender");
        fileAppender.setFile(logFile);
        fileAppender.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
        fileAppender.setAppend(true);
        fileAppender.setThreshold(Level.INFO);
        fileAppender.setBufferSize(1024*1024*5);    // 5 Mb
        fileAppender.activateOptions();
        Logger.getRootLogger().addAppender(fileAppender);

//        ConsoleAppender console = new ConsoleAppender(); //create appender
//        //configure the appender
//        String PATTERN = "%d [%p|%c|%C{1}] %m%n";
//        console.setLayout(new PatternLayout(PATTERN));
//        console.setThreshold(Level.FATAL);
//        console.activateOptions();
//        //add appender to any Logger (here is root)
//        Logger.getRootLogger().addAppender(console);
//
//        FileAppender fa = new FileAppender();
//        fa.setName("FileLogger");
//        fa.setFile("mylog.log");
//        fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
//        fa.setThreshold(Level.DEBUG);
//        fa.setAppend(true);
//        fa.activateOptions();
//
//        //add appender to any Logger (here is root)
//        Logger.getRootLogger().addAppender(fa)
    }
    */
}
