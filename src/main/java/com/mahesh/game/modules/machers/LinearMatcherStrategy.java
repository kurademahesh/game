package com.mahesh.game.modules.machers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mahesh.game.config.Cell;
import com.mahesh.game.config.CombinationMatch;
import com.mahesh.game.config.WinCombination;
import com.mahesh.game.modules.CombinationDetail;
import com.mahesh.game.modules.GameBoard;

/**
 * @author Mahesh
 * 
 */
public class LinearMatcherStrategy extends MatchingStrategy {

	private final List<CombinationDetail> combinations;

	public LinearMatcherStrategy(Map<String, WinCombination> winCombinations) {
		this.combinations = winCombinations.entrySet().stream()
				.filter(e -> (((WinCombination) e.getValue()).when() == CombinationMatch.LINEAR_SYMBOLS))
				.map(e -> new CombinationDetail((String) e.getKey(), (WinCombination) e.getValue())).toList();
	}

	@Override
	public List<MatchingResult> match(GameBoard gameBoard) {
		return combinations.stream()
				.flatMap(cd -> cd.winCombination().coveredAreas().stream()
						.filter(coveredArea -> matchCoveredArea(gameBoard.boardMatrix(), coveredArea))
						.map(coveredArea -> new MatchingResult(
								gameBoard.boardMatrix()[coveredArea.get(0).row()][coveredArea.get(0).column()], 
								cd.name(), cd.winCombination().group(), cd.winCombination().rewardMultiplier())))
				.collect(Collectors.toList());
	}

	private boolean matchCoveredArea(String[][] board, List<Cell> coveredArea) {
		Iterator<Cell> iterator = coveredArea.iterator();
		Cell first = iterator.next();
		String symbol = board[first.row()][first.column()];
		while (iterator.hasNext()) {
			Cell next = iterator.next();
			if (!symbol.equals(board[next.row()][next.column()])) {
				return false;
			}
		}
		return true;
	}
}
