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
public class Chemin {

   //Attributs
    
    int ref1, ref2; // référence du point d'arrivée et du point de départ 
    int km, min; // distance et durée
    boolean sens; // double sens (oui ou non)
    boolean peage; // péage (oui ou non)

    public Chemin(int ref1, int ref2, int km, int min, boolean sens, boolean peage) {
        this.ref1 = ref1;
        this.ref2 = ref2;
        this.km = km;
        this.min = min;
        this.sens = sens; 
        this.peage = peage;
    }

    public int getRef1() {
        return ref1; // Pour récupérer la référence du point de départ
    }

    public void setRef1(int ref1) {
        this.ref1 = ref1; // Pour changer la référence du point de départ
    }

    public int getRef2() {
        return ref2; // Pour récupérer la référence du point d'arrivée
    }

    public void setRef2(int ref2) {
        this.ref2 = ref2; // Pour changer la référence du point d'arrivée
    }

    public int getKm() {
        return km; // Pour récupérer la distance entre deux points
    }

    public void setKm(int km) {
        this.km = km; // Pour changer la distance
    }

    public int getMin() {
        return min; // Pour récupérer la durée entre deux points 
    }

    public void setMin(int min) {
        this.min = min; // Pour changer le temps 
    }

    public boolean isSens() {
        return sens; // Pour signaler la présence d'un double sens
    }

    public void setSens(boolean sens) {
        this.sens = sens; // Pour changer la présence ou non d'un double sens 
    }

    public boolean isPeage() {       
        return peage; // Pour signaler la présence d'un péage
    }

    public void setPeage(boolean peage) {
        this.peage = peage; // Pour changer la présence ou non d'un péage
    }  
}

