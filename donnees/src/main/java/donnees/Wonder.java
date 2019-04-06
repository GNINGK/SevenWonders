package donnees;

public class Wonder {
    private String name;
    private String resource;


    public Wonder() { setResource("-empty-");}
    public Wonder(String n) { this(); setName(n);}


    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String toString() {
        return "Wonder "+ getName();
    }
}
