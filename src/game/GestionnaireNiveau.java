/*
 CLASSE DE GESTION DES NIVEAUX PERMETTANT DE DONNER LE NIVEAU SOUHAITE
 */

package game;
import org.newdawn.slick.SlickException;

public class GestionnaireNiveau {
	//23 niveaux : 6 faciles, 6 medium, 6 hard, maxi 5 perso
	private Niveau[] niveaux = new Niveau[23];

	public GestionnaireNiveau() throws SlickException {
		for(int i=0;i<niveaux.length;i++) {
			niveaux[i] = new Niveau(i+1);
			if(i < 6) { 
				niveaux[i].setCategorie("easy"); 
			}
			else if(i < 12) {
				niveaux[i].setCategorie("medium");
			}
			else if(i < 18) { 
				niveaux[i].setCategorie("hard");
			}
			else if (i< 23){
				niveaux[i].setCategorie("perso");
			}
		}
	}

	//obtenir le niveau choisi par l'utilisateur
	public Niveau niveauActuel(int niveau) throws SlickException {
		
		//indice du niveau actuel + streak si on appuyait sur next
		int indiceNiveau = niveau+ Niveau.getStreak(); 
		
		//reset le niveau si il était déjà fini
		if(niveaux[indiceNiveau].niveauFini()) {
			niveaux[indiceNiveau].resetNiveau();
		}
		if(indiceNiveau < 23)
			return niveaux[indiceNiveau];
		else {
			System.out.println("Dernier niveau atteint !");
			return null;
		}
		
		
	}

	public Niveau[] getNiveaux() {
		return niveaux;
	}

	public void setUnNiveau(int indice, Niveau n, String categorie) {
		if(indice < niveaux.length && n!= null) {
			n.setCategorie(categorie);
			niveaux[indice] = n;
		}
	}
	
}
