package projet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Louis
 */
public class Lieu {

    
    // Attributs
    int posX; // abscisse du point
    int posY; // ordonnée du point
    int ref; // ref correspond au numéro du point genre point n°1
    
    // Constructeur
    public Lieu(int ref, int posX,int posY){
        
        this.posX = posX;
        this.posY = posY;
        this.ref = ref;
        
    }
    
    // Méthodes

    public int getPosX() {
        return posX; // Pour récupérer la valeur de l'abscisse du point
    }

    public void setPosX(int posX) {
        this.posX = posX; // Pour changer l'abscisse du point
    }

    public int getPosY() {
        return posY; // Pour récupérer la valeur de l'ordonnée du point
    }

    public void setPosY(int posY) {
        this.posY = posY; // Pour changer l'ordonnéee du point
    }

    public int getRef() {
        return ref; // Pour récupérer la valeur de la référence du point
    }

    public void setRef(int ref) {
        this.ref = ref; // Pour changer la référence du point
    }
    
}
