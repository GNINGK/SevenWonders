package donnees;

import java.util.ArrayList;

public class Hand {

    private ArrayList<Card> cards;
    
  //creation of brown cards (resource) for 1st age
    Card card1 = new Card("Chantier");
	Card card2 = new Card("Cavite");
	Card card3 = new Card("bassin-Argileux");
	Card card4 = new Card("Filon");
	Card card5 = new Card("Friche");
	Card card6 = new Card("Excavation");
	Card card7 = new Card("Fosse-Argileuse");
	Card card8 = new Card("Exploitation-Forestiere");
	Card card9 = new Card("Gisement");
	Card card10 = new Card("Mine");
	Card card11 = new Card("Scierie");
	Card card12 = new Card("Carriere");
	Card card13 = new Card("Briqueterie");
	Card card14 = new Card("Fonderie");
	

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Hand() {
    	cards = new ArrayList<>();
    	cards.add(card1);
    	cards.add(card2);
    	cards.add(card3);
    	cards.add(card4);
    	cards.add(card5);
    	cards.add(card6);
    	cards.add(card7);
    	cards.add(card8);
    	cards.add(card9);
    	cards.add(card10);
    	cards.add(card11);
    	cards.add(card12);
    	cards.add(card13);
    	cards.add(card14);
    	
    }
    public Hand(ArrayList<Card> cards) {
        this.cards = cards;
    }


    public void addCard(Card c) {
        getCards().add(c);
    }


    public String toString()  {
        String handStr = "[";

        for(Card c : cards) handStr += c +" ; ";

        if (handStr.length() > 4) handStr = handStr.substring(0, handStr.length()-3);

        handStr += "]";
        return handStr;
    }
}
