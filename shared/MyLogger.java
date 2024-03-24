package shared;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;

public class MyLogger {
    
    static boolean initialized = false;
    static final Logger logger = Logger.getLogger("MyLogger");
    static FileHandler fh;

    public static void initializeLogger(String fileName) {
        if (initialized) {
            return;
        }

        // Initialize logger
        try {
            // Remove console logger
            Logger rootLogger = Logger.getLogger("");
            rootLogger.removeHandler(rootLogger.getHandlers()[0]);

            fh = new FileHandler(fileName);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } 
        catch (SecurityException | IOException e) {
            e.printStackTrace();
        }

        initialized = true;
    }

    public static void logTCPTo(int peerOne, int peerTwo) {
        if (initialized) logger.info("Peer " + peerOne +" makes a connection to Peer " + peerTwo);
    }

    public static void logTCPFrom(int peerOne, int peerTwo) {
        if (initialized) logger.info("Peer " + peerOne +" is connected from Peer " + peerTwo);
    }
}