import shared.Info;
import server.Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import client.Client;
import peer.Peer;

public class peerProcess {

    public static ArrayList<Peer> peers = new ArrayList<>();

    public static void main(String[] args) {
        int peerID = 0;

        // Get Peer ID
        if (args.length > 0) {
            String firstArgument = args[0];
            try {
                peerID = Integer.parseInt(firstArgument);
                System.out.println("My PeerID: " + peerID);
            } catch (NumberFormatException e) {
                System.err.println("Cannot convert to int" + firstArgument);
            }
        } 
        else {
            System.err.println("No arguments provided.");
        }
        
        // Read config data
        Info.readCommonConfig();

        // Read Peer info
        peerProcess.readPeers(peerID);
        Peer currentPeer = null;
        for (Peer peer : peers) {
            if (peer.getId() == peerID) {
                currentPeer = peer;
            }
        }

        // Start Server
        Server server = new Server(currentPeer.getlisteningPort());
        server.start();

        // Connect to peers
        for (Peer peer : peers) {
            if (peer != null && peer.getId() == currentPeer.getId()) {
                break;
            }

            Client client = new Client(currentPeer.getId(), peer.getId(), "", peer.getlisteningPort());
            client.start();
        }

    }

    private static void readPeers(int currentPeer) {
        try (BufferedReader br = new BufferedReader(new FileReader("./project_config_file_small/PeerInfo.cfg"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int peerId = Integer.parseInt(parts[0]);
                String hostname = parts[1];
                int port = Integer.parseInt(parts[2]);
                int hasFile = Integer.parseInt(parts[3]);

                Peer peer = new Peer(peerId, hostname, port, hasFile);
                peers.add(peer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}