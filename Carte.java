package projet;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Menu;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Louis
 */
public class Carte {
    
    Fenetre fen;
    Menu Fichier;
    Menu Peage;
    
    public ArrayList <Lieu> LesLieuxDeFrance = new ArrayList();
    public ArrayList <Ville> LesVillesDeFrance = new ArrayList(); //<attribut> puis nom
    public ArrayList <Chemin> LesChemins = new ArrayList();
            
    public Carte(Fenetre fen){
        this.fen=fen;
    }
    
    public Carte(String fic){
        try {
            BufferedReader br = new BufferedReader(new FileReader(fic)); //pour lire un fichier
            String lu = br.readLine(); //pour lire chaque ligne du fichier
            while(lu!=null){ 
                String [] dec = lu.split(" "); //décompose la ligne en X éléments à chaque espace
                switch(dec[0]){
                    
                    case "P": // le cas où on a P = Point
                        Lieu lieu = new Lieu(Integer.parseInt(dec[1]),Integer.parseInt(dec[2]),Integer.parseInt(dec[3]));
                        // parseInt transforme un string en entier
                        // On crée un lieu avec numéro du point, coordonnée x, coordonnée y
                        LesLieuxDeFrance.add(lieu);
                        break;
                        
                    case "V": // le cas où on a V = Ville
                        Ville ville = new Ville(Integer.parseInt(dec[1]),Integer.parseInt(dec[2]),Integer.parseInt(dec[3]),dec[4]);
                        // On crée une ville avec numéro de la ville, coordonnée x, coordonnée y, nom de la ville
                        LesVillesDeFrance.add(ville);
                        LesLieuxDeFrance.add(ville);
                        break;
                        
                    case "C": // le cas où on a C = Chemin
                    boolean doubleSens = true; // On initialise  la présence d'un double sens
                    boolean gratuit = true; // On initialise l'absence d'un péage
                    if("u".equals(dec[5])){
                        doubleSens=false; // On indique que c'est à sens unique
                    }
                    if(dec.length==7 && "$".equals(dec[6])){
                        gratuit=false; // On indique qu'il y un péage
                    }
                    Chemin chemin = new Chemin(Integer.parseInt(dec[1]),Integer.parseInt(dec[2]),Integer.parseInt(dec[3]),Integer.parseInt(dec[4]),doubleSens,gratuit);
                    /* On crée un chemin avec point de départ, point d'arrivée, la longueur, la durée, 
                    le sens et s'il y a un péage */
                    LesChemins.add(chemin);
                    
                    if(doubleSens){
                     Chemin cheminInv = new Chemin(Integer.parseInt(dec[2]),Integer.parseInt(dec[1]),Integer.parseInt(dec[3]),Integer.parseInt(dec[4]),doubleSens,gratuit);
                       LesChemins.add(cheminInv);
                    }
                }
                
                
                lu=br.readLine();
        }
            
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        }
        afficher();
    }
    
    // Cette méthode vérifie que le fichier lit bien la carte en affichant les différents Lieux, Villes et Chemins
    public void afficher(){
        
        for(Lieu l:LesLieuxDeFrance){ // vérifie si l'élément a bien été crée comme une ville au début
             if(l instanceof Ville){
                 Ville v = (Ville)l; // cast = transtypage où l devient une Ville qui s'appelle v
                 
            System.out.println("V "+v.ref+ " "+v.posX+ " "+v.posY+ " "+v.nom);// On affiche les villes + leurs attributs
             }else{
                 System.out.println("P "+l.ref+ " "+l.posX+ " "+l.posY); // On affiche les lieux + leurs attributs
             }
        }
        for (Chemin c:LesChemins){
            System.out.println("C "+c.ref1+ " "+c.ref2+ " "+c.km+ " "+c.min+ " "+c.sens+ " "+c.peage) ; // On affiche les chemins + leurs attributs
            
        }
    } 
}
