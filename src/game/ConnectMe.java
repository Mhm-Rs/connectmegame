/*
 CLASSE DE GESTION DES OBJETS PRINCIPAUX DU PROGRAMME
 */

package game;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class ConnectMe extends BasicGame {
	private GestionnaireNiveau levelHandler;
	private EcranTitre titleScreen;
	private ChoixNiveau choice;
	private ConnectMeMaker gameMaker;

	public ConnectMe(String title) {
		super(title);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		//afficher écran titre
		titleScreen.show(g);

		//afficher écran choix niveau
		if (!titleScreen.isActif()) {
			choice.show(g);
		}
		
		//afficher écran création niveau
		if(!titleScreen.isActif() && !choice.isActif() && gameMaker.isActif()) {
			gameMaker.render(gc,g);
		}

		//afficher niveau
		if (!titleScreen.isActif() && !choice.isActif() && !gameMaker.isActif()
				&& levelHandler.niveauActuel(choice.getCategorieSelectionee()) != null) {
			levelHandler.niveauActuel(choice.getCategorieSelectionee()).renderNiveau(gc, g);
		}
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		levelHandler = new GestionnaireNiveau();
		titleScreen = new EcranTitre();
		choice = new ChoixNiveau(levelHandler);
		gameMaker = new ConnectMeMaker(levelHandler);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input inp = gc.getInput();
		
		//gérer click sur écran titre
		titleScreen.hover(gc);

		//gérer choix niveau
		if (!titleScreen.isActif()) {
			choice.chooseLevel(gc,delta);
		}
		
		//pour activer l'écran de création de niveau
		if(choice.isCreerNiveau()) {
			gameMaker.setActif(true);
		}
		
		//gérer créateur de niveau
		if(!titleScreen.isActif() && !choice.isActif() && gameMaker.isActif()) {
			gameMaker.update(gc,delta);
		}

		//gérer niveau
		if (!titleScreen.isActif() && !choice.isActif() && !gameMaker.isActif()
				&& levelHandler.niveauActuel(choice.getCategorieSelectionee()) != null) {
			levelHandler.niveauActuel(choice.getCategorieSelectionee()).updateNiveau(gc, delta);
		}
		
		//revenir à l'écran de choix de niveau si on appuie sur home dans une catégorie
		if(!titleScreen.isActif() && choice.isGoBack()) {
			choice = new ChoixNiveau(levelHandler);
		}
		
		//revenir à l'écran de choix de niveau si on appuie sur home dans un niveau, si on termine le jeu fermer le game container
		try {
			if(!titleScreen.isActif() && !choice.isActif() && levelHandler.niveauActuel(choice.getCategorieSelectionee()).isGoBack() && !gameMaker.isActif()) {
				levelHandler.niveauActuel(choice.getCategorieSelectionee()).setGoBack(false);
				levelHandler.niveauActuel(choice.getCategorieSelectionee()).setJouerMusique(true);
				choice = new ChoixNiveau(levelHandler);
			}
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("Jeu terminé !");
			gc.exit();
		}
		
		
		//revenir à l'écran de choix de niveau si on appuie sur home dans éditeur de niveau
		if(!titleScreen.isActif() && !choice.isActif() && gameMaker.isGoBack()) {
			gameMaker = new ConnectMeMaker(levelHandler);
			choice = new ChoixNiveau(levelHandler);
		}
		
		//arrêter le jeu si on appuie sur ESC à tout moment
		if(inp.isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}
	}

}
