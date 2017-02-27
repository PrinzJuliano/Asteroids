package de.pjog.prinzJuliano.asteroids.entites;

import java.util.ArrayList;

import de.pjog.prinzJuliano.asteroids.MainClass;
import de.pjog.prinzJuliano.asteroids.Util;
import de.pjog.prinzJuliano.asteroids.io.InputListener;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class Ship extends Entity {
	public boolean isDestroyed = false;
	public int destroyFrames = 600;
	public int shields;
	public float rmax2;

	public ArrayList<BrokenPart> brokenParts;

	public Ship(MainClass p) {
		super(p, p.width / 2, p.height / 2, 20);
		shields = p.shieldTime;
		this.rmax = 4 / 3 * this.r;

		brokenParts = new ArrayList<BrokenPart>();

		final Ship scope = this;
		// Input handlers
		p.inputs.register((int)(" ".charAt(0)), new InputListener() {
			public void on(char c, int code, boolean press) {
				if (!press)
					return;

				Laser laser = new Laser(scope.p, scope.pos, scope.heading);
				scope.p.lasers.add(laser);
			}
		});

		InputListener right = new InputListener() {
			public void on(char c, int code, boolean press) {
				scope.setRotation(press ? 0.08f : 0f);
			}
		};
		InputListener left = new InputListener() {
			public void on(char c, int code, boolean press) {
				scope.setRotation(press ? -0.08f : 0f);
			}
		};
		InputListener up = new InputListener() {
			public void on(char c, int code, boolean press) {
				scope.setAccel(press ? 0.1f : 0f);
			}
		};

		p.inputs.register(PConstants.RIGHT, right);
		p.inputs.register(68, right);
		p.inputs.register(PConstants.LEFT, left);
		p.inputs.register(65, left);
		p.inputs.register(PConstants.UP, up);
		p.inputs.register(87, up);
	}

	public void destroy() {
		this.isDestroyed = true;

		for (int i = 0; i < 4; i++) {
			BrokenPart b = new BrokenPart();
			b.pos = this.pos.copy();
			b.vel = PVector.random2D();
			b.heading = p.random(0f, 360f);
			b.rot = p.random(-0.07f, 0.07f);
			brokenParts.add(b);
		}
		
	}

	public void draw(PGraphics src) {
		if (this.isDestroyed) {
			for (int i = 0; i < this.brokenParts.size(); i++) {
				src.pushMatrix();
				src.strokeWeight(1);
				src.stroke(PApplet.floor(255f * ((this.destroyFrames--) / 600f)));
				this.destroyFrames = (int)PApplet.constrain(this.destroyFrames, 0f, 600f);
				BrokenPart bp = this.brokenParts.get(i);
				src.translate(bp.pos.x, bp.pos.y);
				src.rotate(bp.heading);
				src.line(-this.r / 2, -this.r / 2, this.r / 2, this.r / 2);
				src.popMatrix();
			}
		} else {
			src.pushMatrix();
			src.translate(this.pos.x, this.pos.y);
			src.rotate(this.heading);
			src.fill(p.bgColor);
			src.strokeWeight(1);
			int shieldCol = (int) p.random(PApplet.map(this.shields, 0f, p.shieldTime, 255f, 0f), 255f);
			src.stroke(shieldCol, shieldCol, 255);
			src.triangle(-2 / 3 * this.r, -this.r, -2 / 3 * this.r, this.r, 4 / 3 * this.r, 0);

			if (this.accelMagnitude != 0) {
				src.translate(-this.r/2, 0);
				src.stroke(p.shipThrusterColor);
				src.rotate(p.random(PConstants.PI / 4, 3 * PConstants.PI / 4));
				src.line(0, 0, 0, 20);
			}

			src.popMatrix();
		}
	}

	public void update() {
		super.update();
		this.vel.mult(0.99f);
		if (this.isDestroyed) {
			for (int i = 0; i < this.brokenParts.size(); i++) {
				this.brokenParts.get(i).pos.add(this.brokenParts.get(i).vel);
				this.brokenParts.get(i).heading += this.brokenParts.get(i).rot;
			}
		} else {
			this.vel.mult(0.99f);
		}
		if (this.shields > 0) {
			this.shields -= 1;
		}
	}

	public boolean hits(Asteroid asteroid) {
		// Are shields up?
		if (this.shields > 0) {
			return false;
		}

		// Is the ship far from the asteroid?
		float dist2 = (this.pos.x - asteroid.pos.x) * (this.pos.x - asteroid.pos.x)
				+ (this.pos.y - asteroid.pos.y) * (this.pos.y - asteroid.pos.y);
		if (dist2 >= (asteroid.rmax + this.rmax2) * (asteroid.rmax + this.rmax2)) {
			return false;
		}

		// Is the ship inside the asteroid?
		if (dist2 <= asteroid.rmin2) {
			return true;
		}

		// Otherwise, we need to check for line intersection
		PVector[] vertices = { PVector.add(new PVector(-2 / 3 * this.r, this.r).rotate(this.heading), this.pos),
				PVector.add(new PVector(-2 / 3 * this.r, -this.r).rotate(this.heading), this.pos),
				PVector.add(new PVector(4 / 3 * this.r, 0).rotate(this.heading), this.pos) };
		ArrayList<PVector> asteroid_vertices = asteroid.vertecies();

		for (int i = 0; i < asteroid_vertices.size(); i++) {
			for (int j = 0; j < vertices.length; j++) {
				if (Util.lineIntersect(vertices[j], vertices[(j + 1) % vertices.length], asteroid_vertices.get(i),
						asteroid_vertices.get((i + 1) % asteroid_vertices.size()))) {
					return true;
				}
			}
		}
		return false;
	}

}
