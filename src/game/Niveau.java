/*
 CLASSE DE REPRESENTATION D'UN NIVEAU
 */

package game;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;

public class Niveau {
	private Plateau p; // plateau de chaque niveau
	private String categorie;
	private int indice;

	private boolean isFini = false; // vérifier si un niveau est fini
	private boolean jouerMusique = true; // pour jouer de la musique au début de chaque nouveau niveau
	private boolean jouerSon = true; // jouer le jingle à la fin d'un niveau
	private boolean hoveringHome = false; // hover sur le bouton home
	private boolean hoveringNext = false; // hover sur le bouton next
	private boolean showVictory = false; // affichage différent lorsque partie terminée

	// images de l'écran de victoire:
	private Image next;
	private Image home;
	private Image homeHover;
	private Image nextHover;
	private Image etoile;

	// pour assombrir écran de victoire
	private float opacity = 0;

	// son de l'écran de victoire, et musique du niveau
	private Sound victoire;
	private Music musique;
	private boolean goBack = false;

	// polices d'écriture
	private TrueTypeFont ttf;
	private TrueTypeFont icons;

	// streak de niveau (si on appuie sur next)
	private static int streak = 0;

	private int nbreCoups = 0;

	private int nbreEtoiles = -1;

	public Niveau(int indice) throws SlickException {

		victoire = new Sound("sounds/niveaufini.ogg");
		musique = new Music("sounds/jeu.ogg");
		home = new Image("images/home.png");
		homeHover = new Image("images/homehover.png");
		next = new Image("images/nextlevel.png");
		nextHover = new Image("images/nexthover.png");
		etoile = new Image("images/star.png");
		this.indice = indice;

		Font font = new Font("Kristen ITC", Font.BOLD, 48);
		ttf = new TrueTypeFont(font, true); // police d'écriture texte

		Font fontIcon = new Font("Kristen ITC", Font.BOLD, 32);
		icons = new TrueTypeFont(fontIcon, true);

		switch (indice) {
		case 1:
			// REPRODUCTION NIVEAU 15
		case 2:
			// REPRODUCTION NIVEAU 25
		case 3:
			// REPRODUCTION NIVEAU 30
		case 4:
			// REPRODUCTION NIVEAU 40
		case 5:
			// REPRODUCTION NIVEAU 45
		case 6:
			// REPRODUCTION NIVEAU 47
		case 7:
			// REPRODUCTION NIVEAU AVANCE 4
		case 8:
			// REPRODUCTION NIVEAU AVANCE 14
		case 9:
			// HYBRIDES TO LEVEL 18
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
			// LES NIVEAUX PERSO
		case 20:
		case 21:
		case 22:
		case 23:
			genererNiveau(indice);
			break;
		default:
			p = new Plateau();
			System.out.println("Erreur ! Niveau non pris en compte");
		}

	}

	public void renderNiveau(GameContainer gc, Graphics g) throws SlickException {
		Input inp = gc.getInput();

		affichageJeu(inp.getMouseX(), inp.getMouseY(), g);

		ChoixNiveau.afficherHome(hoveringHome, home, homeHover, icons, g, true);

		if (showVictory) {

			// assombrir l'écran
			if (opacity < 0.25) {
				assombrirEcran(gc, g);
			} else {
				afficherEcranFin(gc, g);
			}

		}

	}

	public void updateNiveau(GameContainer gc, int delta) throws SlickException {

		// jouer de la musique au début du niveau, se relance à chaque nouveau niveau
		if (jouerMusique) {
			musique.loop();
			jouerMusique = false;
		}

		// : jouer le jeu tant que la partie n'est pas gagnée

		Input inp = gc.getInput();

		// sortir si on appuie sur l'icone de maison
		handleHome(inp);

		// déplacer un carré
		if (!p.partieGagnee() && inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			p.deplacement(inp.getMouseX(), inp.getMouseY());
			nbreCoups++;
		}

		// tourner un carré
		if (!p.partieGagnee() && inp.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			p.rotation(inp.getMouseX(), inp.getMouseY());
			nbreCoups++;
		}

		// si la partie est finie, si on appuie sur next le niveau suivant se lance

		if (p.partieGagnee()) {

			showVictory = true;
			opacity += 0.01f;

			if (nbreEtoiles == -1) { // comparer score une seule fois
				comparerScore();
			}

			if (jouerSon) { // jouer le jingle une seule fois
				musique.stop();
				victoire.play();
				;
				jouerSon = false;
			}

			handleNext(inp);
		}

	}

	public boolean niveauFini() {
		return isFini;
	}

	public Plateau getPlateau() {
		return p;
	}

	// convertir un tableau de string en un tableau d'entiers
	private int[] convert(String[] elts) {
		int[] converted = new int[elts.length];

		for (int i = 0; i < elts.length; i++) {
			converted[i] = Integer.parseInt(elts[i]);
		}

		return converted;
	}

	public boolean isGoBack() {
		return goBack;
	}

	public void setGoBack(boolean bool) {
		goBack = bool;
	}

	public void setJouerMusique(boolean bool) {
		jouerMusique = bool;
	}

	public void setCategorie(String cat) {
		categorie = cat;
	}

	public String getCategorie() {
		return categorie;
	}

