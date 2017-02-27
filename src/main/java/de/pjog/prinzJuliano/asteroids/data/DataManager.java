package de.pjog.prinzJuliano.asteroids.data;

import de.pjog.prinzJuliano.asteroids.MainClass;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class DataManager {
	public static PlayerEntry[] topFive = new PlayerEntry[5];

	public static void loadColorScheme(MainClass p) {
		JSONObject colors;
		try {
			colors = p.loadJSONObject("config/colors.json");

			if (!colors.isNull("backgroundColor")) {
				JSONObject col = colors.getJSONObject("backgroundColor");

				float r = col.getFloat("r");
				float g = col.getFloat("g");
				float b = col.getFloat("b");
				float a = col.getFloat("a");

				p.bgColor = p.color(r, g, b, a);
			} else {
				p.bgColor = p.color(15, 1, 9);
			}

			if (!colors.isNull("asteroidColor")) {
				JSONObject col = colors.getJSONObject("asteroidColor");

				float r = col.getFloat("r");
				float g = col.getFloat("g");
				float b = col.getFloat("b");
				float a = col.getFloat("a");

				p.asteroidColor = p.color(r, g, b, a);
			} else {
				p.asteroidColor = p.color(255, 255, 255);
			}

			if (!colors.isNull("laserColor")) {
				JSONObject col = colors.getJSONObject("laserColor");

				float r = col.getFloat("r");
				float g = col.getFloat("g");
				float b = col.getFloat("b");
				float a = col.getFloat("a");

				p.laserColor = p.color(r, g, b, a);
			} else {
				p.laserColor = p.color(255, 255, 255);
			}
			
			if (!colors.isNull("vignetteColor")) {
				JSONObject col = colors.getJSONObject("vignetteColor");

				float r = col.getFloat("r");
				float g = col.getFloat("g");
				float b = col.getFloat("b");
				float a = col.getFloat("a");

				p.vignetteColor = p.color(r, g, b, a);
			} else {
				p.vignetteColor = p.color(0);
			}

			if (!colors.isNull("textColor")) {
				JSONObject col = colors.getJSONObject("textColor");

				float r = col.getFloat("r");
				float g = col.getFloat("g");
				float b = col.getFloat("b");
				float a = col.getFloat("a");

				p.textColor = p.color(r, g, b, a);
			} else {
				p.textColor = p.color(255, 255, 255);
			}
			
			if (!colors.isNull("shipThrusterColor")) {
				JSONObject col = colors.getJSONObject("shipThrusterColor");

				float r = col.getFloat("r");
				float g = col.getFloat("g");
				float b = col.getFloat("b");
				float a = col.getFloat("a");

				p.shipThrusterColor = p.color(r, g, b, a);
			} else {
				p.shipThrusterColor = p.color(255, 255, 0);
			}
			
		} catch (Exception e) {
			p.bgColor = p.color(15, 1, 9);
			p.laserColor = p.color(255, 255, 255);
			p.asteroidColor = p.color(255, 255, 255);
			p.textColor = p.color(255, 255, 255);
			p.vignetteColor = p.color(0);
			p.shipThrusterColor = p.color(255, 255, 0);
		}
		saveColorScheme(p);
	}

	public static void saveColorScheme(MainClass p) {
		JSONObject o = new JSONObject();

		// bgcolor
		JSONObject bg = new JSONObject();
		bg.setFloat("r", p.red(p.bgColor));
		bg.setFloat("g", p.green(p.bgColor));
		bg.setFloat("b", p.blue(p.bgColor));
		bg.setFloat("a", p.alpha(p.bgColor));

		o.setJSONObject("backgroundColor", bg);

		JSONObject as = new JSONObject();
		as.setFloat("r", p.red(p.asteroidColor));
		as.setFloat("g", p.green(p.asteroidColor));
		as.setFloat("b", p.blue(p.asteroidColor));
		as.setFloat("a", p.alpha(p.asteroidColor));

		o.setJSONObject("asteroidColor", as);

		JSONObject vig = new JSONObject();
		vig.setFloat("r", p.red(p.vignetteColor));
		vig.setFloat("g", p.green(p.vignetteColor));
		vig.setFloat("b", p.blue(p.vignetteColor));
		vig.setFloat("a", p.alpha(p.vignetteColor));

		o.setJSONObject("vignetteColor", vig);

		JSONObject ls = new JSONObject();
		ls.setFloat("r", p.red(p.laserColor));
		ls.setFloat("g", p.green(p.laserColor));
		ls.setFloat("b", p.blue(p.laserColor));
		ls.setFloat("a", p.alpha(p.laserColor));

		o.setJSONObject("laserColor", ls);

		JSONObject txt = new JSONObject();
		txt.setFloat("r", p.red(p.textColor));
		txt.setFloat("g", p.green(p.textColor));
		txt.setFloat("b", p.blue(p.textColor));
		txt.setFloat("a", p.alpha(p.textColor));

		o.setJSONObject("textColor", txt);
		
		JSONObject stc = new JSONObject();
		stc.setFloat("r", p.red(p.shipThrusterColor));
		stc.setFloat("g", p.green(p.shipThrusterColor));
		stc.setFloat("b", p.blue(p.shipThrusterColor));
		stc.setFloat("a", p.alpha(p.shipThrusterColor));

		o.setJSONObject("shipThrusterColor", stc);

		p.saveJSONObject(o, "data/config/colors.json");
	}

	public static void loadTopFive(MainClass p) {

		for (int i = 0; i < 5; i++) {
			topFive[i] = new PlayerEntry("<Slot Free>", 1, 0);
		}

		JSONArray arr = null;

		try {
			arr = p.loadJSONArray("highscores/players.json");

			for (int i = 0; i < PApplet.constrain(arr.size(), 0, 5); i++) {
				JSONObject o = arr.getJSONObject(i);

				PlayerEntry e = new PlayerEntry();
				e.fromJSON(o);

				topFive[i] = e;
			}

		} catch (Exception e) {
			saveTopFive(p);
		}

	}

	public static void saveTopFive(MainClass p) {
		JSONArray arr = new JSONArray();

		for (int i = 0; i < topFive.length; i++)
			arr.setJSONObject(i, topFive[i].toJSON());

		p.saveJSONArray(arr, "data/highscores/players.json");
	}

	public static void checkTopFive(MainClass p) {

		if (p.you.score > topFive[0].score) {
			topFive[4] = topFive[3];
			topFive[3] = topFive[2];
			topFive[2] = topFive[1];
			topFive[1] = topFive[0];
			topFive[0] = p.you;
			saveTopFive(p);
		} else if (p.you.score > topFive[1].score) {
			topFive[4] = topFive[3];
			topFive[3] = topFive[2];
			topFive[2] = topFive[1];
			topFive[1] = p.you;
			saveTopFive(p);
		} else if (p.you.score > topFive[2].score) {
			topFive[4] = topFive[3];
			topFive[3] = topFive[2];
			topFive[2] = p.you;
			saveTopFive(p);
		} else if (p.you.score > topFive[3].score) {
			topFive[4] = topFive[3];
			topFive[3] = p.you;
			saveTopFive(p);
		} else if (p.you.score > topFive[4].score) {
			topFive[4] = p.you;
			saveTopFive(p);
		}
	}

	public static void loadConfig(MainClass p) {
		JSONObject conf = null;

		try {
			conf = p.loadJSONObject("config/graphics.json");

			if (!conf.isNull("shaders")) {
				JSONObject shaders = conf.getJSONObject("shaders");

				if (!shaders.isNull("blur")) {
					p.blurShader = shaders.getBoolean("blur");
				} else
					throw new Exception("Can't load Config");

				if (!shaders.isNull("crt")) {
					p.crtShader = shaders.getBoolean("crt");
				} else
					throw new Exception("Can't load Config");

				if (!shaders.isNull("scanlines")) {
					p.scanlineShader = shaders.getBoolean("scanlines");
				} else
					throw new Exception("Can't load Config");

			} else {
				throw new Exception("Can't load Config");
			}

			if (!conf.isNull("smoothingLevel")) {
				p.pSmoothLevel = conf.getInt("smoothingLevel");
			} else {
				throw new Exception("Can't load smoothing Level");
			}
		} catch (Exception e) {
			p.blurShader = true;
			p.crtShader = true;
			p.scanlineShader = true;
			p.pSmoothLevel = 16;
			saveConfig(p);
		}
	}

	public static void saveConfig(MainClass p) {
		JSONObject o = new JSONObject();

		JSONObject shaders = new JSONObject();
		shaders.setBoolean("blur", p.blurShader);
		shaders.setBoolean("crt", p.crtShader);
		shaders.setBoolean("scanlines", p.scanlineShader);
		o.setJSONObject("shaders", shaders);

		o.setInt("smoothingLevel", p.pSmoothLevel);

		p.saveJSONObject(o, "data/config/graphics.json");
	}
}
