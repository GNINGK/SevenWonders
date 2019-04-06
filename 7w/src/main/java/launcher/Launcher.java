package launcher;

import config.CONFIG;
import joueur.Player;
import moteur.Game;

public class Launcher {


    public static final void main(String  [] args) {
        Game game = new Game();
        Player[] players = new Player[CONFIG.NB_PLAYERS];

        // Init players
        for(int i = 0; i < CONFIG.NB_PLAYERS; i++) {
            players[i] = new Player("Player"+(i+1));
        }

        // Start game
        game.start();
        for(int i = 0; i < CONFIG.NB_PLAYERS; i++) {
            players[i].start();
        }
    }
}
