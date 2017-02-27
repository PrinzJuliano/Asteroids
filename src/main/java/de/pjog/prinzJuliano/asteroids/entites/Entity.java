package de.pjog.prinzJuliano.asteroids.entites;

import de.pjog.prinzJuliano.asteroids.MainClass;
import processing.core.PVector;

public class Entity {

	public PVector pos;
	public float r;
	public float rmax;
	public float heading = 0;
	public float rotation = 0;
	public PVector vel;
	public float accelMagnitude = 0;
	public MainClass p;
	
	public Entity(MainClass p, float x, float y, float radius){
		this.p = p;
		this.pos = new PVector(x, y);
		this.r = radius;
		this.rmax = radius;
		this.vel = new PVector(0, 0);
	}
	
	public void update(){
		this.heading += this.rotation;
		
		PVector force = PVector.fromAngle(heading);
		force.mult(this.accelMagnitude);
		this.vel.add(force);
		
		this.pos.add(this.vel);
		this.edges();
	}
	
	public void setAccel(float magnitude)
	{
		this.accelMagnitude = magnitude;
	}
	
	public void edges() {
		// constrain the position
		if (this.pos.x < -this.rmax)
			this.pos.x = p.width + this.rmax;
		else if (this.pos.x > p.width + this.rmax)
			this.pos.x = -this.r;

		if (this.pos.y < -this.rmax)
			this.pos.y = p.height + this.rmax;
		else if (this.pos.y > p.height + this.rmax)
			this.pos.y = -this.rmax;
	}
	
	public void setRotation(float rot)
	{
		this.rotation = rot;
	}
	
}
