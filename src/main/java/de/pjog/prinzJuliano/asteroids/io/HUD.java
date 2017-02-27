package de.pjog.prinzJuliano.asteroids.io;

import static de.pjog.prinzJuliano.asteroids.data.DataManager.topFive;

import de.pjog.prinzJuliano.asteroids.MainClass;
import de.pjog.prinzJuliano.asteroids.data.DataManager;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class HUD {
	float size = 20;
	float padding = 10;
	float lifeWidth = 20;

	private boolean calcOnce = false;

	boolean[][] digitMaps = {
			// return a digit map
			{ true, true, true, false, true, true, true }, // 0
			{ false, false, true, false, false, true, false }, // 1
			{ true, false, true, true, true, false, true }, // 2
			{ true, false, true, true, false, true, true }, // 3
			{ false, true, true, true, false, true, false }, // 4
			{ true, true, false, true, false, true, true }, // 5
			{ true, true, false, true, true, true, true }, // 6
			{ true, false, true, false, false, true, false }, // 7
			{ true, true, true, true, true, true, true }, // 8
			{ true, true, true, true, false, true, true } // 9

	};

	MainClass p;

	public HUD(MainClass p) {
		this.p = p;
	}

	public void draw(PGraphics g) {
		String scoreString = "" + p.you.score;
		PVector digitPos = new PVector((p.width / 2 - (scoreString.length() * (size + padding)) / 2), padding);

		for (int i = 0; i < scoreString.length(); i++) {
			boolean[] dmap = digitMaps[Integer.parseInt(String.valueOf(scoreString.charAt(i)))];
			drawDigit(g, dmap, i, digitPos);
			digitPos.x += size + padding;
		}
		drawLives(g);
		if (p.you.lives < 0) {
			g.pushMatrix();
			g.textSize(32);
			g.fill(p.textColor);
			g.textFont(p.fontAtari);

			if (!calcOnce) {
				calcOnce = true;
				DataManager.checkTopFive(p);
			}

			String textTopFive = "Top 5:\n";
			for (int i = 0; i < topFive.length; i++)
				textTopFive += topFive[i].score + ": " + topFive[i].username + "\n";

			String displayText = "Game Over!\nFinal Score: " + p.you.score + "\n\n" + textTopFive;

			g.textMode(PConstants.CENTER);
			g.text(displayText, p.width/2-200, p.height / 2-100);

			// topfive

			g.popMatrix();
		}
	}

	private void drawLives(PGraphics g) {
		g.pushMatrix();
		g.stroke(p.textColor);
		g.strokeWeight(1);
		g.fill(p.bgColor);
		PVector top = new PVector((p.width / 2) + lifeWidth * 2, padding * 2 + size * 2);
		for (int i = 0; i < p.you.lives; i++) {
			g.triangle(top.x, top.y, top.x - lifeWidth / 2, top.y + 25, top.x + lifeWidth / 2, top.y + 25);
			top.x -= 20 + padding;
		}
		g.popMatrix();
	}

	private void drawDigit(PGraphics g, boolean[] dmap, int index, PVector digitPos) {
		g.pushMatrix();
		g.stroke(p.textColor);
		for (int i = 0; i < dmap.length; i++) {
			if (dmap[i] == true)
				drawLine(g, i, digitPos);
		}
		g.popMatrix();

	}

	private void drawLine(PGraphics g, int lineMap, PVector pos) {
		switch (lineMap) {
		case 0:
			g.line(pos.x, pos.y, pos.x + size, pos.y);
			break;
		case 1:
			g.line(pos.x, pos.y, pos.x, pos.y + size);
			break;
		case 2:
			g.line(pos.x + size, pos.y, pos.x + size, pos.y + size);
			break;
		case 3:
			g.line(pos.x, pos.y + size, pos.x + size, pos.y + size);
			break;
		case 4:
			g.line(pos.x, pos.y + size, pos.x, pos.y + 2 * size);
			break;
		case 5:
			g.line(pos.x + size, pos.y + size, pos.x + size, pos.y + 2 * size);
			break;
		case 6:
			g.line(pos.x, pos.y + size * 2, pos.x + size, pos.y + 2 * size);
			break;
		default:
			System.out.println("line map is invalid");
			break;
		}

	}
}
