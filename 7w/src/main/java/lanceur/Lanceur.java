package lanceur;

import config.CONFIG;
import joueur.Player;
import moteur.Partie;

public class Lanceur {


    public static final void main(String  [] args) {
        Partie p = new Partie();
        Player[] players = new Player[CONFIG.NB_JOUEURS];

        // création des 4 players
        for(int i = 0 ; i < CONFIG.NB_JOUEURS; i++) {
            players[i] = new Player("Player"+(i+1));
        }

        // démarrage du jeu
        p.start();
        for(int i = 0 ; i < CONFIG.NB_JOUEURS; i++) {
            players[i].start();
        }
    }
}
