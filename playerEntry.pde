class PlayerEntry {
  String username;
  int stage;
  int score;

  PlayerEntry() {}

  PlayerEntry(String u, int stage, int score) {
    this.username = u;
    this.stage = stage;
    this.score = score;
  }

  JSONObject toJSON() {
    JSONObject o = new JSONObject();
    o.setString("username", username);
    o.setInt("stage", stage);
    o.setInt("score", score);
    return o;
  }

  void fromJSON(JSONObject o) {

    if (!o.isNull("username"))
      username = o.getString("username");
    if (!o.isNull("score"))
      score = o.getInt("score");
    if (!o.isNull("stage"))
      stage = o.getInt("stage");
  }
}