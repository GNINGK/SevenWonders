package moteur;

import com.corundumstudio.socketio.SocketIOClient;
import donnees.Hand;
import donnees.Wonder;


public class Participant {

    private SocketIOClient socket;
    private String name;
    private Wonder wonder;
    private Hand hand;
    private boolean hasPlayed;


    public Participant(SocketIOClient socketIOClient) {
        setSocket(socketIOClient);
    }

    public void setSocket(SocketIOClient socket) {
        this.socket = socket;
    }

    public SocketIOClient getSocket() {
        return socket;
    }



    public String toString() {
        return "[Player "+ getName()+" : "+getSocket().getRemoteAddress()+"]";
    }


    public void setName(String nom) {
        this.name = nom;
    }

    public String getName() {
        return name;
    }

    public void setWonder(Wonder wonder) {
        this.wonder = wonder;
    }

    public Wonder getWonder() {
        return wonder;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    public boolean hasPlayed() { return hasPlayed; }

    public void setHasPlayed(boolean hasPlayed) { this.hasPlayed = hasPlayed; }
}
