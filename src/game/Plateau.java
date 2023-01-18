/*
 CLASSE DE REPRESENTATION DU PLATEAU DE JEU
 */

package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Plateau {
	private Image fond; //image background
	private Carre carres[][];
	private Carre carreSelectionne; // avoir le carré sur lequel on vient de cliquer
	private int max_ligne = 4;
	private int max_colonne = 4;
	private int decColonne = 0; // décalage pour se retrouver dans le plateau
	private int decLigne = 0;
	private static final int[] DECALAGES_COLONNE = { 260, 190, 120 };
	private static final int[] DECALAGES_LIGNE = { 160, 90, 20 }; // les décalages utilisés 

	public Plateau() throws SlickException {
		fond = new Image("images/background.png"); // image de fond

		carreSelectionne = null;

		carres = new Carre[8][8];

		decColonne = DECALAGES_COLONNE[0];

		decLigne = DECALAGES_LIGNE[0];

	}

	public Plateau(int nbLi, int nbCol) throws SlickException {
		this();
		max_ligne = nbLi;

		max_colonne = nbCol;

		decColonne = nbCol == 5 ? DECALAGES_COLONNE[1] : DECALAGES_COLONNE[2];

		decLigne = nbLi == 5 ? DECALAGES_LIGNE[1] : DECALAGES_LIGNE[2];

	}

	public void setCarres(Carre[][] carres) {
		this.carres = carres;
	}

	public void setUnCarre(int ligne, int colonne, Carre carre) {
		this.carres[ligne][colonne] = carre;
	}

	public void dessinerPlateau(Graphics g) {
		g.drawImage(fond, 0, 0); //dessiner background
		g.setColor(new Color(110, 117, 168)); // couleur "violette"
		for (int i = 0; i < max_ligne; i++) {
			for (int j = 0; j < max_colonne; j++) { // création des carrés de placement
				g.fillRect(decColonne + 120 * j + 5 * j, decLigne + 120 * i + 5 * i, 120, 120); 
			}
		}
	}

	public void dessinerCarres(Graphics g)  {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (carres[i][j] != null) {
					carres[i][j].dessiner(g); // dessiner les carrés à partir de la matrice
				}
			}
		}
	}

	// obtenir ligne et colonne du carré dans l'espace de jeu
	private int[] quelCarre(int x, int y) {
		int coo[] = { (y - decLigne) / 120, (x - decColonne) / 120 };
		return coo;
	}

	// permet de faire tourner un carré lorsqu'on clique dessus et qu'il est de type
	// mobile
	public void rotation(int x, int y) {
		int carre[] = quelCarre(x, y);

		int bordColonne = decColonne + max_colonne * 120 + 14;
		int bordLigne = decLigne + decLigne * 120 + 14;

		if (x >= decColonne && x < bordColonne && y >= decLigne && y < bordLigne && carre[0] < max_ligne
				&& carre[1] < max_colonne) { // si on a cliqué dans le plateau de
			// jeu
			Carre carreClique = carres[carre[0]][carre[1]];
			if (carreClique != null) {
				carreClique.tournerBranches(); // on récupère le carré cliqué et on fait tourner les branches
			}
		}
	}

	
	// permet de déplacer un carré lorsqu'on clique dessus puis sur une case vide
	public void deplacement(int x, int y) throws SlickException {
		int carre[] = quelCarre(x, y);

		int bordColonne = decColonne + max_colonne * 120 + 14;
		int bordLigne = decLigne + decLigne * 120 + 14;

		if (x >= decColonne && x < bordColonne && y >= decLigne && y < bordLigne) { // si on a cliqué dans le plateau de
																					// jeu

			if (carreSelectionne == null && carre[0] < max_ligne && carre[1] < max_colonne
					&& carres[carre[0]][carre[1]] != null
					&& (carres[carre[0]][carre[1]].getType() == 1 || carres[carre[0]][carre[1]].getType() >= 3)) {

				// si un carré mobile est sélectionné, on le récupère
				carreSelectionne = carres[carre[0]][carre[1]];
				carres[carre[0]][carre[1]] = null; // on fait disparaître l'endroit où il était

			} else if (carreSelectionne != null && carre[0] < max_ligne && carre[1] < max_colonne
					&& carres[carre[0]][carre[1]] == null) {
				// si un carré est sélectionné, déplacer le carré sélectionné à l'endroit du
				// clic si la zone est vide en fonction de la possibilité
				if (carreSelectionne.getType() == 1 || carreSelectionne.getType() == 3
						|| (carreSelectionne.getType() == 4 && carre[0] == carreSelectionne.getLigne())
						|| (carreSelectionne.getType() == 5 && carre[1] == carreSelectionne.getColonne())
						|| (carreSelectionne.getType() == 6 && carre[0] == carreSelectionne.getLigne())
						|| (carreSelectionne.getType() == 7 && carre[1] == carreSelectionne.getColonne())) {
					carres[carre[0]][carre[1]] = new Carre(carre[0], carre[1], carreSelectionne.getType(),
							carreSelectionne.getBranches(), max_ligne);
					// on crée un carré à l'endroit cliqué, puis on supprime le carré où il était

					carreSelectionne = null; // on libère le carré sélectionné
				}

			}
		}
	}
	

	public void deplacementMaker(int x, int y) throws SlickException {
		int carre[] = quelCarre(x, y);

			if (carreSelectionne == null && carre[0] >= 0 && carre[1] >= 0 && carres[carre[0]][carre[1]] != null) {
				// si un carré est sélectionné, on le récupère
//				carreSelectionne = carres[carre[0]][carre[1]];
				carreSelectionne = new Carre(carre[0],carre[1],carres[carre[0]][carre[1]].getType(),carres[carre[0]][carre[1]].getBranches(),max_ligne);
				if(carre[0] < max_ligne && carre[1] < max_colonne) {
					carres[carre[0]][carre[1]] = null;
				}

			} else if (carreSelectionne != null && carre[0] < max_ligne && carre[1] < max_colonne && carre[0] >= 0 && carre[1] >=0
					&& carres[carre[0]][carre[1]] == null) {
				// si un carré est sélectionné, déplacer le carré sélectionné à l'endroit du
				// clic si la zone est vide en fonction de la possibilité

				carres[carre[0]][carre[1]] = new Carre(carre[0], carre[1], carreSelectionne.getType(),
						carreSelectionne.getBranches(), max_ligne);
				// on crée un carré à l'endroit cliqué, puis on supprime le carré où il était

				carreSelectionne = null; // on libère le carré sélectionné

			}

	}

	public Carre[][] getCarres() {
		return carres;
	}

	public boolean partieGagnee() {
		boolean win = true;
		int checkLigne = 0;
		int checkColonne = 0;
		for (int i = 0; i < max_ligne; i++) {
			for (int j = 0; j < max_colonne; j++) {
				if (carres[i][j] != null) {
					// regarder si le carré en haut correspond :
					checkLigne = i - 1;
					checkColonne = j;
					if (checkLigne >= 0) { // si le carré au dessus de notre carré
						if (carres[checkLigne][checkColonne] != null // n'a pas des branches du bas égales à nos
																		// branches du haut
								&& carres[checkLigne][checkColonne].getBranches()[2] != carres[i][j].getBranches()[0]) {
							win = false;
							break;
						}
						// s'il n'y a pas de carré en haut, mais que les branches du haut de notre carré
						// ne sont pas nulles, on a pas gagné
						if (carres[checkLigne][checkColonne] == null && carres[i][j].getBranches()[0] != 0) {
							win = false;
							break;
						}
					}

					// regarder si le carré en bas correspond :
					checkLigne = i + 1;
					checkColonne = j;
					if (checkLigne <= max_colonne-1) { // si le carré en dessous de notre carré
						if (carres[checkLigne][checkColonne] != null // n'a pas des branches du haut égales à nos
																		// branches du bas
								&& carres[checkLigne][checkColonne].getBranches()[0] != carres[i][j].getBranches()[2]) {
							win = false;
							break;
						}
						// s'il n'y a pas de carré en bas, mais que les branches du bas de notre carré
						// ne sont pas nulles, on a pas gagné
						if (carres[checkLigne][checkColonne] == null && carres[i][j].getBranches()[2] != 0) {
							win = false;
							break;
						}
					}

					// regarder si le carré a droite correspond :
					checkLigne = i;
					checkColonne = j + 1;
					if (checkColonne <= max_colonne-1) { // si le carré a droite de notre carré
						if (carres[checkLigne][checkColonne] != null // n'a pas des branches de gauche égales à nos
																		// branches de droite
								&& carres[checkLigne][checkColonne].getBranches()[3] != carres[i][j].getBranches()[1]) {
							win = false;
							break;
						}
						// s'il n'y a pas de carré a droite, mais que les branches de droite de notre
						// carré
						// ne sont pas nulles, on a pas gagné
						if (carres[checkLigne][checkColonne] == null && carres[i][j].getBranches()[1] != 0) {
							win = false;
							break;
						}
					}

					// regarder si le carré a gauche correspond :
					checkLigne = i;
					checkColonne = j - 1;
					if (checkColonne >= 0) { // si le carré a gauche de notre carré
						if (carres[checkLigne][checkColonne] != null // n'a pas des branches de droite égales à nos
																		// branches de gauche
								&& carres[checkLigne][checkColonne].getBranches()[1] != carres[i][j].getBranches()[3]) {
							win = false;
							break;
						}
						// s'il n'y a pas de carré a gauche, mais que les branches de gauche de notre
						// carré
						// ne sont pas nulles, on a pas gagné
						if (carres[checkLigne][checkColonne] == null && carres[i][j].getBranches()[3] != 0) {
							win = false;
							break;
						}
					}
				}
			}
			if (!win) {
				break;
			}
		}

		return win;
	}

	public Carre getCarreSelectionne() {
		return carreSelectionne;
	}

	// dessiner un carré sélectionné là où se trouve la souris, similaire à dessiner de la classe Carré
	public void dessinerCarreSelectionne(int x, int y, Graphics g) {
		if (carreSelectionne != null) {
			g.drawImage(carreSelectionne.getImg(), x - 50, y - 50);

			g.setColor(Color.white);
			// dessiner les branches du carré : des rectangles de 8x15

			// branches supérieure

			int branches[] = { carreSelectionne.getBranches()[0], carreSelectionne.getBranches()[1],
					carreSelectionne.getBranches()[2], carreSelectionne.getBranches()[3] };

			int decColonne = x - 50;

			int decLigne = y - 50;
			switch (branches[0]) {

			case 1: // 1 branche - la placer au centre
				g.fillRect(decColonne + 45, decLigne - 15, 8, 15);
				break;
			case 2: // 2 branches - les placer au centre avec distance entre les deux
				g.fillRect(decColonne + 18 + 18, decLigne - 15, 8, 15);
				g.fillRect(decColonne + 18 + 18 * 2, decLigne - 15, 8, 15);
				break;
			case 3:
				for (int k = 0; k < 3; k++) {
					g.fillRect(decColonne + 28 + (18 * k), decLigne - 15, 8, 15);
				}
				break;
			case 4:
				for (int k = 0; k < 4; k++) {
					g.fillRect(decColonne + 18 + (18 * k), decLigne - 15, 8, 15);
				}
				break;
			}

			// branches inférieure

			switch (branches[2]) {

			case 1: // 1 branche - la placer au centre
				g.fillRect(decColonne + 45, decLigne - 15 + 115, 8, 15);
				break;
			case 2: // 2 branches - les placer au centre avec distance entre les deux
				g.fillRect(decColonne + 18 + 18, decLigne - 15 + 115, 8, 15);
				g.fillRect(decColonne + 18 + 18 * 2, decLigne - 15 + 115, 8, 15);
				break;
			case 3:
				for (int k = 0; k < 3; k++) {
					g.fillRect(decColonne + 28 + (18 * k), decLigne - 15 + 115, 8, 15);
				}
				break;
			case 4:
				for (int k = 0; k < 4; k++) {
					g.fillRect(decColonne + 18 + (18 * k), decLigne - 15 + 115, 8, 15);
				}
				break;
			}

			// branches de droite

			switch (branches[1]) {

			case 1: // 1 branche - la placer au centre
				g.fillRect(decColonne + 100, decLigne - 8 + 50, 15, 8);
				break;
			case 2: // 2 branches - les placer au centre avec distance entre les deux
				g.fillRect(decColonne + 100, decLigne - 8 + 25 + 18, 15, 8);
				g.fillRect(decColonne + 100, decLigne - 8 + 25 + 18 * 2, 15, 8);
				break;
			case 3:
				for (int k = 0; k < 3; k++) {
					g.fillRect(decColonne + 100, decLigne - 8 + 35 + (18 * k), 15, 8);
				}
				break;
			case 4:
				for (int k = 0; k < 4; k++) {
					g.fillRect(decColonne + 100, decLigne - 8 + 25 + (18 * k), 15, 8);
				}
				break;
			}

			// branches de gauche

			switch (branches[3]) {

			case 1: // 1 branche - la placer au centre
				g.fillRect(decColonne - 15, decLigne - 8 + 50, 15, 8);
				break;
			case 2: // 2 branches - les placer au centre avec distance entre les deux
				g.fillRect(decColonne - 15, decLigne - 8 + 25 + 18, 15, 8);
				g.fillRect(decColonne - 15, decLigne - 8 + 25 + 18 * 2, 15, 8);
				break;
			case 3:
				for (int k = 0; k < 3; k++) {
					g.fillRect(decColonne - 15, decLigne - 8 + 35 + (18 * k), 15, 8);
				}
				break;
			case 4:
				for (int k = 0; k < 4; k++) {
					g.fillRect(decColonne - 15, decLigne - 8 + 25 + (18 * k), 15, 8);
				}
				break;
			}
		}
	}

	//augmenter le nombre de branches dans une direction
	public void augmenterBranches(int indice) {
		if (carreSelectionne != null) {
			int[] branches = new int[4];
			for (int i = 0; i < 4; i++) {
				if(i == indice && carreSelectionne.getBranches()[i] < 4) {
						branches[i] = carreSelectionne.getBranches()[i] + 1;
				}
				else {
					branches[i] = carreSelectionne.getBranches()[i];
				}
					
			}
			
			carreSelectionne.setBranches(branches);
		}
	}
	
	//diminuer le nombre de branches dans une direction
	public void diminuerBranches(int indice) {
		if (carreSelectionne != null) {
			int[] branches = new int[4];
			for (int i = 0; i < 4; i++) {
				if(i == indice && carreSelectionne.getBranches()[i] >= 0) {
						branches[i] = carreSelectionne.getBranches()[i] - 1;
				}
				else {
					branches[i] = carreSelectionne.getBranches()[i];
				}
					
			}
			
			carreSelectionne.setBranches(branches);
		}
	}
	
	public int getMaxLigne() {
		return max_ligne;
	}
	
	public int getMaxColonne() {
		return max_colonne;
	}
	
	public void setCarreSelectionne(Carre c) {
		carreSelectionne = c;
	}
	
	//vérifier si le plateau contient uniquement des carrés ayant au moins 1 branche dans une direction
	public boolean carresValides() {
		boolean valide = true;
		for(int i=0;i<max_ligne;i++) {
			for(int j=0;j<max_colonne;j++) {
				if(carres[i][j]!=null) {
					boolean branchesVides = true;
					for(int k=0;k<4;k++) {
						if(carres[i][j].getBranches()[k] != 0) {
							branchesVides = false;
						}
					}
					if(branchesVides) {
						valide = false;
						break;
					}
				}
			}
		}
		return valide;
	}

	public static int[] getDecalagesColonne() {
		return DECALAGES_COLONNE;
	}

	public static int[] getDecalagesLigne() {
		return DECALAGES_LIGNE;
	}

}
