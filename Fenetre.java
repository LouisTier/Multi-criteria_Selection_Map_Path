package projet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Louis
 */

public class Fenetre {

    Stage st;
    Carte carte;
    Menu Fichier;
    Menu Peage;
    GraphicsContext gc; // Support de dessin
    TextField cd; // Texte pour le départ
    TextField ca; // Texte pour l'arrivée 

    ArrayList<Chemin> lf = new ArrayList(); // Liste finale des chemins
    ArrayList<Chemin> lec = new ArrayList(); // Liste des chemins présents dans la fonction récursive 
    
    boolean arg;
    Ville vd;
    Ville varr;
    boolean peage;
    int min;
    int km;
    Label lmin; // Texte pour afficher la durée
    Label lkm; // Texte pour afficher la distance
    Label error; // Texte pour afficher l'erreur

    public Fenetre(Stage st) throws IOException {
        
        carte = new Carte(this);
        st.setTitle("Itineraire"); //titre interface
        VBox vb = new VBox();
        Scene sc = new Scene(vb); // scène dans la Vbox
        st.setScene(sc);

        MenuBar mb = new MenuBar(); // création de la barre des menus
        vb.getChildren().add(mb); // on ajoute le menu à la Vbox

        Menu mFich = new Menu("Fichier"); // création du menu fichier

        MenuItem mOuvrir = new MenuItem("Ouvrir");
        mOuvrir.setAccelerator(KeyCombination.keyCombination("Ctrl+E")); // création d'un raccourci pour ouvrir une carte
        mOuvrir.setOnAction((ActionEvent e) -> {
            try {
                ouvrir();
            } catch (IOException ex) {
                Logger.getLogger(Fenetre.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        );
        mFich.getItems().add(mOuvrir); // On ajoute le sous menu à Fichier

        MenuItem mQuit = new MenuItem("Quitter"); // création du menu quitter
        mQuit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        mQuit.setOnAction((ActionEvent e) -> {
            System.exit(0);
        }); // création d'un raccourci pour fermer l'interface
        mFich.getItems().add(mQuit); // On ajoute le sous menu à Fichier


        mb.getMenus().addAll(mFich); //On ajoute les deux menus principaux Fichier et Peage à la barre des menus

        BorderPane bp = new BorderPane(); // Canvas avec 5 zones dans chacune des directions + milieux

        GridPane gp = new GridPane(); // Découpe le canvas en grille
        gp.setPadding(new Insets(10, 10, 10, 10)); // Pour créer les marges avec le canvas principal
        gp.setHgap(10); // Marge horizontale entre chaque zone de texte 
        gp.setVgap(20); // Marge verticale entre chaque zone de texte

        Canvas cvt = new Canvas(100, 25); // Partie supérieure "invisible" pour l'esthétique et centrer les zones de texte
        gp.add(cvt, 0, 0);
        Label cd = new Label("Ville de départ"); // On crée l'étiquette
        gp.add(cd, 1, 0); // On ajoute le label à la 1ère colonne ligne 0 de la grille 
        TextField tfd = new TextField();
        gp.add(tfd, 2, 0);
        Label ca = new Label("Ville d'arrivée");
        gp.add(ca, 3, 0);
        TextField tfa = new TextField();
        gp.add(tfa, 4, 0);
        
        
        Canvas cv = new Canvas(650, 650); // Zone de dessin où on insère la carte 
        Canvas cvl = new Canvas(25, 650); // Bordure avec la zone de dessin
        Canvas cvr = new Canvas(25, 650); // Bordure avec la zone de dessin
        Canvas cvb = new Canvas(650, 25); // Bordure avec la zone de dessin 
        bp.setLeft(cvl); // On ajoute la bordure au cîoté gauche
        bp.setRight(cvr);
        bp.setBottom(cvb);
        bp.setTop(gp); // On ajoute la grille à la partie supérieure du Canvas

        lmin = new Label("Temps: " + String.valueOf(min) + " min"); // Affiche le temps
        lkm = new Label("Distance: " + String.valueOf(km) + " km"); // Affiche la durée
        error= new Label(" "); //  En cas d'erreur rien s'il n'y  pas d'erreur
        gp.add(error, 5, 1);
        gp.add(lmin, 0, 1);
        gp.add(lkm, 1, 1);
            
        ChoiceBox cb1 = new ChoiceBox();
        cb1.getItems().addAll("Avec Péage", "Sans Péage");
        cb1.getSelectionModel().select(0);
        gp.add(cb1, 2, 1);

        ChoiceBox cb2 = new ChoiceBox();
        cb2.getItems().addAll("Plus Rapide", "Plus Court");
        cb2.getSelectionModel().select(0);
        gp.add(cb2, 3, 1);

        Button b = new Button("Calculer");
        b.setOnAction((ActionEvent event) -> {
            if (cb2.getSelectionModel().getSelectedItem().equals("Plus Rapide")) {
                arg = false;
            }
            if (cb2.getSelectionModel().getSelectedItem().equals("Plus Court")) {
                arg = true;

            }
            if (cb1.getSelectionModel().getSelectedItem().equals("Avec Péage")) {
                peage = true;
            }
            if (cb1.getSelectionModel().getSelectedItem().equals("Sans Péage")) {
                peage = false;
            }
            
            String lu = tfd.getText();
            boolean err = false; // initialisation du booléen d'erreur
            dessiner(); // Pour réinitialiser la carte à chaque appel de calcul
            
            if (lu.isEmpty()) {
                err = true;
                error.setText("Erreur ville de départ vide");
            }
            String lu2 = tfa.getText();
            if (lu2.isEmpty()) {
                err = true;
                error.setText("Erreur ville d'arrivée vide");
            }
            
            vd = null; // On initialise la variable ville de départ
            varr = null;
            
            if (!err) { // On cherche une correspondance entre la ville rentrée et les villes dans le fichier lu
                for (Ville v : carte.LesVillesDeFrance) {
                    if (v.getNom().equals(lu)) {
                        vd = v;
                    }
                    if (v.getNom().equals(lu2)) {
                        varr = v;
                    }
                }
                if (vd == null) {
                    err = true;
                    error.setText("Erreur ville de départ inconnue");
                    
                }
                if (varr == null) {
                    err = true;
                    error.setText("Erreur ville d'arrivée inconnue");
                }
            }
            
            if (err) {
                return;
            }
            
            if(vd.ref==varr.ref){
                error.setText("Erreur ville de départ et arrivée identique ");
                return;
            }
            
            lec.clear(); // Réinitialise les chemins EN COURS si on fait une deuxième utilisation du programme
            lf.clear(); // Réinitialise les chemins FINAUX si on fait une deuxième utilisation du programme
            
            cheminPlusCourt(vd, varr);
            lmin.setText("Temps :"+String.valueOf(min)+" min");
            lkm.setText("Distance :"+String.valueOf(km)+" km");
            error.setText(" "); 
            

            int n = carte.LesLieuxDeFrance.size(); // Pour la boucle afin de parcourir l'ensemble 
            int Xmax = 0;
            int Ymax = 0;
            for (int i = 0; i < n; i++) { 
                Lieu lieu = carte.LesLieuxDeFrance.get(i);
                if (lieu.getPosX() >= Xmax) {
                    Xmax = lieu.getPosX(); // On récupère la plus grande position en abscisse des lieux lus
                }
            }
            for (int i = 0; i < n; i++) {
                Lieu lieu = carte.LesLieuxDeFrance.get(i);
                if (lieu.getPosY() >= Ymax) {
                    Ymax = lieu.getPosY(); // On récupère la plus grande position en ordonnée des lieux lus
                }
            }
            
            int scaleX = scaleX(Xmax); // Appel de la méthode scaleX pour redimensionner
            int scaleY = scaleX(Ymax);
            
            for (Chemin c : lf) {
                Lieu lieu1 = carte.LesLieuxDeFrance.get(c.getRef1() - 1); // Problème car les ref commencent à 1 et le compteur à 0
                Lieu lieu2 = carte.LesLieuxDeFrance.get(c.getRef2() - 1); // Permet de corriger le problème d'indice 
                gc.setStroke(Color.RED); // Pour choisir la couleur de la ligne du chemin suivi
                gc.strokeLine(lieu1.getPosX() * scaleX, lieu1.getPosY() * scaleY, lieu2.getPosX() * scaleX, lieu2.getPosY() * scaleY);
                // On affiche la ligne entre les deux points
            }
        });
        
        gp.add(b, 4, 1); // On ajoute le bouton 'Calculer' à l'interface

        gc = cv.getGraphicsContext2D();
        gc.setFill(Color.GRAY); // On choisit la couleur pour notre Canvas où on affiche la carte
        gc.fillRect(0, 0, 650, 650); // On applique la couleur

        bp.setCenter(cv);
        vb.getChildren().add(bp);
        st.show(); // Pour afficher le stage et donc notre carte
    }

    private void ouvrir() throws IOException {// utilise une fenêtre spéciale d'accès de JavaFX aux répertoires et fichiers
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir cartographie");// se place sur le répertoire de démarrage de l'application
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));// demnde à l'usager de désigner le fichier, (rép null si abandon)
        File select = fileChooser.showOpenDialog(st);
        if (select != null) {// select indique le fichier choisi dont on récupère le chemin d'accès
            carte = new Carte(select.getAbsolutePath());
        }
        dessiner();
    }

    public void dessiner() {
        
        int n = carte.LesLieuxDeFrance.size(); // Pour la boucle afin de parcourir l'ensemble des lieux
        int Xmax = 0;
        int Ymax = 0;
        for (int i = 0; i < n; i++) { 
            Lieu lieu = carte.LesLieuxDeFrance.get(i);
            if (lieu.getPosX() >= Xmax) { 
                Xmax = lieu.getPosX(); // On récupère la valeur de la position maximale en abscisse des lieux lus
            }
        }
        for (int i = 0; i < n; i++) {
            Lieu lieu = carte.LesLieuxDeFrance.get(i);
            if (lieu.getPosY() >= Ymax) {
                Ymax = lieu.getPosY(); // On récupère la valeur de la position maximale en ordonnée des lieux lus
            }
        }
        
        int scaleX = scaleX(Xmax); // Appel de la méthode scaleX pour redimensionner
        int scaleY = scaleX(Ymax);
        
        int n2 = carte.LesChemins.size(); // Pour la boucle afin de parcourir l'ensemble des chemins
        for (int j = 0; j < n2; j++) {
            Chemin chemin = carte.LesChemins.get(j);
            Lieu lieu1 = carte.LesLieuxDeFrance.get(chemin.getRef1() - 1); // Problème car les ref commencent à 1 et le compteur à 0
            Lieu lieu2 = carte.LesLieuxDeFrance.get(chemin.getRef2() - 1); // Permet de corriger le problème d'indice
            gc.setStroke(Color.BLUE); // On choisit la couleur bleu "initiale" pour les chemins
            gc.strokeLine(lieu1.getPosX() * scaleX, lieu1.getPosY() * scaleY, lieu2.getPosX() * scaleX, lieu2.getPosY() * scaleY);
            // On applique la couleur aux chemins
        }
        for (int i = 0; i < n; i++) {
            Lieu lieu = carte.LesLieuxDeFrance.get(i);
            gc.setFill(Color.ORANGE);
            gc.fillRect((lieu.getPosX() * scaleX) - 2, (lieu.getPosY() * scaleY) - 2, 4, 4);
        }
        int n3 = carte.LesVillesDeFrance.size();
        for (int i = 0; i < n3; i++) {
            Ville lieu = carte.LesVillesDeFrance.get(i);
            gc.setFill(Color.RED); // On choisit la couleur de point qui représente la ville
            gc.fillRect((lieu.getPosX() * scaleX) - 2, (lieu.getPosY() * scaleY) - 2, 4, 4);
            gc.setStroke(Color.BLACK); // On choisit la couleur du texte du nom de la ville
            gc.strokeText(lieu.getNom(), (lieu.getPosX() * scaleX) - 2, (lieu.getPosY() * scaleY) - 2);
        }
    }

    public int scaleX(int Xmax) { // Fonction pour calculer le facteru d'agrandissement / réduction
        return 630 / Xmax; // Car 650 = notre taille sauf que si on prend 650 le point max sera " en dehors "
    }

    public int scaleY(int Ymax) {
        return 630 / Ymax;
    }

    public void cheminPlusCourt(Lieu a, Lieu b) {
        int ba = 0; // Initialise sile chemin n'est pas emprunté 
        for (Chemin c : cheminVoisin(a)) { // On regarde tous les voisins proches de notre lieu de départ A
            if (peage == false && c.isPeage() == false) { // c.isPeage == false indique la présence d'un péage
                return; // On ne compte pas ce chemin dans la liste des chemins voisins
            }
            for (Chemin ch : lec) { // On regarde tous les chemins en cours
                ba = 0;
                if (c.getRef2() == ch.getRef1() || c.getRef2() == ch.getRef2()) { // Si l'arrivée du chemin traitée (c) 
                    //correspond à la ville de départ OU d'arrivée de tous les chemins en cours (ch) 
                    ba = 1; // Indique que le chemin est emprunté pour regarder les autres chemins voisins
                    break; // On sort de la boucle pour éviter une boucle infinie
                }
            }
            if (ba == 0) { // Si le chemin n'a pas été emprunté
                lec.add(c); // On ajoute le chemin à la liste des chemins en cours
                if (c.getRef2() == b.getRef()) { // Si ville d'arrivée du chemin traité = ville d'arrivée souhaitée
                    if (lf.isEmpty()) { // Si on a pas de chemins finaux
                        lf = copie(lec); // On ajoute ce chemin
                    } else {if (getWeight(lf, arg) > getWeight(lec, arg)) { // On compare les "poids" donc soit 
                        //le temps soit la distance des différents chemins finaux possibles
                            // getWeight est une méthode decrite plus tard
                            lf = copie(lec); // On prend le chemin dont le temps ou la distance est le plus "petit"
                        }
                    }
                } 
                else {cheminPlusCourt(getLieu(c), b); // Appel récursif à partir de la ville d'arrivée du chemin traité 
                    //si on a pas atteint la ville souhaitée
                }
                lec.remove(c); // On retire le chemin choisi parmi les chemins en cours pour éviter de repasser dessus
            }
        }
        km = getWeight(lf, true); // Permet de récuperer la valeur de la distance du chemin final
        min = getWeight(lf, false); // permet de récupérer la valeur de la durée du chemin final
    }

    public ArrayList<Chemin> cheminVoisin(Lieu l) {
        
        ArrayList<Chemin> lcv = new ArrayList(); // Liste des chemins voisins
        for (Chemin c : carte.LesChemins) {
            if (l.getRef() == c.getRef1()) { // On forme la liste des chemins voisins,
                //tous les chemins qui partent de se lieu  
                lcv.add(c);
            }
        }
        return lcv;
    }

    public int getWeight(ArrayList<Chemin> lc, boolean arg) {
        int n = 0;
        for (Chemin c : lc) {
            if (arg) {
                n = n + c.getKm(); // On ajoute la distance successive de chacun des chemins 
            } else {
                n = n + c.getMin();// On ajoute la durée successive de chacun des chemins
            } 
        }
        return n;
    }

    public Lieu getLieu(Chemin c) {
        Lieu lf = new Lieu(0, 0, 0); // Initialisation
        for (Lieu l : carte.LesLieuxDeFrance) {
            if (c.getRef2() == l.getRef()) {
                lf = l; // On regarde la ville d'arrivée et on récupère son lieu
                // Permet l'appel récursif du chemin le plus court
            }
        }
        return lf;
    }

    public ArrayList<Chemin> copie (ArrayList<Chemin> lc) {
        ArrayList<Chemin> c = new ArrayList(); // On initialise notre liste de chemins
        for (Chemin ch : lc) {
            c.add(ch);
        } // On crée une copie de nos chemins
        return c;
    } // Permet de ne pas avoir de problèmes d'adresses mémoires

}



