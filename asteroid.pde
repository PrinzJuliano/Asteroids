/**
 * Asteriods are the main obstacle to deal with.
 * 
 * @returns An asteriod
 */

class Asteroid {

  PVector pos;
  float r;
  PVector vel;
  int vertecies;
  ArrayList<Float> offset;

  public Asteroid() {
    this(new PVector(random(width), random(height)), floor(random(20, 50)));
  }

  public Asteroid(PVector pos, float r) {
    this.pos = pos.copy();
    this.r = r;
    this.vel = PVector.random2D();
    this.vertecies = floor(random(5, 15));
    this.offset = new ArrayList<Float>();

    for (int i = 0; i < this.vertecies; i++)
      this.offset.add(random(-this.r*0.5, this.r*0.5));
  }



  void update() {
    this.pos.add(this.vel);
    this.edges();
  }

  void draw(PGraphics src) {
    src.pushMatrix();
    src.translate(this.pos.x, this.pos.y);
    src.stroke(asteroidColor);
    src.strokeWeight(1);
    src.noFill();
    // ellipse(0, 0, this.r*2, this.r*2);
    src.beginShape();
    for (int i = 0; i < this.vertecies; i++) {
      float angle = map(i, 0, this.vertecies, 0, TWO_PI);
      float r = (this.r + this.offset.get(i));

      float x = r * cos(angle);
      float y = r * sin(angle);
      src.vertex(x, y);
    }
    src.endShape(CLOSE);
    src.popMatrix();
  }

  void edges() {
    // constrain the position
    if (this.pos.x < -this.r)
      this.pos.x = width + this.r;
    else if (this.pos.x > width + this.r)
      this.pos.x = -this.r;

    if (this.pos.y < -this.r)
      this.pos.y = height + this.r;
    else if (this.pos.y > height + this.r)
      this.pos.y = -this.r;
  }

  ArrayList<Asteroid> breakup() {
    ArrayList<Asteroid> newA = new ArrayList<Asteroid>();
    newA.add(new Asteroid(this.pos, this.r*0.5));
    newA.add(new Asteroid(this.pos, this.r*0.5));

    return newA;
  }
}