package com.mahesh.game.modules;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mahesh.game.config.Configuration;
import com.mahesh.game.config.Symbol;
import com.mahesh.game.config.WinCombination;
import com.mahesh.game.modules.machers.MatchingResult;
import com.mahesh.game.modules.machers.MatchingStrategy;

/**
 * 
 * Process the game matrix and calculate total reward from the game matrix
 */
public class GameProcessor {

	private Configuration configuration;

	private Map<String, List<String>> winningCombinations;

	private Map<String, BigDecimal> rewardMultipliers;
	
	private MatchingStrategy matchingStrategy;

	public GameProcessor(Configuration configurationuration) {
		this.configuration = configurationuration;
		Map<String, WinCombination> winCombinations = this.configuration.winCombinations();
		matchingStrategy = new MatchingStrategy(winCombinations);
	}

	/**
	 * Process the gameboard and betting amount to calculate reward for the input
	 * 
	 * @param gameBoard
	 * @param bettingAmount
	 * @return
	 */
	public GameResult process(GameBoard gameBoard, BigDecimal bettingAmount) {
		List<MatchingResult> results = matchingStrategy.match(gameBoard);
		BigDecimal reward = BigDecimal.ZERO;
		if (!results.isEmpty()) {
			filterMultipliers(results);
			reward = sameSymbolReward(this.rewardMultipliers, bettingAmount);
			reward = bonusSymbolRewards(gameBoard.bonusSymbol(), reward);
		}
		return new GameResult(gameBoard.boardMatrix(), reward, this.winningCombinations,
				reward.equals(BigDecimal.ZERO) ? null : gameBoard.bonusSymbol());
	}

	private void filterMultipliers(List<MatchingResult> matched) {
		winningCombinations = new HashMap<>();
		rewardMultipliers = new HashMap<>();
		Set<String> covered = new HashSet<>();
		for (MatchingResult matchingResult : matched) {
			if (covered.add(matchingResult.symbol() + ":" + matchingResult.group())) {
				winningCombinations.computeIfAbsent(matchingResult.symbol(), k -> new ArrayList<>())
						.add(matchingResult.combination());

				rewardMultipliers.compute(matchingResult.symbol(),
						(k, v) -> v == null ? matchingResult.rewardMultiplier()
								: v.multiply(matchingResult.rewardMultiplier()));
			}
		}

	}

	/**
	 * Calculate total reward using the matched symbols
	 * 
	 * @param rewardMultipliers
	 * @param reward
	 * @return
	 */
	private BigDecimal sameSymbolReward(Map<String, BigDecimal> rewardMultipliers, BigDecimal reward) {

		BigDecimal totalReward = ZERO;

		for (Map.Entry<String, BigDecimal> entry : rewardMultipliers.entrySet()) {
			BigDecimal rewardMultiplier = configuration.symbols().get(entry.getKey()).rewardMultiplier();
			BigDecimal partialReward = reward.multiply(rewardMultiplier).multiply(entry.getValue());
			totalReward = totalReward.add(partialReward);
		}

		return totalReward;
	}

	/**
	 * Apply bonus symbol to the reward
	 * @param bonusSymbol
	 * @param reward
	 * @return
	 */
	private BigDecimal bonusSymbolRewards(String bonusSymbol, BigDecimal reward) {
		if (bonusSymbol != null) {
			Symbol bonus = configuration.symbols().get(bonusSymbol);

			switch (bonus.impact()) {
			case MULTIPLY_REWARD:
				return reward.multiply(bonus.rewardMultiplier());
			case EXTRA_BONUS:
				return reward.add(bonus.extra());
			case MISS:
			}
		}
		return reward;
	}

}
