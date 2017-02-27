package de.pjog.prinzJuliano.asteroids;

import static de.pjog.prinzJuliano.asteroids.DataManager.checkTopFive;
import static de.pjog.prinzJuliano.asteroids.DataManager.loadColorScheme;
import static de.pjog.prinzJuliano.asteroids.DataManager.loadConfig;
import static de.pjog.prinzJuliano.asteroids.DataManager.loadTopFive;
import static de.pjog.prinzJuliano.asteroids.DataManager.topFive;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.opengl.PShader;

public class MainClass extends PApplet {

	Ship ship;
	ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	ArrayList<Laser> lasers = new ArrayList<Laser>();

	float newH;

	int bgColor, laserColor, asteroidColor, textColor, shipColor;

	boolean inGame = true;
	boolean shownModal = false;
	boolean loopOnce = false;

	PFont fontAtari;
	PFont fontPixel;

	PlayerEntry you = new PlayerEntry(this, "you", 1, 0);

	PGraphics endStage1, endStage2, src, pass1, pass2, textG, scanlines;
	PShader blur, crt;

	boolean blurShader = true;
	boolean scanlineShader = true;
	boolean crtShader = true;

	int pSmoothLevel = 8;

	int lastTime = 0;
	int delta = 0;

	void reset() {
		ship = new Ship(this);
		asteroids = new ArrayList<Asteroid>();
		lasers = new ArrayList<Laser>();
		// score = 0;
		// stage = 1;
		inGame = true;
		shownModal = false;
		generateNew();
	}
	
	public void settings(){
		fullScreen(P2D, SPAN);
	}

	public void setup() {

		frameRate(60);
		
		loadConfig(this);

		if (args != null && args.length != 0)
			for (String s : args) {
				if (s.equalsIgnoreCase("DoTravisDebug")) {
					loopOnce = true;
					scanlineShader = false;
					blurShader = false;
					crtShader = false;

					System.out.println("Entered Debug Mode for Travis!");
				}
			}

		

		src = createGraphics(width, height, P2D);
		endStage1 = createGraphics(width, height, P2D);

		loadConfig(this);

		if (blurShader) {
			pass1 = createGraphics(width, height, P2D);
			pass1.noSmooth();

			pass2 = createGraphics(width, height, P2D);
			pass2.noSmooth();

			blur = loadShader("shaders/blur.glsl");
			blur.set("blurSize", 10);
			blur.set("sigma", 5f);
		}
		if (crtShader) {
			crt = loadShader("shaders/crt.glsl");
			endStage2 = createGraphics(width, height, P2D);
		}

		textG = createGraphics(width, height, P2D);

		surface.setResizable(false);

		fontPixel = createFont("fonts/VCR_OSD_MONO_1.001.ttf", 32);
		fontAtari = createFont("fonts/old.TTF", 32);

		loadColorScheme(this);

		reset();

		loadTopFive(this);

		if (pSmoothLevel > 0) {
			src.smooth(pSmoothLevel);
		} else {
			src.noSmooth();
		}

		// scanlines
		if (scanlineShader) {
			scanlines = createGraphics(width, height, P2D);
			scanlines.beginDraw();
			scanlines.background(0, 0);
			scanlines.strokeWeight(1);
			scanlines.stroke(42, 42, 42, 100);
			for (int y = 0; y < height; y += 2) {

				scanlines.line(0, y, width, y);
			}
			scanlines.endDraw();
		}
	}

	void generateNew() {
		int amount = floor(random(3, 5) + random(1, 3) * you.stage);

		for (int i = 0; i < amount; i++)
			asteroids.add(new Asteroid(this));
	}

