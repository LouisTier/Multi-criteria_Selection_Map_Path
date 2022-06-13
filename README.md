# Multi-criteria_Selection_Map_Path

## **1. About this project**

Everything is explained in the pdf file named "**ProjectReport**". However, as it is written in French, this README file will explain the main points in English.
Let's start with a presentation of the different files:

We have created 5 classes: *Lieu* (Location), *Ville* (City), *Chemin* (Path), *Carte* (Map) et *Fenêtre* (Window). 
The first three are quite simple and simply define what is a place, a city, or a path. 
The *window* class corresponds to our **graphical interface** and it allows you to **define the different methods for choosing your route**, while our *map* class corresponds to the class that allows us to **read the chosen map, check that the reading is good, and create and place points**. 
A path is characterized by **different attributes**, namely: 
  - The starting point 
  - The distance and time 
  - Wether or not there is a toll booth 
  - Whether or not it's a one-way street 
**All cities are places, but not all places are not cities**, so *City* inherits from *Location* and is therefore characterized by its reference, its coordinates, and its name, whereas a place has no name! 

We also have the "**Maps**" folder with **examples of maps to use for our application**.

In everyday life, we are always faced with choices of paths to get from point A to point B. Our final decision is guided by various reasons: 
  - wanting to take our time and go by a beautiful green path
  - to take the shortest route even if it means passing through tolls
  - to take the fastest route even if the consumption will probably be more important
  - Many other examples... 
Finally, there are many routes available, and as the old saying goes: "All roads lead to Rome". 
So, our problem is, **which route to follow to get from one point to another among the proposed paths, cities, and places?**

This project allowed us to become familiar with different problems and to get interested in already known works such as: 
- **The shortest path**
- **The nearest neighbors** 
- **The Dijkstra algorithm**
- **The A* algorithm**

This project was done in a school context with two other classmates. The scope and the subject were free.

## **2. Class diagram**

To better **visualize the methods, attributes and relationships between our 5 classes**, here is the class diagram of our project.
![image](https://user-images.githubusercontent.com/105392989/173350728-de02c8be-72db-4ad7-aa9b-6d79a14798e1.png)

## **3. Some main algorithms**

  **1. Determine the shortest/fastest path**
  
  ```java
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
  ```
  
  **2. Find neighboring paths from a location**
  
  ```java
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
  ```
  
  **3. Create a map with places, towns and roads**
  
  ```java
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
  ```

## **4. Results**

Here are some results obtained after using our application.

  **1. Example Map**
![image](https://user-images.githubusercontent.com/105392989/173352385-53a9ba74-94a2-4209-8c25-bc90d75c06b8.png)

  **2. Occitanie Map** (South of France) 
![image](https://user-images.githubusercontent.com/105392989/173352249-0e8afdea-3dea-4a0c-9053-2a42a0609520.png)

  **3. France Map** 
![image](https://user-images.githubusercontent.com/105392989/173351876-5f9814b0-d1ff-4595-b90b-dbbd327b7ed6.png)

## **Instructions for use**

1. Use ctrl+E to open your "map" text file. Your map is displayed.
2. Enter the starting and ending cities of your route.
3. Choose from the drop-down menu if you accept the presence of tolls on your route.
4. Choose from the drop-down menu if you want to take the shortest route (in terms of distance) or fastest (in terms of time).
5. Press the "Calculer" (Calculate) button.
6. See the most interesting route in red, adapted to your constraints.
7. View in the top left corner of the interface the duration and the mileage of the trip.
8. You can change all the data entered as many times as desired.
9. Use ctrl+Q to close the graphical interface

**Have a nice trip!**
