package joueur;

import config.CONFIG;
import config.MESSAGES;
import donnees.Card;
import donnees.Hand;
import donnees.Wonder;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Random;

public class Player {


    private String name;
    Socket connexion ;
    private Wonder wonder;

    public Player(String un_joueur) {
        setName(un_joueur);

        System.out.println(name +" > creation");
        try {
            // préparation de la connexion
            connexion = IO.socket("http://" + CONFIG.IP + ":" + CONFIG.PORT);
            

            // abonnement à la connexion
            connexion.on("connect", new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    System.out.println(getName() + " > connecte");
                    System.out.println(getName()+" > envoi de mon name");
                    connexion.emit(MESSAGES.MY_NAME, getName());
                }
            });

            
            // réception de la wonder
            connexion.on(MESSAGES.WONDER_SENDING, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    // réception du JSON
                    JSONObject wonderJSON = (JSONObject)objects[0];
                    try {
                        // conversion du JSON en Wonder
                        String n = wonderJSON.getString("name");
                        // les merveilles ont toutes une ressource vide, pour illustrer avec un objet avec plus qu'une seule propriété
                        String ressource = wonderJSON.getString("resource");
                        Wonder m = new Wonder(n);
                        m.setResource(ressource);

                        // mémorisation de la wonder
                        System.out.println(name +" > j'ai recu "+m);
                        setWonder(m);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            // réception de la main
            connexion.on(MESSAGES.HAND_SENDING, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    // réception de l'objet JSON : une main
                    JSONObject handJSON = (JSONObject)objects[0];
                    try {
                        Hand m = new Hand();
                        // la main ne contient qu'une liste de carte, c'est un JSONArray
                        JSONArray cartesJSON = handJSON.getJSONArray("cards");
                        // on recrée chaque carte
                        for(int j = 0 ; j < cartesJSON.length(); j++) {
                            JSONObject carteJSON = (JSONObject) cartesJSON.get(j);
                            Card c = new Card(carteJSON.getString("name"));
                            m.addCard(c);
                        }
                        System.out.println(name +" > I received " + m);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            connexion.on(MESSAGES.YOUR_TURN, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    JSONObject handJSON = (JSONObject)objects[0];
                    try {
                        Hand m = new Hand();
                        // la main ne contient qu'une liste de carte, c'est un JSONArray
                        JSONArray cartesJSON = handJSON.getJSONArray("cards");
                        // on recrée chaque carte
                        for (int j = 0; j < cartesJSON.length(); j++) {
                            JSONObject carteJSON = (JSONObject) cartesJSON.get(j);
                            Card c = new Card(carteJSON.getString("name"));
                            m.addCard(c);
                        }
                        play(m);
                    } catch (JSONException jse){}
                }
            });
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    private void play(Hand m) {
        Random r = new Random();
        int cardIndex = r.nextInt(m.getCards().size());
        JSONObject cardToPlay = new JSONObject(m.getCards().get(cardIndex)) ;

        System.out.println(name + " > Play " + cardToPlay.toString());
        connexion.emit(MESSAGES.IM_PLAYING, cardToPlay);
    }

    public void start() {
        if (connexion != null) connexion.connect();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public static final void main(String  [] args) {
        Player p = new Player("toto");
        p.start();
    }

    public void setWonder(Wonder wonder) {
        this.wonder = wonder;
    }

    public Wonder getWonder() {
        return wonder;
    }
}