	public void draw() {
		delta = millis() - lastTime;
		if (inGame) {
			background(0);
			src.beginDraw();

			src.background(bgColor);

			textG.beginDraw();
			textG.background(0);
			textG.noStroke();
			textG.fill(textColor);
			textG.textSize(24);
			textG.textFont(fontPixel);
			textG.textAlign(LEFT);
			textG.text("Stage: " + you.stage + " Score: " + you.score, 10, height - 10);
			textG.endDraw();

			for (int i = asteroids.size() - 1; i >= 0; i--) {

				asteroids.get(i).update();
				asteroids.get(i).draw(src);

				if (ship.hits(asteroids.get(i))) {
					inGame = false;
				}
			}

			for (int i = lasers.size() - 1; i >= 0; i--) {
				lasers.get(i).update();
				lasers.get(i).draw(src);

				if (lasers.get(i).edges())
					lasers.remove(i);
				else {

					for (int j = asteroids.size() - 1; j >= 0; j--) {
						if (lasers.get(i).hits(asteroids.get(j))) {

							you.score++;
							lasers.remove(i);
							if (asteroids.get(j).r * 0.5 > 8) {
								ArrayList<Asteroid> newAsteroids = asteroids.get(j).breakup();
								asteroids.addAll(newAsteroids);
							} else {
								you.score += 10;
							}
							asteroids.remove(j);
							break;
						}
					}
				}
			}

			ship.draw(src);
			ship.update();

			// check if every asteroid is gone
			if (asteroids.size() == 0) {
				/*
				 * if(score > getMinimumScoreForStage(stage) && score <=
				 * getMaximumScoreForStage(stage)){ stage++; generateNew(); }
				 * else{
				 * 
				 * score = -9; inGame = false; }
				 */
				you.stage++;
				generateNew();
			}

			src.endDraw();

			endStage1.beginDraw();
			endStage1.background(0);

			endStage1.image(src, 0, 0);

			endStage1.blendMode(ADD);

			if (blurShader) {

				pass1.beginDraw();
				blur.set("horizontalPass", 0);
				pass1.shader(blur);
				pass1.image(src, 0, 0);
				pass1.endDraw();

				// Applying the blur shader along the horizontal direction
				blur.set("horizontalPass", 1);
				pass2.beginDraw();
				pass2.shader(blur);
				pass2.image(pass1, 0, 0);
				pass2.endDraw();

				endStage1.image(pass2, 0, 0);
			}

			// Text
			endStage1.blendMode(ADD);
			endStage1.image(textG, 0, 0);

			// scanlines
			if (scanlineShader) {
				endStage1.blendMode(BLEND);
				endStage1.image(scanlines, 0, 0);
			}

			endStage1.endDraw();

			if (crtShader) {
				endStage2.beginDraw();
				endStage2.shader(crt);

				endStage2.image(endStage1, 0, 0);

				// shader vars
				crt.set("iResolution", new PVector(width, height, 1));
				crt.set("iGlobalTime", millis() / 1000);
				crt.set("iChannel0", endStage1);
				endStage2.endDraw();

				image(endStage2, 0, 0);
			} else {
				image(endStage1, 0, 0);
			}
		} else {
			background(0);
			src.beginDraw();

			if (!shownModal) {
				shownModal = true;
				checkTopFive(this);
			}

			src.background(bgColor);

			// translate(width/2, height/2);

			src.noStroke();
			src.fill(textColor);
			src.textFont(fontAtari);
			src.textSize(24);
			src.textAlign(CENTER);

			String textTopFive = "Top 5:\n";
			for (int i = 0; i < topFive.length; i++)
				textTopFive += topFive[i].score + ": " + topFive[i].username + "\n";

			String displayText = "Game Over!\nFinal Score: " + you.score + "\n\n" + textTopFive;

			src.text(displayText, width / 4, (20 * 4), width / 2, height / 2);

			newH = height / 2 - (20 * 4) + height / 4 + 15;

			if (mouseX >= width / 2 - width / 8 && mouseX <= width / 2 - width / 8 + width / 4 && mouseY >= newH
					&& mouseY <= newH + height / 4 - 15)
				src.fill(255);
			else
				src.fill(255, 100);
			src.rect(width / 2 - width / 8, newH, width / 4, height / 4 - 15);

			src.fill(0);
			src.textSize(32);
			src.text("Play new Game", width / 2 - width / 8, newH, width / 4, height / 4 - 15);

			src.endDraw();

			endStage1.beginDraw();

			endStage1.image(src, 0, 0);
			if (scanlineShader) {
				endStage1.blendMode(BLEND);
				endStage1.image(scanlines, 0, 0);
			}
			endStage1.endDraw();

			if (crtShader) {
				endStage2.beginDraw();
				endStage2.shader(crt);

				endStage2.image(endStage1, 0, 0);

				// shader vars
				crt.set("iResolution", new PVector(width, height, 1));
				crt.set("iGlobalTime", millis() / 1000);
				crt.set("iChannel0", endStage1);
				endStage2.endDraw();

				image(endStage2, 0, 0);
			} else {
				image(endStage1, 0, 0);
			}
		}

		if (loopOnce) {
			noLoop();
			exit();
		}

		lastTime = millis();
	}

	public void mousePressed() {
		if (!inGame) {
			if (mouseX >= width / 2 - width / 8 && mouseX <= width / 2 - width / 8 + width / 4 && mouseY >= newH
					&& mouseY <= newH + height / 4 - 15) {
				reset();
				you.score = 0;
				you.stage = 1;
			}
		}
	}

	public void keyPressed() {
		if (key == 'd') {
			ship.setRotation(0.1f);
		} else if (key == 'a') {
			ship.setRotation(-0.1f);
		} else if (key == 'w') {
			ship.boosting(true);
		} else if (key == ' ') {
			lasers.add(new Laser(this, ship.pos, ship.heading, ship.vel));
		}
	}

	public void keyReleased() {
		if (key == 'd' || key == 'a') {
			ship.rotation = 0;
		}
		if (key == 'w') {
			ship.boosting(false);
		}
	}

	public static void main(String[] args) {
		PApplet.main("de.pjog.prinzJuliano.asteroids.MainClass");

	}

}
