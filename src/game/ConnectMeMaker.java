/*
 CLASSE DE L'EDITEUR DE NIVEAUX
 */

package game;

import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class ConnectMeMaker {

	//gérer l'affichage
	private boolean actif;
	
	//plateau et fond de l'éditeur
	private Plateau p;
	private Image fond;
	
	//gérer le bouton home
	private Image home;
	private Image homeHover;
	private boolean goBack = false;
	private boolean hoveringHome = false;

	//images de l'éditeur de niveau
	private Image moins;
	private Image plus;
	private Image haut;
	private Image bas;
	private Image gauche;
	private Image droit;
	private Image poubelle;

	//image du choix entre modifier et créer
	private Image fromscratch;
	private Image editlevel;
	private Image fromscratchhover;
	private Image editlevelhover;
	private Image fondlevel;
	private boolean hoverScratch = false;
	private boolean hoverEdit = false;
	private boolean choisirModifier = false;


	//images pour la création from scratch
	private Image tailles[] = { new Image("images/4X4.png"), new Image("images/5X5.png"), new Image("images/6X6.png") };
	private Image taillesHover[] = { new Image("images/4X4bis.png"), new Image("images/5X5bis.png"),
			new Image("images/6X6bis.png") };

	//hover sur les images de création from scratch
	private boolean[] hover = { false, false, false };

	//musique de fond
	private Music musique;
	private boolean jouerMusique = true;

	//polices d'écriture
	private TrueTypeFont ttf;
	private TrueTypeFont icons;
	private TrueTypeFont buttons;

	//choisir entre création et modification
	private boolean choisirMode = true;

	private boolean demanderTaille = false;

	//gérer bouton save
	private boolean hoveringSave = false;
	private Image save;
	private Image saveHover;

	//afficher le message de recomplétion lorsqu'on appuie sur save la 1ère fois
	private boolean afficherPrompt = false;
	private boolean levelSaved = false;
	private int timer = 0;
	private Image prompt;

	//image de création réussie
	private boolean afficherCreated = false;
	private Image created;

	//données du niveau créé pour le fichier
	private String levelData = "";

	
	//vérifier si on a pas déjà 5 niveaux créés lors de la création
	private boolean checkCreer = false; // a mettre a true après
	private Image warning;
	private boolean afficherWarning = false;

	//pour connaître les niveaux lors de l'affichage
	private GestionnaireNiveau levels;

	//savoir dans quel fichier placer la modification
	private int indiceSave = -1;

	public ConnectMeMaker(GestionnaireNiveau g) throws SlickException {
		actif = false;
		
		poubelle = new Image("images/trash.png");

		warning = new Image("images/warning.png");

		fromscratch = new Image("images/fromscratchbtn.png");
		editlevel = new Image("images/editbtn.png");

		fromscratchhover = new Image("images/fromscratchbtnhover.png");
		editlevelhover = new Image("images/editbtnhover.png");

		home = new Image("images/home.png");
		homeHover = new Image("images/homehover.png");

		p = new Plateau();
		
		fond = new Image("images/background.png");

		musique = new Music("sounds/createlevel.ogg");

		moins = new Image("images/moins.png");

		plus = new Image("images/plus.png");

		haut = new Image("images/haut.png");

		bas = new Image("images/bas.png");

		gauche = new Image("images/gauche.png");

		droit = new Image("images/droit.png");

		save = new Image("images/savelevel.png");
		saveHover = new Image("images/savelevelhover.png");

		prompt = new Image("images/prompt.png");

		created = new Image("images/levelcreated.png");

		fondlevel = new Image("images/levelbg.png");

		levels = g;
		
		Font font = new Font("Kristen ITC", Font.BOLD, 48);
		ttf = new TrueTypeFont(font, true); // police d'écriture texte

		Font fontIcon = new Font("Kristen ITC", Font.BOLD, 32);
		icons = new TrueTypeFont(fontIcon, true);

		Font fontButton = new Font("Kristen ITC", Font.BOLD, 48);
		buttons = new TrueTypeFont(fontButton, true);

	}

	public boolean isActif() {
		return actif;
	}

	public boolean isGoBack() {
		return goBack;
	}

	public void render(GameContainer gc, Graphics g) {
		Input inp = gc.getInput();
		//g.drawImage(fond, 0, 0);
		
		if (choisirMode) {
			afficherMessage("mode",g);

			afficherBtn("new", g, fromscratch, fromscratchhover);

			afficherBtn("edit", g, editlevel, editlevelhover);

		} else if (choisirModifier) {
			afficherMessage("level",g);
			ChoixNiveau.afficherHome(hoveringHome, home, homeHover, icons, g, false);
			
			afficherNiveaux(g);
		}

		else if (demanderTaille) {
			afficherMessage("size",g);

			afficherBoutonsTaille(g);

		} else if (actif) {
			affichageJeu(inp.getMouseX(),inp.getMouseY(),g);

			afficherIcones(g);

			// afficher bouton home
			ChoixNiveau.afficherHome(hoveringHome, home, homeHover, icons, g, false);

			// afficher bouton save
			afficherSave(g);

			if (afficherPrompt) {
				g.drawImage(prompt, 300, 350);
			}

			if (afficherCreated) {
				g.drawImage(created, 300, 350);
			}
		}
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		Input inp = gc.getInput();
		if (jouerMusique) {
			musique.loop();
			jouerMusique = false;
		}

		// regarder si on possède déjà 5 niveaux persos au début.
		if (checkCreer) {

			File[] fichiers = new File[5]; // les 5 fichiers contenant 5 niveaux persos
			// initialiser les fichiers
			for (int i = 0; i < fichiers.length; i++) {
				String path = "C:\\Users\\Mohaman\\Documents\\3IL I1\\WS_POO\\MiniProjetA\\bin\\blueprints\\perso\\levelperso"
						+ (i + 1) + ".txt";
				fichiers[i] = new File(path);
			}

			// chercher s'il y a un fichier qui est vide
			boolean peutCreer = false;
			for (int i = 0; i < fichiers.length; i++) {
				// premier fichier vide
				if (!fichiers[i].exists()) {
					peutCreer = true;
					break;
				}
			}
			//si aucun fichier n'est vide, retourner à l'écran de choix de niveau
			if (!peutCreer) {
				afficherWarning = true;
			}
			checkCreer = false;

		}

		//afficher l'avertissement pendant 2.5 secondes et retourner à l'écran de choix de niveau
		if (afficherWarning) {
			timer += delta;
			if (timer >= 2500) {
				afficherWarning = false;
				musique.stop();
				actif = false;
				goBack = true;
			}
		}

		// choisir entre création et modification
		if (choisirMode) {
			// gérer le bouton "from scratch"
			if (inp.getMouseX() >= 300 && inp.getMouseX() <= 750 && inp.getMouseY() >= 200 && inp.getMouseY() <= 386) {
				hoverScratch = true;
				if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					choisirMode = false;
					demanderTaille = true;
					checkCreer = true;
				}
			} else {
				hoverScratch = false;
			}

			// gérer le bouton "edit level"
			if (inp.getMouseX() >= 300 && inp.getMouseX() <= 750 && inp.getMouseY() >= 600 && inp.getMouseY() <= 786) {
				hoverEdit = true;
				if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					choisirMode = false;
					choisirModifier = true;
				}
			} else {
				hoverEdit = false;
			}
		}

		// si on a choisi de modifier un level
		else if (choisirModifier) {

			// sortir si on appuie sur l'icone de maison
			handleHome(inp);

			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 4; j++) {
					if (inp.getMouseX() >= 170 + 170 * j && inp.getMouseX() <= 270 + 170 * j
							&& inp.getMouseY() >= 180 + 180 * i && inp.getMouseY() <= 280 + 180 * i) {
						if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
							// créer un niveau à modifier sur la base du niveau cliqué
							indiceSave = (i * 4) + j + 18; // indice du niveau dans le tableau des niveaux de
															// levelHandler

							// ajouter les cases du niveau dans le tableau
							try {
								String path = "C:\\Users\\Mohaman\\Documents\\3IL I1\\WS_POO\\MiniProjetA\\bin\\blueprints\\perso\\levelperso"
										+ (indiceSave - 17) + ".txt";
								File f = new File(path);
								Scanner reader = new Scanner(f);
								String firstLine = reader.nextLine();
								int taille = Integer.parseInt(firstLine);
								p = taille == 4 ? new Plateau() : new Plateau(taille, taille);
								while (reader.hasNextLine()) {
									String line = reader.nextLine();
									String[] elements = line.split(" ");

									int[] trueElements = convert(elements);

									int[] branches = { trueElements[3], trueElements[4], trueElements[5],
											trueElements[6] };

									p.setUnCarre(trueElements[0], trueElements[1], new Carre(trueElements[0],
											trueElements[1], trueElements[2], branches, trueElements[7]));

								}
								reader.close();
							} catch (FileNotFoundException e) {
								System.out.println("On a pas trouvé ton fichier bro :(");
								// e.printStackTrace();
							}

							// carrés permettant d'ajouter des carrés
							setCarresEditeur(p.getMaxLigne());

							choisirModifier = false;
						}
					}
				}

			}
		}

		//si on crée un niveau from scratch, gérer la taille
		else if (demanderTaille) {

			//créer éditeur 4X4
			if (inp.getMouseX() >= 100 && inp.getMouseX() <= 259 && inp.getMouseY() >= 458 && inp.getMouseY() <= 578) {
				hover[0] = true;
				if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					p = new Plateau();
					demanderTaille = false;
					// carrés permettant d'ajouter des carrés
					setCarresEditeur(4);
				}
			} else {
				hover[0] = false;
			}

			//créer éditeur 5X5
			if (inp.getMouseX() >= 400 && inp.getMouseX() <= 559 && inp.getMouseY() >= 458 && inp.getMouseY() <= 578) {
				hover[1] = true;
				if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					p = new Plateau(5, 5);
					demanderTaille = false;
					setCarresEditeur(5);

				}
			} else {
				hover[1] = false;
			}

			//créer éditeur 6X6
			if (inp.getMouseX() >= 700 && inp.getMouseX() <= 859 && inp.getMouseY() >= 458 && inp.getMouseY() <= 578) {
				hover[2] = true;
				if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					p = new Plateau(6, 6);
					demanderTaille = false;
					setCarresEditeur(6);
				}
			} else {
				hover[2] = false;
			}
			
		//si l'éditeur est lancé :	
		} else if (actif) {
			// sortir si on appuie sur l'icone de maison
			handleHome(inp);

			// mode édition : augmenter et ajouter des carrés à souhait
			if (!levelSaved) {
				handleButtons(inp);
			}

			// gérer le bouton save
			if (inp.getMouseX() >= 760 && inp.getMouseX() <= 982 && inp.getMouseY() >= 970 && inp.getMouseY() <= 1041) {
				hoveringSave = true;

				// si on choisit d'enregistrer son niveau :
				if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					if (!levelSaved) {
						String toAdd = "";

						toAdd += p.getMaxLigne() + "\n"; // prendre le type de plateau : 4X4, 5X5 ou 6X6, pour le
															// constructeur de plateau plus tard

						for (int i = 0; i < p.getMaxLigne(); i++) {
							for (int j = 0; j < p.getMaxColonne(); j++) {
								if (p.getCarres()[i][j] != null) {
									// enregistrer les infos du carré sous la forme :
									// ligne-colonne-type-branches-décalage
									String line = "";
									line += p.getCarres()[i][j].getLigne() + " " + p.getCarres()[i][j].getColonne()
											+ " " + p.getCarres()[i][j].getType() + " "
											+ p.getCarres()[i][j].getBranches()[0] + " "
											+ p.getCarres()[i][j].getBranches()[1] + " "
											+ p.getCarres()[i][j].getBranches()[2] + " "
											+ p.getCarres()[i][j].getBranches()[3] + " " + p.getMaxLigne() + "\n";
									System.out.println(line);
									toAdd += line;
								}
							}
						}
						levelData += toAdd;
						afficherPrompt = true;
						levelSaved = true;
						
						//en appuyant la deuxieme fois sur save, enregistrer le niveau s'il est valide
					} else if (p.partieGagnee() && p.carresValides()) { 
						
						
						if (indiceSave != -1) {
							//chercher fichier correspondant
							File f = new File(
									"C:\\Users\\Mohaman\\Documents\\3IL I1\\WS_POO\\MiniProjetA\\bin\\blueprints\\perso\\levelperso"
											+ (indiceSave - 17) + ".txt");
							f.delete();
							
							//écrire le nouveau contenu
							try {
								FileWriter writer = new FileWriter(f);
								writer.write(levelData);
								writer.close();
								System.out.println("J'ai bien écrit bro");
								// créer le niveau correspondant :
								levels.setUnNiveau(indiceSave, new Niveau(indiceSave + 1), "perso");
								afficherCreated = true;
							} catch (IOException e) {
								System.out.println("y a eu un bug babana");
								e.printStackTrace();
							}
							
						//sinon, on ajoute au tableau des fichiers un nouveau fichier
						} else {
							try {
								File[] fichiers = new File[5]; // les 5 fichiers contenant 5 niveaux persos

								// initialiser les fichiers
								for (int i = 0; i < fichiers.length; i++) {
									String path = "C:\\Users\\Mohaman\\Documents\\3IL I1\\WS_POO\\MiniProjetA\\bin\\blueprints\\perso\\levelperso"
											+ (i + 1) + ".txt";
									fichiers[i] = new File(path);
								}

								// prendre le premier fichier qui est vide (n'existe pas) :
								for (int i = 0; i < fichiers.length; i++) {
									// premier fichier vide
									if (!fichiers[i].exists()) {
										// le créer :
										fichiers[i].createNewFile();
										// y écrire le contenu du niveau actuel :
										FileWriter writer = new FileWriter(fichiers[i]);
										writer.write(levelData);
										writer.close();
										System.out.println("J'ai bien écrit bro");
										// créer le niveau correspondant :
										levels.setUnNiveau(18 + i, new Niveau(18 + i + 1), "perso");
										afficherCreated = true;
										break;
									}
								}

							} catch (IOException e) {
								System.out.println("Y a eu un bug babana");
								e.printStackTrace();
							}
						}
					}

				}
			} else {
				hoveringSave = false;
			}

			if (afficherPrompt) {
				timer += delta;
				if (timer >= 3000) {
					timer = 0;
					afficherPrompt = false;
				}
			}

			if (afficherCreated) {
				timer += delta;
				if (timer >= 1500) {
					afficherCreated = false;
					musique.stop();
					actif = false;
					goBack = true;
				}
			}
			// déplacer un carré (possible pour tous les carrés en mode édition)
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				if (!levelSaved)
					p.deplacementMaker(inp.getMouseX(), inp.getMouseY());
				else
					p.deplacement(inp.getMouseX(), inp.getMouseY());

			}

			// tourner un carré (pour ceux qui le peuvent)
			if (inp.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
				p.rotation(inp.getMouseX(), inp.getMouseY());
			}

		}

	}

	public void setActif(boolean b) {
		actif = b;
	}

	public void setGoBack(boolean b) {
		goBack = b;
	}

	public void setJouerMusique(boolean b) {
		jouerMusique = b;
	}

	public void setDemanderTaille(boolean b) {
		demanderTaille = b;
	}

	private int[] convert(String[] elts) {
		int[] converted = new int[elts.length];

		for (int i = 0; i < elts.length; i++) {
			converted[i] = Integer.parseInt(elts[i]);
		}

		return converted;
	}

	public void handleHome(Input inp) {
		if (inp.getMouseX() >= 40 && inp.getMouseX() <= 256 && inp.getMouseY() >= 820 && inp.getMouseY() <= 891) {
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

	public void afficherSave(Graphics g) {
		g.drawImage(save, 760, 970);
		if (hoveringSave) {
			g.drawImage(saveHover, 760, 970);
		}
		icons.drawString(798, 982, "SAVE", Color.white);
	}

	private void afficherBtn(String type, Graphics g, Image bouton, Image boutonHover) {
		int xRender = 0;
		int yRender = 0;
		boolean isHover = false;
		switch (type) {
		case "new":
			xRender = 300;
			yRender = 200;
			isHover = hoverScratch;
			break;
		case "edit":
			xRender = 300;
			yRender = 600;
			isHover = hoverEdit;
			break;
		}

		g.drawImage(bouton, xRender, yRender);
		if (isHover) {
			g.drawImage(boutonHover, xRender, yRender);
		}

		buttons.drawString(xRender + 152, yRender + 55, type.toUpperCase(), Color.black);

	}
	
	private void afficherMessage(String nature,Graphics g) {
		float xAffichage=0;
		float yAffichage=20.0f;
		String message = "";
		switch(nature) {
		case "mode":
			xAffichage = 260.0f;
			message="CHOOSE A MODE";
			break;
		case "level":
			xAffichage = 170.0f;
			message="CHOOSE A LEVEL TO EDIT";
			break;
		case "size":
			xAffichage = 280.0f;
			message="CHOOSE A SIZE";
			break;
		}
		g.drawImage(fond, 0, 0);
		ttf.drawString(xAffichage, yAffichage, message, Color.white);
	}
	
	private void afficherNiveaux(Graphics g) {
		for (int i = 18; i < levels.getNiveaux().length; i++) {
			if (levels.getNiveaux()[i] != null && levels.getNiveaux()[i].getCategorie() == "perso") {
				int xDessin = (i - 18) % 4;
				int yDessin = (i - 18) / 4;
				File f = new File(
						"C:\\Users\\Mohaman\\Documents\\3IL I1\\WS_POO\\MiniProjetA\\bin\\blueprints\\perso\\levelperso"
								+ (i - 18 + 1) + ".txt");
				if (f.exists()) {
					g.drawImage(fondlevel, 170 + 170 * xDessin, 180 + 180 * yDessin);
					ttf.drawString(170 + 170 * xDessin + 35, 180 + 180 * yDessin + 20, Integer.toString(i - 18 + 1),
							Color.white);
				}

			}
		}
	}
	
	private void afficherBoutonsTaille(Graphics g) {
		g.drawImage(tailles[0], 100, 458);
		if (hover[0]) {
			g.drawImage(taillesHover[0], 100, 458);
		}

		g.drawImage(tailles[1], 400, 458);
		if (hover[1]) {
			g.drawImage(taillesHover[1], 400, 458);
		}

		g.drawImage(tailles[2], 700, 458);
		if (hover[2]) {
			g.drawImage(taillesHover[2], 700, 458);
		}

		if (afficherWarning) {
			g.drawImage(warning, 300, 150);
		}
	}
	
	public void affichageJeu(int x, int y, Graphics g) {
		p.dessinerPlateau(g);
		p.dessinerCarres(g);
		p.dessinerCarreSelectionne(x, y, g);
	}
	
	private void afficherIcones(Graphics g) {
		g.drawImage(moins, 20, 50);
		g.drawImage(plus, 880, 20);
		g.drawImage(poubelle, 860, 800);
		for (int i = 0; i < 2; i++) {
			g.drawImage(haut, 860 * i + 20, 200);
			g.drawImage(droit, 860 * i + 20, 350);
			g.drawImage(bas, 860 * i + 20, 500);
			g.drawImage(gauche, 860 * i + 20, 650);
		}
	}
	
	private void handleButtons(Input inp) {
		// gérer le bouton +

		// gérer le haut
		if (inp.getMouseX() >= 860 && inp.getMouseX() <= 940 && inp.getMouseY() >= 200
				&& inp.getMouseY() <= 292) {

			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				p.augmenterBranches(0);
			}
		}

		// gérer la droite
		if (inp.getMouseX() >= 860 && inp.getMouseX() <= 940 && inp.getMouseY() >= 350
				&& inp.getMouseY() <= 442) {

			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				p.augmenterBranches(1);
			}
		}

		// gérer le bas
		if (inp.getMouseX() >= 860 && inp.getMouseX() <= 940 && inp.getMouseY() >= 500
				&& inp.getMouseY() <= 592) {

			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				p.augmenterBranches(2);
			}
		}

		// gérer la gauche
		if (inp.getMouseX() >= 860 && inp.getMouseX() <= 940 && inp.getMouseY() >= 650
				&& inp.getMouseY() <= 742) {

			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				p.augmenterBranches(3);
			}
		}

		// gérer le bouton poubelle
		if (inp.getMouseX() >= 860 && inp.getMouseX() <= 940 && inp.getMouseY() >= 800
				&& inp.getMouseY() <= 892) {

			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				p.setCarreSelectionne(null);
			}
		}

		// gérer le bouton -

		// gérer le haut
		if (inp.getMouseX() >= 20 && inp.getMouseX() <= 100 && inp.getMouseY() >= 200
				&& inp.getMouseY() <= 292) {
			// texture hover
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				p.diminuerBranches(0);
			}
		}

		// gérer la droite
		if (inp.getMouseX() >= 20 && inp.getMouseX() <= 100 && inp.getMouseY() >= 350
				&& inp.getMouseY() <= 442) {
			// texture hover
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				p.diminuerBranches(1);
			}
		}

		// gérer le bas
		if (inp.getMouseX() >= 20 && inp.getMouseX() <= 100 && inp.getMouseY() >= 500
				&& inp.getMouseY() <= 592) {
			// texture hover
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				p.diminuerBranches(2);
			}
		}

		// gérer la gauche
		if (inp.getMouseX() >= 20 && inp.getMouseX() <= 100 && inp.getMouseY() >= 650
				&& inp.getMouseY() <= 742) {
			// texture hover
			if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				p.diminuerBranches(3);
			}
		}
	}
	
	private void setCarresEditeur(int taille) throws SlickException {
		int[] br0 = new int[4];
		int[] br1 = new int[4];
		int[] br2 = new int[4];
		int[] br3 = new int[4];
		int[] br4 = new int[4];
		int[] br5 = new int[4];
		int[] br6 = new int[4];
		int[] br7 = new int[4];

		switch (taille) {
		case 4:
			p.setUnCarre(4, 0, new Carre(4, 0, 0, br0, 4));
			p.setUnCarre(4, 1, new Carre(4, 1, 1, br1, 4));
			p.setUnCarre(4, 2, new Carre(4, 2, 2, br2, 4));
			p.setUnCarre(4, 3, new Carre(4, 3, 3, br3, 4));
			p.setUnCarre(5, 0, new Carre(5, 0, 4, br4, 4));
			p.setUnCarre(5, 1, new Carre(5, 1, 5, br5, 4));
			p.setUnCarre(5, 2, new Carre(5, 2, 6, br6, 4));
			p.setUnCarre(5, 3, new Carre(5, 3, 7, br7, 4));
			break;
		case 5:
			p.setUnCarre(5, 1, new Carre(5, 1, 0, br0, 5));
			p.setUnCarre(5, 2, new Carre(5, 2, 1, br1, 5));
			p.setUnCarre(5, 3, new Carre(5, 3, 2, br2, 5));
			p.setUnCarre(5, 4, new Carre(5, 4, 3, br3, 5));
			p.setUnCarre(6, 1, new Carre(6, 1, 4, br4, 5));
			p.setUnCarre(6, 2, new Carre(6, 2, 5, br5, 5));
			p.setUnCarre(6, 3, new Carre(6, 3, 6, br6, 5));
			p.setUnCarre(6, 4, new Carre(6, 4, 7, br7, 5));
			break;
		case 6:
			p.setUnCarre(6, 1, new Carre(6, 1, 0, br0, 6));
			p.setUnCarre(6, 2, new Carre(6, 2, 1, br1, 6));
			p.setUnCarre(6, 3, new Carre(6, 3, 2, br2, 6));
			p.setUnCarre(6, 4, new Carre(6, 4, 3, br3, 6));
			p.setUnCarre(6, 5, new Carre(6, 5, 4, br4, 6));
			p.setUnCarre(7, 1, new Carre(7, 1, 5, br5, 6));
			p.setUnCarre(7, 2, new Carre(7, 2, 6, br6, 6));
			p.setUnCarre(7, 3, new Carre(7, 3, 7, br7, 6));
		}
	}
	

}
