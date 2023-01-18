/*
 CLASSE D'ECRAN DE DEBUT DE JEU
 */

package game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class EcranTitre {
	private Image fond;
	private boolean actif;
	private boolean hovering;

	public EcranTitre() throws SlickException {
		actif = true;
		fond = new Image("images/titlescreen.png");
		hovering = false;
	}

	//afficher l'écran titre
	public void show(Graphics g) throws SlickException {
		if (actif) {
			g.drawImage(fond, 0, 0);
			if (hovering) {
				g.drawImage(new Image("images/playflou.png"), 340, 724);
			}
		}
	}

	//gérer le hover sur le bouton play et ne plus afficher écran titre si on appuie dessus
	public void hover(GameContainer gc) throws SlickException {
		Input inp = gc.getInput();
		if (actif) {
			if (inp.getMouseX() >= 340 && inp.getMouseX() <= 660 && inp.getMouseY() >= 744 && inp.getMouseY() <= 820) {
				hovering = true;
				if (inp.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
					actif = false;
					System.out.println("Bonjour !");
				}
			} else {
				hovering = false;
			}
		}

	}

	public boolean isActif() {
		return actif;
	}

}
