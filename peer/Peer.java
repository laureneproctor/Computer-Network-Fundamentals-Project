package peer;

public class Peer
{

    int id;
    String hostname;
    int listeningPort;
    int hasFile;


    public Peer(int id, String hostname, int listeningPort, int hasFile)
     {
        this.id = id;
        this.hostname = hostname;
        this.listeningPort = listeningPort;
        this.hasFile = hasFile;
    }

    public int getId() 
    {
        return id;
    }

    public int getlisteningPort() 
    {
        return listeningPort;
    }

    public void printVariables() 
    {
        System.out.println("id: " + id);
        System.out.println("hostname: " + hostname);
        System.out.println("listeningPort: " + listeningPort);
        System.out.println("hasFile: " + hasFile);
        System.out.println();
    }
}
