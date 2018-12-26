package com.bugs_eclipse.statistics;

import java.util.TreeMap;

/**
 * Store all words of all products which we collected. We use tree map to make
 * the searching process faster (T(n) = O(1)).
 * 
 * The first element is word, and the second element is its statistics.
 * 
 * @author adn0019
 *
 */
public class Words extends TreeMap<String, WordStatistics> {

	@Override
	public WordStatistics put(String arg0, WordStatistics arg1) {
		if (!this.containsKey(arg0))
			return super.put(arg0, arg1);
		else
			return null;
	}

	/**
	 * Update a word. If this word does not exist, we add this word to the list of
	 * words.
	 * 
	 * @param word
	 * @param product
	 * @param occurrence
	 * @return
	 */
	public boolean update(String word, String product, int occurrence) {
		if (this.containsKey(word)) {
			WordStatistics ws = this.get(word);

			if (ws.containsKey(product)) {
				ws.remove(product);
				ws.put(product, occurrence);
			} else
				ws.put(product, occurrence);

		} else {
			WordStatistics w = new WordStatistics();
			w.put(product, occurrence);
			this.put(word, w);
		}

		return true;
	}
}
