package donnees;

public class Wonder {
    private String name;
    private String ressource;

    public String getRessource() {
        return ressource;
    }

    public void setRessource(String ressource) {
        this.ressource = ressource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wonder() { setRessource("-vide-");}
    public Wonder(String n) { this(); setName(n);}


    public String toString() {
        return "Wonder "+ getName();
    }
}
