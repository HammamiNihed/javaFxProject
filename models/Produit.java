package models;

public class Produit {
    private int id;
    private String nom;
    private double prix;
    private int quantite;
    private int category;

    public Produit() {}

    public Produit(int id,String nom, double prix, int quantite, int category) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.quantite = quantite;
        this.category = category;
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

    public double getPrix() {
        return this.prix;
    }
    

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return this.quantite;
    }
    

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getCategory() {
        return this.category;
    }
    

    public void setCategory(int category) {
        this.category = category;
    }
    
}
