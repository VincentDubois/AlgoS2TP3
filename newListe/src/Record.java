import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JFrame;

////////
// TP Enregistrements
//
// L'objectif de ce TP est d'appliquer ce qui a été vu en TD sur les
// enregistrements pour réaliser un petit annuaire.
// L'interface graphique est déjà fournie (fichier Directory.java), ainsi que le code
// permettant d'accéder à la mémoire (Explorer.java) et la bibliothèque
// de fonctions servant à afficher le résultat sous forme de boites et de flèches
// (canvas.jar). Si eclipse vous indique des erreurs dans les fichiers Directory.java
// et Explorer.java, c'est qu'il faut configurer le projet (clic droit
// -> propriétés -> chemin de compilation -> bibliothèques -> ajouter des fichiers .jar
// -- sélectionnez canvas.jar et validez.)
// Les fichiers Directory.java et Explorer.java ne doivent pas être modifiés.
//
// En revanche, vous allez être amenés à modifier
// ce fichier ( Record.java ) pour modifier la structure Personne définie 
// ci-dessous et compléter un certain nombre de fonctions.
//
// Avant toutes choses, Lancez le programme
// et parcourez la liste ( boutons debut et suivant).
// La partie supérieure de la denêtre représente l'application. La partie inférieure
// de la fenêtre est utilisée pour afficher le contenu de la mémoire. L'affichage de
// la mémoire peut être désactivé dans le main, pour n'afficher que l'application.
//
// Il reste ensuite à compléter les fonctions élémentaires (suppression, recherche).
// L'appel de ces fonctions est déclenché par les boutons de l'interface. 
// Une fois ces tâches réalisées, vous modifiez ce fichier pour :
// - gérer une liste permettant de revenir en arrière (suivant/précédent)
// - gérer une liste triée par ordre alphabétique
//
//
// Remarque : 
// * Les fonctions sont déjà crées, mais ne font pour la plupart pas la tâche demandée.
//   La signature des fonctions doit être conservée telle quelle ( les fonctions sont appelées
//   par l'interface graphique, dans le fichier Record.java), même si certains paramètres ne
//   sont pas utilisés par votre code.

public class Record {
	
	
	// Déclaration d'un enregistrement Personne
	class Personne {
		String nom;
		String prenom;
		// Ajouter ici d'autres champs de type String (ex : prenom, tel, email etc).
		// Relancer l'application pour observer les effets.
		String tel;
		
		Personne suivant;
	}
	
    // Fonction retournant l'enregistrement suivant p dans la liste liste
	Personne suivant(Personne p){
		return p.suivant;
	}
	
	// Ajout d'une personne p dans une liste
	Personne ajoute(Personne liste, Personne p){
		// L'enregistrement à ajouter est déjà fourni (p)
		// En l'état, la liste n'est pas modifiée : à vous de réaliser l'ajout !

		return liste;
	}

	// Suppression d'une personne p dans une liste
	Personne supprime(Personne liste, Personne p){
		// Cette ne fonction ne fait rien. A vous de la compléter pour
		// retourner la nouvelle tête de liste après avoir supprimé l'élément p
		// de la liste
		
		return liste;
	}
	
	// Recherche d'un élément dans la liste ayant le même nom que l'élément p.
	Personne recherche(Personne liste, Personne p){
        // Cette ne fonction ne fait rien. A vous de la compléter !!!
		// Normalement, la Personne retournée doit avoir même nom que p 

		return liste ;
	}

    // Fonction retournant l'enregistrement suivant p dans la liste liste
	Personne precedent(Personne liste, Personne p) {	
		// Pour écrire cette fonction, il faudra probalement modifier l'enregistrement
		// Personne et la plupart des fonctions ci-dessus
		return p;
	}
	
	public static void main(String[] args) {	
		// Lance l'application
		Directory.start(true); // paramètre : affichage de la mémoire.
		// Créer des enregistrements aléatoires pour tester plus facilement le fonctionnement
		Directory.fillRandom(3);
	}
}
