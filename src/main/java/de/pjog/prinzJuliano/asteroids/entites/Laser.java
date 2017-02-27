package de.pjog.prinzJuliano.asteroids.entites;

import java.util.ArrayList;

import de.pjog.prinzJuliano.asteroids.MainClass;
import de.pjog.prinzJuliano.asteroids.Util;
import processing.core.PGraphics;
import processing.core.PVector;

public class Laser extends Entity {

	public Laser(MainClass p, PVector pos, float heading) {
		super(p, pos.x, pos.y, 4);
		this.vel = PVector.fromAngle(heading);
		this.vel.mult(30);
	}

	public void update() {
		this.pos.add(this.vel);
	}

	public void draw(PGraphics src) {
		src.pushMatrix();
		src.strokeWeight(this.r);
		src.stroke(p.laserColor);
		src.point(this.pos.x, this.pos.y);
		src.popMatrix();
	}

	public boolean hits(Asteroid asteroid) {
		float dist2 = (this.pos.x - asteroid.pos.x) * (this.pos.x - asteroid.pos.x) + (this.pos.y - asteroid.pos.y) * (this.pos.y - asteroid.pos.y);
		
		if (dist2 <= asteroid.rmin2) {
		      return true;
		    }
		    if (dist2 >= asteroid.rmax2) {
		      return false;
		    }

		    PVector last_pos = PVector.sub(this.pos, this.vel);
		    ArrayList<PVector> asteroid_vertices = asteroid.vertecies();
		    for (int i = 0; i < asteroid_vertices.size() - 1; i++) {
		      if (Util.lineIntersect(last_pos, this.pos, asteroid_vertices.get(i), asteroid_vertices.get(i + 1))) {
		        return true;
		      }
		    }
		    if (Util.lineIntersect(last_pos, this.pos, asteroid_vertices.get(0), asteroid_vertices.get(asteroid_vertices.size() - 1))) {
		      return true;
		    }
		    return false;
	}

	public boolean offscreen() {
		// constrain the position
		if (this.pos.x > p.width || this.pos.x < 0) {
			return true;
		}
		if (this.pos.y > p.height || this.pos.y < 0) {
			return true;
		}
		return false;
	}
}
