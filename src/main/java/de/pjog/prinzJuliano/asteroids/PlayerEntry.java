package de.pjog.prinzJuliano.asteroids;

import processing.data.JSONObject;

public class PlayerEntry {

	MainClass p;

	String username;
	int stage;
	int score;

	PlayerEntry(MainClass p) {
		this.p = p;
	}

	PlayerEntry(MainClass p, String u, int stage, int score) {
		this(p);
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
