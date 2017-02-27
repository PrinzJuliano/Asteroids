package de.pjog.prinzJuliano.asteroids;

import java.util.ArrayList;

import processing.core.PVector;

public class Util {

	public static float min(ArrayList<Float> array) {
		float min = array.get(0);

		for (float f : array)
			if (f < min)
				min = f;
		return min;
	}

	public static float max(ArrayList<Float> array) {
		float max = array.get(0);

		for (float f : array)
			if (f > max)
				max = f;
		return max;
	}

	public static boolean lineIntersect(PVector l1v1, PVector l1v2, PVector l2v1, PVector l2v2) {
		PVector base = PVector.sub(l1v1, l2v1);
		PVector l1_vector = PVector.sub(l1v2, l1v1);
		PVector l2_vector = PVector.sub(l2v2, l2v1);
		float direction_cross = cross(l2_vector, l1_vector);
		float t = cross(base, l1_vector) / direction_cross;
		float u = cross(base, l2_vector) / direction_cross;
		if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
			return true;
		} else {
			return false;
		}
	}

	public static float cross(PVector v1, PVector v2) {
		return v1.x * v2.y - v2.x * v1.y;
	}
}
