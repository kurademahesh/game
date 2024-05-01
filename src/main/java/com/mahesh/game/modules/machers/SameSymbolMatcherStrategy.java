package com.mahesh.game.modules.machers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mahesh.game.config.CombinationMatch;
import com.mahesh.game.config.WinCombination;
import com.mahesh.game.modules.CombinationDetail;
import com.mahesh.game.modules.GameBoard;

public class SameSymbolMatcherStrategy {
	private final Map<Integer, CombinationDetail> combinations;

	private final Map<String, Integer> symbols = new HashMap<>();

	public SameSymbolMatcherStrategy(Map<String, WinCombination> winCombinations) {
		this.combinations = (Map<Integer, CombinationDetail>) winCombinations.entrySet().stream()
				.filter(e -> (((WinCombination) e.getValue()).when() == CombinationMatch.SAME_SYMBOLS))
				.collect(Collectors.toMap(e -> Integer.valueOf(((WinCombination) e.getValue()).count()),
						e -> new CombinationDetail((String) e.getKey(), (WinCombination) e.getValue())));
	}

	public List<MatchingResult> match(GameBoard gameboard) {
		for (String[] row : gameboard.boardMatrix()) {
			for (String symbol : row)
				this.symbols.put(symbol, this.symbols.getOrDefault(symbol, 0) + 1);
		}
		List<MatchingResult> result = new ArrayList<>();
		for (Map.Entry<String, Integer> e : this.symbols.entrySet()) {
			CombinationDetail combinationDetail = this.combinations.get(e.getValue());
			if (combinationDetail != null) {
				MatchingResult foundResult = new MatchingResult(e.getKey(), combinationDetail.name(),
						combinationDetail.winCombination().group(),
						combinationDetail.winCombination().rewardMultiplier());
				result.add(foundResult);
			}
		}
		return result;
	}

}
