/*
 CLASSE DE CHOIX D'UN NIVEAU SUR L'ECRAN DE CHOIX, CHOIX GERE ENSUITE PAR LE GESTIONNAIRENIVEAU
 */

package game;

import java.awt.Font;
import java.io.File;
import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class ChoixNiveau {
	private boolean actif; // afficher ou pas cet écran
	
	//image de fond
	private Image fond;
	
	//carrés animés
	private AnimCarre[] carresFond;

	//images de difficultés des niveaux
	private Image easy;
	private Image normal;
	private Image hard;
	private Image easyhover;
	private Image normalhover;
	private Image hardhover;
	private Image levelmaker;
	private Image levelmakerhover;
	
	private Image fondlevel;

	//image des boutons de l'éditeur de niveaux
	private Image create;
	private Image createhover;
	private Image play;
	private Image playhover;

	private Image etoile;

	//gestion du hover sur les difficultés
	private boolean[] hover = { false, false, false, false };
	
	//gestion du hover sur les boutons de l'éditeur
	private boolean[] hoverlevelMaker = { false, false };
	
	//afficher les niveaux en fonction de la catégorie
	private boolean easyChoisi;
	private boolean mediumChoisi;
	private boolean hardChoisi;
	private boolean persoChoisi;

	private int categorieSelectionee;

	private Music musique;
	private boolean jouerMusique = true;
	private boolean levelmakerActif = false;

	//savoir si l'éditeur de niveau est choisi
	private boolean creerNiveau;

	private GestionnaireNiveau levels;

	//gestion bouton home
	private Image home;
	private Image homeHover;
	private boolean goBack = false;
	private boolean hoveringHome = false;

	//polices d'écriture
	private TrueTypeFont ttf;
	private TrueTypeFont icons;
	private TrueTypeFont buttons;

	public ChoixNiveau(GestionnaireNiveau g) throws SlickException {
		
		actif = true;
		
		
		fond = new Image("images/background.png"); // image de fond
		carresFond = new AnimCarre[6];
		for (int i = 0; i < carresFond.length; i++) {
			carresFond[i] = new AnimCarre(i);
		}
		etoile = new Image("images/ministar.png");

		easy = new Image("images/easybtn.png");
		normal = new Image("images/normalbtn.png");
		hard = new Image("images/hardbtn.png");
		easyhover = new Image("images/easybtnhover.png");
		normalhover = new Image("images/normalbtnhover.png");
		hardhover = new Image("images/hardbtnhover.png");
		levelmaker = new Image("images/levelmakerbtn.png");
		levelmakerhover = new Image("images/levelmakerbtnhover.png");
		fondlevel = new Image("images/levelbg.png");

		create = new Image("images/createbtn.png");
		createhover = new Image("images/createbtnhover.png");

		play = new Image("images/playbtn.png");
		playhover = new Image("images/playbtnhover.png");


		creerNiveau = false;

		musique = new Music("sounds/menu.ogg");

		levels = g;

		home = new Image("images/home.png");
		homeHover = new Image("images/homehover.png");
		
		Font font = new Font("Kristen ITC", Font.BOLD, 48);
		ttf = new TrueTypeFont(font, true); 

		Font fontIcon = new Font("Kristen ITC", Font.BOLD, 32);
		icons = new TrueTypeFont(fontIcon, true);

		Font fontButton = new Font("Kristen ITC", Font.BOLD, 48);
		buttons = new TrueTypeFont(fontButton, true);
	}

	public void show(Graphics g) {
		if (actif) {

			g.drawImage(fond, 0, 0);
			for (int i = 0; i < carresFond.length; i++) {
				carresFond[i].render(g);
			}

			if (!levelmakerActif && !categorieChoisie()) {
				ttf.drawString(220.0f, 20.0f, "CHOOSE A CATEGORY", Color.white);

				afficherBtn("easy", g, easy, easyhover);

				afficherBtn("mid", g, normal, normalhover);

				afficherBtn("hard", g, hard, hardhover);

				afficherBtn("maker", g, levelmaker, levelmakerhover);

			} else if (!categorieChoisie()) {
				ttf.drawString(260.0f, 20.0f, "CHOOSE A MODE", Color.white);

				afficherBtn("create", g, create, createhover);

				afficherBtn("play", g, play, playhover);

			} else {
				ttf.drawString(260.0f, 20.0f, "CHOOSE A LEVEL", Color.white);
				ChoixNiveau.afficherHome(hoveringHome, home, homeHover, icons, g, false);

				if (easyChoisi) {
					afficherNiveaux(g, "easy");
				}

				if (mediumChoisi) {
					afficherNiveaux(g, "medium");
				}

				if (hardChoisi) {
					afficherNiveaux(g, "medium");
				}

				if (persoChoisi) {
					afficherNiveaux(g, "perso");
				}

			}

		}
	}

	public void chooseLevel(GameContainer gc, int delta) throws SlickException {
		Input inp = gc.getInput();

		if (actif) {

			for (int i = 0; i < carresFond.length; i++) {
				carresFond[i].update(gc, delta);
			}

			if (jouerMusique) {
				musique.loop();
				jouerMusique = false;
			}

			if (!levelmakerActif && !categorieChoisie()) {

				// gérer le bouton easy
				if (inp.getMouseX() >= 50 && inp.getMouseX() <= 450 && inp.getMouseY() >= 180
						&& inp.getMouseY() <= 366) {
					hover[0] = true;
					if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						easyChoisi = true;
					}
				} else {
					hover[0] = false;
				}

				// gérer le bouton medium
				if (inp.getMouseX() >= 570 && inp.getMouseX() <= 970 && inp.getMouseY() >= 180
						&& inp.getMouseY() <= 366) {
					hover[1] = true;
					if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						mediumChoisi = true;
					}
				} else {
					hover[1] = false;
				}

				// gérer le bouton hard
				if (inp.getMouseX() >= 50 && inp.getMouseX() <= 450 && inp.getMouseY() >= 700
						&& inp.getMouseY() <= 886) {
					hover[2] = true;
					if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						hardChoisi = true;
					}
				} else {
					hover[2] = false;
				}

				// gérer le bouton levelmaker
				if (inp.getMouseX() >= 570 && inp.getMouseX() <= 970 && inp.getMouseY() >= 700
						&& inp.getMouseY() <= 886) {
					hover[3] = true;
					if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						levelmakerActif = true;
					}
				} else {
					hover[3] = false;
				}

			}

			if (levelmakerActif && !categorieChoisie()) {

				// gérer le bouton create
				if (inp.getMouseX() >= 300 && inp.getMouseX() <= 750 && inp.getMouseY() >= 200
						&& inp.getMouseY() <= 386) {
					hoverlevelMaker[0] = true;
					if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						creerNiveau = true;
						actif = false;
						musique.stop();
					}
				} else {
					hoverlevelMaker[0] = false;
				}

				// gérer le bouton play
				if (inp.getMouseX() >= 300 && inp.getMouseX() <= 750 && inp.getMouseY() >= 600
						&& inp.getMouseY() <= 786) {
					hoverlevelMaker[1] = true;
					if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
						persoChoisi = true;
					}
				} else {
					hoverlevelMaker[1] = false;
				}
			}

			if (categorieChoisie()) {

				// sortir si on appuie sur l'icone de maison
				handleHome(inp);

				
				//récupérer l'indice du niveau sur lequel on a cliqué : 
				
				int toAdd = 0;
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 4; j++) {
						if (inp.getMouseX() >= 170 + 170 * j && inp.getMouseX() <= 270 + 170 * j
								&& inp.getMouseY() >= 180 + 180 * i && inp.getMouseY() <= 280 + 180 * i) {
							if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON) && (i < 1 || (i == 1 && j < 2) )) {
								if (easyChoisi) {
									toAdd = 0;
								}
								if (mediumChoisi) {
									toAdd = 6;
								}
								if (hardChoisi) {
									toAdd = 12;
								}
								if (persoChoisi) {
									toAdd = 18;
								}
								// if ((i * 4) + j + toAdd < 10) {
								Niveau.setStreak(0);
								categorieSelectionee = (i * 4) + j + toAdd;
								actif = false;
								musique.stop();
								// }

							}
						}
					}

				}
			}

		}

	}

	private boolean categorieChoisie() {
		return easyChoisi || mediumChoisi || hardChoisi || persoChoisi;
	}

	public boolean isActif() {
		return actif;
	}

	public int getCategorieSelectionee() {
		return categorieSelectionee;
	}

	public boolean isCreerNiveau() {
		return creerNiveau;
	}

	public boolean isGoBack() {
		return goBack;
	}

	public static void afficherHome(boolean hoveringHome, Image home, Image homeHover, TrueTypeFont icons, Graphics g,
			boolean estNiveau) {
		int xHome = 35;
		int xText = 118;
		if (estNiveau) {
			xHome = 160;
			xText = 238;
		}
		g.drawImage(home, xHome, 820);
		if (hoveringHome) {
			g.drawImage(homeHover, xHome, 820);
		}
		icons.drawString(xText, 832, "HOME", Color.white);
	}

	public void handleHome(Input inp) {
		if (inp.getMouseX() >= 35 && inp.getMouseX() <= 256 && inp.getMouseY() >= 820 && inp.getMouseY() <= 891) {
			hoveringHome = true;
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				System.out.println("Bye bye :(");
				musique.stop();
				actif = false;
				goBack = true;
			}
		} else {
			hoveringHome = false;
		}
	}

	public void afficherNiveaux(Graphics g, String cat) {
		int indDepart = 0;
		switch (cat) {
		case "easy":
			indDepart = 0;
			break;
		case "medium":
			indDepart = 6;
			break;
		case "hard":
			indDepart = 12;
			break;
		case "perso":
			indDepart = 18;
			break;
		}
		if (cat != "perso") {
			for (int i = indDepart; i < levels.getNiveaux().length; i++) {
				if (levels.getNiveaux()[i] != null && levels.getNiveaux()[i].getCategorie() == cat) {
					int xDessin = (i - indDepart) % 4;
					int yDessin = (i - indDepart) / 4;
					if (levels.getNiveaux()[i].getNbreEtoiles() != -1) {
						afficherEtoiles(g, levels.getNiveaux()[i].getNbreEtoiles(), 170 + 170 * xDessin,
								130 + 180 * yDessin);
					}
					g.drawImage(fondlevel, 170 + 170 * xDessin, 180 + 180 * yDessin);
					ttf.drawString(170 + 170 * xDessin + 35, 180 + 180 * yDessin + 20,
							Integer.toString(i - indDepart + 1), Color.white);
				}
			}
		} else {
			for (int i = indDepart; i < levels.getNiveaux().length; i++) {
				if (levels.getNiveaux()[i] != null && levels.getNiveaux()[i].getCategorie() == "perso") {
					int xDessin = (i - indDepart) % 4;
					int yDessin = (i - indDepart) / 4;
					File f = new File(
							"C:\\Users\\Mohaman\\Documents\\3IL I1\\WS_POO\\MiniProjetA\\bin\\blueprints\\perso\\levelperso"
									+ (i - indDepart + 1) + ".txt");
					if (f.exists()) {
						g.drawImage(fondlevel, 170 + 170 * xDessin, 180 + 180 * yDessin);
						ttf.drawString(170 + 170 * xDessin + 35, 180 + 180 * yDessin + 20,
								Integer.toString(i - indDepart + 1), Color.white);
					}

				}
			}
		}
	}

	private void afficherBtn(String type, Graphics g, Image bouton, Image boutonHover) {
		int xRender = 0;
		int yRender = 0;
		boolean isHover = false;
		switch (type) {
		case "easy":
			xRender = 50;
			yRender = 180;
			isHover = hover[0];
			break;
		case "mid":
			xRender = 570;
			yRender = 180;
			isHover = hover[1];
			break;
		case "hard":
			xRender = 50;
			yRender = 700;
			isHover = hover[2];
			break;
		case "maker":
			xRender = 570;
			yRender = 700;
			isHover = hover[3];
			break;
		case "create":
			xRender = 300;
			yRender = 200;
			isHover = hoverlevelMaker[0];
			break;
		case "play":
			xRender = 300;
			yRender = 600;
			isHover = hoverlevelMaker[1];
			break;
		}

		g.drawImage(bouton, xRender, yRender);
		if (isHover) {
			g.drawImage(boutonHover, xRender, yRender);
		}

		buttons.drawString(xRender + 152, yRender + 55, type.toUpperCase(), Color.black);
	}

	private void afficherEtoiles(Graphics g, int nbreEtoiles, int x, int y) {
		for (int i = 0; i < nbreEtoiles; i++) {
			int decalageX = i == 1 ? 30 : i == 2 ? 60 : 0;
			int decalageY = i == 1 ? 45 : 0;
			g.drawImage(etoile, x + decalageX, y - decalageY);
		}
	}

}
