package projet;


import javafx.application.Application;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Louis
 */
public class Main extends Application{
    
    public static void main(String[] args) {
        launch(); //obligatoire pour appeler une interface graphique ==> va appeler la méthode Start
     }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Fenetre fen = new Fenetre(primaryStage); // Permet de créer la fenêtre à l'exécution
        
        /* Carte carte = new Carte(System.getProperty("user.dir") + "/itineraire"); 
        // pour lire la "carte" composée des données du fichier "itineraire"
        */
    }
    
    
}
