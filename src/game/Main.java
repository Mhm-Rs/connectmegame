/*
Point d'entrée du programme
 */

package game;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {

	public static void main(String[] args) throws SlickException {
		ConnectMe jeu = new ConnectMe("Connect Me");
		AppGameContainer app = new AppGameContainer(jeu);
		app.setTargetFrameRate(100);
		app.setIcon("images/icon.png");
		app.setDisplayMode(1000, 1050, false);
		app.setShowFPS(false);
		app.start();
		
	}

}
