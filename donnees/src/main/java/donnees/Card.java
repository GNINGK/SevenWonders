package donnees;

public class Card {

    private String name;

    public Card() {}
    public Card(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "[card - " + getName() + " -]";
    }

    public boolean equals(Object o) {
        if ( o instanceof Card) {
            return getName().equals(((Card) o).getName());
        }
        else return false;
    }
}
