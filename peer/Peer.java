package peer;

/*
    Peer class contains peer metrics which include the id, name of the host, port the connection is on, and the file it has
 */  
public class Peer {
    int id;
    String hostname;
    int listeningPort;
    int hasFile;

    //constructor for Peer object
    public Peer(int id, String hostname, int listeningPort, int hasFile) {
        this.id = id;
        this.hostname = hostname;
        this.listeningPort = listeningPort;
        this.hasFile = hasFile;
    }

    // returns the id of the peer as an integer
    public int getId() {
        return id;
    }

    // returns the port number as an integer
    public int getlisteningPort() {
        return listeningPort;
    }

    // a function that prints all of the peer info
    public void printVariables() {
        System.out.println("id: " + id);
        System.out.println("hostname: " + hostname);
        System.out.println("listeningPort: " + listeningPort);
        System.out.println("hasFile: " + hasFile);
        System.out.println();
    }
}
