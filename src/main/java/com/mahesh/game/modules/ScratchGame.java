package com.mahesh.game.modules;

import java.math.BigDecimal;
import java.util.Random;

import com.mahesh.game.config.CellProbability;
import com.mahesh.game.config.Configuration;

/**
 * Main Scratch Game class.
 */
public class ScratchGame {

	private Configuration configuration;

	/**
	 * Set the configuration
	 * @param configuration
	 */
	public ScratchGame(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Execute the game and generate result
	 * @param bettingAmount
	 * @return
	 */
	public GameResult play(BigDecimal bettingAmount) {
		GameResult gameResult = null;
		if (this.configuration.isValid()) {
			GameBoard board = createBoard();
			GameProcessor gameProcessor = new GameProcessor(this.configuration);
			gameResult = gameProcessor.process(board, bettingAmount);
		} else {
			System.out.println("Invalid configuration provided to game");
			throw new IllegalArgumentException("Invalid configuration provided to game");
		}
		return gameResult;
	}

	/**
	 * Helper method to generate the board as per the probabilities.
	 * 
	 * @return
	 */
	private GameBoard createBoard() {

		String[][] boardMatrix = new String[this.configuration.rows()][this.configuration.columns()];
		String bonusSymbol = null;
		// Bonus symbol will be generated at max once
		boolean isbonus = (this.configuration.probabilities().bonusSymbols() != null);
		for (CellProbability cellProbability : this.configuration.probabilities().standardSymbols()) {
			if (isbonus && (new Random()).nextBoolean()) {
				bonusSymbol = CellFillerUtils.getValue(this.configuration.probabilities().bonusSymbols());
				boardMatrix[cellProbability.row()][cellProbability.column()] = bonusSymbol;
				isbonus = false;
				continue;
			}
			boardMatrix[cellProbability.row()][cellProbability.column()] = CellFillerUtils.getValue(cellProbability);
		}
		return new GameBoard(boardMatrix, bonusSymbol);
	}
}
