package de.pjog.prinzJuliano.asteroids;

import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

public class Ship {
	MainClass parent;

	PVector pos;
	float r = 20;
	float heading = 0;
	float rotation = 0;
	PVector vel;
	boolean acc = false;

	public Ship(MainClass p) {
		this.parent = p;
		this.pos = new PVector(parent.width / 2, parent.height / 2);
		this.vel = new PVector(0, 0);
	}

	void draw(PGraphics src) {
		src.pushMatrix();
		src.stroke(parent.shipColor);
		// src.fill(bgColor);
		src.noFill();
		src.strokeWeight(1);
		src.translate(this.pos.x, this.pos.y);
		src.rotate(this.heading + PConstants.HALF_PI);
		src.triangle(-this.r, this.r, this.r, this.r, 0, -this.r);

		src.popMatrix();
	}

	void boosting(boolean boo) {
		this.acc = boo;
	}

	void boost() {
		PVector force = PVector.fromAngle(this.heading);
		force.mult(0.1f);
		this.vel.add(force);
		this.vel.limit(20);
	}

	void update() {
		this.heading += this.rotation;

		if (this.acc)
			this.boost();

		this.pos.add(this.vel);

		this.vel.mult(0.995f);

		this.edges();
	}

	boolean hits(Asteroid target) {
		float d = PVector.dist(this.pos, target.pos);

		return (d < this.r + target.r);
	}

	void edges() {
		// constrain the position
		if (this.pos.x < -this.r)
			this.pos.x = parent.width + this.r;
		else if (this.pos.x > parent.width + this.r)
			this.pos.x = -this.r;

		if (this.pos.y < -this.r)
			this.pos.y = parent.height + this.r;
		else if (this.pos.y > parent.height + this.r)
			this.pos.y = -this.r;
	}

	void setRotation(float angle) {
		this.rotation = angle;
	}

}
