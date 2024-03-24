# Computer-Network-Fundamentals-Project

Run:
```
javac ./client/*.java
javac ./server/*.java
javac ./peer/*.java
javac ./shared/*.java
```

Then in new terminals, run peerProcess with peerIDs given in the PeerInfo.cfg file
```
java ./peerProcess.java peerID (replace peerID with actual number)
```

Example:
1) Run compile commands 
2) New terminal and run the peer process command
    ```
    java ./peerProcess.java 1001
    ```
3) New terminal and run the peer process command
    ```
    java ./peerProcess.java 1002
    ```
4) Continue with how many peers you want


After staritng a peer, you should see respective messages in each terminal.