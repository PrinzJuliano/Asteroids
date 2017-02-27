package de.pjog.prinzJuliano.asteroids;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class Asteroid {

	MainClass p;

	PVector pos;
	float r;
	PVector vel;
	int vertecies;
	ArrayList<Float> offset;

	public Asteroid(MainClass p) {
		this(p, new PVector(p.random(p.width), p.random(p.height)), PApplet.floor(p.random(20, 50)));
	}

	public Asteroid(MainClass p, PVector pos, float r) {
		this.p = p;
		this.pos = pos.copy();
		this.r = r;
		this.vel = PVector.random2D();
		this.vertecies = PApplet.floor(p.random(5, 15));
		this.offset = new ArrayList<Float>();

		for (int i = 0; i < this.vertecies; i++)
			this.offset.add(p.random(-this.r * 0.5f, this.r * 0.5f));
	}

	void update() {
		this.pos.add(this.vel);
		this.edges();
	}

	void draw(PGraphics src) {
		src.pushMatrix();
		src.translate(this.pos.x, this.pos.y);
		src.stroke(p.asteroidColor);
		src.strokeWeight(1);
		src.noFill();
		// ellipse(0, 0, this.r*2, this.r*2);
		src.beginShape();
		for (int i = 0; i < this.vertecies; i++) {
			float angle = PApplet.map(i, 0, this.vertecies, 0, PConstants.TWO_PI);
			float r = (this.r + this.offset.get(i));

			float x = r * PApplet.cos(angle);
			float y = r * PApplet.sin(angle);
			src.vertex(x, y);
		}
		src.endShape(PConstants.CLOSE);
		src.popMatrix();
	}

	void edges() {
		// constrain the position
		if (this.pos.x < -this.r)
			this.pos.x = p.width + this.r;
		else if (this.pos.x > p.width + this.r)
			this.pos.x = -this.r;

		if (this.pos.y < -this.r)
			this.pos.y = p.height + this.r;
		else if (this.pos.y > p.height + this.r)
			this.pos.y = -this.r;
	}

	ArrayList<Asteroid> breakup() {
		ArrayList<Asteroid> newA = new ArrayList<Asteroid>();
		newA.add(new Asteroid(p, this.pos, this.r * 0.5f));
		newA.add(new Asteroid(p, this.pos, this.r * 0.5f));

		return newA;
	}

}
