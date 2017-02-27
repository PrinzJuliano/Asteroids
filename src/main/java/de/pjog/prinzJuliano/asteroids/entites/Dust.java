package de.pjog.prinzJuliano.asteroids.entites;

import de.pjog.prinzJuliano.asteroids.MainClass;
import processing.core.PGraphics;
import processing.core.PVector;

public class Dust {

	public MainClass p;
	public PVector pos;
	public PVector vel;
	public float transparency;
	
	public Dust(MainClass p, PVector pos, PVector vel)
	{
		this.p = p;
		this.pos = pos.copy();
		this.vel = vel.copy();
		this.vel.add(PVector.random2D().mult(p.random(0.5f, 1.5f)));
		this.transparency = p.random(200f, 255f);
	}
	
	public void update(){
		this.pos.add(this.vel);
		this.transparency -= 2;
	}
	
	public void draw(PGraphics src)
	{
		if(this.transparency > 0)
		{
			src.stroke(p.asteroidColor, this.transparency);
			src.point(this.pos.x, this.pos.y);
		}
	}
}
