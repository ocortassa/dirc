package com.github.ocortassa.dirc;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Directory Classifier by Extension
 *
 * @author [OC]
 */
public class Classifier {
	private final Logger LOGGER = LogManager.getLogger(Classifier.class);

    //TODO: [OC]
//    statistiche!

    // Parametri
    private String sourcePath = "";
	private String mappingFile = "";
	private String destPath = "";
    private int steps= 0;
    private boolean dryRun = false;

	private Map<String, String> mapping = new HashMap<>();
	private List<File> sources = new ArrayList<>();

    private long[] counters = new long[4];

    private final static int TOTAL_COUNTER = 0;
    private final static int PROCESSED_COUNTER = 1;
    private final static int REJECTED_COUNTER = 2;
    private final static int UNMAPPED_COUNTER = 3;

    private final static int MAX_LENGTH_ALLOWED = 25 + 75;  // directory + nome file

	public Classifier() { }

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getDestPath() {
		return destPath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

    public String getMappingFile() {
        return mappingFile;
    }

    public void setMappingFile(String mappingFile) {
        this.mappingFile = mappingFile;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setDryRun(boolean dryRun) {
        this.dryRun = dryRun;
    }

    public boolean useDryRun() {
        return dryRun;
    }

	public void classify() {
		LOGGER.info("Classification started");
		try {
            resetCounters();
			loadMapping();
			loadSourcesList();
			processSources();
            printStatistics();
		} catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			LOGGER.info("Classification completed.\n\n");
		}
	}

    private void resetCounters() {
        for (int idx = 0; idx < counters.length; idx++) {
            counters[idx] = 0;
        }
    }

    private void increaseCounter(int index) {
        counters[index]++;
    }

    private long getCounter(int index) {
        return counters[index];
    }

    private void printStatistics() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n----------------------------------------------------------\n")
            .append("TOTALE: \t\t\t\t").append(getCounter(TOTAL_COUNTER)).append(" files\n")
            .append("PROCESSATI: \t\t\t").append(getCounter(PROCESSED_COUNTER)).append(" files\n")
            .append("SCARTATI: \t\t\t\t").append(getCounter(REJECTED_COUNTER)).append(" files\n")
            .append("NON MAPPATI: \t\t\t").append(getCounter(UNMAPPED_COUNTER)).append(" files\n")
            .append("----------------------------------------------------------");
        LOGGER.info(buffer.toString());
    }

	private void loadMapping() throws ConfigurationException {
		Configuration config = new PropertiesConfiguration( getMappingFile() );
		Iterator<String> itKeys = config.getKeys();
		while (itKeys.hasNext()) {
			// La chiave diventa il target mentre i valori diventano le chiavi
			String key = itKeys.next();
			String extList = config.getString(key);
			String[] extensions = extList.split(":");
			for (String extension : extensions) {
				mapping.put(extension.trim().toLowerCase(), key.trim().toLowerCase());
			}
		}
		LOGGER.info("Mapping loaded");
	}
	
	private void loadSourcesList() {
		File source = new File(getSourcePath());
		File[] sourcesArray = source.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		    	String lowerName = name.trim().toLowerCase(); 
		        return lowerName.startsWith("recup") &&
		        		!(lowerName.endsWith("done"));
		    }
		});
		sources = sourcesArray != null ? Arrays.asList(sourcesArray) : new ArrayList<File>();
	}
	
	private void processSources() throws IOException {
		int counter = 0;
        for (File source : sources) {
            if (getSteps() > 0 && counter == getSteps()) {
                LOGGER.info("Raggiunto limite step da elaborare [" + getSteps() + "].");
                return;
            }

            LOGGER.info("Processing [" + source.getAbsolutePath() + "]");
			File[] files = source.listFiles();
			if (files == null || files.length == 0) {
				setDoneDirectory(source);
                continue;
			}
            LOGGER.info("Trovati [" + files.length + "] files");
			for (File file : files) {
                increaseCounter(TOTAL_COUNTER);
				String extension = getExtension(file.getAbsolutePath()).trim().toLowerCase();
				if (extension.length() == 0) {
					LOGGER.info("File [" + file.getAbsolutePath() + "] senza estensione! Non viene preso in considerazione.");
                    increaseCounter(REJECTED_COUNTER);
					continue;
				}
                if (file.getAbsolutePath().length() > MAX_LENGTH_ALLOWED) {
                    LOGGER.info("File [" + file.getAbsolutePath() + "] con nome troppo lungo! Non viene preso in considerazione.");
                    increaseCounter(UNMAPPED_COUNTER);
                    continue;
                }
                if (file.getAbsolutePath().contains("?")) {
                    LOGGER.info("File [" + file.getAbsolutePath() + "] contiene caratteri non consentiti! Non viene preso in considerazione.");
                    increaseCounter(UNMAPPED_COUNTER);
                    continue;
                }

				String destinationDir = mapping.get(extension);
				if (destinationDir != null) {
					String destinationPath = destPath + "/" + destinationDir + "/";
                    String destinationFile = destinationPath + file.getName();
					LOGGER.info("File [" + file.getAbsolutePath() + ", " + extension + "] verso [" + destinationPath + "]");
                    File destinationPathRef = new File(destinationPath);
                    File destinationFileRef = new File(destinationFile);

                    if ( !useDryRun() ) {
                        // Se non esiste già, creo la directory di destinazione
                        if ( !destinationPathRef.exists() ) {
                            FileUtils.forceMkdir(destinationPathRef);
                            LOGGER.info("Creata directory [" + destinationPathRef.getAbsolutePath() + "]");
                        }
                    }

                    if ( !useDryRun() ) {
                        moveFile(file, destinationFileRef);
                        LOGGER.info("spostato!");
                    }
                    increaseCounter(PROCESSED_COUNTER);
                } else {
                    // Scartati perche non presenti nel mapping
                    increaseCounter(UNMAPPED_COUNTER);
                }
			}   // end for
            counter++;

            // Se i files non mappati coincidono col totale allora posso flaggare come DONE
            if (getCounter(TOTAL_COUNTER) == getCounter(UNMAPPED_COUNTER)) {
                setDoneDirectory(source);
            }
		}   // end for
	}

    private void setDoneDirectory(File source) throws IOException {
        // La directory è vuota, possiamo rinominarla
        String destDirPath = source.getAbsolutePath() + "_DONE";
        LOGGER.info("Directory [" + source.getAbsolutePath() + "] vuota o esaurita, da rinominare in " + destDirPath);
        moveDirectory(source, destDirPath);
    }

	private String getExtension(String absolutePath) {
		int slashPosition = absolutePath.lastIndexOf("/");
		String fileName = absolutePath.substring(slashPosition + 1, absolutePath.length());
		int dotPosition = fileName.lastIndexOf(".");
		return dotPosition > -1 ? fileName.substring(dotPosition + 1, fileName.length()) : "";
	}

    private void moveFile(File source, String destFileName) throws IOException {
        moveFile(source, new File(destFileName));
    }

	private void moveFile(File source, File dest) throws IOException {
        FileUtils.moveFile(source, dest);
	}

    private void moveDirectory(File source, String destFileName) throws IOException {
        moveDirectory(source, new File(destFileName));
    }

    private void moveDirectory(File source, File dest) throws IOException {
        FileUtils.moveDirectory(source, dest);
    }

}
