package de.pjog.prinzJuliano.asteroids;

import static de.pjog.prinzJuliano.asteroids.data.DataManager.loadColorScheme;
import static de.pjog.prinzJuliano.asteroids.data.DataManager.loadConfig;
import static de.pjog.prinzJuliano.asteroids.data.DataManager.loadTopFive;

import java.util.ArrayList;

import de.pjog.prinzJuliano.asteroids.data.PlayerEntry;
import de.pjog.prinzJuliano.asteroids.entites.Asteroid;
import de.pjog.prinzJuliano.asteroids.entites.Dust;
import de.pjog.prinzJuliano.asteroids.entites.Laser;
import de.pjog.prinzJuliano.asteroids.entites.Ship;
import de.pjog.prinzJuliano.asteroids.io.HUD;
import de.pjog.prinzJuliano.asteroids.io.InputHandler;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.opengl.PShader;

public class MainClass extends PApplet {

	public Ship ship;
	public HUD hud;
	public ArrayList<Dust> dust = new ArrayList<Dust>();
	public ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	public ArrayList<Laser> lasers = new ArrayList<Laser>();

	public boolean canPlay = true;
	public int shieldTime = 180;

	public int[] points = { 100, 50, 20 };

	public InputHandler inputs;

	public int bgColor;
	public int laserColor;
	public int asteroidColor;
	public int textColor;
	public int vignetteColor;
	public int shipThrusterColor;

	public PFont fontAtari;
	public PFont fontPixel;

	public PlayerEntry you = new PlayerEntry("you", 1, 0);

	public PGraphics endStage1, endStage2, src, pass1, pass2, textG, scanlines;
	public PShader blur, crt;

	public boolean blurShader;
	public boolean scanlineShader;
	public boolean crtShader;

	public int pSmoothLevel = 8;

	public int lastTime = 0;
	public int delta = 0;

	public void reset() {
		ship = new Ship(this);
		asteroids = new ArrayList<Asteroid>();
		lasers = new ArrayList<Laser>();
		hud = new HUD(this);
		// score = 0;
		// stage = 1;
		canPlay = true;
		spawnAsteroids();
	}

	public void settings() {
		fullScreen(P2D, SPAN);
	}

	public void setup() {

		frameRate(60);

		inputs = new InputHandler();

		loadConfig(this);

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

	public void spawnAsteroids() {
		int amount = floor(random(3, 5) + random(1, 3) * you.stage);

		for (int i = 0; i < amount; i++)
			asteroids.add(new Asteroid(this));
	}

	public void draw() {
		background(0);
		for (int i = 0; i < asteroids.size(); i++) {
			if (ship.hits(asteroids.get(i)) && canPlay) {
				canPlay = false;
				ship.destroy();
				inputs.reset();

				new java.util.Timer().schedule(new java.util.TimerTask() {
					public void run() {
						you.lives--;
						if (you.lives >= 0) {
							reset();
							canPlay = true;
						}
					}
				}, 3000);

			}
			asteroids.get(i).update();
		}

		for (int i = lasers.size() - 1; i >= 0; i--) {
			lasers.get(i).update();
			if (lasers.get(i).offscreen()) {
				lasers.remove(i);

				continue;
			}

			for (int j = asteroids.size() - 1; j >= 0; j--) {
				if (lasers.get(i).hits(asteroids.get(j))) {
					you.score += points[asteroids.get(j).size];
					PVector dustVel = PVector.add(lasers.get(i).vel.mult(0.2f), asteroids.get(j).vel);
					int dustNum = (asteroids.get(j).size + 1) * 5;
					addDust(asteroids.get(j).pos, dustVel, dustNum);
					ArrayList<Asteroid> newAsteroids = asteroids.get(j).breakup();
					asteroids.addAll(newAsteroids);
					asteroids.remove(j);
					lasers.remove(i);
					if (asteroids.size() == 0) {
						you.stage++;
						spawnAsteroids();
						ship.shields = shieldTime;
					}
					break;
				}
			}
		}

		ship.update();

		for (int i = dust.size() - 1; i >= 0; i--) {
			dust.get(i).update();
			if (dust.get(i).transparency <= 0) {
				dust.remove(i);
			}
		}

		// Render
		src.beginDraw();
		src.background(bgColor);
		
		textG.beginDraw();
		textG.background(0);

		for (int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(src);
		}

		for (int i = lasers.size() - 1; i >= 0; i--) {
			lasers.get(i).draw(src);
		}

		ship.draw(src);
		hud.draw(textG);

		for (int i = dust.size() - 1; i >= 0; i--) {
			dust.get(i).draw(src);
		}
		
		textG.endDraw();
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
			endStage2.background(0);

			endStage2.image(endStage1, 0, 0);

			// shader vars
			crt.set("iResolution", new PVector(width, height, 1));
			crt.set("iGlobalTime", millis() / 1000);
			crt.set("iChannel0", endStage1);
			crt.set("vignetteColor", PVector.div(new PVector(red(vignetteColor), green(vignetteColor), blue(vignetteColor)), 255));
			endStage2.endDraw();

			image(endStage2, 0, 0);
		} else {
			image(endStage1, 0, 0);
		}
	}

	public void keyPressed() {
		inputs.handleEvent(key, keyCode, true);
	}

	public void keyReleased() {
		inputs.handleEvent(key, keyCode, false);
	}

	public void addDust(PVector pos, PVector vel, int n) {
		for (int i = 0; i < n; i++)
			dust.add(new Dust(this, pos, vel));
	}

	public static void main(String[] args) {
		PApplet.main("de.pjog.prinzJuliano.asteroids.MainClass");

	}

}
