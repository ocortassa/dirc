package com.github.ocortassa.dirc.test;

import com.github.ocortassa.dirc.cli.Launcher;
import org.junit.Test;

/**
 * Created by [OC] on 18/08/2014.
 */
public class ClassifierTest {

    @Test
    public void doTest() {
        String[] args = new String[] {
                "-s", "./Restore/in",
                "-d", "./Restore/out",
                "-m", "./mapping.properties",
                "-log", "./classifier/",
                "-steps", "25",
                "-dryrun", "false"
        };

        Launcher.main(args);
    }
}
