package de.pjog.prinzJuliano.asteroids.data;

import processing.data.JSONObject;

public class PlayerEntry {

	public String username;
	public int stage;
	public int score;
	public int lives = 3;

	public PlayerEntry() {
	}

	public PlayerEntry(String u, int stage, int score) {
		this.username = u;
		this.stage = stage;
		this.score = score;
	}

	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		o.setString("username", username);
		o.setInt("stage", stage);
		o.setInt("score", score);
		return o;
	}

	public void fromJSON(JSONObject o) {

		if (!o.isNull("username"))
			username = o.getString("username");
		if (!o.isNull("score"))
			score = o.getInt("score");
		if (!o.isNull("stage"))
			stage = o.getInt("stage");
	}
}
