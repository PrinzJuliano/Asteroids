package de.pjog.prinzJuliano.asteroids.entites;

import java.util.ArrayList;

import de.pjog.prinzJuliano.asteroids.MainClass;
import de.pjog.prinzJuliano.asteroids.Util;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class Asteroid extends Entity {

	int vertecies;
	ArrayList<Float> offset;

	float rmin, rmin2, rmax2;
	public int size;

	public Asteroid(MainClass p) {
		this(p, new PVector(p.random(p.width), p.random(p.height)), PApplet.floor(p.random(40, 60)), 2);
	}

	public Asteroid(MainClass p, PVector pos, float r, int size) {
		super(p, pos.x, pos.y, r);
		this.p = p;
		this.size = size;
		this.pos = pos.copy();
		this.r = r;
		this.vel = PVector.random2D();
		this.vertecies = PApplet.floor(p.random(5, 15));
		this.offset = new ArrayList<Float>();

		for (int i = 0; i < this.vertecies; i++)
			this.offset.add(p.random(-this.r * 0.5f, this.r * 0.5f));

		this.rmin = this.r + Util.min(this.offset);
		this.rmin2 = this.rmin * this.rmin;
		this.rmax = this.r + Util.max(this.offset);
		this.rmax2 = this.rmax * this.rmax;
		setRotation(p.random(-0.03f, 0.03f));

		switch (this.size) {
		case 1:
			this.vel.mult(1.5f);
			break;
		case 0:
			this.vel.mult(2f);
			break;
		}
	}

	public void draw(PGraphics src) {
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

	public ArrayList<Asteroid> breakup() {
		if(size > 0){
		ArrayList<Asteroid> newA = new ArrayList<Asteroid>();
		newA.add(new Asteroid(p, this.pos, this.r * 0.5f, this.size-1));
		newA.add(new Asteroid(p, this.pos, this.r * 0.5f, this.size-1));

		return newA;
		} else {
			return new ArrayList<Asteroid>();
		}
	}

	public ArrayList<PVector> vertecies() {
		ArrayList<PVector> verts = new ArrayList<PVector>();

		for (int i = 0; i < this.vertecies; i++) {
			float angle = this.heading + PApplet.map(i, 0, this.vertecies, 0, PConstants.TWO_PI);
			float r = this.r + this.offset.get(i);
			verts.add(PVector.add(new PVector(r * PApplet.cos(angle), r * PApplet.sin(angle)), this.pos));
		}

		return verts;
	}

}
