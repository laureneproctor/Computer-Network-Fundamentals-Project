package shared;

import java.util.ArrayList;
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

    public static void logChangedPreferredNeighbors(int peerID, ArrayList<Integer> peerIDs) {
        if (initialized) logger.info("Peer [peer_ID] has the preferred neighbors [preferred neighbor ID list]");
    }

    public static void logChangeOfOptimisticallyUnchokedNeighbor(int peerID, ArrayList<Integer> peerIDs) {
        if (initialized) logger.info("Peer [peer_ID] has the optimistically unchoked neighbor [optimistically unchoked neighbor ID]");
    }

    public static void logUnchoking(int peerID, ArrayList<Integer> peerIDs) {
        if (initialized) logger.info("Peer [peer_ID 1] is unchoked by [peer_ID 2]");
    }

    public static void logChoking(int peerID, ArrayList<Integer> peerIDs) {
        if (initialized) logger.info("Peer [peer_ID 1] is choked by [peer_ID 2]");
    }

    public static void logReceivingHaveMessage(int peerID, ArrayList<Integer> peerIDs) {
        if (initialized) logger.info("Peer [peer_ID 1] received the 'have' message from [peer_ID 2] for the piece [piece index]");
    }

    public static void logReceivingInterestedMessage(int peerID, ArrayList<Integer> peerIDs) {
        if (initialized) logger.info("Peer [peer_ID 1] received the 'interested' message from [peer_ID 2]");
    }

    public static void logReceivingNotInterestedMessage(int peerID, ArrayList<Integer> peerIDs) {
        if (initialized) logger.info("Peer [peer_ID 1] received the 'not interested' message from [peer_ID 2]");
    }

    public static void logDownloadingAPiece(int peerID, ArrayList<Integer> peerIDs) {
        if (initialized) {
            logger.info("""
                Peer [peer_ID 1] has downloaded the piece [piece index] from [peer_ID 2].
                Now the number of pieces it has is [number of pieces]
                """);
        }
    }

    public static void logCompletionOfDownload(int peerID, ArrayList<Integer> peerIDs) {
        if (initialized) logger.info("Peer [peer_ID] has downloaded the complete file");
    }
}