	// gérer le hover et le click sur le bouton home
	public void handleHome(Input inp) {
		if (inp.getMouseX() >= 160 && inp.getMouseX() <= 381 && inp.getMouseY() >= 820 && inp.getMouseY() <= 891) {
			hoveringHome = true;
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				victoire.stop();
				musique.stop();
				goBack = true;
			}
		} else {
			hoveringHome = false;
		}
	}

	// gérer le hover et le click sur le bouton next
	public void handleNext(Input inp) {
		if (inp.getMouseX() >= 620 && inp.getMouseX() <= 841 && inp.getMouseY() >= 820 && inp.getMouseY() <= 891) {
			hoveringNext = true;
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				victoire.stop();
				streak++;
				isFini = true;
			}
		} else {
			hoveringNext = false;
		}
	}

	private void afficherNext(Graphics g) {
		g.drawImage(next, 620, 820);
		if (hoveringNext) {
			g.drawImage(nextHover, 620, 820);
		}
		icons.drawString(660, 832, "NEXT", Color.white);
	}

	public static int getStreak() {
		return streak;
	}

	public static void setStreak(int str) {
		streak = str;
	}

	// créer un niveau à partir de son blueprint correspondant
	private void genererNiveau(int indice) throws SlickException {
		String suiteChemin = "";
		if (indice < 7) {
			suiteChemin = "easy\\easy" + indice + ".txt";
		} else if (indice < 13) {
			suiteChemin = "medium\\medium" + (indice - 6) + ".txt";
		} else if (indice < 19) {
			suiteChemin = "hard\\hard" + (indice - 12) + ".txt";
		} else if (indice < 24) {
			suiteChemin = "perso\\levelperso" + (indice - 18) + ".txt";
		}

		// ouvrir le fichier blueprint correspondant, créer un plateau de la bonne
		// taille, et placer les carrés sur le plateau
		try {
			String path = "C:\\Users\\Mohaman\\Documents\\3IL I1\\WS_POO\\MiniProjetA\\bin\\blueprints\\" + suiteChemin;
			File f = new File(path);
			Scanner reader = new Scanner(f);
			String firstLine = reader.nextLine();
			int taille = Integer.parseInt(firstLine);
			p = taille == 4 ? new Plateau() : new Plateau(taille, taille);
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				String[] elements = line.split(" ");

				int[] trueElements = convert(elements);

				int[] branches = { trueElements[3], trueElements[4], trueElements[5], trueElements[6] };

				p.setUnCarre(trueElements[0], trueElements[1],
						new Carre(trueElements[0], trueElements[1], trueElements[2], branches, trueElements[7]));

			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("On a pas trouvé ton fichier bro :(");
		}
	}

	// reset un niveau : remettre tous les attributs à leurs états initiaux
	public void resetNiveau() throws SlickException {
		isFini = false;
		this.genererNiveau(this.indice);
		goBack = false;
		jouerSon = true;
		jouerMusique = true;
		hoveringHome = false; // hover sur le bouton home
		hoveringNext = false;
		showVictory = false;
		opacity = 0;
		nbreCoups = 0;
		nbreEtoiles = -1;

	}

	// comparer le score obtenu par l'utilisateur au score minimum pour compléter un
	// niveau, présent dans le fichier scores.txt, et attribuer un nombre d'étoiles
	// en fonction de l'écart entre score utilisateur et score du fichier
	private void comparerScore() {
		try {
			String path = "C:\\Users\\Mohaman\\Documents\\3IL I1\\WS_POO\\MiniProjetA\\bin\\scores\\scores.txt";
			File f = new File(path);
			Scanner reader = new Scanner(f);

			// passer les lignes qui ne correspondent pas au niveau actuel
			for (int i = 0; i < this.indice - 1; i++) {
				reader.nextLine();
			}
			//récupérer le score du niveau actuel
			String scoreLine = reader.nextLine();
			int scoreMax = Integer.parseInt(scoreLine);
		
			//attribuer le nbre d'étoiles correspondant
			if (nbreCoups <= scoreMax)
				nbreEtoiles = 3;
			else if (nbreCoups < scoreMax + 5)
				nbreEtoiles = 2;
			else
				nbreEtoiles = 1;
			reader.close();

		} catch (FileNotFoundException e) {
			System.out.println("On a pas trouvé ton fichier bro :(");
		}
	}

	//dessiner étoiles à la fin du niveau
	private void afficherEtoiles(Graphics g) {
		switch (nbreEtoiles) {
		case 1:
			g.drawImage(etoile, 435, 80);
			break;
		case 2:
			for (int i = 0; i < 2; i++)
				g.drawImage(etoile, 400 + (90 * i), 80);
			break;
		case 3:
			for (int i = 0; i < 3; i++)
				g.drawImage(etoile, 380 + (90 * i), 80);
			break;
		}
	}

	public int getNbreEtoiles() {
		return nbreEtoiles;
	}

	//afficher le plateau et les carrés
	public void affichageJeu(int x, int y, Graphics g) {
		p.dessinerPlateau(g);
		p.dessinerCarres(g);
		p.dessinerCarreSelectionne(x, y, g);
	}

	private void assombrirEcran(GameContainer gc, Graphics g) {

		Color dark = new Color(0, 0, 0, opacity);
		g.setColor(dark);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());

	}

	private void afficherEcranFin(GameContainer gc, Graphics g) {
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		ttf.drawString(300.0f, 20.0f, "Level Complete !", Color.white);
		if (nbreEtoiles != -1) {
			afficherEtoiles(g);
		}
		ChoixNiveau.afficherHome(hoveringHome, home, homeHover, icons, g, true);
		afficherNext(g);
	}

}
