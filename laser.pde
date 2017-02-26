/**
 * 
 */

class Laser {
  PVector pos;
  PVector vel;


  public Laser(PVector pos, float heading, PVector startVel) {
    this.pos = pos.copy();
    this.vel = PVector.fromAngle(heading);
    this.vel.mult(10);
    this.vel.add(startVel);
  }

  void update() {
    this.pos.add(this.vel);
  }

  void draw(PGraphics src) {
    src.pushMatrix();
    src.strokeWeight(4);
    src.stroke(laserColor);
    src.point(this.pos.x, this.pos.y);
    src.popMatrix();
  }

  boolean hits(Asteroid asteroid) {
    float distance = PVector.dist(this.pos, asteroid.pos);
    if (distance < asteroid.r)
      return true;
    else
      return false;
  }

  boolean edges() {
    // constrain the position
    if (this.pos.x < 0 || this.pos.x > width || this.pos.y < 0 || this.pos.y > height)
      return true;
    else
      return false;
  }
}