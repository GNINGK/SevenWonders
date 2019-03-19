package moteur;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import config.CONFIG;
import config.MESSAGES;
import donnees.Card;
import donnees.Hand;
import donnees.Wonder;

import java.util.ArrayList;

public class Partie {
    SocketIOServer serveur;
    private ArrayList<Participant> participants;


    public Partie() {

        // création du serveur (peut-être externalisée)
        Configuration config = new Configuration();
        config.setHostname(CONFIG.IP);
        config.setPort(CONFIG.PORT);
        serveur = new SocketIOServer(config);

        // init de la liste des participants
        participants = new ArrayList<>();

        // abonnement aux connexions
        serveur.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                System.out.println("serveur > connexion de "+socketIOClient.getRemoteAddress());
                System.out.println("serveur > connexion de "+socketIOClient);

                // mémorisation du participant
                // ajout d'une limitation sur le nombre de joueur
                if (participants.size() < CONFIG.NB_JOUEURS) {
                    Participant p = new Participant(socketIOClient);
                    participants.add(p);
                }
            }
        });



        // réception de l'identification du joueur
        serveur.addEventListener(MESSAGES.MON_NOM, String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
                Participant p = findParticipant(socketIOClient);
                if (p != null) {
                    p.setName(s);
                    System.out.println("server > Identification of " + p.getName() + " (" +
                            socketIOClient.getRemoteAddress() + ")");

                    if (isEveryoneCheckIn()) {
                        startGame();
                    }
                }
            }
        });


        // réception de la carte jouée
        serveur.addEventListener(MESSAGES.JE_JOUE, Card.class, new DataListener<Card>() {
            @Override
            public void onData(SocketIOClient socketIOClient, Card card, AckRequest ackRequest) throws Exception {
                // retrouver le participant
                Participant p = findParticipant(socketIOClient);
                if (p != null) {
                    System.out.println("server > " + p + " played "+ card);
                    p.getHand().getCards().remove(card);
                    System.out.println("server > " + p + " still has : " + p.getHand().getCards());

                    p.setHasPlayed(true);

                    if(hasEveryonePlayed()){
                        switchHands();
                        prepareNewTurn();
                    }

                    // etc.
                }
            }
        });
    }

    private void startGame() {
        // création des wonders, au début de simple nom
        Wonder[] wonders = new Wonder[CONFIG.NB_JOUEURS];
        
        wonders[0] = new Wonder("Babylon");//(name, side)
		wonders[1] = new Wonder("Rhodes");
		wonders[2] = new Wonder("Halicarnassus");
		wonders[3] = new Wonder("Giza");
//		wonders[4] = new Wonder("Alexandria");
//		wonders[5] = new Wonder("Olympia");
//		wonders[6] = new Wonder("Ephesus");

        for(int i = 0; i < CONFIG.NB_JOUEURS; i++) {
        	
            // association joueur - merveille
            participants.get(i).setWonder(wonders[i]);
            System.out.println("server > Send to " + participants.get(i) + " wonder " + wonders[i]);

            // envoi de la merveille au joueur
            participants.get(i).getSocket().sendEvent(MESSAGES.ENVOI_DE_MERVEILLE, wonders[i]);
        }

        // création des cartes initiales
        Hand[] hands = new Hand[CONFIG.NB_JOUEURS];
        
        for(int i = 0; i < CONFIG.NB_JOUEURS; i++) {
            hands[i] = new Hand();
            for(int j = 0 ; j < 8; j++) {
                hands[i].ajouterCarte(hands[i].getCards().get(j));
                
            }
            // association main initiale - joueur
            participants.get(i).setHand(hands[i]);
            // envoi de la main au joueur
            participants.get(i).getSocket().sendEvent(MESSAGES.ENVOI_DE_MAIN, hands[i]);

        }

    }

    private boolean isEveryoneCheckIn() {
        boolean resultat = true;
        for(Participant p : participants) {
            // pas nom, pas identifié
            if (p.getName() == null) {
                resultat = false;
                break;
            }
        }

        return resultat;
    }

    private boolean hasEveryonePlayed() {
        for(Participant p : participants) {
            if (!p.hasPlayed()) {
                return false;
            }
        }

        return true;
    }

    private void switchHands(){
        Hand previousHand = participants.get(0).getHand();
        for(int i=1; i < participants.size(); i++) {
            Hand hand = participants.get(i).getHand();
            participants.get(i).setHand(previousHand);
            previousHand = hand;
        }
        participants.get(0).setHand(previousHand);

        System.out.println("server > Hands have been switched");
        for(Participant p : participants) {
            System.out.println("server > " + p.getName() + " received " + p.getHand());
        }
    }

    private void prepareNewTurn() {
        for (Participant p : participants) {
            p.setHasPlayed(false);
        }
    }

    public void start() {
        // démarrage du serveur
        serveur.start();
    }


    /**
     * méthode pour retrouver un participant à partir de la socket cliente (disponible à la réception d'un message)
     * @param socketIOClient le client qui vient d'envoyer un message au serveur
     * @return le Participant correspondant à la socketIOClient
     */
    private Participant findParticipant(SocketIOClient socketIOClient) {
        Participant p = null;

        for(Participant part : participants) {
            if (part.getSocket().equals(socketIOClient)) {
                p = part;
                break;
            }
        }
        return p;
    }



    public static final void main(String  [] args) {
        Partie p = new Partie();
        p.start();
    }
}
