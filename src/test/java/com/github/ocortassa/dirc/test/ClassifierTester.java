package com.github.ocortassa.dirc.test;

import com.github.ocortassa.dirc.Launcher;
import org.junit.Test;

/**
 * Created by [OC] on 18/08/2014.
 */
public class ClassifierTester {

    @Test
    public void doTest() {
//        String[] args = new String[] {
//            "-s", "E:/Restore",
//            "-d", "E:/Restore/out",
//            "-m", "C:/Users/IG01850/Desktop/mapping.properties",
//            "-log", "E:/logs/classifier/",
//            "-steps", "0",
//            "-simulate", "false"
//        };

        String[] args = new String[] {
                "-s", "G:/Restore",
                "-d", "G:/Restore/out",
                "-m", "C:/Users/IG01850/Desktop/mapping.properties",
                "-log", "E:/logs/classifier/",
                "-steps", "25",
//                "-simulate", "true"
                "-simulate", "false"
        };

        Launcher.main(args);
    }
}
