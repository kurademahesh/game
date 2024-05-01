package com.mahesh.game.modules.machers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.mahesh.game.config.WinCombination;
import com.mahesh.game.modules.GameBoard;

/**
 * @author Mahesh
 * 
 * This is strategy class for matching all needed combinations
 */
public class MatchingStrategy {
	private final Map<String, WinCombination> winCombinations;

	protected MatchingStrategy() {
		this.winCombinations = Map.of();
	}

	public MatchingStrategy(Map<String, WinCombination> winCombinations) {
		this.winCombinations = winCombinations;
	}

	public List<MatchingResult> match(GameBoard gameBoard) {
		List<MatchingResult> result = new ArrayList<>();
		List<Callable<List<MatchingResult>>> tasks = new ArrayList<>();
		tasks.add(() -> (new SameSymbolMatcherStrategy(this.winCombinations)).match(gameBoard));
		tasks.add(() -> (new LinearMatcherStrategy(this.winCombinations)).match(gameBoard));
		ExecutorService executor = Executors.newCachedThreadPool();
		try {
			List<Future<List<MatchingResult>>> futures = executor.invokeAll(tasks);
			for (Future<List<MatchingResult>> future : futures)
				result.addAll(future.get());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
		return result;
	}
}
