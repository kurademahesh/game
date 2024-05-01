package com.mahesh.game.modules;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.mahesh.game.config.CellProbability;

/**
 * @author Mahesh
 * 
 * Select the symbol for the cell depends on the cell probabilities
 * 
 */
public class CellFillerUtils {
	public static String getValue(CellProbability cellDistribution) {
		TreeMap<Double, String> map = new TreeMap<>();
		Random random = new Random();
		double total = 0.0D;
		for (Map.Entry<String, Integer> entry : cellDistribution.symbols()
				.entrySet()) {
			String symbol = entry.getKey();
			double weight = ((Integer) entry.getValue()).intValue();
			if (weight > 0.0D) {
				total += weight;
				map.put(Double.valueOf(total), symbol);
			}
		}
		double value = random.nextDouble() * total;
		return map.higherEntry(Double.valueOf(value)).getValue();
	}

}
