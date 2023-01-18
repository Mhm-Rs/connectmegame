/*
 CLASSE POUR ANIMATION DES CARRES EN ARRIERE-PLAN DANS L'ECRAN DE CHOIX DU NIVEAU
 */

package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class AnimCarre {
	private Image img;
	private float x;
	private float y;
	private float vitesseX = 300, vitesseY = 300; //vitesse de déplacement
	private int direction = 1; //-1 pour ceux qui tournent

	public AnimCarre(int test) throws SlickException {
		//placer les carrés à des endroits différents
		switch(test) {
		case 0:
			x = (float)(Math.random() * 500);
			y = -100;
			break;
		case 1:
			y = (float)(Math.random() * 500);
			x = -100;
			break;
		case 2:
			y = 500;
			x = 500;
			break;
		case 3:
			y = (float)(Math.random() * 500);
			x = 1100;
			direction = -1;
			break;
		case 4:
			y = 1100;
			x = (float)(Math.random() * 500);
			direction = -1;
			break;
		case 5:
			y = 1100;
			x = (float)(Math.random() * 500);
			direction = -1;
			break;
		}	
		//leur donner une image :
		setImageRandom();
	}

	public void render(Graphics g) {
		//afficher carré
		g.drawImage(img, x, y);
	}

	public void update(GameContainer gc, int delta) throws SlickException {
		
		//faire avancer les carrés
		y += vitesseY * direction *  delta / 1000f;
		x += vitesseX * direction * delta / 1000f;
		
		//avancer jusqu'à atteindre le bord de l'écran puis retéléporter ailleurs
		if(direction == 1) {
			if (y >= gc.getHeight() || x > gc.getWidth()) {
				double spawn = Math.random();
				if (spawn < 0.5) {
					vitesseX = 300;
					vitesseY = 150;
					if(direction == 1) {
						x = -100;
					}
					else {
						x = 1100;
					}
					y = (float) (Math.random() * gc.getHeight());					
					setImageRandom();
				} else {
					vitesseY = 300;
					vitesseX = 150;
					if(direction == 1) {
						y = -100;					
					}
					else {
						y = 1100;
					}
					x = (float) (Math.random() * gc.getWidth());
					setImageRandom();
				}

			}
		}
		//reculer jusqu'à atteindre le bord de l'écran puis retéléporter ailleurs
		else {
			if (x < - 100 || y < - 100) {
				double spawn = Math.random();
				if (spawn < 0.5) {
					vitesseX = 300;
					vitesseY = 150;
					x = 1100;
					y = (float) (Math.random() * gc.getHeight());					
					setImageRandom();
				} else {
					vitesseY = 300;
					vitesseX = 150;
					y = 1100;
					x = (float) (Math.random() * gc.getWidth());
					setImageRandom();
				}

			}
		}

		
	}

	//pour mettre une image aléatoire
	private void setImageRandom() throws SlickException {
		String[] nomImages = { "immobiletest", "mobiletest", "tournetest", "mobiletournetest", "mobilehorizontest",
				"mobilevertitest", "mobiletournehoritest", "mobiletournevertitest" };

		int choix = (int) (Math.random() * 8);
		String cheminImageChoisie = "images/" + nomImages[choix] + ".png";
		img = new Image(cheminImageChoisie);
	}

}
