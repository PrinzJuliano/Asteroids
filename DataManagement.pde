PlayerEntry[] topFive = new PlayerEntry[5];

void loadColorScheme() {
  JSONObject colors;
  try {
    colors = loadJSONObject("colors.json");
  }
  catch(Exception e) {
    bgColor = color(15, 1, 9);
    shipColor = color(255, 255, 255);
    laserColor = color(255, 255, 255);
    asteroidColor = color(255, 255, 255);
    textColor = color(255, 255, 255);

    saveColorScheme();
    return;
  }

  if (!colors.isNull("backgroundColor")) {
    JSONObject col = colors.getJSONObject("backgroundColor");

    float r = col.getFloat("r");
    float g = col.getFloat("g");
    float b = col.getFloat("b");
    float a = col.getFloat("a");

    bgColor = color(r, g, b, a);
  } else {
    bgColor = color(15, 1, 9);
  }

  if (!colors.isNull("shipColor")) {
    JSONObject col = colors.getJSONObject("shipColor");

    float r = col.getFloat("r");
    float g = col.getFloat("g");
    float b = col.getFloat("b");
    float a = col.getFloat("a");

    shipColor = color(r, g, b, a);
  } else {
    shipColor = color(255, 255, 255);
  }

  if (!colors.isNull("asteroidColor")) {
    JSONObject col = colors.getJSONObject("asteroidColor");

    float r = col.getFloat("r");
    float g = col.getFloat("g");
    float b = col.getFloat("b");
    float a = col.getFloat("a");

    asteroidColor = color(r, g, b, a);
  } else {
    asteroidColor = color(255, 255, 255);
  }

  if (!colors.isNull("laserColor")) {
    JSONObject col = colors.getJSONObject("laserColor");

    float r = col.getFloat("r");
    float g = col.getFloat("g");
    float b = col.getFloat("b");
    float a = col.getFloat("a");

    laserColor = color(r, g, b, a);
  } else {
    laserColor = color(255, 255, 255);
  }

  if (!colors.isNull("textColor")) {
    JSONObject col = colors.getJSONObject("textColor");

    float r = col.getFloat("r");
    float g = col.getFloat("g");
    float b = col.getFloat("b");
    float a = col.getFloat("a");

    textColor = color(r, g, b, a);
  } else {
    textColor = color(255, 255, 255);
  }
}

void saveColorScheme() {
  JSONObject o = new JSONObject();

  //bgcolor
  JSONObject bg = new JSONObject();
  bg.setFloat("r", red(bgColor));
  bg.setFloat("g", green(bgColor));
  bg.setFloat("b", blue(bgColor));
  bg.setFloat("a", alpha(bgColor));

  o.setJSONObject("backgroundColor", bg);

  JSONObject sh = new JSONObject();
  sh.setFloat("r", red(shipColor));
  sh.setFloat("g", green(shipColor));
  sh.setFloat("b", blue(shipColor));
  sh.setFloat("a", alpha(shipColor));

  o.setJSONObject("shipColor", sh);

  JSONObject as = new JSONObject();
  as.setFloat("r", red(asteroidColor));
  as.setFloat("g", green(asteroidColor));
  as.setFloat("b", blue(asteroidColor));
  as.setFloat("a", alpha(asteroidColor));

  o.setJSONObject("asteroidColor", as);

  JSONObject ls = new JSONObject();
  ls.setFloat("r", red(laserColor));
  ls.setFloat("g", green(laserColor));
  ls.setFloat("b", blue(laserColor));
  ls.setFloat("a", alpha(laserColor));

  o.setJSONObject("laserColor", ls);

  JSONObject txt = new JSONObject();
  txt.setFloat("r", red(textColor));
  txt.setFloat("g", green(textColor));
  txt.setFloat("b", blue(textColor));
  txt.setFloat("a", alpha(textColor));

  o.setJSONObject("textColor", txt);

  saveJSONObject(o, "data/colors.json");
}

void loadTopFive() {
  
  for (int i = 0; i< 5; i++)
  {
    topFive[i] = new PlayerEntry("<Slot Free>", 1, 0);
  }
  
  JSONArray arr = loadJSONArray("players.json");

  for (int i = 0; i < constrain(arr.size(), 0, 5); i++) {
    JSONObject o = arr.getJSONObject(i);

    PlayerEntry e = new PlayerEntry();
    e.fromJSON(o);

    topFive[i] = e;
  }
}

void saveTopFive() {
  JSONArray arr = new JSONArray();

  for (int i = 0; i < topFive.length; i++)
    arr.setJSONObject(i, topFive[i].toJSON());

  saveJSONArray(arr, "data/players.json");
}

void checkTopFive() {

  if (you.score > topFive[0].score) {
    topFive[4] = topFive[3];
    topFive[3] = topFive[2];
    topFive[2] = topFive[1];
    topFive[1] = topFive[0];
    topFive[0] = you;
    saveTopFive();
  } else if (you.score > topFive[1].score) {
    topFive[4] = topFive[3];
    topFive[3] = topFive[2];
    topFive[2] = topFive[1];
    topFive[1] = you;
    saveTopFive();
  } else if (you.score > topFive[2].score) {
    topFive[4] = topFive[3];
    topFive[3] = topFive[2];
    topFive[2] = you;
    saveTopFive();
  } else if (you.score > topFive[3].score) {
    topFive[4] = topFive[3];
    topFive[3] = you;
    saveTopFive();
  } else if (you.score > topFive[4].score) {
    topFive[4] = you;
    saveTopFive();
  }
}

void loadConfig() {
  
  
}