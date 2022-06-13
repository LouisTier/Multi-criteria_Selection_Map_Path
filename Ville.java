package projet;

/**
 *
 * @author Louis
 */

// Ville = Lieu + un nom donc ville hérite de Lieu
public class Ville extends Lieu {
    
    // Attribut
    String nom; // nom de la ville
    
    // Constructeur
    public Ville(int ref, int posX, int posY, String nom){
        
        super(ref,posX,posY);
        this.nom = nom;
    }
    
     // Méthodes
    public String getNom() {
        return nom; // Pour récupérer la valeur du nom
    }

    public void setNom(String nom) {
        this.nom = nom; // Pour changer un nom
    }    
}
