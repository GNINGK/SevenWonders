package donnees;

public class Card {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card() {}
    public Card(String name) {
        this.name = name;
    }


    public String toString() {
        return "[carte - "+getName()+" -]";
    }

    public boolean equals(Object o) {
        if ((o != null) && (o instanceof Card)) {
            return getName().equals(((Card) o).getName());
        }
        else return false;
    }


}
