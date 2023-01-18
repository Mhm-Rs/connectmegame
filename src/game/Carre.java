/*
 CLASSE DE REPRESENTATION D'UN ELEMENT CARRE DANS LE PLATEAU DE JEU
 */

package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Carre {
	private int ligne; // entre 0 et 5

	private int colonne; // entre 0 et 5

	private int type; // 0 immobile - 1 mobile - 2 tourne - 3 mobiletourne - 4 mobilehorizon - 5
						// mobileverti - 6 mobiletournehori - 7 mobiletourneverti

	private Image img; // image en fonction du type
	private int[] branches; // tableau des branches, 0 branche du haut, 1 branche de droite, 2 branche du
							// bas, 3 branches de gauche

	private int disposition; // décalage à respecter en fonction du plateau pour dessiner
	// plateau 4x4 = 4 , 5x5 = 5 , 6x6 = 6

	private boolean transition = false; // pour animation de transition
	private float tempsTransition = 0;

	Image brancheVerti; // rectangles représentant les branches
	Image brancheHori;

	public Carre() throws SlickException {
		ligne = 2;
		colonne = 1;
		type = 1;
		img = new Image("images/mobile.png");
		branches = new int[4];
		for (int i = 0; i < 4; i++) {
			branches[i] = 4;
		}
		brancheVerti = new Image("images/rectangle.png");

		brancheHori = new Image("images/rectangle.png");
		brancheHori.rotate(90);
	}

	public Carre(int uLigne, int uColonne, int uType, int[] uBranches, int uDispo) throws SlickException {
		this();

		ligne = uLigne;

		colonne = uColonne;

		if (uType >= 0 && uType < 8) {
			type = uType;
		}

		String[] nomImages = { "immobiletest", "mobiletest", "tournetest", "mobiletournetest", "mobilehorizontest",
				"mobilevertitest", "mobiletournehoritest", "mobiletournevertitest" };

		String cheminImageChoisie = "images/" + nomImages[type] + ".png";
		img = new Image(cheminImageChoisie); // affecter image en ft° du type

		branches = uBranches;

		if (uDispo >= 4 && uDispo < 7) { // disposition dans le plateau en fonction de la taille
			disposition = uDispo;
		}
	}

	public void dessiner(Graphics g) {

		transitionTourner(); // gérer transition

		// dessiner le carré sans ses branches (placer l'image au bon endroit)

		// pour se placer en haut à gauche du carré
		int decColonnePlateau = disposition == 4 ? Plateau.getDecalagesColonne()[0]
				: disposition == 5 ? Plateau.getDecalagesColonne()[1] : Plateau.getDecalagesColonne()[2];

		int decLignePlateau = disposition == 4 ? Plateau.getDecalagesLigne()[0]
				: disposition == 5 ? Plateau.getDecalagesLigne()[1] : Plateau.getDecalagesLigne()[2];

		// 120 : taille du carré derrière et 5 : espace entre deux carrés , 8 décalage
		int decColonne = decColonnePlateau + 120 * colonne + 5 * colonne + 8;

		int decLigne = decLignePlateau + 120 * ligne + 5 * ligne + 8;

		g.drawImage(img, decColonne, decLigne);

		g.setColor(Color.white);

		int longueurBranche = 8;
		int largeurBranche = 14;
		// dessiner les branches du carré : des rectangles de 8x14

		// branches supérieure

		switch (branches[0]) {

		case 1: // 1 branche - la placer au centre
			g.drawImage(brancheVerti, decColonne + 45, decLigne - largeurBranche);
			break;
		case 2: // 2 branches - les placer au centre avec distance entre les deux
			for (int i = 0; i < 2; i++) {
				g.drawImage(brancheVerti, decColonne + 18 + 18 * (i + 1), decLigne - largeurBranche);
			}
			break;
		case 3:
			for (int k = 0; k < 3; k++) {
				g.drawImage(brancheVerti, decColonne + 28 + (18 * k), decLigne - largeurBranche);
			}
			break;
		case 4:
			for (int k = 0; k < 4; k++) {
				g.drawImage(brancheVerti, decColonne + 18 + (18 * k), decLigne - largeurBranche);
			}
			break;
		}

		// branches inférieure

		switch (branches[2]) {

		case 1: // 1 branche - la placer au centre
			g.drawImage(brancheVerti, decColonne + 45, decLigne - largeurBranche + 115);
			break;
		case 2: // 2 branches - les placer au centre avec distance entre les deux
			for (int i = 0; i < 2; i++) {
				g.drawImage(brancheVerti, decColonne + 18 + 18 * (i + 1), decLigne - largeurBranche + 115);
			}
			break;
		case 3:
			for (int k = 0; k < 3; k++) {
				g.drawImage(brancheVerti, decColonne + 28 + (18 * k), decLigne - largeurBranche + 115);
			}
			break;
		case 4:
			for (int k = 0; k < 4; k++) {
				g.drawImage(brancheVerti, decColonne + 18 + (18 * k), decLigne - largeurBranche + 115);
			}
			break;
		}

		// branches de droite

		switch (branches[1]) {

		case 1: // 1 branche - la placer au centre
			g.drawImage(brancheHori, decColonne + 103, decLigne - longueurBranche + 50);
			break;
		case 2: // 2 branches - les placer au centre avec distance entre les deux
			for (int i = 0; i < 2; i++) {
				g.drawImage(brancheHori, decColonne + 103, decLigne - longueurBranche + 25 + 18 * (i + 1));
			}
			break;
		case 3:
			for (int k = 0; k < 3; k++) {
				g.drawImage(brancheHori, decColonne + 103, decLigne - longueurBranche + 35 + (18 * k));
			}
			break;
		case 4:
			for (int k = 0; k < 4; k++) {
				g.drawImage(brancheHori, decColonne + 103, decLigne - longueurBranche + 25 + (18 * k));
			}
			break;
		}

		// branches de gauche

		switch (branches[3]) {

		case 1: // 1 branche - la placer au centre
			g.drawImage(brancheHori, decColonne - 12, decLigne - longueurBranche + 50);
			break;
		case 2: // 2 branches - les placer au centre avec distance entre les deux
			for (int i = 0; i < 2; i++) {
				g.drawImage(brancheHori, decColonne - 12, decLigne - longueurBranche + 25 + 18 * (i + 1));
			}
			break;
		case 3:
			for (int k = 0; k < 3; k++) {
				g.drawImage(brancheHori, decColonne - 12, decLigne - longueurBranche + 35 + (18 * k));
			}
			break;
		case 4:
			for (int k = 0; k < 4; k++) {
				g.drawImage(brancheHori, decColonne - 12, decLigne - longueurBranche + 25 + (18 * k));
			}
			break;
		}

	}

	public Image getImg() {
		return img;
	}

	public int[] getBranches() {
		return branches;
	}

	public void setBranches(int[] branches) {
		this.branches = branches;
	}

	// faire une rotation des branches
	public void tournerBranches() {
		if (type == 2 || type == 3 || type == 6 || type == 7) {
			this.setTransition(true);
			this.setTempsTransition(0);
			int[] newBranches = { branches[3], branches[0], branches[1], branches[2] };
			branches = newBranches;
		}
	}

	public int getType() {
		return type;
	}

	public int getLigne() {
		return ligne;
	}

	public int getColonne() {
		return colonne;
	}

	public void setLigne(int ligne) {
		this.ligne = ligne;
	}

	public void setColonne(int colonne) {
		this.colonne = colonne;
	}

	// animation de carré qui tourne au click
	public void transitionTourner() {
		if (transition && tempsTransition == 0) {
			img.rotate(15);

		}

		if (tempsTransition >= 0.01 && tempsTransition < 0.02 && transition) {
			img.rotate(15);

		}

		if (tempsTransition >= 0.03 && tempsTransition < 0.04 && transition) {
			img.rotate(15);

		}

		if (tempsTransition >= 0.05 && tempsTransition < 0.06 && transition) {
			img.rotate(15);

		}

		if (tempsTransition >= 0.07 && tempsTransition < 0.08 && transition) {
			img.rotate(15);

		}

		if (tempsTransition >= 0.09 && tempsTransition < 0.10 && transition) {
			img.rotate(15);

			tempsTransition = 0;
			transition = false;
		}

		tempsTransition += 0.01f;

	}

	public void setTransition(boolean b) {
		transition = b;
	}

	public void setTempsTransition(float f) {
		tempsTransition = f;
	}

}
