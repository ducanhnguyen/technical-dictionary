package com.bugs_eclipse.statistics;

import java.util.HashMap;

/**
 * The statistics of a word in terms of (product, occurrence)+
 * 
 * @author adn0019
 *
 */
public class WordStatistics extends HashMap<String, Integer> {

	@Override
	public String toString() {
		String output = "";
		for (String key : this.keySet())
			output += "(" + key + ", " + this.get(key) + "), ";

		return output;
	}
}
