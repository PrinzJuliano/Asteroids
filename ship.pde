/**
 * The player object as ship.
 * 
 * @returns a ship object
 */

class Ship {

  PVector pos;
  float r = 20;
  float heading = 0;
  float rotation = 0;
  PVector vel;
  boolean acc = false;

  public Ship() {
    this.pos = new PVector(width/2, height/2);
    this.vel = new PVector(0, 0);
  }

  void draw(PGraphics src) {
    src.pushMatrix();
    src.stroke(shipColor);
    //src.fill(bgColor);
    src.noFill();
    src.strokeWeight(1);
    src.translate(this.pos.x, this.pos.y);
    src.rotate(this.heading + HALF_PI);
    src.triangle(-this.r, this.r, this.r, this.r, 0, -this.r);

    src.popMatrix();
  }

  void boosting(boolean boo)
  {
    this.acc = boo;
  }

  void boost() {
    PVector force = PVector.fromAngle(this.heading);
    force.mult(0.1);
    this.vel.add(force);
    this.vel.limit(20);
  }

  void update() {
    this.heading += this.rotation;

    if (this.acc)
      this.boost();

    this.pos.add(this.vel);

    this.vel.mult(0.995);

    this.edges();
  }

  boolean hits(Asteroid target)
  {
    float d = PVector.dist(this.pos, target.pos);

    return (d < this.r + target.r);
  }

  void edges() {
    // constrain the position
    if (this.pos.x < -this.r)
      this.pos.x = width+this.r;
    else if (this.pos.x > width+this.r)
      this.pos.x = -this.r;

    if (this.pos.y < -this.r)
      this.pos.y = height+this.r;
    else if (this.pos.y > height+this.r)
      this.pos.y = -this.r;
  }

  void setRotation(float angle)
  {
    this.rotation = angle;
  }
}