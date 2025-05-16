package models;

public class Category {
    private int id;
    private String nom;
    private String description;

    public Category() {}

    public Category(int id,String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    public int getId() {
        return this.id;
    }
    

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }
    

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }
    

    public void setDescription(String description) {
        this.description = description;
    }
}
